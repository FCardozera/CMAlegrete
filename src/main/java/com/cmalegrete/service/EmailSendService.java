package com.cmalegrete.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cmalegrete.dto.request.model.member.MemberRegisterRequest;
import com.cmalegrete.service.util.UtilService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailSendService {

    private final EmailService emailService;

    @Value("${spring.mail.username}")
    private String remetente;

    @Value("${spring.mail.username}")
    private String membershipApprovalEmailAddress;

    @Async
    public void sendConfirmationEmailToUser(MemberRegisterRequest request, byte[] contratoBytes) {
        String htmlMemberMsg = generateMemberConfirmationMessage(request);

        emailService.enviarEmailComAnexo(
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

        emailService.enviarEmailTexto(
            membershipApprovalEmailAddress,
            "Requerimento de Associação - " + UtilService.toCapitalize(request.getName()),
            htmlRequestAlertMsg
        );
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
}
