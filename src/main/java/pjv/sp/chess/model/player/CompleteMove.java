package pjv.sp.chess.model.player;

import pjv.sp.chess.model.pieces.Move;
import pjv.sp.chess.model.pieces.Piece;

/**
 * CompleteMove class wraps piece and performed moved with that piece to get
 * from computer player.
 * @author Jakub Rada
 * @version 1.0
 */
public class CompleteMove {

    /**
     * Piece that is being moved
     */
    private final Piece piece;

    /**
     * Move that is performed with piece
     */
    private final Move move;

    /**
     * Create new CompleteMove object
     * @param piece Piece that is being moved
     * @param move Move that is being performed with that piece
     * @see Piece
     * @see Move
     */
    public CompleteMove(Piece piece, Move move) {
        this.piece = piece;
        this.move = move;
    }

    /**
     * Gets Piece that is being moved
     * @return piece to move on the board
     * @see Piece
     */
    public Piece getPiece() {
        return this.piece;
    }

    /**
     * Gets move that is performed
     * @return move to be performed on the board with piece in this object
     */
    public Move getMove() {
        return this.move;
    }
}
