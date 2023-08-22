package pjv.sp.chess.model.pgn;

/**
 * Disambiguation enum holds possible ambiguities in creating pgn move that has
 * to be corrected.
 * @author Jakub Rada
 * @version 1.0
 */
public enum Disambiguation {
    OK,
    ORIGINATING_FILE,
    ORIGINATING_RANK,
    ORIGINATING_POSITION;
}