package main;

import java.io.File;

import controller.HunterController;
import controller.MonsterController;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import view.MainView;

public class MonsterHunter extends Application {

    public static final String PATH = System.getProperty("user.dir");
    public static final String RESOURCES_PATH = PATH + File.separator + "resources";

    private boolean gameIsOver = false;

    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        MainView mv = new MainView();
        mv.showAndWait();
        MonsterController mc = new MonsterController(mv.getMaze());
        HunterController hc = new HunterController(mv.getMaze());
        Alert turnChange = new Alert(Alert.AlertType.INFORMATION);
        turnChange.setTitle("Changement de tour");
        Alert winner = new Alert(Alert.AlertType.INFORMATION);
        winner.setTitle("Fin de la partie");
        boolean playerHasPlayed;
        while (!gameIsOver) {
            if (mv.getMaze().getWinner() != null) {
                gameIsOver = true;
            } else {
                do {
                    turnChange.setHeaderText("C'est au tour du chasseur");
                    turnChange.showAndWait();
                    playerHasPlayed = hc.play();
                } while (!playerHasPlayed);
            }
            if (mv.getMaze().getWinner() != null) {
                gameIsOver = true;
            } else {
                do {
                    turnChange.setHeaderText("C'est au tour du monstre");
                    turnChange.showAndWait();
                    playerHasPlayed = mc.play();
                } while (!playerHasPlayed);
            }
        }
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