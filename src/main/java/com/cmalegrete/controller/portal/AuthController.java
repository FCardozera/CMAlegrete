package com.cmalegrete.controller.portal;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmalegrete.dto.request.auth.PasswordResetRequest;
import com.cmalegrete.dto.request.auth.UserRecoverPasswordRequest;
import com.cmalegrete.service.portal.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "portal/sign-in";
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<Object> recoverPassword(@Valid @RequestBody UserRecoverPasswordRequest request) {
        try {
            userService.recoverPassword(request);
            return ResponseEntity.ok("E-mail de recuperação de senha enviado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/recuperar-senha/{token}")
    public ResponseEntity<String> resetPassword(@PathVariable String token, @RequestBody PasswordResetRequest request) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.badRequest().body("Link inválido ou expirado.");
        }

        if (request.getPassword().equals(request.getConfirmPassword())) {
            userService.updatePassword(token, request.getPassword());
            return ResponseEntity.ok("Senha redefinida com sucesso.");
        } else {
            return ResponseEntity.badRequest().body("As senhas não coincidem.");
        }
    }

}
