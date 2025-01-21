package com.cmalegrete.service.portal;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmalegrete.dto.request.model.admin.AdminRegisterRequest;
import com.cmalegrete.dto.request.model.util.RequestEmail;
import com.cmalegrete.exception.generic.EmailAlreadyRegisteredException;
import com.cmalegrete.model.admin.AdminEntity;
import com.cmalegrete.model.log.LogEnum;
import com.cmalegrete.model.user.UserEntity;
import com.cmalegrete.service.util.UtilService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminService extends UtilService{

    public ResponseEntity<Object> registerAdmin(AdminRegisterRequest request) {
        verifyIfUserExists(request);
        UserEntity admin = createNewAdmin(request);
        super.userRepository.save(admin);
        logMemberRegistration(admin);
        return ResponseEntity.ok().build();
    }

    private void logMemberRegistration(UserEntity admin) {
        log(LogEnum.INFO, "Admin registered: " + admin.getId(), HttpStatus.CREATED.value());
    }

    private void verifyIfUserExists(RequestEmail request) {
        if (super.userExists(request)) {
            throw new EmailAlreadyRegisteredException("Email registration attempt: " + request.getEmail());
        }
    }

    private UserEntity createNewAdmin(AdminRegisterRequest request) {
        return new AdminEntity(request, generateRegistrationId());
    }

    private String generateRegistrationId() {
        LocalDate dataAtual = LocalDate.now();
        long userCount = userRepository.count();
        
        String userCountFormatado = String.format("%05d", userCount);
        return dataAtual.getYear() + userCountFormatado;
    }
}
