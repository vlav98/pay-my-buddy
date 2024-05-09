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

    public void delete(Transaction transaction) throws Exception {
        boolean transactionExists = transactionRepository.existsByTransactionID(transaction.getTransactionID());
        if (transactionExists) {
            transactionRepository.deleteById(transaction.getTransactionID());
        } else {
            throw new Exception("The transaction you're trying to delete doesn't exist.");
        }
    }

    public Transaction updateDescription(Transaction transaction, String description) throws Exception {
        Transaction transactionToUpdate = transactionRepository.getTransactionBySenderIDAndAmountAndRecipient(transaction.getSenderID(),
                transaction.getAmount(),
                transaction.getRecipient());
        if (transactionToUpdate != null) {
            transactionToUpdate.setDescription(description);
            return transactionRepository.save(transactionToUpdate);
        } else {
            throw new Exception("The transaction you're trying to update doesn't exist.");
        }
    }
    
    public Iterable<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }
}
