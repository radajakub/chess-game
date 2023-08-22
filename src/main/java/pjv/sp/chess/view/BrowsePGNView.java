package pjv.sp.chess.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * BrowsePGNView handles displaying of 
 */
public class BrowsePGNView extends View {

    /**
     * Root pane of the view
     */
    private final BorderPane root = new BorderPane();

    /**
     * Button to go back
     */
    private final Button back = new Button("Back");

    /**
     * Label that contains event of the game
     */
    private final Label event = new Label("");

    /**
     * Label that contains site of the game
     */
    private final Label site = new Label("");

    /**
     * Label that contains date of the game
     */
    private final Label date = new Label("");

    /**
     * Label that contains round of the game
     */
    private final Label round = new Label("");

    /**
     * Label that contains white player full name
     */
    private final Label white = new Label("");

    /**
     * Label that contains black player full name
     */
    private final Label black = new Label("");

    /**
     * Label that contains result of the game
     */
    private final Label result = new Label("");

    /**
     * Label that contains status of the current move
     */
    private final Label status = new Label("");

    /**
     * Button to show next move
     */
    private final Button next = new Button("->");

    /**
     * Size of one "step" in width to relatively place properties into Anchor pane
     */
    private final double widthStep = ViewUtil.WINDOW_WIDTH / 10;

    /**
     * Size of one "step" in height to relateively place properties into Anchor pane
     */
    private final double heightStep = ViewUtil.WINDOW_HEIGHT / 10;

    /**
     * Board view that shows pieces and moves
     */
    private final BoardView chessBoard = new BoardView(this.heightStep < this.widthStep ? 7 * this.heightStep : 7 * this.widthStep);

    /**
     * Create new view object
     */
    public BrowsePGNView() {
        this.prepareScene();
    }

    /**
     * Puts components onto their places
     */
    @Override
    protected void prepareScene() {
        this.root.setRight(ViewUtil.addVBox(
            "browse-right",
            this.back,
            ViewUtil.addHSeparator(),
            ViewUtil.addHBox("pgn-event", new Label("Event: "), this.event),
            ViewUtil.addHBox("pgn-site", new Label("Site: "), this.site),
            ViewUtil.addHBox("pgn-date", new Label("Date: "), this.date),
            ViewUtil.addHBox("pgn-round", new Label("Round: "), this.round),
            ViewUtil.addHBox("pgn-white", new Label("White: "), this.white),
            ViewUtil.addHBox("pgn-black", new Label("Black: "), this.black),
            ViewUtil.addHBox("pgn-result", new Label("Result: "), this.result)
        ));
        this.root.setBottom(ViewUtil.addHBox("browse-bottom", this.next));
        this.root.setTop(ViewUtil.addHBox("browse-top", this.status));
        this.root.setCenter(this.chessBoard.getBoard());
    }

    /**
     * Gets root of the view
     * @return BorderPane that holds all components
     */
    public BorderPane getRoot() {
        return this.root;
    }

    /**
     * Gets button to go back
     * @return back Button
     */
    public Button getBack() {
        return this.back;
    }

    /**
     * Gets button to show next move
     * @return next Button
     */
    public Button getNext() {
        return this.next;
    }

    /**
     * Gets the chessboard
     * @return BoardView that shows pieces and moves
     */
    public BoardView getBoard() {
        return this.chessBoard;
    }

    /**
     * Sets event of the game
     * @param text name of the event
     */
    public void setEvent(String text) {
        this.event.setText(text);
    }

    /**
     * Sets site of the game
     * @param text where the game was played
     */
    public void setSite(String text) {
        this.site.setText(text);
    }

    /**
     * Set date of the game
     * @param text datum when then game was played
     */
    public void setDate(String text) {
        this.date.setText(text);
    }

    /**
     * Set round of the game
     * @param text round of the tournament
     */
    public void setRound(String text) {
        this.round.setText(text);
    }

    /**
     * Set name of the white player
     * @param text who played as white
     */
    public void setWhite(String text) {
        this.white.setText(text);
    }

    /**
     * Set name of the black player
     * @param text who played as black
     */
    public void setBlack(String text) {
        this.black.setText(text);
    }

    /**
     * Set result of the game
     * @param text how the game ended
     */
    public void setResult(String text) {
        this.result.setText(text);
    }

    /**
     * Set status of the last move
     * @param text commentary for the last move
     */
    public void setStatus(String text) {
        this.status.setText(text);
    }
}