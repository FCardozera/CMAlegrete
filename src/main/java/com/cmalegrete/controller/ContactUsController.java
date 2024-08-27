package com.cmalegrete.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.cmalegrete.dto.request.model.util.ContactMessageRequest;
import com.cmalegrete.service.ContactUsService;

@Controller
@RequestMapping("/fale-conosco")
public class ContactUsController {

    private final SpringTemplateEngine templateEngine;

    @Autowired
    private ContactUsService contactUsService;

    public ContactUsController(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @GetMapping
    public ResponseEntity<String> getContactUsPage() {
        // Contexto do Thymeleaf
        Context context = new Context();

        // Processa o template Thymeleaf e gera o conteúdo HTML como String
        String htmlContent = templateEngine.process("contact-us", context);

        // Retorna a página HTML com status OK
        return new ResponseEntity<>(htmlContent, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> sendContactMessage(@RequestBody ContactMessageRequest request) {
        return contactUsService.sendContactMessage(request);
    }
}
