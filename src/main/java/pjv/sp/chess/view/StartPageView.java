package pjv.sp.chess.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * StartPageView handles starting page of the application.
 * It contains controls to get to every part of aplication.
 */
public class StartPageView extends View {

    /**
     * Root pane that holds the whole view
     */
    private final BorderPane root = new BorderPane();

    /**
     * Button to create and start a new game
     */
    private final Button startGame = new Button("New Game");

    /**
     * Button to load game from pgn
     */
    private final Button loadGame = new Button("Load Game");

    /**
     * Button to go to browsing pgn
     */
    private final Button browsePGN = new Button("Browse PGN");

    /**
     * Button to go to application settings page
     */
    private final Button settings = new Button("Settings");

    /**
     * Button to quit the application
     */
    private final Button quit = new Button("Quit");

    /**
     * Create new StartPageView
     */
    public StartPageView() {
        this.prepareScene();
    }

    /**
     * Puts all properties to root pane
     */
    @Override
    protected void prepareScene() {
        this.root.setTop(ViewUtil.addHBox("start-page-top", new Label("Chess")));
        this.root.setCenter(ViewUtil.addVBox("start-page-center", this.startGame, this.loadGame, this.browsePGN, this.settings));
        this.root.setBottom(ViewUtil.addHBox("start-page-bottom", this.quit));
    }

    /**
     * Gets button to exit the application
     * @return quit Button property
     */
    public Button getQuitButton() {
        return this.quit;
    }

    /**
     * Gets button to go browse pgn
     * @return browsePgn button property
     */
    public Button getBrowsePGN() {
        return this.browsePGN;
    }

    /**
     * Gets button to load game from pgn
     * @return loadGame button property
     */
    public Button getLoadGame() {
        return this.loadGame;
    }

    /**
     * Gets button to show settings of the application
     * @return settings Button property
     */
    public Button getSettingsButton() {
        return this.settings;
    }

    /**
     * Gets whole settings view layout
     * @return BorderPane root
     */
    public BorderPane getRoot() {
        return this.root;
    }

    /**
     * Gets button to start new game
     * @return startGame Button property
     */
    public Button getStartGameButton() {
        return this.startGame;
    }
}
