package pjv.sp.chess.controller;

import pjv.sp.chess.Chess;
import pjv.sp.chess.model.board.File;
import pjv.sp.chess.model.board.Promotion;
import pjv.sp.chess.model.board.Rank;
import pjv.sp.chess.model.pieces.Color;
import pjv.sp.chess.model.pieces.Piece;
import pjv.sp.chess.view.PromotionView;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * Promotion controller provedes selection of new piece to be selected instead of
 * the promoted one.
 * @author Jakub Rada
 * @version 1.0
 */
public class PromotionController {

    /**
     * Model that holds pieces that can be used to promotion
     */
    private final Promotion model;

    /**
     * View that mirrors model in a modal dialogue
     */
    private final PromotionView view;

    /**
     * Piece that was selected from dialogue
     */
    private Piece selected;

    /**
     * Creates new PromotionContoller object
     * @param color Color of the promoted piece
     * @param size size of the square on the board
     * @param parentX horizontal position of the modal relative to screen
     * @param parentY vertical position of the modal relative to screen
     */
    public PromotionController(Color color, double size, double parentX, double parentY) {
        this.model = new Promotion(color);
        this.view = new PromotionView(this.model.getPromotions().size(), size);
        this.initView(parentX, parentY);
        this.selected = null;
    }

    /**
     * Gets piece that was selected in promotion modal dialogue
     * @return Piece to be placed instead of promoted pawn
     */
    public Piece getSelected() {
        return this.selected;
    }

    /**
     * Fill dialogue with piece icons
     * @param x horizontal position of the modal relative to screen
     * @param y vertical position of the modal relative to screen
     */
    private void initView(double x, double y) {
        for (Piece piece : this.model.getPromotions()) {
            this.view.putIcon(piece.getIcon());
        }
        this.view.show(x, y);
    }

    /**
     * Request selection of piece to be promoted to by user
     * @param file File position on the board of the promoted piece
     * @param rank Rank position on the board of the promoted piece
     */
    public void getPromotion(File file, Rank rank) {
        for (StackPane pane : this.view.getIcons()) {
            pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    PromotionController.this.view.close();
                    Chess.GAME_CONTROLLER.applyPromotion(file, rank, PromotionController.this.model.getPromotions().get(PromotionController.this.view.getIndex(pane)));
                }
            });
        }
    }

}