package pjv.sp.chess.model.player;

import pjv.sp.chess.model.Game;
import pjv.sp.chess.model.pieces.Color;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Player is abstract class that holds information about player that are shared
 * with Human and Computer Players.
 * @author Jakub Rada
 * @version 1.0
 */
public abstract class Player {

    /**
     * Mouse event that is invoken on squares when ComputerPlayer makes moves
     */
    public static final MouseEvent click = new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1, false, false, false, false, true, false, false, false, false, true, null);

    /**
     * Name of the player
     */
    protected final String name;

    /**
     * Surname of the player
     */
    protected final String surname;

    /**
     * Color of the player
     */
    protected final Color color;

    /**
     * Field that holds if player is computer or human
     */
    protected final boolean computer;

    /**
     * Create new Player object
     * @param name name of the player
     * @param surname surname of the player
     * @param color Color of the player
     * @param computer boolean if the player is computer
     */
    public Player(String name, String surname, Color color, boolean computer) {
        this.name = name;
        this.surname = surname;
        this.color = color;
        this.computer = computer;
    }

    /**
     * Gets full name of the player formatted in the pgn format
     * @return String that contains full name
     */
    public String getFullName() {
        return (this.surname.isEmpty() && this.name.isEmpty()) ? "?" : String.format("%s %s", this.name, this.surname);
    }

    /**
     * Gets first name of the player
     * @return String with first name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets surname of the player
     * @return String with last name
     */
    public String getSurname() {
        return this.surname;
    }

    /**
     * Gets color of the player
     * @return Player's Color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Gets if the computer is human or computer
     * @return if player is computer, true, else false
     */
    public boolean getComputer() {
        return this.computer;
    }

    /**
     * Gets complete move from player selected from possible moves from pased game
     * @param game Game to selecte moves from
     * @return CompleteMove object that contains Piece and Move
     * @see CompleteMove
     */
    public abstract CompleteMove getCompleteMove(Game game);
}