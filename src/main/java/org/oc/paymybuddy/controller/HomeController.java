package org.oc.paymybuddy.controller;

import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.service.BeneficiaryService;
import org.oc.paymybuddy.service.TransactionService;
import org.oc.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private BeneficiaryService beneficiaryService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public String showHomePage(Model model) throws Exception {
        User connectedUser = userService.getAuthenticatedUser();

        model.addAttribute("user", connectedUser);
        model.addAttribute("page", "home");
        return "home";
    }
}
