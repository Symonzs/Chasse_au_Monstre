package view.play;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class PlayView extends Stage {
    private Scene playScene;
    private Scene waitScene;

    public void setPlayScene(Scene scene) {
        playScene = scene;
    }

    public void setWaitScene(Scene scene) {
        waitScene = scene;
    }

    public Scene getPlayScene() {
        return playScene;
    }

    public Scene getWaitScene() {
        return waitScene;
    }

    public void showPlayScene() {
        setScene(playScene);
    }

    public void showWaitScene() {
        setScene(waitScene);
    }

}
