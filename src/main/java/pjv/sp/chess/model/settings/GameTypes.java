package pjv.sp.chess.model.settings;

/**
 * GameTypes enum contains possible game lengths with official labels.
 * @author Jakub Rada
 * @version 1.0
 */
public enum GameTypes {
    BLITZ("Bullet", 5),
    RAPID("Rapid", 25),
    LONG("Long", 60);

    /**
     * How many minutes can play one player
     */
    private final int minutes;
    
    /**
     * How is the version named
     */
    private final String label;

    /**
     * Create new GameType item
     * @param label name of the version
     * @param minutes lengths of player's move in minutes
     */
    private GameTypes(String label, int minutes) {
        this.minutes = minutes;
        this.label = label;
    }

    /**
     * Gets name of the version
     * @return value of the label property
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Gets number of seconds for one player's moves
     * @return int value of seconds
     */
    public int getSeconds() {
        return this.minutes * 60;
    }

    /**
     * Gets number of minutes for one player's moves
     * @return int value of minutes
     */
    public int getMinutes() {
        return this.minutes;
    }
}
