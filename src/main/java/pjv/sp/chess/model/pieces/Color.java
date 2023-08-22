package pjv.sp.chess.model.pieces;

/**
 * Color enum holds possible colors of pieces on the board
 * @author Jakub Rada
 * @version 1.0
 */
public enum Color {

    /**
     * Color of white pieces
     */
    WHITE(0, "White"),

    /**
     * Color of black pieces
     */
    BLACK(1, "Black"),

    /**
     * Color of empty pieces
     */
    EMPTY(2, "");

    /**
     * Index of color used for indexing arrays of pieces
     */
    private final int index;

    /**
     * String name of the color
     */
    private final String label;

    /**
     * Creates new item for the enum
     * @param index index with which number will be pieces of this color indexed in arrays
     * @param label string name of the color to be displayed
     */
    private Color(int index, String label) {
        this.index = index;
        this.label = label;
    }

    /**
     * Gets index of the color to index some arrays of pieces
     * @return unique number of the color (0-2)
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Gets label of the color
     * @return value of the label property
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Gets oposite color to this color
     * White -&gt; Black
     * Black -&gt; White
     * Empty -&gt; Empty
     * @return oppocite color to this
     */
    public Color getOppositeColor() {
        Color ret;
        if (this.equals(Color.WHITE)) {
            ret = Color.BLACK;
        } else if (this.equals(Color.BLACK)) {
            ret = Color.WHITE;
        } else {
            ret= Color.EMPTY;
        }
        return ret;
    }
}
