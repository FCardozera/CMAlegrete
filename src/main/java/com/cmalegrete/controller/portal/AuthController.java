package com.cmalegrete.controller.portal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.cmalegrete.dto.request.auth.PasswordResetRequest;
import com.cmalegrete.dto.request.auth.RequestAuthentication;
import com.cmalegrete.dto.request.auth.UserRecoverPasswordRequest;
import com.cmalegrete.dto.response.auth.LoginResponse;
import com.cmalegrete.dto.response.error.ErrorResponse;
// import com.cmalegrete.jwt.JwtToken;
// import com.cmalegrete.jwt.JwtUserDetailsService;
import com.cmalegrete.service.portal.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/entrar")
public class AuthController {

    private final SpringTemplateEngine templateEngine;

    // private final JwtUserDetailsService detailsService;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    public AuthController(SpringTemplateEngine templateEngine, AuthenticationManager authenticationManager, UserService userService) {
        this.templateEngine = templateEngine;
        // this.detailsService = detailsService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<String> getSignInPage() {
        Context context = new Context();

        String htmlContent = templateEngine.process("portal/sign-in", context);

        return new ResponseEntity<>(htmlContent, HttpStatus.OK);
    }

    // @PostMapping("/login")
    // public ResponseEntity<?> autenticar(@RequestBody @Valid RequestAuthentication dto, HttpServletRequest request) {
    //     try {
    //         UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
    //                 dto.getEmail(), dto.getPassword());

    //         authenticationManager.authenticate(authenticationToken);

    //         JwtToken token = detailsService.getTokenAuthenticated(dto.getEmail());

    //         LoginResponse response = new LoginResponse(token.getToken());

    //         return ResponseEntity.ok(response);
    //     } catch (Exception ex) {
    //     }
    //     return ResponseEntity
    //             .badRequest()
    //             .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Credenciais inválidas", request));
    // }

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
