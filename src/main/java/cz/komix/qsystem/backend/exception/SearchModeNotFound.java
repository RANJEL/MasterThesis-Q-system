package cz.komix.qsystem.backend.exception;

/**
 * @author Jan Lejnar
 */
public class SearchModeNotFound extends Exception {
    public SearchModeNotFound() {
    }

    public SearchModeNotFound(String message) {
        super(message);
    }

    public SearchModeNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchModeNotFound(Throwable cause) {
        super(cause);
    }

    public SearchModeNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
