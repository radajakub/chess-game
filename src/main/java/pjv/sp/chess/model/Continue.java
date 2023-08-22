package pjv.sp.chess.model;

/**
 * Continue class holds information if continue or not, result and message to show.
 * @author Jakub Rada
 * @version 1.0
 * @see Result
 */
public class Continue {

    /**
     * If continue game or not
     */
    private final boolean continueGame;

    /**
     * Result of the game
     */
    private final Result result;

    /**
     * message to show in status
     */
    private final String message;

    /**
     * Create new Continue object
     * @param continueGame if continue or not
     * @param result Result of the game
     * @param message String to show in status
     * @see Result
     */
    public Continue(boolean continueGame, Result result, String message) {
        this.continueGame = continueGame;
        this.result = result;
        this.message = message;
    }

    /**
     * Gets message to show
     * @return value of message property
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets Result of the game
     * @return Result object
     */
    public Result getResult() {
        return result;
    }

    /**
     * Gets if continue or not
     * @return boolean value
     */
    public boolean getContinueGame() {
        return continueGame;
    }

}