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
import com.cmalegrete.model.member.MemberRepository;
import com.cmalegrete.model.user.UserEntity;
import com.cmalegrete.service.util.UtilService;

@Service
public class MembershipService extends UtilService{

    @Autowired
    private EmailService emailService;

    @Autowired
    private MemberRepository memberRepository;

    @Value("${spring.mail.enable}")
    private boolean mailInviteEnabled;

    public ResponseEntity<Object> sendEmailRequest(MemberRegisterRequest request) {
        if (super.userExists(request)) {
            throw new EmailAlreadyRegisteredException("Email registration attempt: " + request.getEmail());
        }

        UserEntity member = new MemberEntity(request);

        super.userRepository.save(member);
        log(LogEnum.INFO, "Member registered: " + member.getId(), HttpStatus.CREATED.value());

        String htmlMsg = "<p>Prezado(a) " + request.getName() + ",</p>"
                    + "<p>Estamos felizes em informar que sua aplicação foi enviada com sucesso! Abaixo estão os detalhes fornecidos:</p>"
                    + "<p><strong>CPF:</strong> " + request.getCpf() + "<br>"
                    + "<strong>E-mail:</strong> " + request.getEmail() + "<br>"
                    + "<strong>Organização Militar:</strong> " + request.getMilitaryOrganization() + "<br>"
                    + "<strong>Nome Completo:</strong> " + request.getName() + "<br>"
                    + "<strong>Telefone:</strong> " + request.getPhoneNumber() + "</p>"
                    + "<p>Por favor, entre em contato conosco através do site caso haja alguma dúvida ou se você precisar atualizar qualquer uma das informações fornecidas.</p>"
                    + "<p>Atenciosamente,<br>Equipe do Clube Militar de Alegrete</p>"
                    + "<img src='cid:logoImage' alt='Logo do Clube Militar de Alegrete' style='height:270px;' />";

        emailService.enviarEmailTexto(request.getEmail(), "Confirmação de Envio de Aplicação", htmlMsg);

        return ResponseEntity.ok().build();
    }
    
}
