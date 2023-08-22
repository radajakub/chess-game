package pjv.sp.chess.model.pieces;

/**
 * MoveType enum lists all move types that can be played
 * @author Jakub Rada
 * @version 1.0
 */
public enum MoveType {
    NORMAL,
    CAPTURE,
    QUEENSIDE_CASTLING,
    KINGSIDE_CASTLING,
    ENPASSANT,
}