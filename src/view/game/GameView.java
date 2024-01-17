package view.game;

import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Observer;
import model.Subject;

public class GameView extends Stage implements Observer {

    public void setSceneInFullScreen(Scene scene) {
        this.setScene(scene);
        this.setFullScreen(true);
        this.setFullScreenExitHint("");
        this.showAndWait();
    }

    public void setSceneInFullScreenAndShow(Scene scene) {
        this.setScene(scene);
        this.setFullScreen(true);
        this.setFullScreenExitHint("");
        this.show();
    }

    @Override
    public void update(Subject arg0) {
        // Ne fait rien
    }

    @Override
    public void update(Subject arg0, Object arg1) {
        this.close();
    }
}
