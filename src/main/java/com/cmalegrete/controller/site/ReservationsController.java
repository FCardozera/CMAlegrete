package com.cmalegrete.controller.site;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reservas")
public class ReservationsController {

    @GetMapping
    public String getReservationsPage() {
        return "site/reservations";
    }
}
