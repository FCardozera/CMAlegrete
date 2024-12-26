package com.cmalegrete.controller.portal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/socios")
public class MembersController {

    @GetMapping
    public String getMembersPage() {
        return "portal/tables";
    }
}
