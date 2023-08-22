package pjv.sp.chess.view;

import pjv.sp.chess.model.pieces.Color;
import pjv.sp.chess.model.pieces.MoveType;
import pjv.sp.chess.model.pieces.Position;
import pjv.sp.chess.model.board.File;
import pjv.sp.chess.model.board.Rank;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.util.logging.Logger;

/**
 * BoardView class handles visuals of chessboard, that are inclded in another
 * views.
 * It creates the board with labels and puts icons of pieces on it.
 * @author Jakub Rada
 * @version 1.0
 * @see Cell
 */
public class BoardView {

    private static final Logger LOG = Logger.getLogger(BoardView.class.getName());

    /**
     * Layout of the board, represented as a grid
     */
    private final GridPane board = new GridPane();

    /**
     * Size of one cell in grid pane (cell is a square)
     */
    private final double cellSize;

    /**
     * Two dimensional array of cells, which visual part is placed into the
     * grid.
     * Provides access to cells by their File and Rank
     * @see Cell
     */
    private final Cell[][] cells;

    /**
     * Cell that is currently selected and piece on corresponding position
     * on the board is to be moved
     */
    private Cell selected;

    /**
     * Creates new BoardView object
     * @param size - size of whole board (even with labels)
     */
    public BoardView(double size) {
        this.cellSize = size / 10;
        this.cells = new Cell[Rank.COUNT][File.COUNT];
        this.createBoardLayout();
    }

    /**
     * Saves Cell on passed File and Rank to selected property
     * @param file File part of Position on the board (horizontal)
     * @param rank Rank part of Position on the board (vertical)
     * @see File
     * @see Rank
     */
    public void setSelected(File file, Rank rank) {
        this.selected = this.getCell(file, rank);
        BoardView.LOG.info(String.format("%s %s is selected", this.selected.getPosition().getFile().getLabel(), this.selected.getPosition().getRank().getLabel()));
    }

    /**
     * Gets Cell that is selected - piece on corresponding Position on the board
     * is displaying its possible moves
     * @return Cell saved in the selected property
     * @see Cell
     */
    public Cell getSelected() {
        return this.selected;
    }

    /**
     * Gets position of the Cell that is currently selected,
     * this position corresponds with Piece position on the board
     * @return Position object that points on both Cell in View and Piece in Model
     * @see Position
     */
    public Position getSelectedPosition() {
        return this.selected.getPosition();
    }

    /**
     * Gets board layout object to place into another view
     * @return returns board GridPane
     */
    public GridPane getBoard() {
        return this.board;
    }

    /**
     * Gets size of one cell
     * @return size of a cell
     */
    public double getCellSize() {
        return this.cellSize;
        }

    /**
     * Gets Cell on passed position
     * @param file File position on the board (row index)
     * @param rank Rank position on the board (column index)
     * @return Cell object on passed position
     * @see Cell
     * @see File
     * @see Rank
     */
    public Cell getCell(File file, Rank rank) {
        return this.cells[rank.getValue()][file.getValue()];
    }

    /**
     * Puts icon on passed position on the board
     * @param icon Image object of icon to be placed
     * @param file File (row index)
     * @param rank Rank (column index)
     * @see File
     * @see Rank
     */
    public void putPieceIcon(Image icon, File file, Rank rank) {
        this.getCell(file, rank).setIcon(icon);
    }

    /**
     * Adds target overlay icon to Cell on passed Position
     * @param file File part of Position of the Cell
     * @param rank Rank part of Position of the Cell
     * @param moveType type of move that will occurr if this Cell is selected as target
     * @see Cell
     * @see File
     * @see Rank
     * @see MoveType
     */
    public void addTarget(File file, Rank rank, MoveType moveType) {
        this.getCell(file, rank).addTargetIcon();
        this.getCell(file, rank).setMoveType(moveType);
    }

    /**
     * Removes target overlay icon from Cell on passed Position
     * @param file File part of Position of the Cell
     * @param rank Rank part of Position of the Cell
     * @see Cell
     * @see File
     * @see Rank
     */
    public void removeTarget(File file, Rank rank) {
        this.getCell(file, rank).removeTargetIcon();
    }

    /**
     * Removes target overlay icons from all Cells on the board
     * @see Cell
     */
    public void clearTargets() {
        for (Rank rank : Rank.VALUES) {
            for (File file : File.VALUES) {
                this.removeTarget(file, rank);
            }
        }
    }

    /**
     * Moves piece icon from one Cell to another
     * @param sourceIcon Icon to place on inital Position
     * @param source initial Position
     * @param targetIcon Icon to place on target Position
     * @param target target Positoin
     * @see Position
     */
    public void movePiece(Image sourceIcon, Position source, Image targetIcon, Position target) {
        this.putPieceIcon(sourceIcon, source.getFile(), source.getRank());
        this.putPieceIcon(targetIcon, target.getFile(), target.getRank());
    }

    /**
     * Removes piece icon from the Cell on passed Position
     * @param file File part of the Position of the Cell
     * @param rank Rank part of the Position of the Cell
     * @see File
     * @see Rank
     */
    public void removePiece(File file, Rank rank) {
        this.getCell(file, rank).removeIcon();
    }

    /**
     * Creates board layout
     * <ul>
     *      <li>Puts labels of cells around the board</li>
     *      <li>Fills layout with cells according to {@link BoardView#cells}</li>
     * </ul>
     * @see Cell
     * @see Position
     * @see File
     * @see Rank
     */
    private void createBoardLayout() {
        this.board.setId("game-chessboard");
        // setting size of individual cells to be the same
        for (int n = 0; n < Rank.COUNT + 2; n++) {
            this.board.getRowConstraints().add(new RowConstraints(this.cellSize, this.cellSize, this.cellSize, Priority.NEVER, VPos.CENTER , true));
        }
        for (int n = 0; n < File.COUNT + 2; n++) {
            this.board.getColumnConstraints().add(new ColumnConstraints(this.cellSize, this.cellSize, this.cellSize, Priority.NEVER, HPos.CENTER , true));
        }
        // setting labels for chessboard
        for (File file : File.VALUES) {
            this.board.add(new Label(file.getLabel()), file.getValue() + 1, 0);
            this.board.add(new Label(file.getLabel()), file.getValue() + 1, Rank.COUNT + 1);
        }
        for (Rank rank: Rank.VALUES) {
            this.board.add(new Label(rank.getLabel()), 0, rank.getValue() + 1);
            this.board.add(new Label(rank.getLabel()), File.COUNT + 1, rank.getValue() + 1);
        }
        // placing cells into grid
        for (Rank rank : Rank.VALUES) {
            for (File file : File.VALUES) {
                this.cells[rank.getValue()][file.getValue()] = new Cell(
                    ((rank.getValue() + file.getValue()) % 2 == 0) ? Color.WHITE : Color.BLACK,
                    new Position(file, rank),
                    this.cellSize
                );
                this.board.add(this.cells[rank.getValue()][file.getValue()].getPane(), file.getValue() + 1, rank.getValue() + 1);
            }
        }
    }
}
