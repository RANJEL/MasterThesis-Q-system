package cz.komix.qsystem.backend.exception;

/**
 * @author Jan Lejnar
 */
public class ISAAARequestError {

    private String cause;
    private String recommendedSolution;

    public ISAAARequestError(String cause, String recommendedSolution) {
        this.cause = cause;
        this.recommendedSolution = recommendedSolution;
        // TODO use MessagesController here
    }

    public String getCause() {
        return cause;
    }

    public String getRecommendedSolution() {
        return recommendedSolution;
    }

    @Override
    public String toString() {
        return "ISAAARequestError{" +
                "cause" + " = '" + cause + '\'' +
                ", " + "recommendedSolution" + " = '" + recommendedSolution + '\'' +
                '}';
    }
}
