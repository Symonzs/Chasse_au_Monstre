package view.game;

import fr.univlille.iutinfo.r304.utils.Observer;
import fr.univlille.iutinfo.r304.utils.Subject;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameView extends Stage implements Observer {

    public void setSceneInFullScreen(Scene scene) {
        this.setScene(scene);
        this.setFullScreen(true);
        this.setFullScreenExitHint("");
        System.out.println("La scnène " + scene + "est maintenant afficher.");
        this.showAndWait();
    }

    @Override
    public void update(Subject arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void update(Subject arg0, Object arg1) {
        System.out.println("Update de la vue de jeu");
        this.close();
    }
}
