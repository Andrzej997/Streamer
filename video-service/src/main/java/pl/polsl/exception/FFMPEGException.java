package pl.polsl.exception;


public class FFMPEGException extends Exception {

    public FFMPEGException(String message) {
        super(message);
    }

    public FFMPEGException(String message, Throwable cause) {
        super(message, cause);
    }
}