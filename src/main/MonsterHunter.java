package main;

import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;

import controller.HunterController;
import controller.MonsterController;
import data.DataProp;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import view.game.GameView;
import view.main.MainView;

public class MonsterHunter extends Application {
    public static final File INIT_FILE = Paths.get("./resources/config/init.prop").toFile();
    public static final Properties PROPERTIES = DataProp.read(INIT_FILE);

    // public static final String PATH = System.getProperty("user.dir");
    // public static final String RESOURCES_PATH = PATH + File.separator +
    // "resources";

    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        MainView mv = new MainView(INIT_FILE);
        mv.showAndWait();
        GameView gameView = new GameView();
        MonsterController mc = new MonsterController(mv.getMaze(), gameView);
        HunterController hc = new HunterController(mv.getMaze(), gameView);
        gameView.display(hc.getHunterView().getPlayScene(), true);
        gameView.showAndWait();

        Alert turnChange = new Alert(Alert.AlertType.INFORMATION);
        turnChange.setTitle("Changement de tour");
        Alert winner = new Alert(Alert.AlertType.INFORMATION);
        winner.setTitle("Fin de la partie");
        winner.setHeaderText("Le gagnant est " + mv.getMaze().getWinner().name());
        winner.showAndWait();
    }

    public static void exitedGame() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin de la partie");
        alert.setHeaderText("Vous avez quitté la partie");
        alert.showAndWait();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}