package pjv.sp.chess.model;

import pjv.sp.chess.model.board.Board;
import pjv.sp.chess.model.board.File;
import pjv.sp.chess.model.board.MoveControl;
import pjv.sp.chess.model.board.Rank;
import pjv.sp.chess.model.pgn.Disambiguation;
import pjv.sp.chess.model.pieces.Color;
import pjv.sp.chess.model.pieces.Empty;
import pjv.sp.chess.model.pieces.Move;
import pjv.sp.chess.model.pieces.Piece;
import pjv.sp.chess.model.pieces.Position;
import pjv.sp.chess.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Game class holds information about the game. Its main part is the Board
 * object. It also provides switching of players and counting moves.
 * 
 * @author Jakub Rada
 * @version 1.0
 */
public class Game {

    /**
     * Board object that holds current state of the chessboard
     */
    private final Board board;

    /**
     * MoveControl object that provides possible moves for passed piece
     */
    private final MoveControl moveControl;

    /**
     * Name of the event
     */
    private final String event;

    /**
     * Place where the match was played
     */
    private final String place;

    /**
     * Player object that holds information about white player
     */
    private final Player white;

    /**
     * Player object that holds information about black player
     */
    private final Player black;

    /**
     * Datum object that obtains current datum
     */
    private final Datum datum;

    /**
     * Result of the game
     */
    private Result result;

    /**
     * Holds player which makes next move
     */
    private Player playerToPlay;

    /**
     * Move counter
     */
    private int round;

    /**
     * If current player is in check
     */
    private boolean check;

    /**
     * if current player is in checkmate
     */
    private boolean checkmate;

    /**
     * Creates new game object with passed values
     * 
     * @param event Name of the game
     * @param place Name of the place where the game was played
     * @param white White player object
     * @param black Black player object
     * @param board Board object that provides API for moving pieces
     */
    public Game(String event, String place, Player white, Player black, Board board) {
        this.event = event;
        this.place = place;
        this.white = white;
        this.black = black;
        this.board = board;
        this.result = Result.UNKNOWN;
        this.moveControl = new MoveControl(board);
        this.datum = new Datum();
        this.round = 1;
        this.playerToPlay = this.white;
    }

    /**
     * Gets if next player is in check
     * @return if next player is in check or not
     */
    public boolean isCheck() {
        return check;
    }

    /**
     * Sets round to the game
     * @param round value to be saved into the round property
     */
    public void setRound(int round) {
        this.round = round;
    }

    /**
     * Sets if next player is in check
     * @param check if next player is in check or not
     */
    public void setCheck(boolean check) {
        this.check = check;
    }

    /**
     * Get if next player is in checkmate
     * @return boolean if player is in checkmate or not
     */
    public boolean isCheckmate() {
        return checkmate;
    }

    /**
     * Sets that next player is in checkmate
     * @param checkmate if checkmate or not
     */
    public void setCheckmate(boolean checkmate) {
        this.checkmate = checkmate;
    }

    /**
     * Saves passed value into result property
     * @param result Result of the game
     */
    public void setResult(Result result) {
        this.result = result;
    }

    /**
     * Gets result of this game
     * @return value of the result property
     */
    public Result getResult() {
        return this.result;
    }

    /**
     * Gets current number of move of the current game
     * @return value of the round property
     */
    public int getRound() {
        return this.round;
    }

    /**
     * Ends current round and starts a new one.
     * Swithces players.
     */
    public void nextRound() {
        this.round++;
        this.playerToPlay = Color.WHITE.equals(this.playerToPlay.getColor()) ? this.black : this.white;
    }

    /**
     * Gets player that is supposed to make the next move
     * @return Player object to play next
     */
    public Player getPlayerToPlay() {
        return this.playerToPlay;
    }

    /**
     * Sets player of passed color as next to play
     * @param color Color of the player to be next
     */
    public void setPlayerToPlay(Color color) {
        this.playerToPlay = Color.WHITE.equals(color) ? this.white : this.black;
    }

    /**
     * Gets name of the game
     * @return value of the event property
     */
    public String getEvent() {
        return this.event;
    }

    /**
     * Gets name of the place where the game was played
     * @return value of the place property
     */
    public String getPlace() {
        return this.place;
    }

    /**
     * Gets chessboard object
     * @return value of the board property
     */
    public Board getChessBoard() {
        return this.board;
    }

    /**
     * Gets white player object
     * @return value of the whitePlayer property
     */
    public Player getWhitePlayer() {
        return this.white;
    }

    /**
     * Gets black player object
     * @return value of the blackPlayer property
     */
    public Player getBlackPlayer() {
        return this.black;
    }

    /**
     * Gets datum when the game was played
     * @return value of the datum property
     */
    public Datum getDatum() {
        return this.datum;
    }

    /**
     * Gets type of possible Disambiguation for pgn move creation
     * @param file File position of the move
     * @param rank Rank position of the move
     * @param startingPosition inital position of the piece that is moved
     * @return Disambiguation object
     * @see Disambiguation
     */
    public Disambiguation getDisambiguation(File file, Rank rank, Position startingPosition) {
        Piece movedPiece = this.board.getPiece(file, rank);
        this.board.putPiece(new Empty(new Position(file, rank)), file, rank);
        List<Piece> possibleAmbiguations = new ArrayList<>();
        List<Piece> sameType = this.getPiecesOfSameType(movedPiece, this.getPiecesOfColor(movedPiece.getColor()));
        for (Piece piece : sameType) {
            List<Move> moves = this.getMoves(piece);
            for (Move move : moves) {
                if (move.getPosition().getFile().equals(file) && move.getPosition().getRank().equals(rank)) {
                    possibleAmbiguations.add(piece);
                }
            }
        }
        Disambiguation disambiguation;
        if (possibleAmbiguations.size() == 0) {
            disambiguation = Disambiguation.OK;
        } else {
            disambiguation = Disambiguation.ORIGINATING_FILE;
            for (Piece piece : possibleAmbiguations) {
                if (piece.getPosition().getFile().equals(startingPosition.getFile())) {
                    disambiguation = Disambiguation.ORIGINATING_RANK;
                    if (piece.getPosition().getRank().equals(startingPosition.getRank())) {
                        disambiguation = Disambiguation.ORIGINATING_POSITION;
                        break;
                    }
                }
            }
        }
        this.board.putPiece(movedPiece, file, rank);
        return disambiguation;
    }

    /**
     * Gets all possible Moves for passed piece.
     * It filters out moves that would cause check to ally King or caused trouble
     * during castling
     * @param piece Piece for which search moves
     * @return List of Moves that can be played with this Piece
     */
    public List<Move> getMoves(Piece piece) {
        return this.moveControl.getFilteredMoves(piece, this.round);
    }

    /**
     * Gets all pieces on the board, which have passed color
     * @param color Color of pieces that are requested
     * @return List of Pieces on the board
     * @see Color
     * @see Piece
     */
    public List<Piece> getPiecesOfColor(Color color) {
        return this.board.getPieces(color);
    }

    /**
     * Get all pieces from passed List that are of same type as passed piece
     * @param type Piece which class is compared with Pieces in the List
     * @param pieces List of Pieces from which are selected
     * @return List of Pieces that are in passed List and have same class as passed Piece
     */
    public List<Piece> getPiecesOfSameType(Piece type, List<Piece> pieces) {
        return this.board.getPiecesOfType(type, pieces);
    }

    /**
     * Gets if game should end in draw due to insufficient material (impossible checkmate)
     * @return boolean value if the game should be drawn
     */
    public boolean getInsufficientMaterial() {
        return this.moveControl.checkInsufficientMaterial();
    }

    /**
     * Gets if given color is in check
     * @param color piece color that should be checked for check
     * @return boolean value if given color is in check or not
     */
    public boolean checkCheck(Color color) {
        return this.moveControl.isUnderAttack(color.getOppositeColor(), this.board.getKing(color).getPosition(), round);
    }
}
