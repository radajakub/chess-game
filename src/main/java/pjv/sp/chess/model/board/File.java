package pjv.sp.chess.model.board;

/**
 * File enum specifies possible values of horizontal position on chessboard
 * <ul>
 *      <li> position in chessboard (int)
 *      <li> label of the position (String)
 * </ul>
 * For use of all methods it is necessary to declare constants in desired order by their values
 * @author Jakub Rada
 * @version 1.0
 */
public enum File {
    /**
     * Leftmost / Qeenside position
     */
    A(0, "a"),
    /**
     * Second position from left
     */
    B(1, "b"),
    /**
     * Third position from left
     */
    C(2, "c"),
    /**
     * Fourth position from left
     */
    D(3, "d"),
    /**
     * Fifth position from left
     */
    E(4, "e"),
    /**
     * Sixth position from left
     */
    F(5, "f"),
    /**
     * Seventh position from left
     */
    G(6, "g"),
    /**
     * Rightmost / Kingside position
     */
    H(7, "h");

    /**
     * Nuber of Files
     */
    public static final int COUNT = 8;

    /**
     * Static array that holds all File items to avoid copying on each use
     */
    public static final File[] VALUES = File.values();

    /**
     * Label to be shown around the chessboard as position
     * must be lower case to comply to the PGN chess format
     */
    private final String label;

    /**
     * Actual horizontal position on the chessboard, counted from 0
     */
    private final int value;

    /**
     * Creates new File element
     * @param value horizontal position on the chessboard
     * @param label label for the horizontal position
     */
    private File(int value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * Gets label transformed into upper case
     * @return label in upper case
     */
    public String getUpperCaseLabel() {
        return this.label.toUpperCase();
    }

    /**
     * Gets label in lower case format
     * @return label to be shown next to this position around the chessboard
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Gets value of the value property
     * @return horizontal position on the chessboard
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Gets item before this file or null if it is first
     * @return item prior to the caller item
     */
    public File getPrevious() {
        return File.A.equals(this) ? null : File.VALUES[this.ordinal() - 1];
    }

    /**
     * Gets item after this file or null if it is last
     * @return item subsequent to the caller item
     */
    public File getNext() {
        return File.H.equals(this) ? null : File.VALUES[this.ordinal() + 1];
    }

    /**
     * Gets item from file enum that has passed value or null if that item does not exist
     * @param value desired value of file item
     * @return file item that has passed value
     */
    public static File getFileOfValue(int value) {
        File ret = null;
        for (File file : File.VALUES) {
            if (file.getValue() == value) {
                ret = file;
                break;
            }
        }
        return ret;
    }


    /**
     * Gets item from file enum that has passed label or null if that item does not exist
     * @param label label that identifies searched file item
     * @return file item that has passed value
     */
    public static File getFileOfLabel(String label) {
        File ret = null;
        for (File file : File.VALUES) {
            if (file.getLabel().equals(label)) {
                ret = file;
                break;
            }
        }
        return ret;
    }
}
