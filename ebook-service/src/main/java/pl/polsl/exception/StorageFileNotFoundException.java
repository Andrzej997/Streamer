package pl.polsl.exception;

/**
 * Created by Mateusz on 20.11.2016.
 */
public class StorageFileNotFoundException extends StorageException {

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageFileNotFoundException(String message) {
        super(message);
    }

}
