package pjv.sp.chess.model.board;

import pjv.sp.chess.model.pieces.Bishop;
import pjv.sp.chess.model.pieces.Color;
import pjv.sp.chess.model.pieces.Empty;
import pjv.sp.chess.model.pieces.King;
import pjv.sp.chess.model.pieces.Knight;
import pjv.sp.chess.model.pieces.Pawn;
import pjv.sp.chess.model.pieces.Piece;
import pjv.sp.chess.model.pieces.Position;
import pjv.sp.chess.model.pieces.Queen;
import pjv.sp.chess.model.pieces.Rook;

import java.util.ArrayList;
import java.util.List;

/**
 * StandardBoard class provides utilities to create standard board layout.
 * @author Jakub Rada
 * @version 1.0
 */
public class StandardBoard {

    /**
     * Generates king's rank for passed color and rank
     * @param color color of pieces to add
     * @param rank rank on which create the line
     * @return new List of Pieces that represents line on the board
     */
    public static List<Piece> generateKingLine(Color color, Rank rank) {
        List<Piece> row = new ArrayList<>(8);
        row.add(new Rook(color, new Position(File.A, rank)));
        row.add(new Knight(color, new Position(File.B, rank)));
        row.add(new Bishop(color, new Position(File.C, rank)));
        row.add(new Queen(color, new Position(File.D, rank)));
        row.add(new King(color, new Position(File.E, rank)));
        row.add(new Bishop(color, new Position(File.F, rank)));
        row.add(new Knight(color, new Position(File.G, rank)));
        row.add(new Rook(color, new Position(File.H, rank)));
        return row;
    }

    /**
     * Generates pawn's rank for passed color and rank
     * @param color color of pieces to add
     * @param rank rank on which create the line
     * @return new List of Pieces that represents line on the board
     */
    public static List<Piece> generatePawnLine(Color color, Rank rank) {
        List<Piece> row = new ArrayList<>(8);
        for (File f : File.VALUES) {
            row.add(new Pawn(color, new Position(f, rank)));
        }
        return row;
    }

    /**
     * Generates empty line on passed rank
     * @param rank rank on which create the line
     * @return new List of Pieces that represents empty line on the board
     */
    public static List<Piece> generateEmptyLine(Rank rank) {
        List<Piece> row = new ArrayList<>(8);
        for (File f : File.VALUES) {
            row.add(new Empty(new Position(f, rank)));
        }
        return row;
    }
}
