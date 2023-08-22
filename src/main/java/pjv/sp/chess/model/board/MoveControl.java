package pjv.sp.chess.model.board;

import pjv.sp.chess.model.pieces.Bishop;
import pjv.sp.chess.model.pieces.Color;
import pjv.sp.chess.model.pieces.Empty;
import pjv.sp.chess.model.pieces.King;
import pjv.sp.chess.model.pieces.Knight;
import pjv.sp.chess.model.pieces.Move;
import pjv.sp.chess.model.pieces.MoveType;
import pjv.sp.chess.model.pieces.MoveVector;
import pjv.sp.chess.model.pieces.Pawn;
import pjv.sp.chess.model.pieces.Piece;
import pjv.sp.chess.model.pieces.Position;
import pjv.sp.chess.model.pieces.Rook;

import java.util.ArrayList;
import java.util.List;

/**
 * MoveControl class handles requests for possible moves of selected pieces.
 * It scans the board for both normal and special moves.
 * @author Jakub Rada
 * @version 1.0
 */
public class MoveControl {

    /**
     * Property that holds Board which is searched for moves
     */
    private final Board board;

    /**
     * Creates new MoveControl object
     * @param board Board object that whill be searched
     * @see Board
     */
    public MoveControl(Board board) {
        this.board = board;
    }

    /**
     * Checks if checkmate is impossible for both colors
     * @return if game should end in draw due to insufficient material
     */
    public boolean checkInsufficientMaterial() {
        // array in format [white, black] X [king, knight, bishop, rest]
        int[][] pieces = new int[2][4];
        Color[] bishopSquareColor = new Color[2];
        // count pieces that matters from the board
        for (Rank rank : Rank.VALUES) {
            for (File file : File.VALUES) {
                Piece piece = this.board.getPiece(file, rank);
                if (!Empty.class.equals(piece.getClass())) {
                    if (King.class.equals(piece.getClass())) {
                        pieces[piece.getColor().getIndex()][0]++;
                    } else if (Knight.class.equals(piece.getClass())) {
                        pieces[piece.getColor().getIndex()][1]++;
                    } else if (Bishop.class.equals(piece.getClass())) {
                        pieces[piece.getColor().getIndex()][2]++;
                        bishopSquareColor[piece.getColor().getIndex()] = piece.getPosition().getFile().getValue() + piece.getPosition().getRank().getValue() % 2 == 0 ? Color.WHITE : Color.BLACK;
                    } else {
                        pieces[piece.getColor().getIndex()][3]++;
                    }
                }
            }
        }
        boolean ret = false;
        // check all possible cases of insufficient material
        if (pieces[0][3] == 0 && pieces[1][3] == 0) {
            if (
                (pieces[0][1] + pieces[0][2] + pieces[1][1] + pieces[1][2] == 0) || // king vs king
                (pieces[0][1] + pieces[1][1] == 1 && pieces[0][2] + pieces[1][2] == 0) || // king vs king and knight
                (pieces[0][1] + pieces[1][1] == 0 && pieces[0][2] + pieces[1][2] == 1) || // king vs king and bishop
                (pieces[0][1] + pieces[1][1] == 0 && pieces[0][2] == 1 && pieces[1][2] == 1 && bishopSquareColor[0].equals(bishopSquareColor[1])) // king and bishop vs king and bishop (bishops on same colored square)
            ) {
                ret = true;
            }
        }
        return ret;
    }

    /**
     * Check if Position on board can be taken by Color
     * @param color color that could possibly take the possition
     * @param position position that could be taken by any piece of color
     * @param round current round of the game
     * @return boolean value if given position is under attack of given color
     */
    public boolean isUnderAttack(Color color, Position position, int round) {
        for (Rank rank : Rank.VALUES) {
            for (File file : File.VALUES) {
                Piece piece = this.board.getPiece(file, rank);
                // if piece is of given color
                if (color.equals(piece.getColor())) {
                    for (Move move : this.getPossibleMoves(piece, round)) {
                        // if any possible move can end on position return true
                        if (this.areEqualPositions(position, move.getPosition())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Compares two positions on board
     * @param a first Position
     * @param b second Position
     * @return boolean value if Positions a and b are the same
     */
    public boolean areEqualPositions(Position a, Position b) {
        return a.getRank().equals(b.getRank()) && a.getFile().equals(b.getFile());
    }

    /**
     * Check if after passed move is conducted, the ally color will end up in check
     * @param piece Piece that is possibly moved
     * @param move move that will be applied to the piece
     * @param round current round of the game
     * @return boolean value if the move will end up in check
     */
    public boolean checkForCheckAfterMove(Piece piece, Move move, int round) {
        // save current state
        Piece target = this.board.getPiece(move.getPosition());
        Position sourcePosition = new Position(piece.getPosition().getFile(), piece.getPosition().getRank());
        // apply the move
        this.board.putPiece(piece, move.getPosition().getFile(), move.getPosition().getRank());
        this.board.putPiece(new Empty(sourcePosition), sourcePosition.getFile(), sourcePosition.getRank());
        // check for check
        boolean ret = this.isUnderAttack(piece.getColor().getOppositeColor(), this.board.getKing(piece.getColor()).getPosition(), round);
        // reverse the move
        this.board.putPiece(piece, sourcePosition.getFile(), sourcePosition.getRank());
        this.board.putPiece(target, move.getPosition().getFile(), move.getPosition().getRank());
        return ret;
    }

    /**
     * Gets list of possible moves on the board for passed piece
     * @param piece Piece whose possible moves are searched
     * @param round number of the move of the game
     * @return List of Move objects that can be played for the passed Piece
     * @see Piece
     * @see Move
     */
    public List<Move> getPossibleMoves(Piece piece, int round) {
        List<Move> possibleMoves = new ArrayList<>();
        // for scalable moves add scaling constant
        int upperBound = piece.getScalability() ? Rank.COUNT - 1 : 1;
        if (Pawn.class.equals(piece.getClass()) && !piece.hasMoved()) {
            upperBound = 2;
        }
        // constant to adjust move vectors for each color
        int direction = Color.WHITE.equals(piece.getColor()) ? -1 : 1;
        // for each vector of the piece try if target piece is legal
        for (MoveVector move : piece.getMoves()) {
            for (int n = 1; n <= upperBound; n++) {
                int newFileIndex = piece.getPosition().getFile().getValue() + move.getX() * n;
                int newRankIndex = piece.getPosition().getRank().getValue() + direction * move.getY() * n;
                if (this.isOnBoard(newFileIndex, newRankIndex)) {
                    File newFile = File.getFileOfValue(newFileIndex);
                    Rank newRank = Rank.getRankOfValue(newRankIndex);
                    // if it is enemy or empty piece add move to list
                    if (!this.isAlly(piece, newFile, newRank)) {
                        // pawn cannot take in move vector direction
                        if (!(Pawn.class.equals(piece.getClass()) && !this.isEmpty(newFile, newRank))){
                            if (this.isEmpty(newFile, newRank)) {
                                possibleMoves.add(new Move(new Position(newFile, newRank), MoveType.NORMAL));
                            } else {
                                possibleMoves.add(new Move(new Position(newFile, newRank), MoveType.CAPTURE));
                            }
                        }
                    }
                    // if the place on the board is not empty, stop searching in this vector direction
                    if (!this.isEmpty(newFile, newRank)) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        // search moves for pieces with different take vectors
        if (piece.getDifferentTake()) {
            for (MoveVector takeMove : piece.getTakeMoves()) {
                int newFileIndex = piece.getPosition().getFile().getValue() + takeMove.getX();
                int newRankIndex = piece.getPosition().getRank().getValue() + direction * takeMove.getY();
                if (this.isOnBoard(newFileIndex, newRankIndex)) {
                    File newFile = File.getFileOfValue(newFileIndex);
                    Rank newRank = Rank.getRankOfValue(newRankIndex);
                    if (!isAlly(piece, newFile, newRank) && !isEmpty(newFile, newRank)) {
                        possibleMoves.add(new Move(new Position(newFile, newRank), MoveType.CAPTURE));
                    }
                }
            }
        }
        // check en passant moves for pawns
        if (Pawn.class.equals(piece.getClass())) {
            for (Move move : this.checkEnPassant(piece, round)) {
                possibleMoves.add(move);
            }
        }
        // check castling for kings
        if (King.class.equals(piece.getClass()) && !piece.hasMoved()) {
            for (Move move : this.checkCastling(piece)) {
                possibleMoves.add(move);
            }
        }
        return possibleMoves;
    }

    /**
     * Gets all possible Moves for passed piece.
     * It filters out moves that would cause check to ally King or caused trouble
     * during castling
     * @param piece Piece for which search moves
     * @param round round of the game
     * @return List of Moves that can be played with this Piece
     */
    public List<Move> getFilteredMoves(Piece piece, int round) {
        List<Move> filtered = new ArrayList<>();
        for (Move move : this.getPossibleMoves(piece, round)) {
            if (MoveType.KINGSIDE_CASTLING.equals(move.getType()) || MoveType.QUEENSIDE_CASTLING.equals(move.getType())) {
                File file = piece.getPosition().getFile();
                boolean add = true;
                for (int i = 0; i < 3; i++) {
                    if (this.isUnderAttack(piece.getColor().getOppositeColor(), new Position(file, piece.getPosition().getRank()), round)) {
                        add = false;
                    }
                    file = MoveType.KINGSIDE_CASTLING.equals(move.getType()) ? file.getNext() : file.getPrevious();
                }
                if (add) {
                    filtered.add(move);
                }
            } else if (!this.checkForCheckAfterMove(piece, move, round)) {
                filtered.add(move);
            }
        }
        
        return filtered;
    }

    /**
     * Searches for all possible castlings for passed king (Rook move is conducted on controller)
     * @param piece King Piece for which check for castling moves
     * @return List of Moves that can be legaly played as castling
     * @see Piece
     * @see Move
     */
    private List<Move> checkCastling(Piece piece) {
        List<Move> castling = new ArrayList<>();
        // Rooks has to be on their initial position
        for (Piece potentialRook : new Piece[] {this.board.getPiece(File.A, piece.getPosition().getRank()), this.board.getPiece(File.H, piece.getPosition().getRank())}) {
            // Rooks must not have be moved and there has to be empty spaces between them and king
            if (Rook.class.equals(potentialRook.getClass()) && !potentialRook.hasMoved() && this.filesBetweenAreEmpty(piece, potentialRook)) {
                MoveType type;
                File file;
                // differentiate kingside castling and queenside castling
                if (File.A.equals(potentialRook.getPosition().getFile())) {
                    type = MoveType.QUEENSIDE_CASTLING;
                    file = piece.getPosition().getFile().getPrevious().getPrevious();
                } else {
                    type = MoveType.KINGSIDE_CASTLING;
                    file = piece.getPosition().getFile().getNext().getNext();
                }
                castling.add(new Move(new Position(file, piece.getPosition().getRank()), type));
            }
        }
        return castling;
    }

    /**
     * Check if all pieces between two pieces are Empty
     * @param from piece from which check empty pieces
     * @param to piece to which check empty pieces
     * @return boolean value if path is Empty
     * @see Piece
     */
    private boolean filesBetweenAreEmpty(Piece from, Piece to) {
        boolean ret = true;
        File file = from.getPosition().getFile();
        boolean increase = file.getValue() < to.getPosition().getFile().getValue();
        file = increase ? file.getNext() : file.getPrevious();
        while (!file.equals(to.getPosition().getFile())) {
            if (!Empty.class.equals(this.board.getPiece(file, from.getPosition().getRank()).getClass())) {
                ret = false;
                break;
            }
            file = increase ? file.getNext() : file.getPrevious();
        }
        return ret;
    }

    /**
     * Checks if any en passant move can be conducted for passed piece
     * @param piece Piece for which check en passant moves
     * @param round number of the move of the game
     * @return List of Moves that can be conducted as en passant
     * @see Piece
     */
    private List<Move> checkEnPassant(Piece piece, int round) {
        List<Move> enPassants = new ArrayList<>();
        // white en passant
        if (Color.WHITE.equals(piece.getColor()) && Rank.FIVE.equals(piece.getPosition().getRank())) {
            // left en passant
            if (this.isOnBoard(piece.getPosition().getFile().getValue() - 1, Rank.FIVE.getValue())) {
                Piece nextTo = this.board.getPiece(piece.getPosition().getFile().getPrevious(), Rank.FIVE);
                if (Pawn.class.equals(nextTo.getClass()) && !this.isAlly(piece, nextTo)) {
                    if (nextTo.getDoubleStep() == round - 1) {
                        enPassants.add(new Move(new Position(piece.getPosition().getFile().getPrevious(), Rank.FIVE.getNext()), MoveType.ENPASSANT));
                    }
                }
            }
            // rith en passant
            if (this.isOnBoard(piece.getPosition().getFile().getValue() + 1, Rank.FIVE.getValue())) {
                Piece nextTo = this.board.getPiece(piece.getPosition().getFile().getNext(), Rank.FIVE);
                if (Pawn.class.equals(nextTo.getClass()) && !this.isAlly(piece, nextTo)) {
                    if (nextTo.getDoubleStep() == round - 1) {
                        enPassants.add(new Move(new Position(piece.getPosition().getFile().getNext(), Rank.FIVE.getNext()), MoveType.ENPASSANT));
                    }
                }
            }
        // black en passant
        } else if (Color.BLACK.equals(piece.getColor()) && Rank.FOUR.equals(piece.getPosition().getRank())) {
            // left en passant
            if (this.isOnBoard(piece.getPosition().getFile().getValue() - 1, Rank.FOUR.getValue())) {
                Piece nextTo = this.board.getPiece(piece.getPosition().getFile().getPrevious(), Rank.FOUR);
                if (Pawn.class.equals(nextTo.getClass()) && !this.isAlly(piece, nextTo)) {
                    if (nextTo.getDoubleStep() == round - 1) {
                        enPassants.add(new Move(new Position(piece.getPosition().getFile().getPrevious(), Rank.FOUR.getPrevious()), MoveType.ENPASSANT));
                    }
                }
            }
            // right en passant
            if (this.isOnBoard(piece.getPosition().getFile().getValue() + 1, Rank.FOUR.getValue())) {
                Piece nextTo = this.board.getPiece(piece.getPosition().getFile().getNext(), Rank.FOUR);
                if (Pawn.class.equals(nextTo.getClass()) && !this.isAlly(piece, nextTo)) {
                    if (nextTo.getDoubleStep() == round - 1) {
                        enPassants.add(new Move(new Position(piece.getPosition().getFile().getNext(), Rank.FOUR.getPrevious()), MoveType.ENPASSANT));
                    }
                }
            }
        }
        return enPassants;
    }

    /**
     * Check if sqaure on board is empty
     * @param file File Position part of the square
     * @param rank Rank Position part of the square
     * @return boolean value if the square is empty
     * @see File
     * @see Rank
     * @see Empty
     */
    private boolean isEmpty(File file, Rank rank) {
        return Empty.class.equals(this.board.getPiece(file, rank).getClass());
    }

    /**
     * Checks if piece has same color as the other piece
     * @param piece Piece whose color is compared to
     * @param comparedPiece Piece whose color is compared
     * @return boolean value if comparedPiece is of the same color as piece
     * @see Piece
     */
    private boolean isAlly(Piece piece, Piece comparedPiece) {
        return piece.getColor().equals(comparedPiece.getColor());
    }

    /**
     * Checks if piece has same color as the other piece
     * @param piece Piece whose color is compared to
     * @param file File Position part of the compared piece
     * @param rank Rank Position part of the compared piece
     * @return boolean value if Piece on passed Position parts is of the same color as passed piece
     * @see Piece
     * @see File
     * @see Rank
     */
    private boolean isAlly(Piece piece, File file, Rank rank) {
        return this.isAlly(piece, this.board.getPiece(file, rank));
    }

    /**
     * Checks if passed coordinates are on board or not
     * @param x Horizontal value of checked position
     * @param y Vertical value of checked position
     * @return boolean value if passed coordinates are inside the chessboard
     */
    private boolean isOnBoard(int x, int y) {
        return (this.isInsideRankBounds(x) && this.isInsideFileBounds(y));
    }

    /**
     * Check if value is inside vertical bounds of the board
     * @param value value to be checked
     * @return boolean if any Rank has passed value
     */
    private boolean isInsideRankBounds(int value) {
        return (value >= 0 && value < Rank.COUNT);
    }

    /**
     * Check if value is inside horizontal bounds of the board
     * @param value value to be checked
     * @return boolean if any File has passed value
     */
    private boolean isInsideFileBounds(int value) {
        return (value >= 0 && value < File.COUNT);
    }

}