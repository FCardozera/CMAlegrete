package com.cmalegrete.controller.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmalegrete.dto.request.model.contract.ContractRequest;
import com.cmalegrete.service.site.ContractService;

@Controller
@RequestMapping("/enviar-contrato")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @GetMapping
    public String getContractPage(@RequestParam String token) {
        if (contractService.checkUrlToken(token)) {
            return "site/send-contract";
        }
        return "site/index";
    }

    @PostMapping
    public ResponseEntity<Object> sendContract(@ModelAttribute ContractRequest request) {
        return contractService.sendContract(request);
    }

}