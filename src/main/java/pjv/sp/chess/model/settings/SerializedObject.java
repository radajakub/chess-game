package pjv.sp.chess.model.settings;

/**
 * Serialized object serves for json library to easily load and save values;
 * @author Jakub Rada
 * @version 1.0
 */
public class SerializedObject {

    /**
     * index in the GameType list in Settings
     */
    private final int gameTypeIndex;

    /**
     * Create new SerializedObject
     * @param gameTypeIndex index in the GameType list in Settings
     */
    public SerializedObject(int gameTypeIndex) {
        this.gameTypeIndex = gameTypeIndex;
    }

    /**
     * Gets index in the GameType list in Settings
     * @return value of the gameTypeIndex property
     */
    public int getGameTypeIndex() {
        return this.gameTypeIndex;
    }

}