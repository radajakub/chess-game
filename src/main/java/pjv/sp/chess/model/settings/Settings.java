package pjv.sp.chess.model.settings;

import pjv.sp.chess.Chess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.Gson;

/**
 * Settings class holds together all settings, provides tools for saving to and
 * loading from JSON file. This class implements the singleton model.
 * @author Jakub Rada
 * @version 1.0
 */
public class Settings {

    /**
     * Logger that belongs to Settings class and should be used to log internal events
     */
    private static final Logger LOG = Logger.getLogger(Settings.class.getName());

    /**
     * Private INSTANCE field holds only existing instance of Settings class
     */
    private static Settings INSTANCE = new Settings();

    /**
     * Field that holds currently selected index in gameTypes List
     */
    private int selectedIndex;

    /**
     * List that contains all gameTypes
     */
    private List<GameTypes> gameTypes = new ArrayList<>();

    /**
     * Name of the file where settings are stored
     */
    private final String FILENAME = "settings.json";

    /**
     * Object that can serialize and deserialize objects
     */
    private final Gson PARSER = new Gson();

    /**
     * private constructor needed for the Singleton pattern
     */
    private Settings() {
        this.gameTypes.add(GameTypes.BLITZ);
        this.gameTypes.add(GameTypes.RAPID);
        this.gameTypes.add(GameTypes.LONG);
        this.selectedIndex = 0;
        this.load();
    }

    /**
     * Getter necessary for the Singleton pattern
     * @return only existing instance of Settings class
     */
    public static Settings getInstance() {
        return Settings.INSTANCE;
    }

    /**
     * Gets all GameTypes to choose from
     * @return List of GameTypes
     */
    public List<GameTypes> getTypes() {
        return this.gameTypes;
    }

    /**
     * Set selected index to passed value
     * @param index index in the List of GameTypes
     */
    public void setSelected(int index) {
        Settings.LOG.info("Settings changed to " + index);
        this.selectedIndex = index;
    }

    /**
     * Gets number of seconds of currently selected GameType
     * @return number of seconds for timer
     */
    public int getLengthSettings() {
        return this.gameTypes.get(this.selectedIndex).getSeconds();
    }

    /**
     * Gets index of currently selected GameType
     * @return index in the List of GameTypes
     */
    public int getSelected() {
        return this.selectedIndex;
    }

    /**
     * Private method that takes care of loading settings from JSON file specified
     * by path property
     */
    private void load() {
        try (FileReader reader = new FileReader(Chess.settingsPath + this.FILENAME, StandardCharsets.UTF_8)) {
            SerializedObject deserialized = this.PARSER.fromJson(reader, SerializedObject.class);
            this.selectedIndex = deserialized.getGameTypeIndex();
            Settings.LOG.info(String.format("Settings read from JSON on path %s", Chess.settingsPath));
        } catch (FileNotFoundException e) {
            Settings.LOG.severe(String.format("File '%s' does not exist", Chess.settingsPath));
        } catch (IOException e) {
            Settings.LOG.severe(String.format("Error while reading %s", Chess.settingsPath));
        }
    }

    /**
     * Checks if passed path exists, if not creates it
     * @param path path to check
     * @return File descriptor of setting file
     */
    private File checkFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return new File(path + this.FILENAME);
    }

    /**
     * Save current values of settings checkboxes to JSON file for future use
     */
    public void save() {
        System.out.println(this.selectedIndex);
        try (FileWriter writer = new FileWriter(this.checkFile(Chess.settingsPath), StandardCharsets.UTF_8)) {
            SerializedObject obj = new SerializedObject(this.selectedIndex);
            writer.write(this.PARSER.toJson(obj));
            Settings.LOG.info(String.format("Settings saved to JSON on path %s", Chess.settingsPath));
        } catch (IOException e) {
            Settings.LOG.severe(String.format("Error while reading %s", Chess.settingsPath));
        }
    }

}
