package pjv.sp.chess.model.pieces;

import javafx.scene.image.Image;

/**
 * Rook class represents the rook piece of the chess game. Extends abstract class
 * that describes common properties of a piece.
 * @author Jakub Rada
 * @version 1.0
 * @see Piece
 */
public class Rook extends Piece {

    /**
     * Creates new Rook piece object
     * @param color color of the new rook piece
     * @param position position on the board where is the piece located
     * @see Color
     * @see Position
     */
    public Rook(Color color, Position position) {
        super(color, position, true, false);
    }

    /**
     * Loads bishop icon from resources of this project
     */
    @Override
    protected void loadIcon() {
        this.icon = new Image(
            this.getClass().getResource(String.format("rook_%s.png", this.color.equals(Color.BLACK) ? "b" : "w")).toExternalForm());
    }

    /**
     * Adds all possible moves for this piece from the Move enum
     */
    @Override
    protected void generateMoves() {
        this.moves.add(MoveVector.NORTH);
        this.moves.add(MoveVector.EAST);
        this.moves.add(MoveVector.SOUTH);
        this.moves.add(MoveVector.WEST);
    }
}
