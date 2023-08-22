package pjv.sp.chess.view;

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
import pjv.sp.chess.model.board.File;
import pjv.sp.chess.model.board.Rank;

import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

/**
 * PieceStackView handles selection of which pieces is to be put on the chessboard.
 * @author Jakub Rada
 * @version 1.0
 */
public class PieceStackView {

    /**
     * Main node that contains everything in this component
     */
    private final VBox stack;

    /**
     * Title of the piece stack
     */
    private final Label title = new Label("Pieces: ");

    /**
     * Color toggle group - specifies color of put piece
     */
    private final ToggleGroup colorGroup = new ToggleGroup();

    /**
     * Toggle to indicate put piece is white
     */
    private final ToggleButton white = new ToggleButton("white");

    /**
     * Toggle to indicate put piece is black
     */
    private final ToggleButton black = new ToggleButton("black");

    /**
     * Piece toggle group - specifies type of the put piece
     */
    private final ToggleGroup pieceGroup = new ToggleGroup();

    /**
     * Empty piece selector
     */
    private final ToggleButton empty = new ToggleButton("empty");

    /**
     * Pawn piece selector
     */
    private final ToggleButton pawn = new ToggleButton("pawn");

    /**
     * Rook piece selector
     */
    private final ToggleButton rook = new ToggleButton("rook");

    /**
     * Knight piece selector
     */
    private final ToggleButton knight = new ToggleButton("knight");

    /**
     * Bishop piece selector
     */
    private final ToggleButton bishop = new ToggleButton("bishop");

    /**
     * Queen piece selector
     */
    private final ToggleButton queen = new ToggleButton("queen");

    /**
     * King piece selector
     */
    private final ToggleButton king = new ToggleButton("king");

    /**
     * Creates new PieceStackView
     */
    public PieceStackView() {
        this.groupButtons();
        this.stack = ViewUtil.addVBox(
            "customLayout-stack",
            ViewUtil.addHBox("stack-colors", this.white, this.black),
            ViewUtil.addHSeparator(),
            this.empty,
            this.pawn,
            this.rook,
            this.knight,
            this.bishop,
            this.queen,
            this.king
        );
    }

    /**
     * Places all buttons to their toggle group
     */
    private void groupButtons() {
        this.empty.setToggleGroup(this.pieceGroup);
        this.empty.setSelected(true);
        this.pawn.setToggleGroup(this.pieceGroup);
        this.rook.setToggleGroup(this.pieceGroup);
        this.knight.setToggleGroup(this.pieceGroup);
        this.bishop.setToggleGroup(this.pieceGroup);
        this.queen.setToggleGroup(this.pieceGroup);
        this.king.setToggleGroup(this.pieceGroup);
        this.white.setToggleGroup(this.colorGroup);
        this.white.setSelected(true);
        this.black.setToggleGroup(this.colorGroup);
    }

    /**
     * Gets which color is currently selected
     * @return selected Color of the new Piece
     * @see Color
     */
    private Color getSelectedColor() {
        return this.colorGroup.getSelectedToggle().equals(this.white) ? Color.WHITE : Color.BLACK;
    }

    /**
     * Gets piece currently to be placed on the board
     * @param file File position on the board (column index)
     * @param rank Rank position on the board (row index)
     * @return new Piece of the selected type and color and passed position
     * @see File
     * @see Rank
     */
    public Piece getPiece(File file, Rank rank) {
        Color color = this.getSelectedColor();
        Piece piece;
        if (this.pawn.isSelected()) {
            piece = new Pawn(color, new Position(file, rank));
        } else if (this.rook.isSelected()) {
            piece = new Rook(color, new Position(file, rank));
        } else if (this.knight.isSelected()) {
            piece = new Knight(color, new Position(file, rank));
        } else if (this.bishop.isSelected()) {
            piece = new Bishop(color, new Position(file, rank));
        } else if (this.queen.isSelected()) {
            piece = new Queen(color, new Position(file, rank));
        } else if (this.king.isSelected()) {
            piece = new King(color, new Position(file, rank));
        } else {
            piece = new Empty(new Position(file, rank));
        }
        return piece;
    }

    /**
     * Gets the root of PieceStackView
     * @return VBox main node of the component
     */
    public VBox getStack() {
        return ViewUtil.addVBox(
            "select-piece",
            this.title,
            this.stack
        );
    }
}
