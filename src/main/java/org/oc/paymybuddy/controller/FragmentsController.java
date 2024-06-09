package org.oc.paymybuddy.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oc.paymybuddy.constants.Pagination;
import org.oc.paymybuddy.exceptions.NotAuthenticatedException;
import org.oc.paymybuddy.model.Beneficiary;
import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.model.dto.ContactMessage;
import org.oc.paymybuddy.service.BeneficiaryService;
import org.oc.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
public class FragmentsController {
    @Autowired
    private UserService userService;
    @Autowired
    private BeneficiaryService beneficiaryService;

    private static final Logger logger = LogManager.getLogger(FragmentsController.class.getName());

    @GetMapping("/profile")
    public String showProfile(Model model,
                              @RequestParam(value = "page", required = false) Integer page,
                              @RequestParam(value = "size", required = false) Integer size) throws Exception {
        User connectedUser = userService.getAuthenticatedUser();

        int currentPage = page == null ? Pagination.DEFAULT_PAGE : page;
        int pageSize = size == null ? Pagination.DEFAULT_SIZE : size;

        Page<?> pagedList = beneficiaryService.findPaginatedResults(
                PageRequest.of(currentPage -1, pageSize),
                connectedUser);

        model.addAttribute("totalBeneficiaryItems", pagedList.getTotalElements());
        model.addAttribute("pagedList", pagedList);
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

    @PostMapping("/contact")
    public String sendContactMessage(@ModelAttribute ContactMessage contactMessage, RedirectAttributes redirectAttributes) {
        logger.info("Received a message with object: {} and message content : {}",
                contactMessage.getObject(), contactMessage.getMessageText());
        redirectAttributes.addFlashAttribute("successMessage", "Your message has been received!");
        return "redirect:/contact";
    }
}
