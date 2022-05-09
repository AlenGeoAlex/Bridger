package io.github.alenalex.bridger.exceptions;

public final class IllegalThreadOperation extends RuntimeException{

    public IllegalThreadOperation() {
    }

    public IllegalThreadOperation(String message) {
        super(message);
    }

    public IllegalThreadOperation(String message, Throwable cause) {
        super(message, cause);
    }
}
