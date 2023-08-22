package pjv.sp.chess.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * CustomLayoutView handles page where starting layout of a game can be set.
 * It allows only possible starts following to rules of chess.
 * @author Jakub Rada
 * @version 1.0
 * @see View
 */
public class CustomLayoutView extends View {

    /**
     * Root pane that holds the whole view
     */
    private final BorderPane root = new BorderPane();

    /**
     * Title of the page
     */
    private final Label title = new Label("Place pieces on chessboard");

    /**
     * Subtitle with instructions what to do
     */
    private final Label subtitle = new Label("Clicking on chessboard places pieces selected in right menu");

    /**
     * BoardView that holds board visuals
     * @see BoardView
     */
    private final BoardView chessBoard = new BoardView(ViewUtil.WINDOW_HEIGHT > ViewUtil.WINDOW_WIDTH ? ViewUtil.WINDOW_WIDTH * 7 / 10 : ViewUtil.WINDOW_HEIGHT * 7 / 10);

    /**
     * PieceStackView holds buttons that select which piece is to be placed
     * @see PieceStackView
     */
    private final PieceStackView pieces = new PieceStackView();

    /**
     * Button to confirm created layout and start game
     */
    private final Button confirm = new Button("Confirm starting layout");

    /**
     * Button to go back to previous page
     */
    private final Button back = new Button("Back");

    /**
     * Label for status error messages
     */
    private final Label status = new Label();

    /**
     * Button to clear created layout and start from scratch
     */
    private final Button clearBoard = new Button("Clear board");

    /**
     * Creates new CustomLayoutView object
     */
    public CustomLayoutView() {
        this.title.getStyleClass().add("title");
        this.prepareScene();
    }

    /**
     * Places properties to root pane
     * @see View
     */
    @Override
    protected void prepareScene() {
        this.root.setCenter(this.chessBoard.getBoard());
        this.root.setTop(ViewUtil.addVBox("customLayout-top", ViewUtil.addHBox("customLayout-title", this.title), this.subtitle));
        this.root.setBottom(ViewUtil.addVBox("customlayout-bot", ViewUtil.addHBox("customLayou-buttons", this.back, this.clearBoard, this.confirm), this.status));
        this.root.setRight(this.pieces.getStack());
    }

    /**
     * Gets button group that determines which piece is placed to board
     * @return PieceStackView property with piece selection
     * @see PieceStackView
     */
    public PieceStackView getPieceStackView() {
        return this.pieces;
    }

    /**
     * Gets view that contains chessboard
     * @return BoardView object that maintains chessboard
     * @see BoardView
     */
    public BoardView getBoardView() {
        return this.chessBoard;
    }

    /**
     * Gets whole layout selection page
     * @return BorderPane root of custom layout page
     */
    public BorderPane getRoot() {
        return this.root;
    }

    /**
     * Gets button to go back to previous page
     * @return Button to go to previous page
     */
    public Button getBack() {
        return this.back;
    }

    /**
     * Gets button to clear board and start over
     * @return Button to clear board
     */
    public Button getClearBoard() {
        return this.clearBoard;
    }

    /**
     * Gets button to confirm cretaed layout
     * @return Button to confirm current layout
     */
    public Button getConfirm() {
        return this.confirm;
    }

    /**
     * Sets error message to status label
     * @param message String to show as status message
     */
    public void setStatus(String message) {
        this.status.setText(message);
    }
}
