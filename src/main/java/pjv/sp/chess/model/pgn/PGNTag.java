package pjv.sp.chess.model.pgn;

import pjv.sp.chess.model.Datum;
import pjv.sp.chess.model.Result;
import pjv.sp.chess.model.board.Board;
import pjv.sp.chess.model.board.File;
import pjv.sp.chess.model.board.Rank;
import pjv.sp.chess.model.pieces.Bishop;
import pjv.sp.chess.model.pieces.Color;
import pjv.sp.chess.model.pieces.Empty;
import pjv.sp.chess.model.pieces.King;
import pjv.sp.chess.model.pieces.Knight;
import pjv.sp.chess.model.pieces.MoveType;
import pjv.sp.chess.model.pieces.Pawn;
import pjv.sp.chess.model.pieces.Piece;
import pjv.sp.chess.model.pieces.Position;
import pjv.sp.chess.model.pieces.Queen;
import pjv.sp.chess.model.pieces.Rook;
import pjv.sp.chess.model.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PGNTag class contains utilities for formatting pgn tags
 * @author Jakub Rada
 * @version 1.0
 */
public class PGNTag {

    /**
     * maps piece classes to their chars in SAN notation
     */
    private static final Map<Class<? extends Piece>, Character> SAN_PIECES = new HashMap<>(Map.of(
        Pawn.class, 'P',
        Rook.class, 'R',
        Knight.class, 'N',
        Bishop.class, 'B',
        Queen.class, 'Q',
        King.class, 'K'
    ));

    /**
     * maps SAN notation to their sample pieces
     */
    private static final Map<Character, Piece> SAN_PIECES_REVERSED = new HashMap<>(Map.of(
        'R', new Rook(Color.WHITE, new Position(File.A, Rank.ONE)),
        'N', new Knight(Color.WHITE, new Position(File.A, Rank.ONE)),
        'B', new Bishop(Color.WHITE, new Position(File.A, Rank.ONE)),
        'Q', new Queen(Color.WHITE, new Position(File.A, Rank.ONE)),
        'K', new King(Color.WHITE, new Position(File.A, Rank.ONE)),
        'r', new Rook(Color.BLACK, new Position(File.A, Rank.ONE)),
        'n', new Knight(Color.BLACK, new Position(File.A, Rank.ONE)),
        'b', new Bishop(Color.BLACK, new Position(File.A, Rank.ONE)),
        'q', new Queen(Color.BLACK, new Position(File.A, Rank.ONE)),
        'k', new King(Color.BLACK, new Position(File.A, Rank.ONE))
    ));

    /**
     * maps MoveTypes to special moves in SAN notation
     */
    private static final Map<MoveType, String> SAN_MOVES = new HashMap<>(Map.of(
        MoveType.CAPTURE, "x",
        MoveType.KINGSIDE_CASTLING, "O-O",
        MoveType.QUEENSIDE_CASTLING, "O-O-O"
    ));

    /**
     * Decodes piece type from char
     * @param c SAN notation for piece
     * @return sample Piece
     */
    public static Piece decodePiece(char c) {
        return PGNTag.SAN_PIECES_REVERSED.get(c);
    }

    /**
     * Gets if passed string is queenside castling in SAN notation
     * @param move string to check
     * @return is it queenside castling
     */
    public static boolean isQueensideCastling(String move) {
        return SAN_MOVES.get(MoveType.QUEENSIDE_CASTLING).equals(move);
    }

    /**
     * Gets if passed string is kingside castling in SAN notation
     * @param move string to check
     * @return is it kingside castling
     */
    public static boolean isKingsideCastling(String move) {
        return SAN_MOVES.get(MoveType.KINGSIDE_CASTLING).equals(move);
    }

    /**
     * Creates move in pgn notation from passed parameters
     * @param piece Piece that was moved
     * @param start its initial position
     * @param end its end position
     * @param type type of the move
     * @param disambiguation ambiguities to remove
     * @param promotion was it promotion
     * @param check was it check
     * @param checkmate was it checkmate
     * @return string in proper pgn format
     */
    public static String createMove(Piece piece, Position start, Position end, MoveType type, Disambiguation disambiguation, boolean promotion, boolean check, boolean checkmate) {
        StringBuilder sb = new StringBuilder();
        if (MoveType.KINGSIDE_CASTLING.equals(type) || MoveType.QUEENSIDE_CASTLING.equals(type)) {
            sb.append(PGNTag.SAN_MOVES.get(type));
        } else {
            if (!promotion && !Pawn.class.equals(piece.getClass())) {
                sb.append(PGNTag.SAN_PIECES.get(piece.getClass()));
            }
            if (Disambiguation.ORIGINATING_FILE.equals(disambiguation)) {
                sb.append(start.getFile().getLabel());
            } else if (Disambiguation.ORIGINATING_RANK.equals(disambiguation)) {
                sb.append(start.getRank().getLabel());
            } else if (Disambiguation.ORIGINATING_POSITION.equals(disambiguation)) {
                sb.append(start.getFile().getLabel());
                sb.append(start.getRank().getLabel());
            }
            if (MoveType.CAPTURE.equals(type)) {
                sb.append(PGNTag.SAN_MOVES.get(type));
            }
            sb.append(end.getFile().getLabel());
            sb.append(end.getRank().getLabel());
            if (promotion) {
                sb.append("=");
                sb.append(SAN_PIECES.get(piece.getClass()));
            }
            if (checkmate) {
                sb.append("#");
            } else if (check) {
                sb.append("+");
            }
        }
        return sb.toString();
    }

    /**
     * Creates pgn tag
     * @param label name of the tag
     * @param content value of the field
     * @return tag in proper pgn format
     */
    public static String createTag(PGNTagLabels label, String content) {
        return String.format("[%s \"%s\"]", label.getLabel(), content);
    }

    /**
     * Creates event value string
     * @param event value of event property
     * @return event tag value in proper formating
     */
    public static String formatEvent(String event) {
        return event.isEmpty() ? "?" : event;
    }

    /**
     * Creates site value string
     * @param site value of site property
     * @return site tag value in proper formating
     */
    public static String formatSite(String site) {
        return site.isEmpty() ? "?" : site;
    }


    /**
     * Creates date value string
     * @param datum value of date property
     * @return date tag value in proper formating
     */
    public static String formatDate(Datum datum) {
        return String.format(
            "%d.%02d.%02d",
            datum.getYear(),
            datum.getMonth(),
            datum.getDay()
        );
    }

    /**
     * Creates player value string
     * @param player value of player property
     * @return player tag value in proper formating
     */
    public static String formatPlayer(Player player) {
        String s;
        if (player.getName().isEmpty() && player.getSurname().isEmpty()) {
            s = "?";
        } else if (player.getName().isEmpty()) {
            s = player.getSurname();
        } else {
            s = String.format("%s, %s", player.getSurname(), player.getName());
        }
        return s;
    }

    /**
     * Creates round value string
     * @param round value of round property
     * @return round tag value in proper formating
     */
    public static String formatRound(int round) {
        return "-";
    }

    /**
     * Creates result value string
     * @param result value of result property
     * @return result tag value in proper formating
     */
    public static String formatResult(Result result) {
        return result.getResult();
    }


    /**
     * Creates setUp value string
     * @param customLayout value of customLayout property
     * @return setUp tag value in proper formating
     */
    public static String formatSetUp(boolean customLayout) {
        return customLayout ? "1" : "0";
    }

    /**
     * Creates fen value string
     * @param board Board to create fen from
     * @return fen tag value in proper formating
     */
    public static String formatFEN(Board board) {
        StringBuilder sb = new StringBuilder();
        int blank;
        char pieceChar;
        int ranks = 0;
        for (List<Piece> row : board.getBoard()) {
            ranks++;
            blank = 0;
            for (Piece piece : row) {
                if (Empty.class.equals(piece.getClass())) {
                    blank++;
                } else {
                    if (blank > 0) {
                        sb.append(blank);
                        blank = 0;
                    }
                    pieceChar = PGNTag.SAN_PIECES.get(piece.getClass());
                    if (Color.BLACK.equals(piece.getColor())) {
                        pieceChar = Character.toLowerCase(pieceChar);
                    }
                    sb.append(pieceChar);
                }
            }
            if (blank > 0) {
                sb.append(blank);
            }
            blank = 0;
            if (ranks < board.getBoard().size()) {
                sb.append("/");
            }
        }
        sb.append(" w - - 0 1");
        return sb.toString();
    }

    /**
     * Splits tag into label and value
     * @param tag tag to be split
     * @return String[] that contains label and value in this order
     * @throws IllegalArgumentException when the tag is not surrounded with square brackets
     */
    public static String[] splitTag(String tag) throws IllegalArgumentException {
        tag = tag.strip();
        if (!tag.startsWith("[") || !tag.endsWith("]")) {
            throw new IllegalArgumentException("Wrong tag format");
        }
        String[] parts = new String[2];
        parts[0] = tag.substring(1, tag.indexOf(" "));
        parts[1] = tag.substring(tag.indexOf(" ") + 1, tag.length() - 1);
        return parts;
    }

    /**
     * Strips value of quotation marks
     * @param value valut ot be stripped
     * @return value without quotation marks
     * @throws IllegalArgumentException if the value is not in quotation marks
     */
    public static String stripValue(String value) throws IllegalArgumentException {
        if (!value.startsWith("\"") || !value.endsWith("\"")) {
            throw new IllegalArgumentException("Wrong tag format");
        }
        StringBuilder sb = new StringBuilder(value);
        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}