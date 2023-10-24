package view;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import r304.main.java.utils.Observer;
import r304.main.java.utils.Subject;

public class HunterView extends Stage implements Observer {

    public void showView() {

    }

    public void start(Stage primaryStage) {
        VBox vBox = new VBox();
        Scene scene = new Scene(vBox, 1080, 780);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /* Overide Methode */
    @Override
    public void update(Subject arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void update(Subject arg0, Object arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

}
