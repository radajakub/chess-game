package pjv.sp.chess.view;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * GameInitView handles page where are filled details about new game.
 * This view contains fields for PGN format, player name fields and also
 * player-computer choice.
 * @author Jakub Rada
 * @version 1.0
 * @see View
 */
public class GameInitView extends View {

    /**
     * Root pane that holds whole page layout
     */
    private final BorderPane root = new BorderPane();

    /**
     * Checkbox if this player vs computer game
     */
    private final CheckBox onePlayer = new CheckBox("One Player");

    /**
     * Group that selects human player's color (when onePlayer checkbox is selected)
     */
    private final ToggleGroup playerIsGroup = new ToggleGroup();

    /**
     * Choice that human player is white
     */
    private final RadioButton playerIsWhite = new RadioButton("Player plays as white");

    /**
     * Choice that human player is black
     */
    private final RadioButton playerIsBlack = new RadioButton("Player plays as black");

    /**
     * Label for computers intelligence - to be replaced with some selection control
     */
    private final Label ai = new Label("AI default");

    /**
     * Button to go back to previous page
     */
    private final Button back = new Button("Back");

    /**
     * Text field for name of the event where the game took place
     */
    private final TextField event = new TextField();

    /**
     * Text field for city or place where the game was played
     */
    private final TextField place = new TextField();

    /**
     * Line for white player information
     */
    private final HBox whitePlayer;

    /**
     * Line for black player information
     */
    private final HBox blackPlayer;

    /**
     * Text field for white player first name
     */
    private final TextField whiteName = new TextField();

    /**
     * Text field for white player last name
     */
    private final TextField whiteSurname = new TextField();

    /**
     * Text field for black player first name
     */
    private final TextField blackName = new TextField();

    /**
     * Text field for black player last name
     */
    private final TextField blackSurname = new TextField();

    /**
     * Toggle for selecting whether game is to be started with standartd layout
     * or custom layout
     */
    private final ToggleButton customLayout = new ToggleButton("Custom layout");

    /**
     * Button for moving to the next page
     */
    private final Button startGame = new Button("Start game");

    /**
     * Label to display status of filled information
     */
    private final Label status = new Label();

    /**
     * Creates new GameInitView object
     */
    public GameInitView() {
        // initialize text fields and reset choice buttons
        this.playerIsWhite.setToggleGroup(this.playerIsGroup);
        this.playerIsBlack.setToggleGroup(this.playerIsGroup);
        this.resetPlayerOptions();
        this.whiteName.setPromptText("First Name");
        this.whiteSurname.setPromptText("Surname");
        this.blackName.setPromptText("First Name");
        this.blackSurname.setPromptText("Surname");
        this.whitePlayer = ViewUtil.addHBox("game-init-white", new Label("White player: "), this.whiteName, this.whiteSurname);
        this.blackPlayer = ViewUtil.addHBox("game-init-black", new Label("Black player: "), this.blackName, this.blackSurname);
        this.prepareScene();
    }

    /**
     * Resets checkboxes and hide one player selection
     */
    private void resetPlayerOptions() {
        this.onePlayer.setSelected(false);
        this.playerIsWhite.setVisible(false);
        this.playerIsBlack.setVisible(false);
    }

    /**
     * Shows both radio buttons for human player color
     */
    public void showPlayerOptions() {
        this.playerIsWhite.setVisible(true);
        this.playerIsBlack.setVisible(true);
    }

    /**
     * Hides both radio buttons for human player color and resets options
     */
    public void hidePlayerOptions() {
        this.playerIsWhite.setVisible(false);
        this.playerIsBlack.setVisible(false);
        this.resetPlayerOptions();
    }

    /**
     * Hides white player line with both name fields
     */
    public void hideWhitePlayer() {
        this.whitePlayer.setVisible(false);
    }

    /**
     * Shows white player line with both name fields
     */
    public void showWhitePlayer() {
        this.whitePlayer.setVisible(true);
    }

    /**
     * Hides black player line with both name fields
     */
    public void hideBlackPlayer() {
        this.blackPlayer.setVisible(false);
    }

    /**
     * Shows black player line with both name fields
     */
    public void showBlackPlayer() {
        this.blackPlayer.setVisible(true);
    }

    /**
     * Places all properties into root pane layout
     */
    @Override
    protected void prepareScene() {
        this.root.setTop(ViewUtil.addHBox("game-init-top", this.back, new Label("Game Info")));
        this.root.setCenter(ViewUtil.addVBox(
            "game-init-center",
            ViewUtil.addHSeparator(),
            ViewUtil.addHBox("game-init-event", new Label("Event: "), this.event),
            ViewUtil.addHBox("game-init-place", new Label("Place: "), this.place),
            ViewUtil.addHSeparator(),
            this.onePlayer,
            this.playerIsWhite,
            this.playerIsBlack,
            this.ai,
            ViewUtil.addHSeparator(),
            this.whitePlayer,
            this.blackPlayer,
            this.customLayout,
            ViewUtil.addHSeparator()
        ));
        this.root.setBottom(ViewUtil.addVBox("game-init-bottom", this.startGame, this.status));
    }

    /**
     * Gets whole page layout pane
     * @return BorderPane root pane of the page
     */
    public BorderPane getRoot() {
        return this.root;
    }

    /**
     * Gets button to go to previous page
     * @return back Button
     */
    public Button getBackButton() {
        return this.back;
    }

    /**
     * Gets button to go to next page (game or layout setup)
     * @return start Button
     */
    public Button getStartGameButton() {
        return this.startGame;
    }

    /**
     * Gets name of the event where game was played
     * @return String content of the event text field
     */
    public String getEvent() {
        return this.event.getCharacters().toString();
    }

    /**
     * Gets palce where the game was played
     * @return String content of the place text field
     */
    public String getPlace() {
        return this.place.getCharacters().toString();
    }

    /**
     * Gets first name of white player
     * @return String content of the whiteName text field
     */
    public String getWhiteName() {
        return this.whiteName.getCharacters().toString();
    }

    /**
     * Gets last name of white player
     * @return String content of the whiteSurname text field
     */
    public String getWhiteSurname() {
        return this.whiteSurname.getCharacters().toString();
    }

    /**
     * Gets first name of black player
     * @return String content of the blackName text field
     */
    public String getBlackName() {
        return this.blackName.getCharacters().toString();
    }

    /**
     * Gets last name of black player
     * @return String content of the blackSurname text field
     */
    public String getBlackSurname() {
        return this.blackSurname.getCharacters().toString();
    }

    /**
     * Gets whether the game will start with standard or custom piece layout
     * @return boolean value of the customLayout toggle
     */
    public boolean getCustomLayout() {
        return this.customLayout.isSelected();
    }

    /**
     * Puts message into status label
     * @param string text to put into the status label property
     */
    public void setStatus(String string) {
        this.status.setText(string);
    }

    /**
     * Gets checkbox control that specifies type of game
     * @return onePlayer CheckBox property
     */
    public CheckBox getOnePlayerChB() {
        return this.onePlayer;
    }

    /**
     * Gets radio button if the player in player-computer game is white
     * @return playerIsWhite RadioButton property
     */
    public RadioButton getPlayerIsWhite() {
        return this.playerIsWhite;
    }

    /**
     * Gets radio button if the player in player-computer game is white
     * @return playerIsBlack RadioButton property
     */
    public RadioButton getPlayerIsBlack() {
        return this.playerIsBlack;
    }

    /**
     * Empties event text field
     */
    public void clearEvent() {
        this.event.clear();
    }

    /**
     * Empties place text field
     */
    public void clearPlace() {
        this.place.clear();
    }

    /**
     * Empties white name text field
     */
    public void clearWhiteName() {
        this.whiteName.clear();
    }

    /**
     * Empties white surname text field
     */
    public void clearWhiteSurname() {
        this.whiteSurname.clear();
    }

    /**
     * Empties black name text field
     */
    public void clearBlackName() {
        this.blackName.clear();
    }

    /**
     * Empties black surname text field
     */
    public void clearblackSurname() {
        this.blackSurname.clear();
    }

    /**
     * Sets custom layout button to not selected
     */
    public void resetCustomLayout() {
        this.customLayout.setSelected(false);
    }
}
