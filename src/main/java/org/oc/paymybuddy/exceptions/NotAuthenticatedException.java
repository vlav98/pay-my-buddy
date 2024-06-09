package org.oc.paymybuddy.exceptions;

import java.nio.file.AccessDeniedException;

public class NotAuthenticatedException extends AccessDeniedException {

    public NotAuthenticatedException() {
        super("You are not authenticated.");
    }

    public NotAuthenticatedException(String message) {
        super(message);
    }
}
