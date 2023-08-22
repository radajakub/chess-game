package pjv.sp.chess.controller;

import pjv.sp.chess.Chess;
import pjv.sp.chess.view.StartPageView;
import pjv.sp.chess.view.ViewUtil;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.logging.Logger;

/**
 * StartPageController handles user selection of action performed by the application.
 * It basically calls other controller based on clicked button.
 * @author Jakub Rada
 * @version 1.0
 */
public class StartPageController {

    private static final Logger LOG = Logger.getLogger(StartPageController.class.getName());

    /**
     * View that holds buttons for each action available in the application
     */
    private final StartPageView startPageView;

    /**
     * Creates new StartPageController
     */
    public StartPageController() {
        this.startPageView = new StartPageView();
        this.start();
    }

    /**
     * Shows the view in the primary stage
     */
    public void showStartPage() {
        ViewUtil.setRoot(this.startPageView.getRoot());
    }

    /**
     * Adds action listeners to buttons available in the view
     */
    private void start() {

        this.startPageView.getQuitButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                StartPageController.LOG.info("Quit button pressed - exiting");
                Platform.exit();
            }
        });;

        this.startPageView.getSettingsButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                StartPageController.LOG.info("Settings button pressed - going to settings");
                Chess.SETTINGS_CONTROLLER.showSettingsPage();
            }
        });

        this.startPageView.getStartGameButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                StartPageController.LOG.info("Start game pressed - going to game init");
                Chess.GAME_INIT_CONTROLLER.showGameInitPage();
            }
        });

        this.startPageView.getBrowsePGN().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                StartPageController.LOG.info("Browse pgn pressed - going to pgn");
                Chess.BROWSE_PGN_CONTROLLER.showBrowsePage();
            }
        });

        this.startPageView.getLoadGame().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                StartPageController.LOG.info("Load game pressed - going to pgn");
                Chess.LOAD_GAME_CONTROLLER.load();
            }
        });
    }
}
