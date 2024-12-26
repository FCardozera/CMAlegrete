package com.cmalegrete.controller.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmalegrete.dto.request.model.util.ContactMessageRequest;
import com.cmalegrete.service.site.ContactUsService;

@Controller
@RequestMapping("/fale-conosco")
public class ContactUsController {

    @Autowired
    private ContactUsService contactUsService;

    @GetMapping
    public String getContactUsPage() {
        return "site/contact-us";
    }

    @PostMapping
    public ResponseEntity<Object> sendContactMessage(@RequestBody ContactMessageRequest request) {
        return contactUsService.sendContactMessage(request);
    }
}
