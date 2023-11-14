package view.play;

import java.io.File;

import data.DataStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import view.game.GameView;

public abstract class PlayView {
    GameView gameView;
    ImageView imageView;
    VBox[] mainvBoxs;

    public PlayView(GameView gameview, File fileName) {
        // this.imageView = new ImageView(new Image(DataStream.read(fileName)));
        this.gameView = gameview;
    }

    public void display(VBox[] vBoxs) {
        gameView.display(vBoxs);
        this.mainvBoxs = vBoxs;
    }

    public GameView getGameView() {
        return gameView;
    }

    public void close() {
        gameView.clear();
    }

    public void showAndWait() {
        display(mainvBoxs);
    }
}
