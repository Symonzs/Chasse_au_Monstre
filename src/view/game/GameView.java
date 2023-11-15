package view.game;

import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameView extends Stage {
    private ArrayList<Scene> playScenes;
    private Scene lastPlayedScene;
    // private Scene nextPlayScene;

    public GameView(Scene scene1, Scene scene2) {
        this.playScenes = new ArrayList<Scene>();
        if (scene1 != null && scene2 != null) {
            playScenes.add(scene1);
            playScenes.add(scene2);
            // nextPlayScene = scene2;
            setScene(scene1);
        }
    }

    public GameView() {
        this(null, null);
    }

    public ArrayList<Scene> getPlayScenes() {
        return playScenes;
    }

    public void addPlayScene(Scene scene) {
        if (!playScenes.contains(scene)) {
            playScenes.add(scene);
            System.out.println("added to playscenes : " + scene);
        } else {
            System.out.println("not added to playscenes :" + scene + " is already in the list.");
        }
    }

    public void display(Scene scene, boolean isPlayScene) {
        setScene(scene);
        setFullScreenExitHint("");
        setFullScreen(true);
        if (isPlayScene) {
            lastPlayedScene = scene;
            addPlayScene(scene);
        }

    }

    public void nextPlayScenes() {
        setScene(playScenes.get((playScenes.indexOf(lastPlayedScene) + 1) % (playScenes.size())));
        lastPlayedScene = getScene();
        setFullScreenExitHint("");
        setFullScreen(true);

    }

}
