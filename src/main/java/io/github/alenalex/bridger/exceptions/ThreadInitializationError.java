package io.github.alenalex.bridger.exceptions;

public final class ThreadInitializationError extends RuntimeException{

    public ThreadInitializationError(String message) {
        super(message);
    }

    public ThreadInitializationError(String message, Throwable cause) {
        super(message, cause);
    }
}
