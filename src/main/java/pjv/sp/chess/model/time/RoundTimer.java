package pjv.sp.chess.model.time;

import pjv.sp.chess.Chess;

import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * RoundTimer is timecontrol class that runs on separate thread and measures remaining
 * time for player to finish the game. It can be paused and resumed
 * @author Jakub Rada
 * @version 1.0
 */
public class RoundTimer extends Thread {

    /**
     * View that shows remaining time
     */
    private final Label view;

    /**
     * time that remains
     */
    private long time;

    /**
     * If the Thread is active
     */
    private boolean active;

    /**
     * If the thread is not counting down
     */
    private boolean pause;

    /**
     * Creates new RoundTimer object
     * @param seconds number of seconds to countdown
     * @param view view to display remaining time in
     */
    public RoundTimer(int seconds, Label view) {
        this.time = seconds * 10;
        this.view = view;
        this.active = true;
        this.pause = true;
        // show initial value
        Platform.runLater(()->this.setTime(this.formatTime()));
    }

    /**
     * Deactivates the thread which results in its destruction
     */
    public void end() {
        this.active = false;
    }

    /**
     * Sets String to view
     * @param time String that is show in the view
     */
    private void setTime(String time) {
        this.view.setText(time);
    }

    /**
     * Pause counting down
     */
    public void pause() {
        this.pause = true;
    }

    /**
     * Resume counting down
     */
    public void proceed() {
        this.pause = false;
    }

    /**
     * Start thread and perform countdown if active and not paused
     */
    @Override
    public void run() {
        while(this.active) {
            if (this.time > 0) {
                synchronized(this) {
                    try {
                        wait(100);
                        if (!this.pause) {
                            Platform.runLater(()->this.setTime(this.formatTime()));
                            this.time--;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Platform.runLater(()->Chess.GAME_CONTROLLER.timeRunOut());
                break;
            }
        }
    }

    /**
     * Format remaining time
     * @return time in proper format
     */
    private String formatTime() {
        long minutes = this.time / 600;
        long tenths = this.time - minutes * 600;
        long seconds = tenths / 10;
        tenths -= seconds * 10;
        return String.format("%02d:%02d.%d0", minutes, seconds, tenths);
    }

}