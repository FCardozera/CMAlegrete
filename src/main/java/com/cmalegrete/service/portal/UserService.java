package com.cmalegrete.service.portal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.cmalegrete.dto.request.auth.UserRecoverPasswordRequest;
import com.cmalegrete.exception.generic.NoContentException;
import com.cmalegrete.exception.generic.NotFoundException;
import com.cmalegrete.model.user.UserEntity;
import com.cmalegrete.model.user.UserRepository;
import com.cmalegrete.service.site.EmailSendService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final JwtService jwtService;

    AuthenticationManager authManager;

    private final UserRepository userRepository;

    private final EmailSendService emailSendService;

    private static final String USER_NOT_FOUND = "Usuário não encontrado, ID: ";

    public String verify(UserEntity user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getRegistrationId(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getRegistrationId());
        } else {
            return "fail";
        }
    }

    public void recoverPassword(@Valid UserRecoverPasswordRequest request) {
        try {
            UserEntity user = userRepository.findByEmail(request.getEmail());

            String token = UUID.randomUUID().toString();
            user.setPasswordResetToken(token);
            user.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(24));

            userRepository.save(user);

            emailSendService.sendPasswordResetConfirmationEmailToUser(user);
        } catch (Exception e) {
            throw new NotFoundException("E-mail não cadastrado.");
        }
    }

    public boolean isTokenValid(String token) {
        UserEntity user = userRepository.findByPasswordResetToken(token);
        return user.isPasswordResetTokenValid(token);
    }

    public void updatePassword(String token, String newPassword) {
        UserEntity user = userRepository.findByPasswordResetToken(token);
        user.setPassword(newPassword);
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiry(null);
        userRepository.save(user);
    }

    public ResponseEntity<List<UserEntity>> listUsers() {
        List<UserEntity> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new NoContentException("Não há usuários registrados");
        }

        return ResponseEntity.ok(users);
    }

    public ResponseEntity<Object> searchUserById(UUID id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + id));

        return ResponseEntity.ok(user);
    }

    public ResponseEntity<Object> deleteUserById(UUID id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + id));

        userRepository.delete(user);

        return ResponseEntity.noContent().build();
    }
}

