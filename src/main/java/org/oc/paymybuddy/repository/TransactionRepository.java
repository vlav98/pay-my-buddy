package org.oc.paymybuddy.repository;

import org.oc.paymybuddy.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findAllBySenderIDOrRecipientOrderByDateDesc(Integer senderId, String recipient);

}
