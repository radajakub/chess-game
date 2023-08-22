package pjv.sp.chess.model.pieces;

import javafx.scene.image.Image;

/**
 * Bishop class represents the bishop piece of the chess game. Extends abstract class
 * that describes common properties of a piece.
 * @author Jakub Rada
 * @version 1.0
 * @see Piece
 */
public class Bishop extends Piece {

    /**
     * Creates new Bishop piece object
     * @param color color of the new bishop piece
     * @param position position on the board where is the piece located
     * @see Color
     * @see Position
     */
    public Bishop(Color color, Position position) {
        super(color, position, true, false);
    }

    /**
     * Loads bishop icon from resources of this project
     */
    @Override
    protected void loadIcon() {
        this.icon = new Image(
            this.getClass().getResource(String.format("bishop_%s.png", this.color.equals(Color.BLACK) ? "b" : "w")).toExternalForm()
        );
    }

    /**
     * Adds all possible moves for this piece from the Move enum
     */
    @Override
    protected void generateMoves() {
        this.moves.add(MoveVector.NORTH_EAST);
        this.moves.add(MoveVector.NORTH_WEST);
        this.moves.add(MoveVector.SOUTH_EAST);
        this.moves.add(MoveVector.SOUTH_WEST);
    }
}
