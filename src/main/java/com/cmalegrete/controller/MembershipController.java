package com.cmalegrete.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.cmalegrete.dto.request.model.member.MemberRegisterRequest;
import com.cmalegrete.service.MembershipService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/associe-se")
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
        String htmlContent = templateEngine.process("membership", context);

        return new ResponseEntity<>(htmlContent, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> sendMembershipRequest(@RequestBody MemberRegisterRequest request) {
        return membershipService.sendMembershipRequest(request);
    }
    
}
