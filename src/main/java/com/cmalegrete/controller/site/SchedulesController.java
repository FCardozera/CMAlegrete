package com.cmalegrete.controller.site;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/horarios")
public class SchedulesController {

    @GetMapping
    public String getSchedulesPage() {
        return "site/schedules";
    }
}
