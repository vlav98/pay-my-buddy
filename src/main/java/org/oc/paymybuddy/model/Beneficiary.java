package org.oc.paymybuddy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "beneficiary")
public class Beneficiary {
    @Id
    @Column(name = "sender")
    private Integer sender;
    @Id
    @Column(name = "recipient")
    private Integer recipient;

}
