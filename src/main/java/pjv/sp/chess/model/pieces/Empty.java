package pjv.sp.chess.model.pieces;

import javafx.scene.image.Image;

/**
 * Empty class represents the empty square on the chess board. Extends abstract class
 * that describes common properties of a piece.
 * @author Jakub Rada
 * @version 1.0
 * @see Piece
 */
public class Empty extends Piece {

    /**
     * Creates new Empty piece object
     * @param position position on the board where is the piece located
     * @see Position
     */
    public Empty(Position position) {
        super(Color.EMPTY, position, false, false);
    }

    /**
     * Loads bishop icon from resources of this project
     */
    @Override
    protected void loadIcon() {
        this.icon = new Image(
            this.getClass().getResource("empty.png").toExternalForm()
        );
    }

    /**
     * Adds all possible moves for this piece from the Move enum
     */
    @Override
    public void generateMoves() {
        this.moves.add(MoveVector.EMPTY);
    }
}
