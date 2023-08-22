package pjv.sp.chess.model.pieces;

/**
 * Move class that wraps target position and type of the move
 * @author Jakub Rada
 * @version 1.0
 * @see Position
 * @see MoveType
 */
public class Move {

    /**
     * Target Position of the move
     */
    private final Position position;

    /**
     * Type of the move
     */
    private final MoveType type;

    /**
     * Creates new Move object
     * @param position target Position of the move
     * @param type MoveType of the move
     * @see Position
     * @see MoveType
     */
    public Move(Position position, MoveType type) {
        this.position = position;
        this.type = type;
    }

    /**
     * Gets target Position of the move
     * @return Position property of the move
     * @see Position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Gets type of the move
     * @return MoveType property
     * @see MoveType
     */
    public MoveType getType() {
        return type;
    }

}
