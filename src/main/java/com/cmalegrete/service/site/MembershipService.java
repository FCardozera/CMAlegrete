package com.cmalegrete.service.site;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmalegrete.dto.request.model.member.MemberRegisterRequest;
import com.cmalegrete.dto.request.model.util.RequestEmail;
import com.cmalegrete.exception.generic.EmailAlreadyRegisteredException;
import com.cmalegrete.model.log.LogEnum;
import com.cmalegrete.model.member.MemberEntity;
import com.cmalegrete.model.user.UserEntity;
import com.cmalegrete.service.util.UtilService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MembershipService extends UtilService {

    private final ContractService contractService;

    @Value("${spring.mail.enable}")
    private boolean mailEnabled;

    public ResponseEntity<Object> sendMembershipRequest(MemberRegisterRequest request) {
        verifyIfUserExists(request);
        UserEntity member = createNewMember(request);
        super.userRepository.save(member);
        logMemberRegistration(member);
        contractService.gerarEnviarContrato(member);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Object> resendEmailtoUser(@Valid RequestEmail request) {
        if(!userExists(request)) {
            return ResponseEntity.badRequest().body("E-mail não registrado!");
        }
        MemberEntity member = (MemberEntity)userRepository.findByEmail(request.getEmail());
        contractService.gerarReenviarContrato(member);
        return ResponseEntity.ok().build();
    }

    private void verifyIfUserExists(RequestEmail request) {
        if (super.userExists(request)) {
            throw new EmailAlreadyRegisteredException("Email registration attempt: " + request.getEmail());
        }
    }

    private UserEntity createNewMember(MemberRegisterRequest request) {
        return new MemberEntity(request, generateRegistrationId());
    }

    private void logMemberRegistration(UserEntity member) {
        log(LogEnum.INFO, "Member registered: " + member.getId(), HttpStatus.CREATED.value());
    }

    private String generateRegistrationId() {
        LocalDate dataAtual = LocalDate.now();
        long userCount = userRepository.count();
        
        String userCountFormatado = String.format("%05d", userCount);
        return dataAtual.getYear() + userCountFormatado;
    }
}
