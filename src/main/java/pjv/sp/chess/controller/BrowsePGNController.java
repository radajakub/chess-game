package pjv.sp.chess.controller;

import pjv.sp.chess.Chess;
import pjv.sp.chess.model.pgn.PGNBrowser;
import pjv.sp.chess.view.BrowsePGNView;
import pjv.sp.chess.view.ViewUtil;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.util.logging.Logger;

/**
 * BrowsePGNController interprets user input on BrowsePGNView and makes
 * PGNBrowser model class perform appropriate actions.
 * It also switches to other controllers and views when requested by user.
 * @author Jakub Rada
 * @version 1.0
 * @see PGNBrowser
 * @see BrowsePGNView
 */
public class BrowsePGNController {

    private static final Logger LOG = Logger.getLogger(BrowsePGNController.class.getName());

    /**
     * Property that holds view that shows performed actions
     */
    private final BrowsePGNView view;

    /**
     * Model that is responsible for data manipulation
     */
    private PGNBrowser model;

    /**
     * Property that indicates if last PGN move of the selected game was played
     */
    private boolean end;

    /**
     * Creates new instance of this class
     */
    public BrowsePGNController() {
        this.view = new BrowsePGNView();
    }

    /**
     * Gets PGN file to open from user, initiates model for browsing it and
     * shows its view
     */
    public void showBrowsePage() {
        this.end = false;
        // get PGN file from user by selecting it in file system
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose PGN file");
        chooser.getExtensionFilters().add(
            new ExtensionFilter("PGN files", "*.pgn")
        );
        File selectedFile = chooser.showOpenDialog(Chess.getPrimaryStage());
        // if no file was selected, return back to start page
        if (selectedFile == null) {
            BrowsePGNController.LOG.info("No file was selected - going back to start page");
            Chess.START_PAGE_CONTROLLER.showStartPage();
            return;
        }
        BrowsePGNController.LOG.info(String.format("%s was selected - preparing for browsing", selectedFile.getName()));
        // create model that will parse files contents
        this.model = new PGNBrowser(selectedFile, this.view);
        ViewUtil.setRoot(this.view.getRoot());
        this.view.getNext().setDisable(false);
        this.start();
    }

    /**
     * Method that sets action listeners for ui elements
     */
    private void start() {

        /** Set action after pressing the back button */
        this.view.getBack().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BrowsePGNController.LOG.info("Back button pressed - going back to start menu");
                Chess.START_PAGE_CONTROLLER.showStartPage();
            }
        });

        /** Set action after pressing button to show next move */
        this.view.getNext().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BrowsePGNController.LOG.info("Next move button pressed - conducting next move");
                // if there is no next move show status and set end to true
                if (!BrowsePGNController.this.model.hasNextMove()) {
                    BrowsePGNController.this.model.removeHighlights();
                    BrowsePGNController.this.end = true;
                    BrowsePGNController.this.view.setStatus("Game ended");
                }
                // if end is false perform next move from pgn file
                if (!BrowsePGNController.this.end) {
                    BrowsePGNController.this.model.nextMove();
                } else {
                    BrowsePGNController.this.view.getNext().setDisable(true);
                }
            }
        });
    }
}
