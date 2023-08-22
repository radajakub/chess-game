package pjv.sp.chess.view;

import pjv.sp.chess.Chess;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * PromotionView handles visuals of Promotion move. Displays icons of possible Pieces to promote to
 * in new modal window. Icons can be selected. It mirrors the Promotion model.
 * @author Jakub Rada
 * @version 1.0
 * @see pjv.sp.chess.model.board.Promotion
 */
public class PromotionView {

    /**
     * Root node of the whole view, everything is placed in it
     */
    private final HBox root = new HBox();

    /**
     * Size of icons displayed in this window (preferably is set to same value as size of squares)
     */
    private final double iconSize;

    /**
     * List of icons that are placed in the root node, used to obtain index of selected icon
     */
    private final List<StackPane> icons = new ArrayList<>();

    /**
     * Undecorated window that is created for this view
     */
    private final Stage modal = new Stage(StageStyle.UNDECORATED);

    /**
     * Scene to fill the modal window
     */
    private Scene scene;

    /**
     * Number of icons in this view
     */
    private final int count;

    /**
     * Create new window for promotion icons (are placed externally from controller)
     * @param count number of icons that will display in the window
     * @param size size of an icon square
     */
    public PromotionView(int count, double size) {
        this.iconSize = size;
        this.count = count;
        this.scene = new Scene(this.root, this.count * this.iconSize, this.iconSize);
        this.scene.getStylesheets().add(this.getClass().getResource("modal.css").toExternalForm());
    }

    /**
     * Sets modal window's paramters and shows it
     * @param x horizontal position on the Screen
     * @param y vertical positon on the Screen
     */
    public void show(double x, double y) {
        this.modal.initModality(Modality.APPLICATION_MODAL);
        this.modal.initOwner(Chess.getPrimaryStage());
        this.modal.setIconified(false);
        this.modal.setResizable(false);
        this.modal.setAlwaysOnTop(true);
        this.modal.setScene(this.scene);
        this.modal.sizeToScene();
        this.modal.setX(x);
        this.modal.setY(y);
        this.modal.show();
    }

    /**
     * Close the promotion window
     */
    public void close() {
        this.modal.close();
    }

    /**
     * Puts icon in the modal view on next position
     * @param icon Image to place in the window
     */
    public void putIcon(Image icon) {
        // create containers for the icon
        StackPane pane = new StackPane();
        ImageView iconView = new ImageView(icon);
        // sets paraters of the icon
        iconView.setFitHeight(this.iconSize);
        iconView.setPreserveRatio(true);
        iconView.setSmooth(true);
        this.root.getStyleClass().add("promotion");
        // add icon to all lists and views
        pane.getChildren().add(iconView);
        this.root.getChildren().add(pane);
        this.icons.add(pane);
    }

    /**
     * Gets index of passed StackPane (StackPane can be clicked on and represents button of the icon)
     * @param pane StackPane that contains any icon from the modal window
     * @return index in the List of icons that mirrors List of pieces in Promotion model
     * @see pjv.sp.chess.model.board.Promotion
     */
    public int getIndex(StackPane pane) {
        return this.icons.indexOf(pane);
    }

    /**
     * Gets List of icon containers in the modal window (can be clicked on)
     * @return List of StackPanes that contains promotion pieces icons
     */
    public List<StackPane> getIcons() {
        return this.icons;
    }

}