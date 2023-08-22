package pjv.sp.chess.controller;

import pjv.sp.chess.Chess;
import pjv.sp.chess.model.Game;
import pjv.sp.chess.model.board.File;
import pjv.sp.chess.model.board.Rank;
import pjv.sp.chess.model.pgn.PGNLoader;
import pjv.sp.chess.model.pgn.PGNRecorder;
import pjv.sp.chess.model.pieces.Color;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * LoadGameController handles selecting pgn file to load and passing it to GameController
 * when in ready state
 * @author Jakub Rada
 * @version 1.0
 */
public class LoadGameController {

    /**
     * File that holds file descriptor of the file to load
     */
    private java.io.File file;

    /**
     * PGNLoader object that provides getting game to state after all moves in pgn file
     */
    private PGNLoader loader;

    /**
     * Creates new LoadGameController object
     */
    public LoadGameController() {
    }

    /**
     * Select file to load and then manipulate with loader to get game to required state
     */
    public void load() {
        this.selectFile();
        if (this.file == null) {
            return;
        }
        // init loader with selected file
        this.loader = new PGNLoader(this.file);
        // create game in intial position from game tags
        Game game = this.loader.createGame();
        game.getChessBoard().setCustom(this.loader.getCustom());
        // check if castlings can be performed in the future
        this.processCastlings(this.loader.getMovedForCastling(), game);
        // create pgn recorder for created game and fill it with game information
        PGNRecorder pgn = new PGNRecorder(game, Chess.GAME_CONTROLLER.getGameView().getPGN());
        // set game parameters to current state and pass it to game controller
        game.setPlayerToPlay(this.loader.getColorToPlay());
        game.setRound(this.loader.getMoves().size());
        game.setResult(this.loader.getResult());
        Chess.GAME_CONTROLLER.resetGamePage();
        pgn.addMoves(this.loader.getMoves());
        Chess.GAME_CONTROLLER.setGame(game, pgn);
        Chess.GAME_CONTROLLER.showGamePage();
    }

    /**
     * Set piece properties when castling can be performed in the future
     * @param movedForCastling boolean values for kings and rooks if they moved or not
     * @param game Game whose state is modified
     */
    private void processCastlings(boolean[][] movedForCastling, Game game) {
        for (Color color : new Color[] {Color.WHITE, Color.BLACK}) {
            if (!movedForCastling[color.getIndex()][0]) {
                game.getChessBoard().getPiece(File.A, color.equals(Color.WHITE) ? Rank.ONE : Rank.EIGHT).setNotMoved();
            }
            if (!movedForCastling[color.getIndex()][1]) {
                game.getChessBoard().getPiece(File.H, color.equals(Color.WHITE) ? Rank.ONE : Rank.EIGHT).setNotMoved();
            }
            if (!movedForCastling[2][color.getIndex()]) {
                game.getChessBoard().getKing(color).setNotMoved();
            }
        }
    }

    /**
     * Provide user with file selection dialogue
     */
    private void selectFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose PGN file");
        chooser.getExtensionFilters().addAll(
            new ExtensionFilter("PGN files", "*.pgn")
        );
        this.file = chooser.showOpenDialog(Chess.getPrimaryStage());
    }

}