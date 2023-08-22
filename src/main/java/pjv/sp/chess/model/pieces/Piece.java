package pjv.sp.chess.model.pieces;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Piece abstract class unites properties and functionality of all pieces.
 * Specific pieces inherits theses from this class.
 * @author Jakub Rada
 * @version 1.0
 */
public abstract class Piece {

    /**
     * Total number of piece in the game (Empty does not count even though it
     * inherits as well)
     */
    public static int COUNT = 6;

    /**
     * List of classes that inherit from Piece and can be used to mirror indexes
     * in other arrays
     * @see pjv.sp.chess.model.board.BoardChecker
     */
    public static List<Class<? extends Piece>> pieceClasses = new ArrayList<>(Arrays.asList(
        Pawn.class,
        Rook.class,
        Knight.class,
        Bishop.class,
        Queen.class,
        King.class
    ));

    /**
     * Color of the piece
     */
    protected final Color color;

    /**
     * List of all moves that a piece can conduct
     */
    protected final List<MoveVector> moves;

    /**
     * Position of the piece on the chessboard
     */
    protected final Position position;

    /**
     * Boolean value whether the moves can be multiplied by any n or not
     */
    protected final boolean isMoveScalable;

    /**
     * Boolean value whether the piece takes pieces differently than moves
     */
    protected final boolean hasDifferentTake;

    /**
     * List of all moves that a piece can make only for taking (for Pawn)
     */
    protected final List<MoveVector> takeMoves;

    /**
     * Icon of the piece that is shown in the chessborad view
     */
    protected Image icon;

    /**
     * Boolean value whether the piece was moved or is on it's initial position
     */
    protected boolean wasMoved;

    /**
     * Field that saves round of the game when the piece performed double step (for Pawn)
     */
    protected int doubleStep;

    /**
     * Creates new Piece object
     * @param color Color of the new piece
     * @param position Position object that holds position on the board
     * @param isMoveScalable can basic moves be multiplied by any n
     * @param hasDifferentTake does piece take differently than moves
     */
    public Piece(Color color, Position position, boolean isMoveScalable, boolean hasDifferentTake) {
        this.moves = new ArrayList<>();
        this.takeMoves = new ArrayList<>();
        this.color = color;
        this.position = position;
        this.isMoveScalable = isMoveScalable;
        this.hasDifferentTake = hasDifferentTake;
        this.wasMoved = false;
        this.doubleStep = 0;
        this.generateMoves();
        this.loadIcon();
    }

    /**
     * Adds all possible moves for this piece from the Move enum
     */
    protected abstract void generateMoves();

    /**
     * Loads bishop icon from resources of this project
     */
    protected abstract void loadIcon();

    /**
     * Gets list of MoveVectors used to take other pieces
     * @return List of MoveVector objects to take pieces
     * @see MoveVector
     */
    public List<MoveVector> getTakeMoves() {
        return this.takeMoves;
    }

    /**
     * Gets icon of the piece
     * @return Image object that contains the icon
     */
    public Image getIcon() {
        return this.icon;
    }

    /**
     * Gets possible moves of the piece
     * @return List of Moves by which the piece can move
     * @see MoveVector
     */
    public List<MoveVector> getMoves() {
        return this.moves;
    }

    /**
     * Gets if the moves can be multiplied by any n
     * @return boolean value of the isMoveScalable property
     */
    public boolean getScalability() {
        return this.isMoveScalable;
    }

    /**
     * Get if the take moves are different than normal moves
     * @return boolean value of the hasDifferentTake property
     */
    public boolean getDifferentTake() {
        return this.hasDifferentTake;
    }

    /**
     * Gets color of the piece
     * @return Color value of the color property
     * @see Color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Gets position of the piece
     * @return value of the position property
     * @see Position
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * Gives information if piece moved from its original position
     * @return true if it was moved at least once, false if it was not moved
     */
    public boolean hasMoved() {
        return this.wasMoved;
    }

    /**
     * Saves information that piece was moved at least once
     */
    public void setMoved() {
        this.wasMoved = true;
    }

    /**
     * Save information that piece was not moved once
     */
    public void setNotMoved() {
        this.wasMoved = false;
    }

    /**
     * Gets number of move of the game when double step was conducted (used for Pawn only)
     * @return value of the doubleStep property
     */
    public int getDoubleStep() {
        return this.doubleStep;
    }

    /**
     * Saves move number of the game when double step occurred
     * @param round number of the move of the game
     */
    public void setDoubleStep(int round) {
        this.doubleStep = round;
    }
}
