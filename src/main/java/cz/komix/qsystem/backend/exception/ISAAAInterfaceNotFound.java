package cz.komix.qsystem.backend.exception;

/**
 * @author Jan Lejnar
 */
public class ISAAAInterfaceNotFound extends Exception {
    public ISAAAInterfaceNotFound() {
    }

    public ISAAAInterfaceNotFound(String message) {
        super(message);
    }

    public ISAAAInterfaceNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public ISAAAInterfaceNotFound(Throwable cause) {
        super(cause);
    }

    public ISAAAInterfaceNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
