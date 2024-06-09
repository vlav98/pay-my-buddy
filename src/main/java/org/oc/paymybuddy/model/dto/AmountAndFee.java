package org.oc.paymybuddy.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
public class AmountAndFee {
    BigDecimal amount;
    BigDecimal fee;
    BigDecimal amountWithFee;
}
