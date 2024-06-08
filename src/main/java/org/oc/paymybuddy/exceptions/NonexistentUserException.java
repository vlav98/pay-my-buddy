package org.oc.paymybuddy.exceptions;

import javassist.NotFoundException;

public class NonexistentUserException extends NotFoundException {

    public NonexistentUserException() {
        super("The user you're trying to delete doesn't exist.");
    }

    public NonexistentUserException(String message) {
        super(message);
    }
}
