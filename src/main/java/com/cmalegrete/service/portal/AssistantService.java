package com.cmalegrete.service.portal;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmalegrete.dto.request.model.assistant.AssistantRegisterRequest;
import com.cmalegrete.dto.request.model.util.RequestEmail;
import com.cmalegrete.exception.generic.EmailAlreadyRegisteredException;
import com.cmalegrete.model.assistant.AssistantEntity;
import com.cmalegrete.model.log.LogEnum;
import com.cmalegrete.model.user.UserEntity;
import com.cmalegrete.service.util.UtilService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AssistantService extends UtilService {

    public ResponseEntity<Object> registerAssistant(AssistantRegisterRequest request) {
        verifyIfUserExists(request);
        UserEntity assistant = createNewAssistant(request);
        super.userRepository.save(assistant);
        logMemberRegistration(assistant);
        return ResponseEntity.ok().build();
    }

    private void logMemberRegistration(UserEntity assistant) {
        log(LogEnum.INFO, "Assistant registered: " + assistant.getId(), HttpStatus.CREATED.value());
    }

    private void verifyIfUserExists(RequestEmail request) {
        if (super.userExists(request)) {
            throw new EmailAlreadyRegisteredException("Email registration attempt: " + request.getEmail());
        }
    }

    private UserEntity createNewAssistant(AssistantRegisterRequest request) {
        return new AssistantEntity(request, generateRegistrationId());
    }

    private String generateRegistrationId() {
        LocalDate dataAtual = LocalDate.now();
        long userCount = userRepository.count();
        
        String userCountFormatado = String.format("%05d", userCount);
        return dataAtual.getYear() + userCountFormatado;
    }
}
