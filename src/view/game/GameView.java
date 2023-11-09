package view.game;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameView extends Stage {
    private HBox hBox;
    private static ArrayList<Scene> scenes = new ArrayList<Scene>();

    public GameView(HBox hBox) {
        this.hBox = hBox;
        hBox.getChildren().addAll(new Button("truc truc"));
        this.setScene(new Scene(hBox));
    }

    public void display(VBox[] vBoxs) {
        clear();
        this.hBox.getChildren().addAll(vBoxs);
        hBox.requestLayout();
        this.setScene(new Scene(hBox));
    }

    public void display(Scene scene) {
        this.setScene(scene);
        addScene(scene);
    }

    public void clear() {
        this.hBox.getChildren().clear();
    }

    public static void addScene(Scene scene) {
        if (!scenes.contains(scene)) {
            scenes.add(scene);
        }
    }

    public void nextScene() {
        int i = 0;
        while (i < scenes.size()) {
            if (getScene().equals(scenes.get(i))) {
                setScene(scenes.get((i + 1) % scenes.size()));
                i = scenes.size();
            }
            i += 1;
        }
    }

    public HBox gethBox() {
        return hBox;
    }
}
