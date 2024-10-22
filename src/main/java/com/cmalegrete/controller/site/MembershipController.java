package com.cmalegrete.controller.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.cmalegrete.dto.request.model.member.MemberRegisterRequest;
import com.cmalegrete.dto.request.model.util.RequestEmail;
import com.cmalegrete.service.MembershipService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/associe")
public class MembershipController {

    private final SpringTemplateEngine templateEngine;

    @Autowired
    private MembershipService membershipService;

    public MembershipController(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @GetMapping
    public ResponseEntity<String> getMembershipPage() {
        Context context = new Context();
        String htmlContent = templateEngine.process("site/membership", context);

        return new ResponseEntity<>(htmlContent, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> sendMembershipRequest(@Valid @RequestBody MemberRegisterRequest request) {
        return membershipService.sendMembershipRequest(request);
    }

    @PostMapping("/reenviarEmail")
    public ResponseEntity<Object> reenviarEmail(@Valid @RequestBody RequestEmail request) {
        return membershipService.resendEmailtoUser(request);
    }
    
    
}
