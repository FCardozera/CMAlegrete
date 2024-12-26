package com.cmalegrete.controller.portal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/perfil")
public class ProfileController {

    @GetMapping
    public String getProfilePage() {
        return "portal/profile";
    }
}
