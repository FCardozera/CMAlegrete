package com.cmalegrete.controller.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmalegrete.dto.request.model.member.MemberRegisterRequest;
import com.cmalegrete.dto.request.model.util.RequestEmail;
import com.cmalegrete.service.site.MembershipService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/associe")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @GetMapping
    public String getMembershipPage() {
        return "site/membership";
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
