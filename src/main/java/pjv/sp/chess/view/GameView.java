package pjv.sp.chess.view;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * GameView handles page of the game. Contains chessboard, timer, pgn recorder
 * and other controls of the game.
 * @author Jakub Rada
 * @version 1.0
 * @see View
 */
public class GameView extends View {

    /**
     * Root pane that holds the whole view
     */
    private final AnchorPane root = new AnchorPane();

    /**
     * Label for showing messages about the state of the game
     */
    private final Label status = new Label("Status");

    /**
     * Title of the game, contains event name and both player names
     */
    private final Label title = new Label("Player vs Player");

    /**
     * Field that shows timer for white player
     */
    private final Label whiteTimer = new Label("Timer");

    /**
     * Field that shows timer for black player
     */
    private final Label blackTimer = new Label("Timer");

    /**
     * Field that shows records that are saved into the PGN recorder.
     */
    private final TextArea PGNMoves = new TextArea();

    /**
     * Button that ends the game with loss of player who pressed it
     */
    private final Button resign = new Button("Resign");

    /**
     * Button that saves current state to file for future load
     */
    private final Button save = new Button("Save");

    /**
     * Button that goes back to main menu
     */
    private final Button exit = new Button("Exit");

    /**
     * Button that initiates draw process with the other player
     */
    private final Button offerDraw = new Button("Offer draw");

    /**
     * Button that confirms draw initiated by the other player
     */
    private final Button acceptDraw = new Button("Accept");

    /**
     * Button that declines draw initiated by the other player
     */
    private final Button declineDraw = new Button("Decline");

    /**
     * Size of one "step" in width to relatively place properties into Anchor pane
     */
    private final double widthStep = ViewUtil.WINDOW_WIDTH / 10;

    /**
     * Size of one "step" in height to relateively place properties into Anchor pane
     */
    private final double heightStep = ViewUtil.WINDOW_HEIGHT / 10;

    /**
     * View that holds and maintains chessboard with labels
     */
    private final BoardView chessBoard = new BoardView(this.heightStep < this.widthStep ? 7 * this.heightStep : 7 * this.widthStep);

    /**
     * Creates new GameView page
     */
    public GameView() {
        this.PGNMoves.setEditable(false);
        this.acceptDraw.setDisable(true);
        this.declineDraw.setDisable(true);
        this.prepareScene();
    }

    /**
     * Places properties relatively into Anchor pane
     */
    @Override
    protected void prepareScene() {
        this.placeNode(ViewUtil.addHBox("game-title", this.title), 0, 2 * this.widthStep, 9 * this.heightStep, 0);
        this.placeNode(
            ViewUtil.addVBox(
                "game-timer",
                ViewUtil.addHBox("whiteTimer", new Label("White: "), this.whiteTimer),
                ViewUtil.addHBox("blackTimer", new Label("Black: "), this.blackTimer)
            ), 0, 0, 9 * this.heightStep, 7 * this.widthStep
        );
        this.placeNode(ViewUtil.addHBox("game-pgn", this.PGNMoves), this.heightStep, 0, 3 * this.heightStep, 7 * this.widthStep);
        this.placeNode(this.createControls(), 7 * this.heightStep, 0, 0, 7 *this.widthStep);
        this.placeNode(ViewUtil.addHBox("game-status", this.status), 9 * this.heightStep, 3 * this.widthStep, 0, 0);
        this.placeNode(this.chessBoard.getBoard(), this.heightStep, 3 * this.widthStep, this.heightStep, 0);
    }

    /**
     * Places node into Anchor pane with specified anchors to the edge of the pane
     * @param node Control element to be placed into Anchor pane
     * @param top distance from the top edge
     * @param right distance from the right edge
     * @param bottom distance from the bottom edge
     * @param left distance from the left edge
     */
    private void placeNode(Node node, double top, double right, double bottom, double left) {
        AnchorPane.setTopAnchor(node, top);
        AnchorPane.setRightAnchor(node, right);
        AnchorPane.setBottomAnchor(node, bottom);
        AnchorPane.setLeftAnchor(node, left);
        this.root.getChildren().add(node);
    }

    /**
     * Create container filled with control buttons
     * @return VBox that holds all controls
     */
    private VBox createControls() {
        VBox box = new VBox();
        box.getChildren().add(new HBox(this.resign, this.offerDraw));
        box.getChildren().add(new HBox(this.acceptDraw, this.declineDraw));
        box.getChildren().add(new HBox(this.save, this.exit));
        return box;
    }

    /**
     * Gets the chessboard view
     * @return BoardView property
     * @see BoardView
     */
    public BoardView getBoard() {
        return this.chessBoard;
    }

    /**
     * Gets the whole game page
     * @return AnchorPane root of game page
     */
    public AnchorPane getRoot() {
        return this.root;
    }

    /**
     * Gets TextArea that displays PGN move records
     * @return TextArea to place text in
     */
    public TextArea getPGN() {
        return this.PGNMoves;
    }

    /**
     * Sets text to the title label
     * @param title String message to show in title
     */
    public void setTitle(String title) {
        this.title.setText(title);
    }

    /**
     * Gets button that ends game in surrender
     * @return Button to press when resigning
     */
    public Button getResign() {
        return this.resign;
    }

    /**
     * Gets button that offers draw to the other player
     * @return Button to offer draw
     */
    public Button getOfferDraw() {
        return this.offerDraw;
    }

    /**
     * Gets button that accepts draw and ends the game in draw
     * @return Button to accept draw
     */
    public Button getAcceptDraw() {
        return this.acceptDraw;
    }

    /**
     * Gets button that declines draw and the other player has to continue
     * @return Button to decline draw
     */
    public Button getDeclineDraw() {
        return this.declineDraw;
    }

    /**
     * Gets button to export current state of a game into a file
     * @return Button to save game
     */
    public Button getSave() {
        return this.save;
    }

    /**
     * Gets button that immediately exits this game and goes to start page
     * @return Button to exit game
     */
    public Button getExit() {
        return this.exit;
    }

    /**
     * Sets text to dislplay as Status of the game
     * @param message String to place into Status label
     */
    public void setStatus(String message) {
        this.status.setText(message);
    }

    /**
     * Gets label for white timer
     * @return Label object to set text to
     */
    public Label getWhiteTimer() {
        return this.whiteTimer;
    }

    /**
     * Gets label for black timer
     * @return Label object to set text to
     */
    public Label getBlackTimer() {
        return this.blackTimer;
    }

    /**
     * Resets controls and status to their initial states
     */
    public void reset() {
        this.PGNMoves.clear();
        this.acceptDraw.setDisable(true);
        this.declineDraw.setDisable(true);
        this.offerDraw.setDisable(false);
        this.resign.setDisable(false);
        this.save.setDisable(false);
        this.status.setText("");
        this.chessBoard.clearTargets();
        if (this.chessBoard.getSelected() != null) {
            this.chessBoard.getSelected().unsetSelectedBackground();
        }
    }
}
