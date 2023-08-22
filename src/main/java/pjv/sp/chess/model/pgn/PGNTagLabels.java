package pjv.sp.chess.model.pgn;

/**
 * PGNTagLabels enum contains pgn tags that are considered and used in this
 * implementation.
 * @author Jakub Rada
 * @version 1.0
 */
public enum PGNTagLabels {
    EVENT("Event"),
    SITE("Site"),
    DATE("Date"),
    ROUND("Round"),
    WHITE("White"),
    BLACK("Black"),
    RESULT("Result"),
    SETUP("SetUp"),
    FEN("FEN");

    /**
     * Array that contains all tags so they are not copied on every request
     */
    public static final PGNTagLabels[] VALUES = PGNTagLabels.values();

    /**
     * name of the tag
     */
    private String label;

    /**
     * Create new PGNTagLabels item
     * @param label name of the tag
     */
    private PGNTagLabels(String label) {
        this.label = label;
    }

    /**
     * Gets label of the enum item
     * @return String with name of the tag
     */
    public String getLabel() {
        return this.label;
    }

}