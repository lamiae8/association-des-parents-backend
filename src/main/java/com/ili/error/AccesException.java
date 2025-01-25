package com.ili.error;

public class AccesException extends RuntimeException{
    public AccesException(String message) {
        super("Vous n'êtes pas autorisé à effectuer cette action: "+ message);
    }
}
