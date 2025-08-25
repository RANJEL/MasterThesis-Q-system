package cz.komix.qsystem.backend.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jan Lejnar
 */
public class ISAAARequestFailed extends Exception {
    private List<ISAAARequestError> ISAAARequestErrors = new ArrayList<>();
    private String responseStatus = "REQUEST_NOT_SENT";

    public ISAAARequestFailed(String message) {
        super(message);
    }

    public ISAAARequestFailed(String message, Throwable cause) {
        super(message, cause);
    }

    public ISAAARequestFailed(String responseStatus, List<ISAAARequestError> ISAAARequestErrors) {
        this(createMessage(responseStatus, ISAAARequestErrors));
        this.responseStatus = responseStatus;
        this.ISAAARequestErrors = ISAAARequestErrors;
    }

    private static String createMessage(String responseStatus, List<ISAAARequestError> requestErrors) {
        // TODO use MessagesController here
        StringBuilder sb = new StringBuilder();
        sb.append("responseStatus" + " = ");
        sb.append(responseStatus);
        sb.append(",\n" + "requestErrors" + " = ");
        sb.append(requestErrors);
        return sb.toString();
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public List<ISAAARequestError> getISAAARequestErrors() {
        return ISAAARequestErrors;
    }
}
