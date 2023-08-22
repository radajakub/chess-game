package pjv.sp.chess.model.board;

import pjv.sp.chess.model.pieces.Bishop;
import pjv.sp.chess.model.pieces.Color;
import pjv.sp.chess.model.pieces.Knight;
import pjv.sp.chess.model.pieces.Piece;
import pjv.sp.chess.model.pieces.Position;
import pjv.sp.chess.model.pieces.Queen;
import pjv.sp.chess.model.pieces.Rook;

import java.util.ArrayList;
import java.util.List;

/**
 * Promotion class wraps all pieces to which a Pawn can be promoted.
 * Used to construct and mirro PromotionView
 * @author Jakub Rada
 * @version 1.0
 */
public class Promotion {

    /**
     * List of all possible promotion values for a Pawn
     */
    private final List<Piece> promotions = new ArrayList<>();

    /**
     * Create new promotion object of passed color
     * @param color Color of the promoted Pieces
     * @see pjv.sp.chess.model.pieces.Color
     * @see pjv.sp.chess.model.pieces.Piece
     */
    public Promotion(Color color) {
        promotions.add(new Rook(color, new Position(File.A, Rank.ONE)));
        promotions.add(new Knight(color, new Position(File.A, Rank.ONE)));
        promotions.add(new Bishop(color, new Position(File.A, Rank.ONE)));
        promotions.add(new Queen(color, new Position(File.A, Rank.ONE)));
    }

    /**
     * Gets the list of promotions
     * @return List that contains Pieces to select from
     * @see pjv.sp.chess.model.pieces.Piece
     */
    public List<Piece> getPromotions() {
        return this.promotions;
    }

}