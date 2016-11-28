package pl.polsl.exception;

/**
 * Created by Mateusz on 20.11.2016.
 */
public class StorageException extends RuntimeException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}