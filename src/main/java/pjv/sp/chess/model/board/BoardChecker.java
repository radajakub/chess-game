package pjv.sp.chess.model.board;

import pjv.sp.chess.model.Status;
import pjv.sp.chess.model.pieces.Bishop;
import pjv.sp.chess.model.pieces.Color;
import pjv.sp.chess.model.pieces.Empty;
import pjv.sp.chess.model.pieces.King;
import pjv.sp.chess.model.pieces.Knight;
import pjv.sp.chess.model.pieces.Pawn;
import pjv.sp.chess.model.pieces.Piece;
import pjv.sp.chess.model.pieces.Queen;
import pjv.sp.chess.model.pieces.Rook;

/**
 * Class that checks if the associated board is correct and can be used for a 
 * chess game
 * @author Jakub Rada
 * @version 1.0
 */
public class BoardChecker {

    /**
     * Array that holds counts of individual piece types
     */
    private final int[][] pieceCounts = new int[2][Piece.COUNT];

    /**
     * Max number of kings present in one color
     */
    private final int kingMax = 1;

    /**
     * Max number of pawns present in one color
     */
    private final int pawnMax = 8;

    /**
     * Pawns that are over the max limit
     */
    private int invalidPawns = 0;

    /**
     * Creates new board checker
     */
    public BoardChecker() {
    }

    /**
     * Resets all piece type counts to zero
     */
    public void reset() {
        for (int i = 0; i < 2; i++) {
            for (int n = 0; n < Piece.COUNT; n++) {
                this.pieceCounts[i][n] = 0;
            }
        }
        invalidPawns = 0;
    }

    /**
     * Add piece to board checker
     * @param piece piece that was added on the board
     * @see Piece
     */
    public void addPiece(Piece piece) {
        this.incrementPiece(piece);
    }

    /**
     * Include change on board in the counter
     * @param oldPiece piece that was on the board
     * @param newPiece piece that is now on the board
     * @return Status object that contains information about board validity
     * @see Piece
     */
    public Status changeOnBoard(Piece oldPiece, Piece newPiece) {
        if (this.checkPiece(newPiece)) {
            this.invalidPawns++;
        } else if (this.checkPiece(oldPiece) && Empty.class.equals(newPiece.getClass())) {
            this.invalidPawns--;
        }
        this.decrementPiece(oldPiece);
        this.incrementPiece(newPiece);
        return this.checkBoard();
    }

    /**
     * Checks the board validity and returns status
     * @return Status object that contains information about board validity
     * @see Status
     */
    public Status checkBoard() {
        Status status;
        if (this.invalidPawns > 0) {
            status = new Status(false, "Pawns cannot be on rank 1 or rank 8");
        } else if (this.getCount(Color.WHITE, King.class) != this.kingMax) {
            status = new Status(false, "Wrong number of white kings, precisely one required");
        } else if (this.getCount(Color.BLACK, King.class) != this.kingMax) {
            status = new Status(false, "Wrong number of black kings, precisely one required");
        } else if (this.getCount(Color.WHITE, Pawn.class) > this.pawnMax) {
            status = new Status(false, "Too many white pawns, more than 8 are not allowed");
        } else if (this.getCount(Color.BLACK, Pawn.class) > this.pawnMax) {
            status = new Status(false, "Too many black pawns, more than 8 are not allowed");
        } else if (this.getCountWithPromotions(Color.WHITE) > this.pawnMax) {
            status = new Status(false, "White - Impossible to get even with promotions");
        } else if (this.getCountWithPromotions(Color.BLACK) > this.pawnMax) {
            status = new Status(false, "Black - Impossible to get even with promotions");
        } else {
            status = new Status(true, "Board is ok");
        }
        return status;
    }

    /**
     * Checks piece if it is not on illegal position
     * @param piece piece to check
     * @return boolean value if it is placed legally
     * @see Piece
     */
    private boolean checkPiece(Piece piece) {
        // Pawn cannot be on first or last rank of it's color (cannot go backwards or was promoted)
        return (
            Pawn.class.equals(piece.getClass())
            &&
            (
                Rank.ONE.equals(piece.getPosition().getRank())
                ||
                Rank.EIGHT.equals(piece.getPosition().getRank())
            )
        );
    }

    /**
     * Gets number of piece type and color on the board
     * @param color color of the requested piece
     * @param pieceClass class of requested piece
     * @return count of piece with passed type and color
     * @see Color
     * @see Piece
     */
    private int getCount(Color color, Class<? extends Piece> pieceClass) {
        return this.pieceCounts[color.getIndex()][Piece.pieceClasses.indexOf(pieceClass)];
    }

    /**
     * Returns maximum number of pawns considering maximum number of pawn promotions
     * @param color color of requested count
     * @return number of pawns on the beggining needed for the baord to reach current state
     * @see Color
     */
    private int getCountWithPromotions(Color color) {
        return this.getCount(color, Pawn.class) +
            this.getCount(color, Rook.class) - 2 +
            this.getCount(color, Knight.class) - 2 +
            this.getCount(color, Bishop.class) - 2 +
            this.getCount(color, Queen.class) - 1;
    }

    /**
     * Increment counter of passed piece
     * @param piece piece which counter should be increased
     * @see Piece
     */
    private void incrementPiece(Piece piece) {
        if (!Empty.class.equals(piece.getClass())) {
            this.pieceCounts[piece.getColor().getIndex()][Piece.pieceClasses.indexOf(piece.getClass())]++;
        }
    }

    /**
     * Decrement counter of passed piece
     * @param piece piece which counter should be decreased
     * @see Piece
     */
    private void decrementPiece(Piece piece) {
        if (!Empty.class.equals(piece.getClass())) {
            this.pieceCounts[piece.getColor().getIndex()][Piece.pieceClasses.indexOf(piece.getClass())]--;
        }
    }
}
