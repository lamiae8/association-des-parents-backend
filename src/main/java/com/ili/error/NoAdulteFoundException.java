package com.ili.error;

public class NoAdulteFoundException extends RuntimeException{
    public NoAdulteFoundException() {
        super("Aucun adulte trouvé.");
    }
}
