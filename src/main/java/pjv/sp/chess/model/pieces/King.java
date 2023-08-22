package pjv.sp.chess.model.pieces;

import javafx.scene.image.Image;

/**
 * King class represent the king piece of the chess game. Extends abstract class
 * that describes common properties of a piece.
 * @author Jakub Rada
 * @version 1.0
 * @see Piece
 */
public class King extends Piece {

    /**
     * Creates new King piece object
     * @param color color of the new king piece
     * @param position position on the board where is the piece located
     * @see Color
     * @see Position
     */
    public King(Color color, Position position) {
        super(color, position, false, false);
    }

    /**
     * Loads bishop icon from resources of this project
     */
    @Override
    protected void loadIcon() {
        this.icon = new Image(
            this.getClass().getResource(String.format("king_%s.png", this.color.equals(Color.BLACK) ? "b" : "w")).toExternalForm()
        );
    }

    /**
     * Adds all possible moves for this piece from the Move enum
     */
    @Override
    public void generateMoves() {
        this.moves.add(MoveVector.NORTH);
        this.moves.add(MoveVector.NORTH_EAST);
        this.moves.add(MoveVector.NORTH_WEST);
        this.moves.add(MoveVector.SOUTH);
        this.moves.add(MoveVector.SOUTH_EAST);
        this.moves.add(MoveVector.SOUTH_WEST);
        this.moves.add(MoveVector.EAST);
        this.moves.add(MoveVector.WEST);
    }
}
