package com.cmalegrete.service;

import org.springframework.http.ResponseEntity;

import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
import com.cmalegrete.service.util.UtilService;

@RequiredArgsConstructor
@Service
public class ContractService extends UtilService {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Value("${spring.mail.enable}")
    private boolean mailEnabled;

    private final EmailSendService emailSendService;

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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao processar arquivo, apenas .pdf são aceitos.");
        }

        try {
            byte[] pdfBytes = request.getFile().get(0).getBytes();
            PDDocument document = Loader.loadPDF(pdfBytes);

            List<PDSignature> signatures = document.getSignatureDictionaries();
            if (signatures.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O documento não contém assinaturas digitais.");
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
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A assinatura digital do contrato não é válida ou não está conforme gov.br.");
                    }

                    Date agora = new Date();

                    if (certificate.getNotAfter().before(agora)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A assinatura digital está expirada ou inválida.");
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
