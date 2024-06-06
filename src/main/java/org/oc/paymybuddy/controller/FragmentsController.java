package org.oc.paymybuddy.controller;

import org.oc.paymybuddy.exceptions.NotAuthenticatedException;
import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
public class FragmentsController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String getProfile(Model model) throws NotAuthenticatedException {
        User connectedUser = userService.getAuthenticatedUser();

        model.addAttribute("user", connectedUser);
        model.addAttribute("page", "profile");

        return "profile";
    }

    @GetMapping("/profile/update-balance")
    public String showUpdateBalancePage(Model model) throws NotAuthenticatedException {
        model.addAttribute("page", "update-balance");
        model.addAttribute("user", userService.getAuthenticatedUser());

        return "update-balance";
    }

    @PostMapping("/profile/update-balance")
    public String updateBalance(@RequestParam String action, BigDecimal amount, Model model,
                                RedirectAttributes redirectAttributes) {
        try {
            switch (action) {
                case "deposit" -> userService.deposit(userService.getAuthenticatedUser(), amount);
                case "withdrawal" -> userService.withdraw(userService.getAuthenticatedUser(), amount);
            }
            redirectAttributes.addFlashAttribute("success",
                    "Your " + action + " of " + amount.toString() + "€ was successful!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/update-balance";
        }
        return "redirect:/profile";
    }

    @GetMapping("/contact")
    public String getContact() {
        return "contact";
    }
}
