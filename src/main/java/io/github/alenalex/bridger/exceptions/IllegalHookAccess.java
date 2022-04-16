package io.github.alenalex.bridger.exceptions;

public class IllegalHookAccess extends RuntimeException{

    public IllegalHookAccess(String message) {
        super(message);
    }

    public IllegalHookAccess(String message, Throwable cause) {
        super(message, cause);
    }
}
