package org.oc.paymybuddy.exceptions;

import javassist.NotFoundException;

public class InexistentUserException extends NotFoundException {
    public InexistentUserException(String message) {
        super(message);
    }
}
