package pjv.sp.chess.model.pieces;

/**
 * MoveVector enum holds all movements of pieces that can be conducted on the board.
 * @author Jakub Rada
 * @version 1.0
 */
public enum MoveVector {
    EMPTY(0, 0),
    NORTH(0, 1),
    NORTH_EAST(1, 1),
    EAST(1, 0),
    SOUTH_EAST(1, -1),
    SOUTH(0, -1),
    SOUTH_WEST(-1, -1),
    WEST(-1, 0),
    NORTH_WEST(-1, 1),
    KNIGHT_NNW(-1, 2),
    KNIGHT_NNE(1, 2),
    KNIGHT_NEE(2, 1),
    KNIGHT_SEE(2, -1),
    KNIGHT_SSE(1, -2),
    KNIGHT_SSW(-1, -2),
    KNIGHT_SWW(-2, -1),
    KNIGHT_NWW(-2, 1);

    /**
     * Change of coordinates in horizontal direction on the board
     */
    private final int x;

    /**
     * Change of coordinates in vertical direction on the board
     */
    private final int y;

    /**
     * Creates new Move object
     * @param x change in horizontal direction
     * @param y change in verticlal direction
     */
    private MoveVector(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets change in horizontal direction
     * @return value of the x property of the move
     */
    public int getX() {
        return this.x;
    }

    /**
     * Gets change in vertical direction
     * @return value of the y property of the move
     */
    public int getY() {
        return this.y;
    }
}
