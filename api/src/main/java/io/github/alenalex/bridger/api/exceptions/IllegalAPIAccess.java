package io.github.alenalex.bridger.api.exceptions;

public class IllegalAPIAccess extends Exception{

    public IllegalAPIAccess() {
    }

    public IllegalAPIAccess(String message) {
        super(message);
    }

    public IllegalAPIAccess(String message, Throwable cause) {
        super(message, cause);
    }
}
