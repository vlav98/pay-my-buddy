package org.oc.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionID")
    private Integer transactionID;
    @Column(name = "sender_ID")
    private Integer senderID;
    @Column(name = "recipient")
    private String recipient;
    @Column(name = "description")
    private String description;
    @Column(name = "amount")
    private Integer amount;
    @Column(name = "bank_accountID")
    private Integer bankAccountID;
}
