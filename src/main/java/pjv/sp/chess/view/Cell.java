package pjv.sp.chess.view;

import pjv.sp.chess.model.pieces.Color;
import pjv.sp.chess.model.pieces.MoveType;
import pjv.sp.chess.model.pieces.Piece;
import pjv.sp.chess.model.pieces.Position;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Cell class represents individual squares on the chessboard.
 * It holds its position in it and an icon can be placed inside.
 * An event handler can be attached to each of these.
 * @author Jakub Rada
 * @version 1.0
 */
public class Cell {

    /**
     * Target overlay icon that is placed on top of Piece icon
     */
    private static final Image targetIcon = new Image(Piece.class.getResource("move.png").toExternalForm());

    /**
     * Blank icon that represents Empty Piece or no target overlay
     */
    private static final Image blankIcon = new Image(Piece.class.getResource("empty.png").toExternalForm());

    /**
     * CSS class for white square background
     */
    private static final String whiteClass = "whiteCell";

    /**
     * CSS class for black square background
     */
    private static final String blackClass = "blackCell";

    /**
     * CSS class for selected square background
     */
    private static final String selectedClass = "selectedCell";

    /**
     * Layout object that represents the square on the chessboard
     */
    private final StackPane cell = new StackPane();

    /**
     * Position of the cell on the chessboard
     * @see Position
     */
    private final Position position;

    /**
     * ImageView layer in which is placed Piece icon
     */
    private final ImageView icon = new ImageView();

    /**
     * ImageView layer that is on top of the icon layer and displays target overlays for possible moves
     */
    private final ImageView target = new ImageView();

    /**
     * Color of background of the Cell on the board
     */
    private final Color color;

    /**
     * Property that indicates if this cell can be selected
     */
    private boolean active;

    /**
     * Property that indicates if this cell can be target of a move
     */
    private boolean targetable;

    /**
     * Property that indicates what type of move will occurr if this Cell is targeted
     */
    private MoveType moveType;

    /**
     * Creates new cell object with passed parameters
     * @param color Color of the square on chessboard (a css property is
     * added to specify the color)
     * @param position Position on the board
     * @param size size of the square Cell
     * @see Position
     */
    public Cell(Color color, Position position, double size) {
        this.cell.getStyleClass().add(color.equals(Color.WHITE) ? Cell.whiteClass : Cell.blackClass);
        this.position = position;
        this.color = color;
        // place and set ImageView layers
        this.setImageView(this.icon, size);
        this.setImageView(this.target, size);
        this.cell.getChildren().add(this.icon);
        this.cell.getChildren().add(this.target);
        // reset values
        this.active = false;
        this.targetable = false;
    }

    /**
     * Adjusts size and parameters of ImageView layers to be the same across the board
     * @param imageView ImageView layer to set
     * @param size size of the Cell
     */
    private void setImageView(ImageView imageView, double size) {
        imageView.setFitHeight(size);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
    }

    /**
     * Sets background of this Cell to selected (highlights pice icon that is now showing possible moves)
     */
    public void setSelectedBackground() {
        this.cell.getStyleClass().remove(this.color.equals(Color.WHITE) ? Cell.whiteClass : Cell.blackClass);
        this.cell.getStyleClass().add(Cell.selectedClass);
    }

    /**
     * Sets background of this Cell to its inital color before highligting for selection
     */
    public void unsetSelectedBackground() {
        this.cell.getStyleClass().remove(Cell.selectedClass);
        this.cell.getStyleClass().add(this.color.equals(Color.WHITE) ? Cell.whiteClass : Cell.blackClass);
    }

    /**
     * Gets if this Cell is active and can be selected for searching moves
     * @return boolena value of the active property
     */
    public boolean getActive() {
        return this.active;
    }

    /**
     * Sets this Cell as active or inactive
     * @param value boolean value to be saved into active property
     */
    public void setActive(boolean value) {
        this.active = value;
    }

    /**
     * Gets if this Cell is targetable and can be used as target Cell for a Move
     * @return boolean value of the targetable property
     */
    public boolean getTargetable() {
        return this.targetable;
    }

    /**
     * Gets the cell visual object
     * @return StackPane property of the cell to show on the board
     */
    public StackPane getPane() {
        return this.cell;
    }

    /**
     * Gets position of the cell on chessboard
     * @return Position object that contains coordinates on the board
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * Sets image in the Cell
     * @param img Image to set as Piece icon in this Cell
     */
    public void setIcon(Image img) {
        this.icon.setImage(img);
    }

    /**
     * Removes icon image from this Cell (sets it to blank icon)
     */
    public void removeIcon() {
        this.icon.setImage(Cell.blankIcon);
    }

    /**
     * Gets icon that is currently placed in this Cell
     * @return Image object that is in icon layer of this Cell
     */
    public Image getIcon() {
        return this.icon.getImage();
    }

    /**
     * Adds target icon to the target layer of this Cell and sets targetable property to true
     */
    public void addTargetIcon() {
        this.targetable = true;
        this.target.setImage(Cell.targetIcon);
    }

    /**
     * Removes target icon from the target layer of this Cell and sets targetable property to false
     */
    public void removeTargetIcon() {
        this.targetable = false;
        this.target.setImage(Cell.blankIcon);
    }

    /**
     * Saves MoveType that will occurr if this Cell is used as target of a Move
     * @param type type of Move that has this Cell as target
     * @see MoveType
     */
    public void setMoveType(MoveType type) {
        this.moveType = type;
    }

    /**
     * Gets MoveType that will occurr if this Cell is used as target of a Move
     * @return type of Move that has this Cell as target
     * @see MoveType
     */
    public MoveType getMoveType() {
        return this.moveType;
    }
}
