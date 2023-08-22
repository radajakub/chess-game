package pjv.sp.chess.model.pieces;

import javafx.scene.image.Image;

/**
 * Knight class represents the knight piece of the chess game. Extends abstract class
 * that describes common properties of a piece.
 * @author Jakub Rada
 * @version 1.0
 * @see Piece
 */
public class Knight extends Piece {

    /**
     * Creates new Knigh piece object
     * @param color color of the new knight piece
     * @param position position on the board where is the piece located
     * @see Color
     * @see Position
     */
    public Knight(Color color, Position position) {
        super(color, position, false, false);
    }

    /**
     * Loads bishop icon from resources of this project
     */
    @Override
    protected void loadIcon() {
        this.icon = new Image(
            this.getClass().getResource(String.format("knight_%s.png", this.color.equals(Color.BLACK) ? "b" : "w")).toExternalForm()
        );
    }

    /**
     * Adds all possible moves for this piece from the Move enum
     */
    @Override
    protected void generateMoves() {
        this.moves.add(MoveVector.KNIGHT_NNW);
        this.moves.add(MoveVector.KNIGHT_NNE);
        this.moves.add(MoveVector.KNIGHT_NEE);
        this.moves.add(MoveVector.KNIGHT_SEE);
        this.moves.add(MoveVector.KNIGHT_SSE);
        this.moves.add(MoveVector.KNIGHT_SSW);
        this.moves.add(MoveVector.KNIGHT_SWW);
        this.moves.add(MoveVector.KNIGHT_NWW);
    }
}
