package com.cmalegrete.service.site;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;

import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Store;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.cmalegrete.dto.request.model.contract.ContractRequest;
import com.cmalegrete.model.member.MemberEntity;
import com.cmalegrete.model.sendcontracttoken.SendContractTokenEntity;
import com.cmalegrete.model.user.UserEntity;
import com.cmalegrete.service.util.UtilService;

@RequiredArgsConstructor
@Service
public class ContractService extends UtilService {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Value("${spring.mail.enable}")
    private boolean mailEnabled;

    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);

    private final EmailSendService emailSendService;

    private final DocumentService documentService;

    public boolean checkUrlToken(String token) {
        return super.tokenExists(token);
    }

    public ResponseEntity<Object> sendContract(ContractRequest request) {
        if (request.getFile() == null || request.getFile().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Um arquivo .pdf deve ser enviado.");
        }

        if (request.getFile().size() > 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Só é permitido o envio de 01 arquivo.");
        }

        String contentType = request.getFile().get(0).getContentType();
        if (contentType == null || !contentType.equals(MediaType.APPLICATION_PDF_VALUE)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao processar arquivo, apenas .pdf são aceitos.");
        }

        try {
            byte[] pdfBytes = request.getFile().get(0).getBytes();
            PDDocument document = Loader.loadPDF(pdfBytes);

            List<PDSignature> signatures = document.getSignatureDictionaries();
            if (signatures.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("O documento não contém assinaturas digitais.");
            }

            for (PDSignature signature : signatures) {
                byte[] signatureBytes = signature.getContents(pdfBytes);
                CMSSignedData cmsSignedData = new CMSSignedData(signatureBytes);
                SignerInformationStore signerInfoStore = cmsSignedData.getSignerInfos();
                Store<X509CertificateHolder> certStore = cmsSignedData.getCertificates();
                Collection<SignerInformation> signers = signerInfoStore.getSigners();

                for (SignerInformation signer : signers) {
                    X509CertificateHolder certHolder = (X509CertificateHolder) certStore.getMatches(signer.getSID())
                            .iterator().next();
                    X509Certificate certificate = new JcaX509CertificateConverter().setProvider("BC")
                            .getCertificate(certHolder);

                    if (!isValidGovBrCertificate(certificate)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("A assinatura digital do contrato não é válida ou não está conforme gov.br.");
                    }

                    Date agora = new Date();

                    if (certificate.getNotAfter().before(agora)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("A assinatura digital está expirada ou inválida.");
                    }
                }
            }

            MemberEntity member = super.memberRepository.findById(getMemberbyToken(request.getToken()).getId()).get();
            SendContractTokenEntity token = super.sendContractTokenRepository.findByToken(request.getToken()).get();
            member.setContract(pdfBytes);
            super.memberRepository.save(member);

            if (mailEnabled) {
                emailSendService.sendContractToTeam(member, pdfBytes,
                        request.getFile().get(0).getOriginalFilename());
                emailSendService.sendReceivedContractConfirmationToMember(member);
            }

            sendContractTokenRepository.delete(token);

            return ResponseEntity.ok().body("Arquivo enviado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar o arquivo enviado.");
        }
    }

    @Async
    public void gerarEnviarContrato(UserEntity member) {
        byte[] contratoPdfBytes = gerarContrato((MemberEntity) member);
        if (mailEnabled) {
            emailSendService.sendConfirmationEmailToUser((MemberEntity) member, contratoPdfBytes,
                    criarTokenContrato(member));
        }
    }
    @Async
    public void gerarReenviarContrato(UserEntity member) {
        byte[] contratoPdfBytes = gerarContrato((MemberEntity)member);
        try {
            SendContractTokenEntity token = sendContractTokenRepository.findByUserId(member.getId()).get();
            if (mailEnabled) {
                emailSendService.sendConfirmationEmailToUser((MemberEntity)member, contratoPdfBytes, token.getToken());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Gera o contrato em PDF com as substituições especificadas
    private byte[] gerarContrato(MemberEntity member) {
        Map<String, String> replacements = new HashMap<>();
        // Substituições de texto no contrato
        replacements.put("#NOME#", UtilService.toCapitalize(member.getName()));
        replacements.put("#ENDERECO#", member.getAddress());
        replacements.put("#CPF#", UtilService.formatarCpf(member.getCpf()));
        replacements.put("#DATA#", getDataAtualPorExtenso());
        return documentService.replaceTextAndConvertToPdf(replacements);
    }

    // Método utilitário para obter a data atual por extenso
    private String getDataAtualPorExtenso() {
        LocalDate dataAtual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale.of("pt", "BR"));
        return dataAtual.format(formatter);
    }
    private String criarTokenContrato(UserEntity user) {
        String token = new String(Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey()),
                StandardCharsets.US_ASCII);
        
        SendContractTokenEntity tokenEntity = new SendContractTokenEntity(token, user);
        super.sendContractTokenRepository.save(tokenEntity);
        return token;
    }

    private boolean isValidGovBrCertificate(X509Certificate certificate) {
        try {
            String dn = certificate.getIssuerX500Principal().getName();
            return dn.contains("Gov-Br");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private MemberEntity getMemberbyToken(String token) {
        return (MemberEntity) sendContractTokenRepository.findByToken(token).get().getUser();
    }
}
