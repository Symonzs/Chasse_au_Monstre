package main;

import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;

import controller.HunterController;
import controller.MonsterController;
import data.DataProp;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import view.game.GameView;
import view.main.MainView;

public class MonsterHunter extends Application {
    public static final File INIT_FILE = Paths.get("./resources/config/init.conf").toFile();
    public static final Properties PROPERTIES = DataProp.read(INIT_FILE);

    private static MainView mainView;
    private static GameView gameView;

    private static MonsterController monsterController;
    private static HunterController hunterController;

    @Override
    public void start(Stage primaryStage) throws InterruptedException {

        initgame();
        while (mainView.getMaze().getWinner() == null || !mainView.getMaze().gameIsExited()) {
            play();
        }
        exitedGame();
    }

    public static void initgame() {
        mainView = new MainView(INIT_FILE);
        mainView.showAndWait();

        gameView = new GameView();
        monsterController = new MonsterController(mainView.getMaze(), gameView, PROPERTIES);
        hunterController = new HunterController(mainView.getMaze(), gameView, PROPERTIES);

        gameView.display(hunterController.getHunterView().getPlayScene(), true);
        gameView.showAndWait();

    }

    public static void play() {

    }

    public static void exitedGame() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin de la partie");
        alert.setHeaderText("La partie est terminée.");
        if (mainView.getMaze().getWinner() != null) {
            alert.setHeaderText(alert.getHeaderText() + " Le gagnant est " + mainView.getMaze().getWinner());
        }
        alert.showAndWait();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}