package org.oc.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "bank_accounts")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_accountID")
    private Integer bankAccountID;
    @Column(name = "email")
    private String email;
    @Column(name = "RIB")
    private String RIB;
    @Column(name = "userID")
    private Integer userID;
}
