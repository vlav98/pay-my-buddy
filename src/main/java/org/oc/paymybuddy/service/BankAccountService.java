package org.oc.paymybuddy.service;

import org.oc.paymybuddy.model.BankAccount;
import org.oc.paymybuddy.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    public BankAccount create(BankAccount bankAccount) throws Exception {
        boolean accountExists = bankAccountRepository.existsByUserID(bankAccount.getUserID());
        if (accountExists) throw new Exception("This user already have an account !");
        else {
            return bankAccountRepository.save(bankAccount);
        }
    }

    public void delete(BankAccount bankAccount) throws Exception {
        boolean accountExists = bankAccountRepository.existsByUserID(bankAccount.getUserID());
        if (accountExists) bankAccountRepository.delete(bankAccount);
        else {
            throw new Exception("This bank account doesn't exist.");
        }
    }

    public Iterable<BankAccount> getBankAccounts() {
        return bankAccountRepository.findAll();
    }

    public BankAccount getBankAccountByUserId(Integer userId) {
        return bankAccountRepository.findBankAccountByUserID(userId);
    }
}
