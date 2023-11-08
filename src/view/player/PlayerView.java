package view.player;

import java.io.File;

import data.DataStream;
import javafx.scene.image.Image;
import view.game.GameView;

public abstract class PlayerView {
    GameView gameView;
    Image image;

    public PlayerView(GameView gameview, File fileName) {
        image = new Image(DataStream.read(fileName));
    }

    public GameView getGameView() {
        return gameView;
    }

    public void close() {
        gameView.clear();
    }

}
