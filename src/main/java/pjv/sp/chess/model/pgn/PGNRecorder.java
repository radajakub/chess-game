package pjv.sp.chess.model.pgn;

import pjv.sp.chess.Chess;
import pjv.sp.chess.model.Game;
import pjv.sp.chess.model.pieces.MoveType;
import pjv.sp.chess.model.pieces.Piece;
import pjv.sp.chess.model.pieces.Position;

import javafx.scene.control.TextArea;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * PGNRecorder saves moves and information about a game to a file in standard pgn format
 * @author Jakub Rada
 * @version 1.0
 */
public class PGNRecorder {

    private static final Logger LOG = Logger.getLogger(PGNRecorder.class.getName());

    /**
     * Game that is observed
     */
    private final Game game;

    /**
     * Area where are moves shown
     */
    private final TextArea view;

    /**
     * List where are moves stored
     */
    private final List<String> moveText = new ArrayList<>();

    /**
     * String that specifies starting position in custom games
     */
    private String fen;

    /**
     * round of the game
     */
    private int round;

    /**
     * path where to store the file
     */
    private String path;

    /**
     * Create new PGNRecorder object
     * @param game game that is recorded
     * @param view view where moves are show to user in pgn format
     */
    public PGNRecorder(Game game, TextArea view) {
        this.round = 0;
        this.game = game;
        this.view = view;
        this.path = Chess.finishedPath;
    }

    /**
     * Sets new value to the round property
     * @param round number of the next move
     */
    public void setRound(int round) {
        this.round = round;
    }

    /**
     * Sets new path to path property
     * @param path path to where store files
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Record a move
     * @param piece Piece which was moved
     * @param start initial Position
     * @param end end Position
     * @param type type of the move
     * @param disambiguation possible disambiguations to remove
     * @param round number of the move
     * @param promotion was it promotion
     * @param check was it check
     * @param checkmate was it checkmate
     */
    public void addMove(Piece piece, Position start, Position end, MoveType type, Disambiguation disambiguation, int round, boolean promotion, boolean check, boolean checkmate) {
        String move = PGNTag.createMove(piece, start, end, type, disambiguation, promotion, check, checkmate);
        this.moveText.add(move);
        this.put(round, move);
    }

    /**
     * Adds all moves from passed List to this recorder
     * @param moves moves to be stored
     */
    public void addMoves(List<String> moves) {
        moves.remove(moves.size() - 1);
        this.moveText.addAll(moves);
        int i = this.round;
        for (String move : moves) {
            this.put(i / 2 + 1, move);
            i++;
        }
    }

    /**
     * creates fen for starting position
     */
    public void setFEN() {
        if (game.getChessBoard().getCustom()) {
            this.fen = PGNTag.createTag(PGNTagLabels.FEN, PGNTag.formatFEN(game.getChessBoard()));
        } else {
            this.fen = "";
        }
    }

    /**
     * puts move into view
     * @param round number of the move
     * @param message move to be put
     */
    private void put(int round, String message) {
        if (this.round == round) {
            this.view.appendText(String.format(" %s", message));
            this.view.appendText("\n");
        } else {
            this.round = round;
            this.view.appendText(String.format("%d. %s", this.round, message));
        }
    }

    /**
     * Check if file already exists in path
     * @param path where to search for the file
     * @param filename name of the searched file
     * @return does the file exist
     */
    private boolean fileExists(String path, String filename) {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File[] files = folder.listFiles();
        boolean ret = false;
        for (File f : files) {
            if (f.isFile() && f.getName().equals(filename)) {
                ret = true;
            }
        }
        return ret;
    }

    /**
     * Saves property values into file in pgn format
     */
    public void saveToFile() {
        String subname = String.format(
            "%d%d%d_%s-%s",
            this.game.getDatum().getYear(),
            this.game.getDatum().getMonth(),
            this.game.getDatum().getYear(),
            this.game.getWhitePlayer().getSurname().equals("?") ? "" : this.game.getWhitePlayer().getSurname(),
            this.game.getBlackPlayer().getSurname().equals("?") ? "" : this.game.getBlackPlayer().getSurname()
        );
        String name;
        int count = 1;
        do {
            name = String.format("%s_%d.pgn", subname, count++);
        } while(this.fileExists(this.path, name));
        try (BufferedWriter w = new BufferedWriter(new  OutputStreamWriter(new FileOutputStream(this.path + name), "utf-8"))) {
            w.write(PGNTag.createTag(PGNTagLabels.EVENT, PGNTag.formatEvent(this.game.getEvent())));
            w.newLine();
            w.write(PGNTag.createTag(PGNTagLabels.SITE, PGNTag.formatSite(this.game.getPlace())));
            w.newLine();
            w.write(PGNTag.createTag(PGNTagLabels.DATE, PGNTag.formatDate(this.game.getDatum())));
            w.newLine();
            w.write(PGNTag.createTag(PGNTagLabels.ROUND, PGNTag.formatRound(1)));
            w.newLine();
            w.write(PGNTag.createTag(PGNTagLabels.WHITE, PGNTag.formatPlayer(this.game.getWhitePlayer())));
            w.newLine();
            w.write(PGNTag.createTag(PGNTagLabels.BLACK, PGNTag.formatPlayer(this.game.getBlackPlayer())));
            w.newLine();
            w.write(PGNTag.createTag(PGNTagLabels.RESULT, PGNTag.formatResult(this.game.getResult())));
            w.newLine();
            if (this.game.getChessBoard().getCustom()) {
                w.write(PGNTag.createTag(PGNTagLabels.SETUP, PGNTag.formatSetUp(this.game.getChessBoard().getCustom())));
                w.newLine();
                w.write(this.fen);
                w.newLine();
            }
            w.newLine();
            int lineLen = 0;
            String move = "";
            for (int i = 0; i < this.moveText.size(); i++) {
                if (i % 2 == 0) {
                    move = String.format("%d. %s", i / 2 + 1, this.moveText.get(i));
                } else {
                    move += String.format(" %s", this.moveText.get(i));
                    if (lineLen + move.length() > 80) {
                        w.newLine();
                        lineLen = 0;
                    } else {
                        lineLen += move.length();
                        if (i / 2 != 0) {
                            w.write(" ");
                        }
                    }
                    w.write(move);
                }
            }
            w.write(String.format(" %s", this.game.getResult().getResult()));
        } catch (IOException e) {
            PGNRecorder.LOG.severe(String.format("PGN could not be writte into file %s", this.path));
        }
    }

}