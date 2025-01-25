package com.ili.error;

public class EvenementCreateError extends RuntimeException{
    public EvenementCreateError(String message) {
        super(message);
    }
}
