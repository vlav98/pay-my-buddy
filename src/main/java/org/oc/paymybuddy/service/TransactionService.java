package org.oc.paymybuddy.service;

import jakarta.transaction.Transactional;
import org.oc.paymybuddy.constants.Fee;
import org.oc.paymybuddy.exceptions.InsufficientBalanceException;
import org.oc.paymybuddy.model.Transaction;
import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.model.dto.AmountAndFee;
import org.oc.paymybuddy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private BeneficiaryService beneficiaryService;
    @Autowired
    private PaginationService paginationService;
    @Autowired
    private BankAccountService bankAccountService;

    @Transactional
    public Transaction create(User sender, User recipient, String description, double amount) throws Exception {
        if (amount <= 0) {
            throw new Exception("Transaction can't be negative or equal to 0");
        }
        BigDecimal amountWithFees = calculateAmountWithFee(amount).getAmountWithFee();
        if (sender.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
            throw new InsufficientBalanceException();
        }

        if (Objects.equals(sender, recipient)) {
            throw new Exception("You can't send money to yourself !");
        }

        boolean hasRecipientId = beneficiaryService.getBeneficiaryBySender(sender, recipient);
        if (!hasRecipientId) {
            throw new Exception("Sender doesn't have recipient as friend.");
        }

        sender.setBalance(sender.getBalance().subtract(amountWithFees));
        recipient.setBalance(recipient.getBalance().add(BigDecimal.valueOf(amount)));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setBankAccountID(bankAccountService.getBankAccountByUserId(sender.getUserID()).getBankAccountID());
        transaction.setAmount(BigDecimal.valueOf(amount));
        transaction.setSenderID(sender.getUserID());
        transaction.setRecipient(recipient.getEmail());

        return transactionRepository.save(transaction);
    }

    public Iterable<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getUserTransactionById(Integer id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> getAllUserTransactionsByUser(User user) {
        return transactionRepository.findAllBySenderIDOrRecipient(user.getUserID(), user.getEmail());
    }

    public Iterable<Transaction> getCurrentUserTransaction(User user) {
        return transactionRepository.getTransactionBySenderID(user.getUserID());
    }

    public Page<?> findPaginatedResults(Pageable pageable, User user) {
        List<Transaction> transactions = getAllUserTransactionsByUser(user);
        return paginationService.getPaginatedList(pageable, transactions);
    }

    public AmountAndFee calculateAmountWithFee(double amount) {
        BigDecimal bigDecimalAmount = new BigDecimal(Double.toString(amount)).setScale(Fee.SCALE, RoundingMode.HALF_UP);
        BigDecimal bigDecimalFee = new BigDecimal(Double.toString(amount * Fee.TRANSACTION_FEE)).setScale(Fee.SCALE, RoundingMode.HALF_UP);

        return new AmountAndFee(
                bigDecimalAmount,
                bigDecimalFee,
                bigDecimalAmount.add(bigDecimalFee)
        );
    }
}
