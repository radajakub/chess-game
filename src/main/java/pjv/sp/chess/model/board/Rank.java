package pjv.sp.chess.model.board;

/**
 * Rank enum specifies possible values of vertical position on chessboard
 * It contains
 * <ul>
 *      <li> position in chessboard (int)
 *      <li> label of the position (String)
 * </ul>
 * For all features working properly it is necessary to declare items in correct order by their values
 * @author Jakub Rada
 * @version 1.0
 */
public enum Rank {
    /**
     * Bottom side position (White's first rank)
     */
    ONE(7, "1"),
    /**
     * Second position going upwards
     */
    TWO(6, "2"),
    /**
     * Third position going upwards
     */
    THREE(5, "3"),
    /**
     * Fourth position going upwards
     */
    FOUR(4, "4"),
    /**
     * Fifth position going upwards
     */
    FIVE(3, "5"),
    /**
     * Sixth position going upwards
     */
    SIX(2, "6"),
    /**
     * Seventh position going upwards
     */
    SEVEN(1, "7"),
    /**
     * Top side position (Black's first rank)
     */
    EIGHT(0, "8");

    /**
     * Number of ranks
     */
    public static final int COUNT = 8;

    /**
     * Static array of all items to prevent copying on each use
     */
    public static final Rank[] VALUES = Rank.values();

    /**
     * Label to be shown around the chessboard as position
     */
    private final String label;
    /**
     * Actual vertical position on the chessboard, counted from 0
     */
    private final int value;

    /**
     * Creates new Rank element
     * @param value - vertical position on chessboard
     * @param label - label for the vertical position
     */
    private Rank(int value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * Gets value of the label property
     * @return label to be shown next to this position around the chessboard
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Gets value of the value property
     * @return vertical position on the chessboard
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Gets item before this rank or null if it is first
     * @return item prior to the caller item
     */
    public Rank getPrevious() {
        return Rank.ONE.equals(this) ? null : Rank.VALUES[this.ordinal() - 1];
    }

    /**
     * Gets item after this rank or null if it is last
     * @return item subsequent to the caller item
     */
    public Rank getNext() {
        return Rank.EIGHT.equals(this) ? null : Rank.VALUES[this.ordinal() + 1];
    }

    /**
     * Gets item from rank enum that has passed value or null if that item does not exist
     * @param value desired value of rank item
     * @return rank item that has passed value
     */
    public static Rank getRankOfValue(int value) {
        Rank ret = null;
        for (Rank rank : Rank.VALUES) {
            if (rank.getValue() == value) {
                ret = rank;
                break;
            }
        }
        return ret;
    }

    /**
     * Gets item from rank enum that has passed label or null if that item does not exist
     * @param label label that identifies searched rank item
     * @return rank item that has passed value
     */
    public static Rank getRankOfLabel(String label) {
        Rank ret = null;
        for (Rank rank : Rank.VALUES) {
            if (rank.getLabel().equals(label)) {
                ret = rank;
                break;
            }
        }
        return ret;
    }
}
