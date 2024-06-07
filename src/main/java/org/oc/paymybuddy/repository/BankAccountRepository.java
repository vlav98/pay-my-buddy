package org.oc.paymybuddy.repository;

import org.oc.paymybuddy.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    Boolean existsByEmailAndUserID(String email, Integer userID);
    Boolean existsByUserID(Integer userID);

    BankAccount findBankAccountByUserID(Integer userID);
}
