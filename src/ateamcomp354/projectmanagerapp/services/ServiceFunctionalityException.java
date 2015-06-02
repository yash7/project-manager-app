package ateamcomp354.projectmanagerapp.services;

/**
 * An exception to be thrown by the service functions that have failed.
 */
public class ServiceFunctionalityException extends RuntimeException {

    public ServiceFunctionalityException(String message) {
        super(message);
    }

    public ServiceFunctionalityException(String message, Throwable cause) {
        super(message, cause);
    }
}
