package com.cmalegrete.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmalegrete.dto.request.model.util.ContactMessageRequest;
import com.cmalegrete.service.util.UtilService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ContactUsService extends UtilService {

    private final EmailSendService emailSendService;

    @Value("${spring.mail.enable}")
    private boolean mailEnabled;

    @Value("${spring.mail.username}")
    private String membershipApprovalEmailAddress;

    public ResponseEntity<Object> sendContactMessage(ContactMessageRequest request) {
        if (mailEnabled) {
            String htmlMemberConfirmationMsg = "";
            String htmlContactMsg = "";

            // E-mail de confirmação para o usuário que enviou a mensagem
            htmlMemberConfirmationMsg = "<p>Prezado(a) " + request.getName() + ",</p>"
                    + "<p>Informamos que sua mensagem foi enviada com sucesso! Abaixo estão os detalhes fornecidos:</p>"
                    + "<p><strong>Nome Completo:</strong> " + request.getName() + "<br>"
                    + "<strong>E-mail:</strong> " + request.getEmail() + "<br>"
                    + "<strong>Assunto:</strong> " + request.getSubject() + "<br>"
                    + "<strong>Mensagem:</strong> " + request.getMessage() + "</p>"
                    + "<p>Desde já agradecemos o seu contato.</p>"
                    + "<p>Atenciosamente,<br>Equipe do Círculo Militar de Alegrete</p>"
                    + "<br><p style='font-size:8;'>Este é um e-mail automático, por favor não responder.</p>";

            // E-mail mensagem do usuário
            htmlContactMsg = "<p>Equipe do Círculo Militar de Alegrete,</p>"
                    + "<p>Foi recebida uma nova mensagem!</p>"
                    + "<p>Informações do Remetente:</p>"
                    + "<p><strong>Nome Completo:</strong> " + request.getName() + "<br>"
                    + "<strong>E-mail:</strong> " + request.getEmail() + "<br><br>"
                    + "<strong>Mensagem:</strong> " + request.getMessage() + "</p>" +
                    "<p>Por favor, prossigam com a análise do contrato o mais breve possível.</p>" +
                    "<p>Atenciosamente,<br>Sistema de Gestão do Círculo Militar de Alegrete</p>";

            emailSendService.sendMessageToTeam(membershipApprovalEmailAddress, request.getSubject(), htmlContactMsg);
            emailSendService.sendMessageToTeam(request.getEmail(), "Confirmação de Envio de Mensagem",
                    htmlMemberConfirmationMsg);
        }

        return ResponseEntity.ok().build();
    }

}
