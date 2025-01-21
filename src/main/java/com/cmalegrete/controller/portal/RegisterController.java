package com.cmalegrete.controller.portal;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmalegrete.dto.request.model.admin.AdminRegisterRequest;
import com.cmalegrete.dto.request.model.assistant.AssistantRegisterRequest;
import com.cmalegrete.service.portal.AdminService;
import com.cmalegrete.service.portal.AssistantService;

import jakarta.validation.Valid;
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
    public ResponseEntity<Object> registerAdmin(@Valid @RequestBody AdminRegisterRequest request) {
        return adminService.registerAdmin(request);
    }

    @PostMapping("/assistente")
    public ResponseEntity<Object> registerAssistant(@Valid @RequestBody AssistantRegisterRequest request) {
        return assistantService.registerAssistant(request);
    }
}
