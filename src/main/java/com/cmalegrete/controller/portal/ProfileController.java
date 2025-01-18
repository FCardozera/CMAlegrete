package com.cmalegrete.controller.portal;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmalegrete.model.user.UserEntity;
import com.cmalegrete.service.portal.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/perfil")
public class ProfileController {

    private final UserService userService;

    @GetMapping
    public String getProfilePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userService.getUserByRegistrationId(userDetails.getUsername());

        model.addAttribute("user", user);

        return "portal/profile";
    }
}
