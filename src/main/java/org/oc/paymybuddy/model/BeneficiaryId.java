package org.oc.paymybuddy.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Embeddable
public class BeneficiaryId implements Serializable {
    private Integer sender;
    private Integer recipient;

    public BeneficiaryId() {

    }

    public BeneficiaryId(Integer sender, Integer recipient) {
        this.sender = sender;
        this.recipient = recipient;
    }

}
