package com.ili.error;

public class NoEleveException extends RuntimeException {
    public NoEleveException(String message) {
        super("Aucun élève trouvé pour l'id : " + message + ".");
    }
}
