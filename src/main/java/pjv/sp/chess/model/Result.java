package pjv.sp.chess.model;

/**
 * Result enum holds possible endings of a game. It also contains values for PGN
 * recorder;
 * @author Jakub Rada
 * @version 1.0
 */
public enum Result {
    WHITE_WINS("1-0"),
    BLACK_WINS("0-1"),
    DRAW("1/2-1/2"),
    UNKNOWN("*");

    /**
     * String that represents the result in PGN format
     */
    private final String result;

    /**
     * Creates new item of the enum
     * @param result PGN formated string that represents the enum
     */
    private Result(String result) {
        this.result = result;
    }

    /**
     * Gets String represented of the item
     * @return value of the result property
     */
    public String getResult() {
        return this.result;
    }
}