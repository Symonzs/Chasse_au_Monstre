package view.game;

import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameView extends Stage {
    private ArrayList<Scene> playScenes;

    public GameView(Scene scene1, Scene scene2) {
        this.playScenes = new ArrayList<Scene>();
        this.setFullScreen(true);
        if (scene1 != null && scene2 != null) {
            playScenes.add(scene1);
            playScenes.add(scene2);
        }
        // setScene(scene1);
    }

    public GameView() {
        this(null, null);
    }

    public void addPlayScene(Scene scene) {
        if (!playScenes.contains(scene)) {
            playScenes.add(scene);
        }
    }

    public void display(Scene scene, boolean isPlayScene) {
        setScene(scene);
        setFullScreen(true);
        if (isPlayScene) {
            addPlayScene(scene);
        }

    }

    public void nextPlayScenes() {
        System.out.println("scene is changed");
        setScene(playScenes.get((playScenes.indexOf(getScene()) + 1) % playScenes.size()));
        setFullScreen(true);
    }

}
