package org.oc.paymybuddy.repository;

import org.oc.paymybuddy.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
}
