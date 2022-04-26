package io.github.alenalex.bridger.exceptions;

public class UIManipulationException extends RuntimeException{

    public UIManipulationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UIManipulationException(String message) {
        super(message);
    }
}
