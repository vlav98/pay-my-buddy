package org.oc.paymybuddy.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/signup")
public class SignUpController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LogManager.getLogger(SignUpController.class.getName());

    @GetMapping
    public String showSignUpPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping
    public String signup(@ModelAttribute User user, Model model) {
        try {
            userService.create(user);
            logger.info("User created: " + user.getEmail());
            return "redirect:/login";
        } catch (Exception e) {
            return "redirect:/signup";
        }
    }
}
