package com.cmalegrete.controller.portal;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmalegrete.model.assistant.AssistantEntity;
import com.cmalegrete.service.portal.AdminService;
import com.cmalegrete.service.portal.AssistantService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
@RequestMapping("/registrar")
public class RegisterController {

    private final AdminService adminService;

    private final AssistantService assistantService;

    @PostMapping("/admin")
    public ResponseEntity<Object> registerAdmin(@RequestBody String entity) {
        //TODO: process POST request
        
        return ResponseEntity.ok().build();
    }

    @PostMapping("/assistant")
    public ResponseEntity<Object> registerAssistant(@RequestBody String entity) {
        //TODO: process POST request
        
        return ResponseEntity.ok().build();
    }
}
