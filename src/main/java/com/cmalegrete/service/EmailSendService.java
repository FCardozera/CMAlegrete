package com.cmalegrete.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cmalegrete.dto.request.model.member.MemberRegisterRequest;
import com.cmalegrete.model.member.MemberEntity;
import com.cmalegrete.service.util.UtilService;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailSendService extends UtilService{

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    @Value("${spring.mail.username}")
    private String membershipApprovalEmailAddress;

    @Async
    public void sendConfirmationEmailToUser(MemberRegisterRequest request, byte[] contratoBytes) {
        String htmlMemberMsg = generateMemberConfirmationMessage(request);

        enviarEmailComAnexo(
            request.getEmail(),
            "Confirmação de Envio de Aplicação",
            htmlMemberMsg,
            contratoBytes,
            "contrato_" + request.getName().toLowerCase() + ".pdf"
        );
    }

    @Async
    public void sendAlertEmailToTeam(MemberRegisterRequest request) {
        String htmlRequestAlertMsg = generateAlertMessageForTeam(request);

        enviarEmailTexto(
            membershipApprovalEmailAddress,
            "Requerimento de Associação - " + UtilService.toCapitalize(request.getName()),
            htmlRequestAlertMsg
        );
    }

    @Async
    public void sendMessageToTeam(String destinatario, String assunto, String htmlMsg) {
        enviarEmailTexto(destinatario, assunto, htmlMsg);
    }

    @Async
    public void sendContractToTeam(MemberEntity member , List<MultipartFile> file) {
        String htmlContractMsg = generateContractMessageForTeam(member);

        try {
            enviarEmailComAnexo(membershipApprovalEmailAddress, "Recebimento de Contrato", htmlContractMsg, file.get(0).getBytes(), file.get(0).getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    private void enviarEmailTexto(String destinatario, String assunto, String htmlMsg) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(destinatario);
            helper.setSubject(assunto);

            htmlMsg += "<img src='cid:logoImage' alt='Logo do Clube Militar de Alegrete' style='height:270px;' />";
            helper.setText(htmlMsg, true);

            ClassPathResource logo = new ClassPathResource("static/images/logo.png");
            helper.addInline("logoImage", logo);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Novo método para envio de e-mail com anexo
    private void enviarEmailComAnexo(String destinatario, String assunto, String htmlMsg, byte[] arquivoAnexoBytes, String nomeArquivoAnexo) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(destinatario);
            helper.setSubject(assunto);
            htmlMsg += "<img src='cid:logoImage' alt='Logo do Clube Militar de Alegrete' style='height:270px;' />";
            helper.setText(htmlMsg, true);

            // Adiciona logo no corpo do e-mail
            ClassPathResource logo = new ClassPathResource("static/images/logo.png");
            helper.addInline("logoImage", logo);

            // Adiciona o anexo
            helper.addAttachment(nomeArquivoAnexo, new ByteArrayDataSource(arquivoAnexoBytes, "application/pdf"));

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateAlertMessageForTeam(MemberRegisterRequest request) {
        StringBuilder msg = new StringBuilder();

        msg.append("<p>Equipe do Círculo Militar de Alegrete,</p>")
            .append("<p>Foi recebido um novo requerimento de associação! Abaixo seguem os dados do requerente:</p>")
            .append("<p><strong>Nome Completo:</strong> ").append(UtilService.toCapitalize(request.getName())).append("<br>")
            .append("<strong>CPF:</strong> ").append(request.getCpf()).append("<br>")
            .append("<strong>E-mail:</strong> ").append(request.getEmail()).append("<br>")
            .append("<strong>Telefone:</strong> ").append(request.getPhoneNumber()).append("<br>");

        if (request.getMilitaryOrganization() != null) {
            msg.append("<strong>Organização Militar:</strong> ").append(request.getMilitaryOrganization()).append("</p>");
        } else {
            msg.append("</p>");
        }

        return msg.toString();
    }

    private String generateMemberConfirmationMessage(MemberRegisterRequest request) {
        StringBuilder msg = new StringBuilder();

        msg.append("<p>Prezado(a) ").append(request.getName()).append(",</p>")
            .append("<p>Estamos felizes em informar que sua aplicação foi enviada com sucesso! Abaixo estão os detalhes fornecidos:</p>")
            .append("<p><strong>Nome Completo:</strong> ").append(UtilService.toCapitalize(request.getName())).append("<br>")
            .append("<strong>CPF:</strong> ").append(request.getCpf()).append("<br>")
            .append("<strong>E-mail:</strong> ").append(request.getEmail()).append("<br>")
            .append("<strong>Telefone:</strong> ").append(request.getPhoneNumber()).append("<br>");

        if (request.getMilitaryOrganization() != null) {
            msg.append("<strong>Organização Militar:</strong> ").append(request.getMilitaryOrganization()).append("</p>");
        } else {
            msg.append("</p>");
        }

        msg.append("<p>Por favor, entre em contato conosco através do site caso haja alguma dúvida ou se você precisar atualizar qualquer uma das informações fornecidas.</p>")
            .append("<p>Atenciosamente,<br>Equipe do Círculo Militar de Alegrete</p>")
            .append("<br><p style='font-size:8;'>Este é um e-mail automático, por favor não responder.</p>");

        return msg.toString();
    }

    private String generateContractMessageForTeam(MemberEntity member) {
        StringBuilder msg = new StringBuilder();

        msg.append("<p>Equipe do Círculo Militar de Alegrete,</p>")
            .append("<p>Chegou um novo contrato! Abaixo seguem os dados do requerente:</p>")
            .append("<p><strong>Nome Completo:</strong> ").append(UtilService.toCapitalize(member.getName())).append("<br>")
            .append("<strong>CPF:</strong> ").append(member.getCpf()).append("<br>")
            .append("<strong>E-mail:</strong> ").append(member.getEmail()).append("<br>")
            .append("<strong>Telefone:</strong> ").append(member.getPhoneNumber()).append("<br>");

        if (member.getMilitaryOrganization() != null) {
            msg.append("<strong>Organização Militar:</strong> ").append(member.getMilitaryOrganization()).append("</p>");
        } else {
            msg.append("</p>");
        }

        return msg.toString();
    }
}
