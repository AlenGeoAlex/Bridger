package io.github.alenalex.bridger.exceptions;

/**
 * If a plugin tried to register again requesting the plugin api, this error will be thrown
 */
public class DuplicateAPIRequest extends Exception{


    public DuplicateAPIRequest(String message) {
        super(message);
    }

    public DuplicateAPIRequest(String message, Throwable cause) {
        super(message, cause);
    }
}
