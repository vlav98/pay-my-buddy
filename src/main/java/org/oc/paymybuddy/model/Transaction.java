package org.oc.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
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
    private BigDecimal amount;
    @Column(name = "bank_accountID")
    private Integer bankAccountID;
    @Column(name = "transaction_date")
    private Date date;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionID, that.transactionID) && Objects.equals(senderID, that.senderID) && Objects.equals(recipient, that.recipient) && Objects.equals(description, that.description) && Objects.equals(amount, that.amount) && Objects.equals(bankAccountID, that.bankAccountID) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionID, senderID, recipient, description, amount, bankAccountID, date);
    }
}
