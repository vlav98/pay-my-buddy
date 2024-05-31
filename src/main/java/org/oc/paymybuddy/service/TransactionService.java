package org.oc.paymybuddy.service;

import jakarta.transaction.Transactional;
import org.oc.paymybuddy.model.Beneficiary;
import org.oc.paymybuddy.model.Transaction;
import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Service
//@Transactional -> par méthode
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private BeneficiaryService beneficiaryService;

    @Transactional
    public Transaction create(User sender, User recipient, String description, double amount) throws Exception {
        if (amount <= 0) {
            throw new Exception("Transaction can't be negative or equal to 0");
        }
        BigDecimal amountWithFees = new BigDecimal(amount*0.05);
        if (sender.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
            throw new Exception("Sender doesn't have enough balance for this transfer");
        }

        Iterable<Beneficiary> beneficiaries = beneficiaryService.getBeneficiariesBySender(sender);
        boolean hasRecipientId = false;
        for (Beneficiary beneficiary: beneficiaries) {
            if (Objects.equals(beneficiary.getRecipient(), recipient.getUserID())) {
                hasRecipientId = true;
                break;
            }
        }

        if (!hasRecipientId) {
            throw new Exception("Sender doesn't have recipient as friend.");
        }

        sender.setBalance(sender.getBalance().subtract(amountWithFees));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(BigDecimal.valueOf(amount));
        transaction.setSenderID(sender.getUserID());
        transaction.setRecipient(recipient.getEmail());

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

//    public Transaction updateDescription(Transaction transaction, String description) throws Exception {
//        Transaction transactionToUpdate = transactionRepository.getTransactionBySenderIDAndAmountAndRecipient(transaction.getSenderID(),
//                transaction.getAmount(),
//                transaction.getRecipient());
//        if (transactionToUpdate != null) {
//            transactionToUpdate.setDescription(description);
//            return transactionRepository.save(transactionToUpdate);
//        } else {
//            throw new Exception("The transaction you're trying to update doesn't exist.");
//        }
//    }
    
    public Iterable<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getUserTransactions(Integer id) {
        return transactionRepository.findById(id);
    }
}
