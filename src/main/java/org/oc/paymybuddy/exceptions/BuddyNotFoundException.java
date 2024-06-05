package org.oc.paymybuddy.exceptions;

import javassist.NotFoundException;

public class BuddyNotFoundException extends NotFoundException {
    public BuddyNotFoundException(String message) {
        super(message);
    }
}
