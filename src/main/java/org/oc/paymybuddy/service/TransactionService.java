package org.oc.paymybuddy.service;

import org.oc.paymybuddy.model.Transaction;
import org.oc.paymybuddy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction create(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public void delete(Transaction transaction) {
        transactionRepository.delete(transaction);
    }
    
    public Iterable<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }
}