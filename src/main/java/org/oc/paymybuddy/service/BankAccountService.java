package org.oc.paymybuddy.service;

import org.oc.paymybuddy.model.BankAccount;
import org.oc.paymybuddy.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    public BankAccount create(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    public void delete(BankAccount bankAccount) {
        bankAccountRepository.delete(bankAccount);
    }

    public Iterable<BankAccount> getBankAccounts() {
        return bankAccountRepository.findAll();
    }
}
