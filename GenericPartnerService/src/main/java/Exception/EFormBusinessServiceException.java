package Exception;

public class EFormBusinessServiceException extends RuntimeException{
    public EFormBusinessServiceException(final EFormsErrorCode errorCode, final String message) {
        super(message);
    }
}
