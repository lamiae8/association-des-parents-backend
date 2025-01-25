package com.ili.error;

public class NoEvenementFoundException extends RuntimeException{
    public NoEvenementFoundException() {
        super("No evenement found");
    }
}
