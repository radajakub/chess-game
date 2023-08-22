package pjv.sp.chess.view;

import java.util.List;

import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import pjv.sp.chess.model.settings.GameTypes;

/**
 * SettingsView handles user input in settings of the application.
 * @author Jakub Rada
 * @version 1.0
 * @see View
 */
public class SettingsView extends View {

    /**
     * Root pane of the whole SettingsView
     */
    private final BorderPane root = new BorderPane();

    /**
     * Button to go back to previous page
     */
    private final Button back = new Button("Back");

    /**
     * Button that initializes saving process of the settings
     */
    private final Button save = new Button("Save");

    /**
     * Structure that maps checkbox to setting field (only one checkbox for each field)
     */
    private final Slider slider = new Slider();

    /**
     * Create new SettingsView object
     */
    public SettingsView() {
        this.prepareScene();
    }

    /**
     * Puts all properties into thie root layout
     */
    @Override
    protected void prepareScene() {
        this.root.setTop(ViewUtil.addHBox("settings-top", this.back, new Label("Settings")));
        this.root.setCenter(ViewUtil.addVBox("settings-slider", this.slider));
        this.root.setBottom(ViewUtil.addHBox("settings-bottom", this.save));
        this.root.requestFocus();
    }

    /**
     * Inits slider for GameTypes
     * @param choices List of GameTypes to show on the slider
     */
    public void setSettings(List<GameTypes> choices) {
        this.slider.setOrientation(Orientation.VERTICAL);
        this.slider.setShowTickMarks(true);
        this.slider.setShowTickLabels(true);
        this.slider.setSnapToTicks(true);
        this.slider.setMin(0);
        this.slider.setMinorTickCount(0);
        this.slider.setMax(choices.size() - 1);
        for (int i = 1; i < choices.size() - 1; i++) {
            this.slider.setMajorTickUnit(i);
        }
        this.slider.setLabelFormatter(new StringConverter<Double>(){
        
            @Override
            public String toString(Double object) {
                return String.format("%3d minutes %s", choices.get(object.intValue()).getMinutes(), choices.get(object.intValue()).getLabel());
            }
        
            @Override
            public Double fromString(String string) {
                return null;
            }
        });
    }

    /**
     * Move slider's thumb to passed value
     * @param index index where to put the thumb of the slider
     */
    public void setValue(int index) {
        this.slider.setValue(index);
    }

    /**
     * Gets the slider component
     * @return Slider object
     */
    public Slider getSlider() {
        return this.slider;
    }

    /**
     * Gets index of current position of the thumb of the slider
     * @return current index of the thumb
     */
    public int getIndex() {
        return (int)this.slider.getValue();
    }

    /**
     * Gets the whole view
     * @return BorderPane root of settings page
     */
    public BorderPane getRoot() {
        return this.root;
    }

    /**
     * Gets button to go to previouw page
     * @return Button to go back
     */
    public Button getBackButton() {
        return this.back;
    }

    /**
     * Gets button to save the settings
     * @return Button to save the settings
     */
    public Button getSaveButton() {
        return this.save;
    }

}
