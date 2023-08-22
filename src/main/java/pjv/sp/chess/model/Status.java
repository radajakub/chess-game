package pjv.sp.chess.model;

/**
 * Status class holds information about error or success
 * @author Jakub Rada
 * @version 1.0
 */
public class Status {

    /**
     * Status is correct or false
     */
    private final boolean correct;

    /**
     * Message to show containing details about status
     */
    private final String message;

    /**
     * Creates new Status object
     * @param correct was action correct
     * @param message message to show
     */
    public Status(boolean correct, String message) {
        this.correct = correct;
        this.message = message;
    }

    /**
     * Gets whehter the Status is positive or negative
     * @return booelan value of the correct property
     */
    public boolean isCorrect() {
        return this.correct;
    }

    /**
     * Gets message to show
     * @return String value of the message property
     */
    public String getMessage() {
        return this.message;
    }

}