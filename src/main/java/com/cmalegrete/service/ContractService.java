package com.cmalegrete.service;

import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.SignerInformationVerifier;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Store;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.cmalegrete.dto.request.model.contract.ContractRequest;
import com.cmalegrete.exception.generic.FileException;
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
            throw new FileException("Um arquivo é necessário.");
        }

        if (request.getFile().size() > 1) {
            throw new FileException("Só é permitido o envio de 01 arquivo.");
        }

        String contentType = request.getFile().get(0).getContentType();
        if (contentType == null || !contentType.equals(MediaType.APPLICATION_PDF_VALUE)) {
            throw new FileException("Formato de arquivo inválido. Apenas PDF é aceito.");
        }

        try {
            // Carregar o documento PDF
            byte[] pdfBytes = request.getFile().get(0).getBytes();
            PDDocument document = Loader.loadPDF(pdfBytes);

            // Verificar se o PDF contém assinaturas digitais
            List<PDSignature> signatures = document.getSignatureDictionaries();
            if (signatures.isEmpty()) {
                throw new FileException("O documento não contém assinaturas digitais.");
            }

            // Para cada assinatura, verificar se é válida
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
                        throw new FileException(
                                "A assinatura digital do contrato não é válida ou não está conforme gov.br.");
                    }

                    Date agora = new Date();

                    if (certificate.getNotAfter().before(agora)) {
                        throw new FileException("A assinatura digital está expirada ou inválida.");
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
            }

            sendContractTokenRepository.delete(token);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new FileException("Erro ao verificar a assinatura digital: " + e.getMessage());
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
