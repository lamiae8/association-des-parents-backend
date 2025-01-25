package com.ili.error;

public class NoEleveFoundException extends RuntimeException {
    public NoEleveFoundException() {
        super("Aucun éléve trouvé.)");
    }
}
