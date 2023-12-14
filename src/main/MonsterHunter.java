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
        do {
            System.out.println("hunter " + hunterController.isGameIsExited());
            System.out.println("monster " + monsterController.isGameIsExited());
            play();

        } while (!gameIsFinished());
        System.out.println("sortie de la boucle");
        exitedGame();
    }

    public void initgame() {
        mainView = new MainView(INIT_FILE);
        mainView.showAndWait();

        gameView = new GameView();

        monsterController = new MonsterController(mainView.getMaze(), PROPERTIES);
        hunterController = new HunterController(mainView.getMaze(), PROPERTIES);

    }

    public boolean gameIsFinished() {
        return mainView.getMaze().getWinner() != null || monsterController.isGameIsExited()
                || hunterController.isGameIsExited();
    }

    public void play() {
        gameView.close();
        System.out.println("on rentre dans play");
        if (!gameIsFinished()) {
            if (!hunterController.hasPlayed()) {
                gameView.setSceneInFullScreen(hunterController.getView().getPlayScene());
            }
            if (hunterController.hasPlayed() && !hunterController.isReadyToNext()) {
                gameView.setSceneInFullScreen(hunterController.getHunterView().getWaitScene());
            }
            if (!monsterController.hasPlayed()) {
                gameView.setSceneInFullScreen(monsterController.getMonsterView().getPlayScene());
            }
            if (monsterController.hasPlayed() && !monsterController.isReadyToNext()) {
                gameView.setSceneInFullScreen(monsterController.getMonsterView().getPlayScene());
            }
        }
    }

    public static void exitedGame() {
        gameView.close();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin de la partie");
        alert.setHeaderText("La partie est terminée.");
        if (mainView.getMaze().gameIsExited()) {
            alert.setHeaderText(alert.getHeaderText() + " La partie à été intérompue.");
        }
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