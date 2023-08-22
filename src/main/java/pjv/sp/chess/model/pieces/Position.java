package pjv.sp.chess.model.pieces;

import pjv.sp.chess.model.board.Rank;
import pjv.sp.chess.model.board.File;

/**
 * Position is a class that holds position of a piece on a chessboard.
 * @author Jakub Rada
 * @version 1.0
 * @see File
 * @see Rank
 */
public class Position {

    /**
     * Horizontal position on the chessboard
     */
    private File file;

    /**
     * Vertical position on the chessboard
     */
    private Rank rank;

    /**
     * Creates new Position object
     * @param file horizontal position on chessboard
     * @param rank vertical position on chessboard
     * @see File
     * @see Rank
     */
    public Position(File file, Rank rank) {
        this.file = file;
        this.rank = rank;
    }

    /**
     * Gets the value of the property file
     * @return horizontal position on chessboard
     * @see File
     */
    public File getFile() {
        return this.file;
    }

    /**
     * Gets the value of the property rank
     * @return vertical position on chessboard
     * @see Rank
     */
    public Rank getRank() {
        return this.rank;
    }

    /**
     * Sets the property file to new value
     * @param file horizontal position on chessboard to be set
     * @see File
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Sets the property rank to new value
     * @param rank vertical position on chessboard to be set
     * @see Rank
     */
    public void setRank(Rank rank) {
        this.rank = rank;
    }
}
