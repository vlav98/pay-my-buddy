package org.oc.paymybuddy.controller;

import org.oc.paymybuddy.model.Transaction;
import org.oc.paymybuddy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public List<Transaction> getTransactions() {
        return (List<Transaction>) transactionService.getTransactions();
    }

    @GetMapping("/id")
    public Optional<Transaction> getTransactionById(@PathVariable Integer id) {
        return transactionService.getUserTransactions(id);
    }
}
