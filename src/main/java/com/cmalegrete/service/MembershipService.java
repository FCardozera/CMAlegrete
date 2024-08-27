package com.cmalegrete.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmalegrete.dto.request.model.member.MemberRegisterRequest;
import com.cmalegrete.exception.generic.EmailAlreadyRegisteredException;
import com.cmalegrete.model.log.LogEnum;
import com.cmalegrete.model.member.MemberEntity;
import com.cmalegrete.model.user.UserEntity;
import com.cmalegrete.service.util.UtilService;

@Service
public class MembershipService extends UtilService {

    @Autowired
    private EmailService emailService;

    @Value("${spring.mail.enable}")
    private boolean mailEnabled;

    @Value("${spring.mail.username}")
    private String membershipApprovalEmailAddress;

    public ResponseEntity<Object> sendMembershipRequest(MemberRegisterRequest request) {
        if (super.userExists(request)) {
            throw new EmailAlreadyRegisteredException("Email registration attempt: " + request.getEmail());
        }

        UserEntity member = new MemberEntity(request);

        super.userRepository.save(member);
        log(LogEnum.INFO, "Member registered: " + member.getId(), HttpStatus.CREATED.value());

        if (mailEnabled) {
            String htmlMemberMsg = "";
            String htmlRequestAlertMsg = "";

            // E-mail para o usuário que requeriu associação
            htmlMemberMsg = "<p>Prezado(a) " + request.getName() + ",</p>"
                + "<p>Estamos felizes em informar que sua aplicação foi enviada com sucesso! Abaixo estão os detalhes fornecidos:</p>"
                + "<p><strong>Nome Completo:</strong> " + request.getName() + "<br>"
                + "<strong>CPF:</strong> " + request.getCpf() + "<br>"
                + "<strong>E-mail:</strong> " + request.getEmail() + "<br>"
                + "<strong>Telefone:</strong> " + request.getPhoneNumber() + "<br>";
            if (request.getMilitaryOrganization() != null) { 
                htmlMemberMsg += "<strong>Organização Militar:</strong> " + request.getMilitaryOrganization() + "</p>";
            } else {
                htmlMemberMsg += "</p>";
            }
            
            htmlMemberMsg += "<p>Por favor, entre em contato conosco através do site caso haja alguma dúvida ou se você precisar atualizar qualquer uma das informações fornecidas.</p>"
                + "<p>Atenciosamente,<br>Equipe do Círculo Militar de Alegrete</p>"
                + "<br><p style='font-size:8;'>Este é um e-mail automático, por favor não responder.</p>";

            // E-mail alerta de recebimento de requerimento de associação 

            htmlRequestAlertMsg = "<p>Equipe do Círculo Militar de Alegrete,</p>"
                + "<p>Foi recebido um novo requerimento de associação! Abaixo seguem os dados do requerente:</p>"
                + "<p><strong>Nome Completo:</strong> " + request.getName() + "<br>"
                + "<strong>CPF:</strong> " + request.getCpf() + "<br>"
                + "<strong>E-mail:</strong> " + request.getEmail() + "<br>"
                + "<strong>Telefone:</strong> " + request.getPhoneNumber() + "<br>";

            if (request.getMilitaryOrganization() != null) { 
                htmlRequestAlertMsg += "<strong>Organização Militar:</strong> " + request.getMilitaryOrganization() + "</p>";
            } else {
                htmlRequestAlertMsg += "</p>";
            }
            
            emailService.enviarEmailTexto(membershipApprovalEmailAddress, "Requerimento de Associação - " + request.getName(), htmlRequestAlertMsg);
            emailService.enviarEmailTexto(request.getEmail(), "Confirmação de Envio de Aplicação", htmlMemberMsg);
        }

        return ResponseEntity.ok().build();
    }

}
