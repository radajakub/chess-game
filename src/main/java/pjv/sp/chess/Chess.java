package pjv.sp.chess;

import pjv.sp.chess.controller.BrowsePGNController;
import pjv.sp.chess.controller.CustomLayoutController;
import pjv.sp.chess.controller.GameController;
import pjv.sp.chess.controller.GameInitController;
import pjv.sp.chess.controller.LoadGameController;
import pjv.sp.chess.controller.SettingsController;
import pjv.sp.chess.controller.StartPageController;
import pjv.sp.chess.view.ViewUtil;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Chess is the main class of the Application
 * @author Jakub Rada
 * @version 1.0
 */
public class Chess extends Application {

    /**
     * Reference to the start page controller
     */
    public static final StartPageController START_PAGE_CONTROLLER = new StartPageController();

    /**
     * Reference to the settings controller
     */
    public static final SettingsController SETTINGS_CONTROLLER = new SettingsController();

    /**
     * Reference to the game init controller
     */
    public static final GameInitController GAME_INIT_CONTROLLER = new GameInitController();

    /**
     * Reference to the game controller
     */
    public static final GameController GAME_CONTROLLER = new GameController();

    /**
     * Reference to the custom layout controller
     */
    public static final CustomLayoutController CUSTOM_LAYOUT_CONTROLLER = new CustomLayoutController();

    /**
     * Reference to the browse pgn controller
     */
    public static final BrowsePGNController BROWSE_PGN_CONTROLLER = new BrowsePGNController();

    /**
     * Reference to the load game controller
     */
    public static final LoadGameController LOAD_GAME_CONTROLLER = new LoadGameController();

    /**
     * Path to finished pgn games
     */
    public static final String finishedPath = "./data/finished/";

    /**
     * Path to unfinished pgn games
     */
    public static final String unfinishedPath = "./data/continue/";

    /**
     * Path to settings file
     */
    public static final String settingsPath = "./settings/";

    /**
     * Primary stage
     */
    private static Stage stage;

    /**
     * Gets primary stage
     * @return main Stage
     */
    public static Stage getPrimaryStage() {
        return Chess.stage;
    }

    /**
     * Starts the application
     * @param args starting arguments
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Inits the Application
     */
    @Override
    public void start(Stage stage) throws IOException {
        Chess.stage = stage;
        Chess.stage.setScene(ViewUtil.SCENE);
        this.loadStyles();
        Chess.START_PAGE_CONTROLLER.showStartPage();
        Chess.stage.show();
    }

    /**
     * Load css styles
     */
    private void loadStyles() {
        ViewUtil.SCENE.getStylesheets().add(this.getClass().getResource("main.css").toExternalForm());
    }

}