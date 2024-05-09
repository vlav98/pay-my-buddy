package org.oc.paymybuddy;

import org.oc.paymybuddy.model.BankAccount;
import org.oc.paymybuddy.model.Transaction;
import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.service.BankAccountService;
import org.oc.paymybuddy.service.TransactionService;
import org.oc.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.oc.paymybuddy.repository")
public class PayMyBuddyApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private TransactionService transactionService;

    public static void main(String[] args) {
        SpringApplication.run(PayMyBuddyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Iterable<User> users = userService.getUsers();
        users.forEach(user -> System.out.println(user.getEmail()));
        User user = userService.create(new User("Val", "val@gmail.com", "Valou1234"));
        User updateUser = userService.getUserByEmailAndFirstName("user1@example.com", "User1" );
        updateUser.setPassword("Update");
        userService.update(updateUser);
        userService.getUsers().forEach(eachUser -> System.out.println(eachUser.getEmail()));
        userService.delete(user);

        Iterable<BankAccount> bankAccounts = bankAccountService.getBankAccounts();
        bankAccounts.forEach(bankAccount -> System.out.println(bankAccount.getEmail()));

        Iterable<Transaction> transactions = transactionService.getTransactions();
        transactions.forEach(transaction -> System.out.println(transaction.getRecipient()));
    }
}
