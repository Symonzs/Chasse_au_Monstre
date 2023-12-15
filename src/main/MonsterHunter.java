package main;

import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;
import javax.sound.sampled.*;
import java.io.IOException;

import controller.HunterController;
import controller.MonsterController;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import util.data.DataProp;
import util.music.BackgroundMusicPlayer;
import view.game.GameView;
import view.main.MainView;

public class MonsterHunter extends Application {
    public static final File INIT_FILE = Paths.get("./resources/config/init.conf").toFile();
    public static final Properties PROPERTIES = DataProp.read(INIT_FILE);
    public static final Properties PLAY_LANGUAGE_FILE = setUpLanguageFile("play");
    public static final Properties MENU_LANGUAGE_FILE = setUpLanguageFile("menu");
    public static final Properties EXCEPTION_LANGUAGE_FILE = setUpLanguageFile("exception");
    private static MainView mainView;
    private static GameView gameView;

    private static MonsterController monsterController;
    private static HunterController hunterController;

    private static Properties setUpLanguageFile(String value) {
        String key = PROPERTIES.getProperty("LanguageValue");

        Properties languageSrcProperties = DataProp.read(Paths.get(PROPERTIES.getProperty("LanguageSetting")).toFile());

        return DataProp.read(Paths.get(languageSrcProperties.getProperty(key) + value + ".conf").toFile());
    }

    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        initgame();
        do {
            play();
            System.out.println("Game is finished : " + gameIsFinished());
        } while (!gameIsFinished());
        System.out.println("sortie de la boucle");
        exitedGame();
    }

    public void initgame() {
        mainView = new MainView(INIT_FILE);
        initMusic();
        mainView.showAndWait();

        gameView = new GameView();
        mainView.getMaze().attach(gameView);
        monsterController = new MonsterController(mainView.getMaze(), PROPERTIES);
        hunterController = new HunterController(mainView.getMaze(), PROPERTIES);

    }

    public boolean gameIsFinished() {
        return mainView.getMaze().getWinner() != null || mainView.getMaze().isGameClosed();
    }

    public void play() {
        if (!gameIsFinished()) {
            if (!mainView.getMaze().getHunterHasPlayed()) {
                gameView.setSceneInFullScreen(hunterController.getView().getPlayScene());
            }
        }
        if (!gameIsFinished()) {
            if (mainView.getMaze().getHunterHasPlayed() && !mainView.getMaze().getIsReadyToNext()) {
                gameView.setSceneInFullScreen(hunterController.getView().getWaitScene());
                mainView.getMaze().setHunterHasPlayed(false);
            }
        }
        if (!gameIsFinished()) {
            if (!mainView.getMaze().getMonsterHasPlayed()) {
                gameView.setSceneInFullScreen(monsterController.getView().getPlayScene());
            }
        }
        if (!gameIsFinished()) {
            if (mainView.getMaze().getMonsterHasPlayed() && !mainView.getMaze().getIsReadyToNext()) {
                gameView.setSceneInFullScreen(monsterController.getView().getWaitScene());
                mainView.getMaze().setMonsterHasPlayed(false);
            }
        }
    }

    public static void exitedGame() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin de la partie");
        alert.setHeaderText("La partie est terminée.");
        if (mainView.getMaze().isGameClosed()) {
            alert.setHeaderText(alert.getHeaderText() + " La partie à été intérompue.");
        }
        if (mainView.getMaze().getWinner() != null) {
            alert.setHeaderText(alert.getHeaderText() + " Le gagnant est " + mainView.getMaze().getWinner());
        }
        alert.showAndWait();
        System.exit(0);
    }

    private void initMusic() {
        String audioFilePath = PROPERTIES.getProperty("MainMusic");
        BackgroundMusicPlayer musicPlayer = new BackgroundMusicPlayer();
        musicPlayer.playAudio(audioFilePath);
    }

    public static void main(String[] args) {
        launch(args);
    }
}