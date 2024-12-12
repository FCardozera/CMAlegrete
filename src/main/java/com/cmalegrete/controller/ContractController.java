package com.cmalegrete.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.cmalegrete.service.ContractService;
import com.cmalegrete.dto.request.model.contract.ContractRequest;

@Controller
@RequestMapping("/enviar-contrato")
public class ContractController {

    private final SpringTemplateEngine templateEngine;

    @Autowired
    private ContractService contractService;

    public ContractController(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @GetMapping
    public ResponseEntity<String> getContractPage(@RequestParam String token) {
        Context context = new Context();
        if (contractService.checkUrlToken(token)) {
            String htmlContent = templateEngine.process("send-contract", context);
            return new ResponseEntity<>(htmlContent, HttpStatus.OK);
        }
        String htmlContent = templateEngine.process("index", context);
        return new ResponseEntity<>(htmlContent, HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Object> sendContract(@ModelAttribute ContractRequest request) {
        return contractService.sendContract(request);
    }

}