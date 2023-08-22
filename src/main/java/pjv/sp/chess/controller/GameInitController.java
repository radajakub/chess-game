package pjv.sp.chess.controller;

import pjv.sp.chess.Chess;
import pjv.sp.chess.model.Game;
import pjv.sp.chess.model.board.Board;
import pjv.sp.chess.model.pieces.Color;
import pjv.sp.chess.model.player.ComputerPlayer;
import pjv.sp.chess.model.player.HumanPlayer;
import pjv.sp.chess.model.player.Player;
import pjv.sp.chess.view.GameInitView;
import pjv.sp.chess.view.ViewUtil;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.logging.Logger;

/**
 * GameInitController class handles getting information about the game from user.
 * It provides filling fields with parameters of the game.
 * @author Jakub Rada
 * @version 1.0
 */
public class GameInitController {

    private static final Logger LOG = Logger.getLogger(StartPageController.class.getName());

    /**
     * View that holds all the fields to fill with
     */
    private final GameInitView gameInitView;

    /**
     * Create new GameInitController object
     */
    public GameInitController() {
        this.gameInitView = new GameInitView();
        this.start();
    }

    /**
     * Shows its view in the main stage and resets its fields
     */
    public void showGameInitPage() {
        this.resetFields();
        ViewUtil.setRoot(this.gameInitView.getRoot());
    }

    /**
     * Resets all fields in the view
     */
    private void resetFields() {
        this.gameInitView.clearEvent();
        this.gameInitView.clearPlace();
        this.gameInitView.clearWhiteName();
        this.gameInitView.clearWhiteSurname();
        this.gameInitView.clearBlackName();
        this.gameInitView.clearblackSurname();
        this.gameInitView.resetCustomLayout();
        if (this.gameInitView.getOnePlayerChB().isSelected()) {
            this.gameInitView.getOnePlayerChB().fire();
        }
    }

    /**
     * Sets acion listeners of user input view components
     */
    private void start() {

        /** go back to main menu if clicked on back button */
        this.gameInitView.getBackButton().setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                GameInitController.LOG.info("Back button pressed - going back to start page");
                Chess.START_PAGE_CONTROLLER.showStartPage();
            }
        });

        /** start game or custom layout selection depending on state of radio button */
        this.gameInitView.getStartGameButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameInitController.LOG.info("Start button pressed - initializing game model");
                Chess.GAME_CONTROLLER.setGame(GameInitController.this.createGame());
                if (Chess.GAME_INIT_CONTROLLER.gameInitView.getCustomLayout()) {
                    GameInitController.LOG.info("Creating custom layout");
                    Chess.CUSTOM_LAYOUT_CONTROLLER.setBoard(Chess.GAME_CONTROLLER.getGame().getChessBoard());
                    Chess.CUSTOM_LAYOUT_CONTROLLER.showLayoutPage();
                } else {
                    GameInitController.LOG.info("Starting game with standard layout");
                    Chess.GAME_CONTROLLER.resetGamePage();
                    Chess.GAME_CONTROLLER.showGamePage();
                }
            }
        });

        /** show/hide some player fields when selecting which player is computer */
        this.gameInitView.getOnePlayerChB().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (GameInitController.this.gameInitView.getOnePlayerChB().isSelected()) {
                    GameInitController.LOG.info("One player checkbox was selected");
                    GameInitController.this.gameInitView.showPlayerOptions();
                    GameInitController.this.gameInitView.getPlayerIsWhite().fire();
                } else {
                    GameInitController.LOG.info("One player checkbox was unselected");
                    GameInitController.this.gameInitView.hidePlayerOptions();
                    GameInitController.this.gameInitView.getPlayerIsWhite().setSelected(false);
                    GameInitController.this.gameInitView.showWhitePlayer();
                    GameInitController.this.gameInitView.showBlackPlayer();
                }
            }
        });

        /** hide black show white player */
        this.gameInitView.getPlayerIsWhite().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameInitController.LOG.info("One player is selected white");
                GameInitController.this.gameInitView.hideBlackPlayer();
                GameInitController.this.gameInitView.showWhitePlayer();
            }
        });

        /** hide white show black player */
        this.gameInitView.getPlayerIsBlack().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameInitController.LOG.info("One player is selected black");
                GameInitController.this.gameInitView.hideWhitePlayer();
                GameInitController.this.gameInitView.showBlackPlayer();
            }
        });
    }

    /**
     * Create game with parameters filled into fields of this view
     * @return Game object ready to start
     */
    private Game createGame() {
        Player whitePlayer;
        Player blackPlayer;
        // create players according to selection in the view
        if (this.gameInitView.getOnePlayerChB().isSelected()) {
            if (this.gameInitView.getPlayerIsWhite().isSelected()) {
                whitePlayer = new HumanPlayer(
                    this.gameInitView.getWhiteName(),
                    this.gameInitView.getWhiteSurname(),
                    Color.WHITE
                );
                blackPlayer = new ComputerPlayer(
                    "Computer",
                    "Random",
                    Color.BLACK
                );
            } else {
                whitePlayer = new ComputerPlayer(
                    "Computer",
                    "Random",
                    Color.WHITE
                );
                blackPlayer = new HumanPlayer(
                this.gameInitView.getBlackName(),
                this.gameInitView.getBlackSurname(),
                Color.BLACK
            );
            }
        } else {
            whitePlayer = new HumanPlayer(
                this.gameInitView.getWhiteName(),
                this.gameInitView.getWhiteSurname(),
                Color.WHITE
            );
            blackPlayer = new HumanPlayer(
                this.gameInitView.getBlackName(),
                this.gameInitView.getBlackSurname(),
                Color.BLACK
            );
        }
        return new Game(
            this.gameInitView.getEvent(),
            this.gameInitView.getPlace(),
            whitePlayer,
            blackPlayer,
            new Board(this.gameInitView.getCustomLayout())
        );
    }
}
