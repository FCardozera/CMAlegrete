package com.cmalegrete.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cmalegrete.dto.request.model.member.MemberRegisterRequest;
import com.cmalegrete.model.member.MemberEntity;
import com.cmalegrete.service.util.UtilService;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailSendService extends UtilService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    @Value("${spring.mail.username}")
    private String membershipApprovalEmailAddress;

    @Async
    public void sendConfirmationEmailToUser(MemberRegisterRequest request, byte[] contratoBytes, String token) {
        String htmlMemberMsg = generateMemberConfirmationMessage(request, token);

        enviarEmailComAnexo(
                request.getEmail(),
                "Confirmação de Envio de Aplicação - Círculo Militar de Alegrete",
                htmlMemberMsg,
                contratoBytes,
                "contrato_" + request.getName().toLowerCase() + ".pdf");
    }

    @Async
    public void sendMessageToTeam(String destinatario, String assunto, String htmlMsg) {
        enviarEmailTexto(destinatario, assunto, htmlMsg);
    }

    @Async
    public void sendContractToTeam(MemberEntity member, byte[] file, String fileName) {
        String htmlContractMsg = generateContractMessageForTeam(member);

        try {
            enviarEmailComAnexo(membershipApprovalEmailAddress, "Recebimento de Contrato - " + UtilService.toCapitalize(member.getName()), htmlContractMsg, file,
                    fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Async
    public void sendReceivedContractConfirmationToMember(MemberEntity member) {
        String htmlContractMsg = generateContractReceivedConfirmation(member);

        try {
            enviarEmailTexto(member.getEmail(), "Confirmação de Recebimento de Contrato - Círculo Militar de Alegrete", htmlContractMsg);
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
    private void enviarEmailComAnexo(String destinatario, String assunto, String htmlMsg, byte[] arquivoAnexoBytes,
            String nomeArquivoAnexo) {
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

    private String generateMemberConfirmationMessage(MemberRegisterRequest request, String token) {
        StringBuilder msg = new StringBuilder();

        msg.append("<p>Prezado(a) ").append(UtilService.toCapitalize(request.getName())).append(",</p>")
                .append("<p>É com grande satisfação que confirmamos o recebimento de sua aplicação. A seguir, estão os dados fornecidos durante o processo de inscrição:</p>")
                .append("<p><strong>Nome Completo:</strong> ").append(UtilService.toCapitalize(request.getName()))
                .append("<br>")
                .append("<strong>CPF:</strong> ").append(request.getCpf()).append("<br>")
                .append("<strong>E-mail:</strong> ").append(request.getEmail()).append("<br>")
                .append("<strong>Telefone:</strong> ").append(request.getPhoneNumber()).append("<br>");

        if (request.getMilitaryOrganization() != null) {
            msg.append("<strong>Organização Militar:</strong> ").append(request.getMilitaryOrganization())
                    .append("</p>");
        } else {
            msg.append("</p>");
        }

        msg.append(
                "<p>Em anexo, você encontrará o <strong>contrato não assinado</strong>, que deverá ser devidamente assinado digitalmente para concluir sua aplicação.</p>")
                .append("<p>Para assinar o contrato, siga os passos abaixo:</p>")
                .append("<ol>")
                .append("<li>Assine o documento digitalmente utilizando a plataforma <a href=\"https://assinador.iti.br/assinatura/\" target=\"_blank\">Assinatura Digital do GOV.BR</a>.</li>")
                .append("<li>Acesse o link abaixo para fazer o envio do contrato assinado:</li>")
                .append("</ol>")
                .append(generateContractLink(token))
                .append("<p>Seu contrato será analisado pela nossa equipe, e você receberá um retorno em até <strong>4 dias úteis</strong>.</p>")
                .append("<p>Caso tenha qualquer dúvida ou precise atualizar alguma das informações fornecidas, por favor, entre em contato conosco através do nosso <a href=\"https://localhost:8080\">site</a>.</p>")
                .append("<p>Agradecemos pela confiança e estamos à disposição para qualquer esclarecimento.</p>")
                .append("<p>Atenciosamente,<br>Equipe do Círculo Militar de Alegrete</p>")
                .append("<br><p style='font-size:8;'>Este é um e-mail automático, por favor, não responda a esta mensagem.</p>");

        return msg.toString();
    }

    private String generateContractMessageForTeam(MemberEntity member) {
        StringBuilder msg = new StringBuilder();

        msg.append("<p>Prezada Equipe do Círculo Militar de Alegrete,</p>")
                .append("<p>Informamos que um novo contrato foi submetido para análise. Abaixo estão os dados do requerente:</p>")
                .append("<p><strong>Nome Completo:</strong> ").append(UtilService.toCapitalize(member.getName()))
                .append("<br>")
                .append("<strong>CPF:</strong> ").append(member.getCpf()).append("<br>")
                .append("<strong>E-mail:</strong> ").append(member.getEmail()).append("<br>")
                .append("<strong>Telefone:</strong> ").append(member.getPhoneNumber()).append("<br>");

        if (member.getMilitaryOrganization() != null) {
            msg.append("<strong>Organização Militar:</strong> ").append(member.getMilitaryOrganization())
                    .append("</p>");
        } else {
            msg.append("</p>");
        }

        msg.append("<p>Por favor, prossigam com a análise do contrato o mais breve possível.</p>")
                .append("<p>Atenciosamente,<br>Sistema de Gestão do Círculo Militar de Alegrete</p>");

        return msg.toString();
    }

    private String generateContractReceivedConfirmation(MemberEntity member) {
        StringBuilder msg = new StringBuilder();

        msg.append("<p>Prezado(a) ").append(UtilService.toCapitalize(member.getName())).append(",</p>")
                .append("<p>Confirmamos o recebimento de seu contrato assinado. Nossa equipe está analisando o documento e entraremos em contato em breve.</p>")
                .append("<p>O prazo estimado para retorno é de até <strong>4 dias úteis</strong>. Caso tenha alguma dúvida ou precise de mais informações, sinta-se à vontade para entrar em contato conosco pelo telefone ou e-mail fornecido.</p>")
                .append("<p>Agradecemos pela confiança e permanecemos à disposição.</p>")
                .append("<p>Atenciosamente,<br>Equipe do Círculo Militar de Alegrete</p>")
                .append("<br><p style='font-size:8;'>Este é um e-mail automático, por favor, não responda a esta mensagem.</p>");

        return msg.toString();
    }

    private String generateContractLink(String token) {
        return "<a href=http://localhost:8080/send-contract?token=" + token
                + "><strong>[Clique aqui para enviar o contrato assinado]</strong></a>";
    }
}
