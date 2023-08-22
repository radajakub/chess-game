package pjv.sp.chess.model.pieces;

import javafx.scene.image.Image;

/**
 * Pawn class represents the pawn piece of the chess game. Extends abstract class
 * that describes common properties of a piece.
 * @author Jakub Rada
 * @version 1.0
 * @see Piece
 */
public class Pawn extends Piece {

    /**
     * Creates new Pawn piece object
     * @param color color of the new pawn piece
     * @param position position on the board where is the piece located
     * @see Color
     * @see Position
     */
    public Pawn(Color color, Position position) {
        super(color, position, false, true);
        this.generateTakeMoves();
    }

    /**
     * Loads bishop icon from resources of this project
     */
    @Override
    protected void loadIcon() {
        this.icon = new Image(
            this.getClass().getResource(String.format("pawn_%s.png", this.color.equals(Color.BLACK) ? "b" : "w")).toExternalForm()
        );
    }

    /**
     * Adds all possible take moves for pawn piece from the MoveVector enum
     */
    private void generateTakeMoves() {
        this.takeMoves.add(MoveVector.NORTH_EAST);
        this.takeMoves.add(MoveVector.NORTH_WEST);
    }

    /**
     * Adds all possible moves for this piece from the MoveVector enum
     */
    @Override
    protected void generateMoves() {
        this.moves.add(MoveVector.NORTH);
    }
}
