package view.game;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameView extends Stage {
    public void setSceneInFullScreen(Scene scene) {
        this.setScene(scene);
        this.setFullScreen(true);
        this.setFullScreenExitHint("");
        this.setScene(scene);
        this.showAndWait();
        System.out.println("La scnène " + scene + "est maintenant afficher.");
    }
}
