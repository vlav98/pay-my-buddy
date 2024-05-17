package org.oc.paymybuddy.repository;

import org.oc.paymybuddy.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    boolean existsByTransactionID(Integer ID);


    Transaction getTransactionBySenderIDAndAmountAndRecipient(Integer senderID, Integer amount, String recipient);
    Iterable<Transaction> getTransactionBySenderID(Integer senderID);
    Iterable<Transaction> getTransactionByBankAccountID(Integer bankAccountID);

    Iterable<Transaction> getTransactionByRecipient(String recipient);

}
