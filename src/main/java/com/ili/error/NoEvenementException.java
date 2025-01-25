package com.ili.error;

public class NoEvenementException extends RuntimeException{
    public NoEvenementException(String id) {
        super("No evenement found with id: " + id);
    }
}
