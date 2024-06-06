package org.oc.paymybuddy.controller;

import jakarta.servlet.http.HttpSession;
import org.oc.paymybuddy.constants.Pagination;
import org.oc.paymybuddy.exceptions.BuddyNotFoundException;
import org.oc.paymybuddy.model.Transaction;
import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.model.viewModel.TransactionFormViewModel;
import org.oc.paymybuddy.service.BeneficiaryService;
import org.oc.paymybuddy.service.TransactionService;
import org.oc.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private BeneficiaryService beneficiaryService;

    @GetMapping
    public String showTransactionPage(Model model, HttpSession httpSession,
                                      @RequestParam(value = "page", required = false) Integer page,
                                      @RequestParam(value = "size", required = false) Integer size) throws Exception {
        String successMessage = (String) httpSession.getAttribute("success");
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
            httpSession.removeAttribute("success");
        }

        User connectedUser = userService.getAuthenticatedUser();
        Iterable<User> beneficiaries = beneficiaryService.getBeneficiariesUserBySender(connectedUser);


        int currentPage = page == null ? Pagination.DEFAULT_PAGE : page;
        int pageSize = size == null ? Pagination.DEFAULT_SIZE : size;

        Page<?> pagedList = transactionService.findPaginatedResults(
                PageRequest.of(currentPage -1, pageSize),
                connectedUser);

        model.addAttribute("pagedList", pagedList);
        model.addAttribute("totalTransactionItems", pagedList.getTotalElements());

        model.addAttribute("connectedUser", connectedUser);
        model.addAttribute("recipient", "beneficiary");
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("beneficiaries", beneficiaries);
        model.addAttribute("transactionForm", new TransactionFormViewModel());

        return "transaction";
    }

    @GetMapping("/add-beneficiary")
    public String showAddConnectionPage(Model model) {
        model.addAttribute("page", "add-beneficiary");
        return "add-beneficiary";
    }

    @PostMapping("/add-beneficiary")
    public String addConnection(String email, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
        try {
            beneficiaryService.addNewBeneficiary(userService.getAuthenticatedUser(), email);
            redirectAttributes.addFlashAttribute("successMessage", "Your buddy has been successfully added!");
            return "redirect:/transaction";
        } catch (BuddyNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "/add-beneficiary";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while adding beneficiary.");
            return "/add-beneficiary";
        }
    }

    @GetMapping("/pay")
    public String showPaymentPage(TransactionFormViewModel transactionFormViewModel, Model model) {
        model.addAttribute("page", "payment");
        model.addAttribute("transactionForm", transactionFormViewModel);
        return "payment";
    }

    @PostMapping("/pay")
    public String pay(@RequestParam String action,
                      TransactionFormViewModel transactionFormViewModel,
                      Model model,
                      RedirectAttributes redirectAttributes) {
        String recipientEmail = transactionFormViewModel.getRecipient();
        try {
            model.addAttribute("page", "payment");
            switch (action) {
                case "payment" -> {
                    Optional<User> recipientUser = userService.getUserByEmail(recipientEmail);
                    if (recipientUser.isEmpty()) {
                        String errorMessage = "Email " + recipientEmail + " does not match any buddy.";
                        throw new BuddyNotFoundException(errorMessage);
                    }
                    transactionService.create(userService.getAuthenticatedUser(),
                            recipientUser.get(),
                            transactionFormViewModel.getDescription(),
                            transactionFormViewModel.getAmount());
                    redirectAttributes.addFlashAttribute("success",
                            "You successfully transferred " + transactionFormViewModel.getAmount() + "€ to " + transactionFormViewModel.getRecipient());
                }
                case "redirect" -> {
                    Map<String, BigDecimal> amountAndFee = transactionService.calculateAmountWithFee(
                            transactionFormViewModel.getAmount());
                    model.addAttribute("transactionForm", transactionFormViewModel);
                    model.addAttribute("amountWithFee", amountAndFee.get("amountWithFee"));
                    return "/payment";
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/transaction";
    }

    @GetMapping("/id")
    public Optional<Transaction> getTransactionById(@PathVariable Integer id) {
        return transactionService.getUserTransactionById(id);
    }


}
