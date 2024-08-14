package com.cmalegrete.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Controller
@RequestMapping("/reservas")
public class ReservationsController {

    private final SpringTemplateEngine templateEngine;

    public ReservationsController(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @GetMapping
    public ResponseEntity<String> getReservationsPage() {
        // Contexto do Thymeleaf
        Context context = new Context();

        // Processa o template Thymeleaf e gera o conteúdo HTML como String
        String htmlContent = templateEngine.process("reservations", context);

        // Retorna a página HTML com status OK
        return new ResponseEntity<>(htmlContent, HttpStatus.OK);
    }
}
