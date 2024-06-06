package org.oc.paymybuddy.model.viewModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionFormViewModel {
    String recipient;
    double amount;
    double amountWithFee;
    String description;
}
