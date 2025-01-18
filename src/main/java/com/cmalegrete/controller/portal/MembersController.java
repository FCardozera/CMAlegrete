package com.cmalegrete.controller.portal;

import java.util.List;

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
@RequestMapping("/socios")
public class MembersController {

    private final UserService userService;

    @GetMapping
    public String getMembersPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userService.getUserByRegistrationId(userDetails.getUsername());
        List<UserEntity> memberList = userService.getAllMembers();
        List<UserEntity> assistantList = userService.getAllAssistants();
        List<UserEntity> adminList = userService.getAllAdmins();

        model.addAttribute("user", user);
        model.addAttribute("memberList", memberList);
        model.addAttribute("assistantList", assistantList);
        model.addAttribute("adminList", adminList);

        return "portal/tables";
    }
}
