package io.github.alenalex.bridger.exceptions;

public class IllegalThreadOperation extends RuntimeException{

    public IllegalThreadOperation() {
    }

    public IllegalThreadOperation(String message) {
        super(message);
    }

    public IllegalThreadOperation(String message, Throwable cause) {
        super(message, cause);
    }
}
