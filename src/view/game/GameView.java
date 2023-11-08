package view.game;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameView extends Stage {
    HBox hBox;

    public GameView(HBox hBox) {
        this.hBox = hBox;
        this.setScene(new Scene(hBox));
    }

    public void display(VBox[] vBoxs) {
        clear();
        for (VBox vBox : vBoxs) {
            this.hBox.getChildren().add(vBox);
        }
    }

    public void clear() {
        this.hBox.getChildren().clear();
    }

    public HBox gethBox() {
        return hBox;
    }
}
