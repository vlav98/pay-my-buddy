package org.oc.paymybuddy.model.viewModel;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionFormViewModel {
    String recipient;
    double amount;
    double amountWithFee;
    String description;
}
