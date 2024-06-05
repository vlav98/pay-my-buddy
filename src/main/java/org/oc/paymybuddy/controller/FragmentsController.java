package org.oc.paymybuddy.controller;

import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FragmentsController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String getProfile(Model model) throws Exception {
        User connectedUser = userService.getAuthenticatedUser();

        model.addAttribute("user", connectedUser);
        model.addAttribute("page", "profile");

        return "profile";
    }

    @GetMapping("/contact")
    public String getContact() {
        return "contact";
    }
}
