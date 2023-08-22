package pjv.sp.chess.controller;

import pjv.sp.chess.Chess;
import pjv.sp.chess.model.settings.Settings;
import pjv.sp.chess.view.SettingsView;
import pjv.sp.chess.view.ViewUtil;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.logging.Logger;

/**
 * SettingsController handles user interaction with settings
 * @author Jakub Rada
 * @version 1.0
 */
public class SettingsController {

    private static final Logger LOG = Logger.getLogger(SettingsController.class.getName());

    /**
     * View that displays settings choices
     */
    private final SettingsView settingsView;

    /**
     * Creates new SettingsController object
     */
    public SettingsController() {
        this.settingsView = new SettingsView();
        this.start();
    }

    /**
     * Gets Settings field from model and inserts them into view, then shows page on the primary stage
     */
    public void showSettingsPage() {
        ViewUtil.setRoot(this.settingsView.getRoot());
        this.settingsView.setSettings(Settings.getInstance().getTypes());
        this.settingsView.setValue(Settings.getInstance().getSelected());
    }

    /**
     * Sets actions listeners for settings components
     */
    private void start() {

        /** when clicked on slider, save selected value into model (not persistable after quitting) */
        this.settingsView.getSlider().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                SettingsController.LOG.info("Setting changed - updating model");
                Settings.getInstance().setSelected(SettingsController.this.settingsView.getIndex());
            }
        });

        /** shows start page when clicked on back button */
        this.settingsView.getBackButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SettingsController.LOG.info("Back button pressed - going back to start page");
                Chess.START_PAGE_CONTROLLER.showStartPage();
            }
        });

        /** Save settings to file (persistable after quitting) */
        this.settingsView.getSaveButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SettingsController.LOG.info("Save button pressed - saving settings");
                Settings.getInstance().save();
            }
        });
    }
}
