package com.cmalegrete.controller.portal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pagamento")
public class BillingController {

    @GetMapping
    public String getBillingPage() {
        // return "portal/billing";
        return "portal/dashboard";
    }
}
