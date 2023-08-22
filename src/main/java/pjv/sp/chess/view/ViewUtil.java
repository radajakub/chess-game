package pjv.sp.chess.view;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * ViewUtil is a class that holds static info about the game window and utility
 * functions to simplify creating layouts.
 * @author Jakub Rada
 * @version 1.0
 */
public class ViewUtil {

    /**
     * Width of the window same for the whole application
     */
    public static final double WINDOW_HEIGHT = 800;

    /**
     * Height of the window same for the whole application
     */
    public static final double WINDOW_WIDTH = 1000;

    /**
     * Scene of the whole application - root is changed depeding on current view
     */
    public static final Scene SCENE = new Scene(new Pane(), ViewUtil.WINDOW_WIDTH, ViewUtil.WINDOW_HEIGHT);

    /**
     * Sets passed view into the application scene
     * @param root main Pane to be placed into scene as current page
     */
    public static void setRoot(Parent root) {
        ViewUtil.SCENE.setRoot(root);
    }

    /**
     * Creates HBox with passed id and multiple nodes
     * @param id String to identify this object for css styles
     * @param nodes any number of nodes to insert into the HBox
     * @return created HBox with id and passed nodes
     */
    public static HBox addHBox(String id, Node ...nodes) {
        HBox container = new HBox();
        container.setId(id);
        for (Node node : nodes) {
            container.getChildren().add(node);
        }
        return container;
    }

    /**
     * Creates VBox with passed id and multiple nodes
     * @param id String to identify this object for css styles
     * @param nodes any number of nodes to insert into the HBox
     * @return created VBox with id and passed nodes
     */
    public static VBox addVBox(String id, Node ...nodes) {
        VBox container = new VBox();
        container.setId(id);
        for (Node node : nodes) {
            container.getChildren().add(node);
        }
        return container;
    }

    /**
     * Creates vertical separator line object
     * @return vertical serparator
     */
    public static Separator addVSeparator() {
        return new Separator(Orientation.VERTICAL);
    }

    /**
     * Creates horizontal separator line object
     * @return horizontal separator
     */
    public static Separator addHSeparator() {
        return new Separator(Orientation.HORIZONTAL);
    }
}
