package view.game;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameView extends Stage {
    private ArrayList<Scene> playScenes;

    public GameView(Scene scene1, Scene scene2) {
        this.playScenes = new ArrayList<Scene>();
        playScenes.add(scene1);
        playScenes.add(scene2);
        // setScene(scene1);
    }

    public void display(Scene scene) {
        setScene(scene);
    }

    public void nextPlayScenes() {
        setScene(playScenes.get(playScenes.indexOf(getScene()) + 1 % playScenes.size()));

    }

}
