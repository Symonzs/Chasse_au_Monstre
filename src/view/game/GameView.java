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
        showAndWait();
    }

    public void display(VBox[] vBoxs) {
        clear();
        this.hBox.getChildren().addAll(vBoxs);
        hBox.requestLayout();
        this.setScene(new Scene(hBox));
    }

    public void clear() {
        this.hBox.getChildren().clear();
    }

    public HBox gethBox() {
        return hBox;
    }
}
