package org.oc.paymybuddy.exceptions;

public class InsufficientBalanceException extends IllegalAccessException {
    public InsufficientBalanceException() {
        super("You don't have enough balance for this transfer");
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
