package model;

public class IBPMServiceException extends RuntimeException {

    public IBPMServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    public IBPMServiceException(String message) {
        super(message);
    }
}
