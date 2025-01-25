package com.ili.error;

public class NoCommandeException extends Exception {
    public NoCommandeException(String arg, String value) {
        super(String.format("No commande found with %s : %s", arg, value));
    }
}
