package com.cmalegrete.controller.site;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/eventos")
public class EventsController {

    @GetMapping
    public String getEventsPage() {
        return "site/events";
    }
}
