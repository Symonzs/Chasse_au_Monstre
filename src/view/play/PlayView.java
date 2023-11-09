package view.play;

import java.io.File;

import data.DataStream;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.game.GameView;

public abstract class PlayView {
    private static GameView gameView;
    private ImageView imageView;
    private Scene scene;

    protected PlayView(File fileName) {
        // this.imageView = new ImageView(new Image(DataStream.read(fileName)));
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public static void setGameView(GameView gV) {
        gameView = gV;
    }

    public void display(VBox[] vBoxs) {
        gameView.display(vBoxs);
    }

    public void display(Scene scene) {
        gameView.display(scene);
    }

    public GameView getGameView() {
        return gameView;
    }

    public void close() {
        gameView.clear();
    }

    public void showAndWait() {
        display(scene);
    }
}
