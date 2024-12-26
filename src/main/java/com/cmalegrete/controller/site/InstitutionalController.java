package com.cmalegrete.controller.site;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/institucional")
public class InstitutionalController {

    @GetMapping
    public String getInstitutionalPage() {
        return "site/institutional";
    }
}
