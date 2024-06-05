package org.oc.paymybuddy.exceptions;

public class AlreadyExistingUserException extends IllegalAccessException {

    public AlreadyExistingUserException() {
        super("An user with the same mail already exists !");
    }

    public AlreadyExistingUserException(String message) {
        super(message);
    }
}
