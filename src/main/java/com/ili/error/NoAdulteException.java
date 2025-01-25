package com.ili.error;

public class NoAdulteException extends RuntimeException{
    public NoAdulteException(String message) {
        super("Aucun adulte trouvé pour l'id : " + message + ".");
    }
}
