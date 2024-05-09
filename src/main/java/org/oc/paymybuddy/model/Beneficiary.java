package org.oc.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@Entity
@Table(name = "beneficiary")
@IdClass(BeneficiaryId.class)
public class Beneficiary implements Serializable {
    @Id
    @Column(name = "sender")
    private Integer sender;
    @Id
    @Column(name = "recipient")
    private Integer recipient;

    public Beneficiary() {}

    public Beneficiary(Integer sender, Integer recipient) {
        this.sender = sender;
        this.recipient = recipient;
    }
}
