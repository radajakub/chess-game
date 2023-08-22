package pjv.sp.chess.model.board;

import pjv.sp.chess.model.pieces.Color;
import pjv.sp.chess.model.pieces.Empty;
import pjv.sp.chess.model.pieces.King;
import pjv.sp.chess.model.pieces.Pawn;
import pjv.sp.chess.model.pieces.Piece;
import pjv.sp.chess.model.pieces.Position;
import pjv.sp.chess.model.pieces.Rook;

import java.util.ArrayList;
import java.util.List;

/**
 * Board class holds information about current state of the game chessboard.
 * It provides API for putting, getting and moving pieces on it as well as
 * creating new layouts.
 * @author Jakub Rada
 * @version 1.0
 */
public class Board {

    /**
     * Two dimensional ArrayList that represents the board and holds pieces in it.
     */
    private List<List<Piece>> board = new ArrayList<>(File.COUNT);

    /**
     * Boolean value that determines if the board starts with standard or custom layout
     */
    private boolean custom;

    /**
     * Field that holds kings pieces for easier access to them (no need to search whole board)
     */
    private final Piece[] kings = new Piece[2];

    /**
     * Creates new board based on passed boolean value
     * <ul>
     *  <li>true - creates blank board and pieces are put manually</li>
     *  <li>false - creates board with standard piece layout</li>
     * </ul>
     * @param custom boolean value if the layout should be custom or standard
     */
    public Board(boolean custom) {
        this.custom = custom;
        if (custom) {
            this.createBlankBoard();
        } else {
            this.createStandardBoard();
        }
    }

    /**
     * Sets value of the custom property (useful for example while loading from pgn)
     * @param custom false if game started in standard layout, false otherwise
     */
    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    /**
     * Clears board - fills it with empty pieces
     */
    public void clearBoard() {
        for (Rank rank : Rank.VALUES) {
            for (File file : File.VALUES) {
                this.board.get(rank.getValue()).set(file.getValue(), new Empty(new Position(file, rank)));
            }
        }
    }

    /**
     * Creates empty board (board is clean)
     */
    private void createBlankBoard() {
        for (Rank rank : Rank.VALUES) {
            this.board.add(StandardBoard.generateEmptyLine(rank));
        }
    }

    /**
     * Creates standard board layout according to chess rules
     */
    private void createStandardBoard() {
        this.board.add(StandardBoard.generateKingLine(Color.BLACK, Rank.EIGHT));
        this.board.add(StandardBoard.generatePawnLine(Color.BLACK, Rank.SEVEN));
        /* Fill empty space between white and black lines */
        for (Rank rank : Rank.VALUES) {
            if (rank.getValue() > 1 && rank.getValue() < File.COUNT - 2) {
                this.board.add(StandardBoard.generateEmptyLine(rank));
            }
        }
        this.board.add(StandardBoard.generatePawnLine(Color.WHITE, Rank.TWO));
        this.board.add(StandardBoard.generateKingLine(Color.WHITE, Rank.ONE));
        this.kings[Color.WHITE.getIndex()] = this.getPiece(File.E, Rank.ONE);
        this.kings[Color.BLACK.getIndex()] = this.getPiece(File.E, Rank.EIGHT);
    }

    /**
     * Puts piece on specified position on the board
     * @param piece chess piece to be put on the board
     * @param file File position of the put piece (column index)
     * @param rank Rank position of the put piece (row index)
     * @see Piece
     * @see File
     * @see Rank
     */
    public void putPiece(Piece piece, File file, Rank rank) {
        piece.getPosition().setFile(file);
        piece.getPosition().setRank(rank);
        this.board.get(rank.getValue()).set(file.getValue(), piece);
    }

    /**
     * Puts piece on board when building custom starting position. The position of the
     * piece is set as its parameter. It handles setting inital values of properties
     * of some pieces (Pawn, King, Rook) needed for special moves.
     * @param piece piece to place on the board
     * @see Piece
     */
    public void putPieceInitial(Piece piece) {
        // if its pawn set its moved property based on its rank
        if (Pawn.class.equals(piece.getClass())) {
            if (Color.WHITE.equals(piece.getColor()) && !Rank.TWO.equals(piece.getPosition().getRank())) {
                piece.setMoved();
            } else if (Color.BLACK.equals(piece.getColor()) && !Rank.SEVEN.equals(piece.getPosition().getRank())) {
                piece.setMoved();
            }
        }
        // automatically set rook and king as moved (there is no way of determining they were not moved)
        if (King.class.equals(piece.getClass())) {
            piece.setMoved();
            this.kings[piece.getColor().getIndex()] = piece;
        } else if (Rook.class.equals(piece.getClass())) {
            piece.setMoved();
        }
        this.board.get(piece.getPosition().getRank().getValue()).set(piece.getPosition().getFile().getValue(), piece);
    }

    /**
     * Remove piece and replace it with Empty
     * @param position position on board to clear
     * @see Position
     */
    public void removePiece(Position position) {
        this.putPiece(new Empty(position), position.getFile(), position.getRank());
    }

    /**
     * Move piece from one position to another
     * @param source initial position of the moved piece
     * @param target position of the piece to be moved at
     * @param round round of the game
     * @see Position
     */
    public void movePiece(Position source, Position target, int round) {
        this.checkPawn(source, target, round);
        this.putPiece(this.getPiece(source.getFile(), source.getRank()), target.getFile(), target.getRank());
        this.putPiece(new Empty(source), source.getFile(), source.getRank());
        this.getPiece(target).setMoved();
    }

    /**
     * Check if the moved piece is pawn and if its performing double step.
     * In this case save round of the move (neede for en passant control)
     * @param source initial position of the moved piece
     * @param target position of the piece to be moved at
     * @param round round of the game
     * @see Position
     */
    private void checkPawn(Position source, Position target, int round) {
        if (Pawn.class.equals(this.getPiece(source).getClass())) {
            // check if it was double step
            if (Math.abs(target.getRank().getValue() - source.getRank().getValue()) == 2) {
                this.getPiece(source).setDoubleStep(round);
            }
        }
    }

    /**
     * Gets the board matrix
     * @return two dimensional List of type Piece
     * @see Piece
     */
    public List<List<Piece>> getBoard() {
        return this.board;
    }

    /**
     * Gets whether the board started with standard or custom layout
     * @return boolean value of the custom property
     */
    public boolean getCustom() {
        return this.custom;
    }

    /**
     * Gets piece on specified position
     * @param file File position on the board
     * @param rank Rank position on the board
     * @return Piece that is present on passed position [rank][file]
     * @see File
     * @see Rank
     * @see Piece
     */
    public Piece getPiece(File file, Rank rank) {
        return this.board.get(rank.getValue()).get(file.getValue());
    }

    /**
     * Gets piece on specified position
     * @param position position on the board
     * @return Piece that is present on passed position
     * @see Piece
     * @see Position
     */
    public Piece getPiece(Position position) {
        return this.getPiece(position.getFile(), position.getRank());
    }

    /**
     * Gets all pieces on the board, which have passed color
     * @param color Color of pieces that are requested
     * @return List of Pieces on the board
     * @see Color
     * @see Piece
     */
    public List<Piece> getPieces(Color color) {
        List<Piece> pieces = new ArrayList<>();
        for (List<Piece> row : this.board) {
            for (Piece piece : row) {
                if (color.equals(piece.getColor())) {
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }

    /**
     * Get all pieces from passed List that are of same type as passed piece
     * @param type Piece which class is compared with Pieces in the List
     * @param pieces List of Pieces from which are selected
     * @return List of Pieces that are in passed List and have same class as passed Piece
     */
    public List<Piece> getPiecesOfType(Piece type, List<Piece> pieces) {
        List<Piece> piecesOfType = new ArrayList<>();
        for (Piece piece : pieces) {
            if (type.getClass().equals(piece.getClass()) && piece != type) {
                piecesOfType.add(piece);
            }
        }
        return piecesOfType;
    }

    /**
     * Gets King piece of passed color
     * @param color color of king to return
     * @return King Piece of passed color
     */
    public Piece getKing(Color color) {
        return this.kings[color.getIndex()];
    }
}
