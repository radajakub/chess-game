package pjv.sp.chess.controller;

import pjv.sp.chess.Chess;
import pjv.sp.chess.model.Status;
import pjv.sp.chess.model.board.Board;
import pjv.sp.chess.model.board.BoardChecker;
import pjv.sp.chess.model.board.File;
import pjv.sp.chess.model.board.Rank;
import pjv.sp.chess.model.pieces.Piece;
import pjv.sp.chess.view.CustomLayoutView;
import pjv.sp.chess.view.ViewUtil;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.logging.Logger;

/**
 * CustomLayoutController interprets user input when creating custom layout
 * and requests appropriate changes on model and view. It also starts other
 * controllers when requested by user.
 * @author Jakub Rada
 * @version 1.0
 * @see CustomLayoutView
 * @see Board
 * @see BoardChecker
 */
public class CustomLayoutController {

    private static final Logger LOG = Logger.getLogger(CustomLayoutController.class.getName());

    /**
     * Property that holds view for this action
     */
    private final CustomLayoutView customLayoutView;

    /**
     * Model that represents created board and contains Pieces in layout
     */
    private Board board;

    /**
     * Property that is responsible for checking validity of the Board model
     */
    private BoardChecker boardChecker = new BoardChecker();

    /**
     * Creates new CustomLayoutController
     */
    public CustomLayoutController() {
        this.customLayoutView = new CustomLayoutView();
        this.start();
    }

    /**
     * Sets board object that is used to save created custom layout
     * @param board board object to modify when creating layout
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Shows view for this action and refreshes status label
     */
    public void showLayoutPage() {
        this.updateView();
        this.setStatus(this.boardChecker.checkBoard());
        ViewUtil.setRoot(this.customLayoutView.getRoot());
    }

    /**
     * Updates view that displays content of the model and BoardChecker property
     */
    private void updateView() {
        this.boardChecker.reset();
        // for every piece on the board add its icon to board view and add it to board checker
        for (Rank rank : Rank.VALUES) {
            for (File file : File.VALUES) {
                Piece newPiece = this.board.getPiece(file, rank);
                this.customLayoutView.getBoardView().putPieceIcon(newPiece.getIcon(), file, rank);
                this.boardChecker.addPiece(newPiece);
            }
        }
    }

    /**
     * Display status message and enables/disables button to continue based on
     * board's validity
     * @param status status object that represents current state of the board
     */
    private void setStatus(Status status) {
        if (status.isCorrect()) {
            this.customLayoutView.getConfirm().setDisable(false);
        } else {
            this.customLayoutView.getConfirm().setDisable(true);
        }
        this.customLayoutView.setStatus(status.getMessage());
    }

    /**
     * Sets action for interactable elements of the view to trigger actions on model and view
     */
    public void start() {

        /** Empty the board when clicked on Clear board button */
        this.customLayoutView.getClearBoard().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CustomLayoutController.LOG.info("Button to clear board pressed");
                CustomLayoutController.this.board.clearBoard();
                CustomLayoutController.this.updateView();
                CustomLayoutController.this.setStatus(CustomLayoutController.this.boardChecker.checkBoard());
            }
        });

        /** Switch to another view and controller when the state of board is confirmed */
        this.customLayoutView.getConfirm().setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                CustomLayoutController.LOG.info("Start button pressed - starting game");
                Chess.GAME_CONTROLLER.resetGamePage();
                Chess.GAME_CONTROLLER.showGamePage();
            }
        });

        /** Go back to game initialization if clicked on back button */
        this.customLayoutView.getBack().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CustomLayoutController.LOG.info("Back button pressed - going back to game initialization");
                Chess.GAME_INIT_CONTROLLER.showGameInitPage();
            }
        });

        /** For each square on the board view add piece icon selected in pieceStackView when clicked on */
        for (Rank rank : Rank.VALUES) {
            for (File file : File.VALUES) {
                // when mouse is clicked on StackPane that belongs to cell on [file, rank] coordinates
                this.customLayoutView.getBoardView().getCell(file, rank).getPane().setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        // get piece that is currently on clicked position
                        Piece oldPiece = CustomLayoutController.this.board.getPiece(file, rank);
                        // piece that is currently selected in pieceStackView
                        Piece newPiece = CustomLayoutController.this.customLayoutView.getPieceStackView().getPiece(file, rank);
                        // put piece on board and its icon on view
                        CustomLayoutController.this.board.putPieceInitial(newPiece);
                        CustomLayoutController.this.customLayoutView.getBoardView().putPieceIcon(CustomLayoutController.this.board.getPiece(file, rank).getIcon(), file, rank);
                        CustomLayoutController.LOG.info(String.format("%s %s placed on %s%s", newPiece.getColor().getLabel(), newPiece.getClass().getSimpleName(), file.getLabel(), rank.getLabel()));
                        // check board for validity
                        Status status = CustomLayoutController.this.boardChecker.changeOnBoard(oldPiece, newPiece);
                        CustomLayoutController.this.setStatus(status);
                    }
                });
            }
        }
    }
}
