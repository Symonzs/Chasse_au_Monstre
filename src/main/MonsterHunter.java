package main;

import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;

import ai.MazeSolver;
import ai.MonsterFinder;
import controller.HunterController;
import controller.MonsterController;
import fr.univlille.iutinfo.cam.player.hunter.IHunterStrategy;
import fr.univlille.iutinfo.cam.player.monster.IMonsterStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.CellEvent;
import model.Maze;
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
    private static IMonsterStrategy monsterStrategy;
    private static IHunterStrategy hunterStrategy;

    private static Properties setUpLanguageFile(String value) {
        String key = PROPERTIES.getProperty("LanguageValue");

        Properties languageSrcProperties = DataProp.read(Paths.get(PROPERTIES.getProperty("LanguageSetting")).toFile());

        return DataProp.read(Paths.get(languageSrcProperties.getProperty(key) + value + ".conf").toFile());
    }

    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        initgame();
        do {
            if (mainView.getHunterIsAnAi()) {
                playWithHunterAI();
            }
            if (mainView.getMonsterIsAnAI()) {
                playWithMonsterAI();
            }
            if (!mainView.getHunterIsAnAi() && !mainView.getMonsterIsAnAI()) {
                playWithHuman();
            }
        } while (!gameIsFinished());
        exitedGame();
    }

    public void initgame() {
        mainView = new MainView(INIT_FILE);
        initMusic();
        mainView.showAndWait();
        gameView = new GameView();
        if (mainView.getMaze() == null) {
            initgame();
        }
        mainView.getMaze().attach(gameView);
        if (mainView.getHunterIsAnAi()) {
            hunterStrategy = new MonsterFinder();
            monsterController = new MonsterController(mainView.getMaze(), PROPERTIES);
        }
        if (mainView.getMonsterIsAnAI()) {
            monsterStrategy = new MazeSolver(mainView.getMaze());
            hunterController = new HunterController(mainView.getMaze(), PROPERTIES);
        }
        if (!mainView.getHunterIsAnAi() && !mainView.getMonsterIsAnAI()) {
            monsterController = new MonsterController(mainView.getMaze(), PROPERTIES);
            hunterController = new HunterController(mainView.getMaze(), PROPERTIES);
        }
    }

    public boolean gameIsFinished() {
        return mainView.getMaze().getWinner() != null || mainView.getMaze().isGameClosed();
    }

    public void playWithHuman() {
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

    public void playWithHunterAI() {
        if (!gameIsFinished()) {
            if (!mainView.getMaze().getHunterHasPlayed()) {
                ICoordinate hunterPosition = hunterStrategy.play();
                mainView.getMaze().cellUpdate(new CellEvent(hunterPosition, Maze.currentTurn,
                        CellInfo.HUNTER));
            }
        }
        if (!gameIsFinished()) {
            if (mainView.getMaze().getHunterHasPlayed() && !mainView.getMaze().getIsReadyToNext()) {
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

    public void playWithMonsterAI() {
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
                ICoordinate monsterPosition = monsterStrategy.play();
                mainView.getMaze().cellUpdate(new CellEvent(monsterPosition, Maze.currentTurn,
                        CellInfo.MONSTER));
            }
        }
        if (!gameIsFinished()) {
            if (mainView.getMaze().getMonsterHasPlayed() && !mainView.getMaze().getIsReadyToNext()) {
                mainView.getMaze().setMonsterHasPlayed(false);
            }
        }
    }

    public void exitedGame() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin de la partie");
        alert.setHeaderText("La partie est terminée.");
        if (mainView.getMaze().isGameClosed()) {
            alert.setHeaderText(alert.getHeaderText() + " La partie à été intérompue.");
            alert.showAndWait();
            System.exit(0);
        }
        if (mainView.getMaze().getWinner() != null) {
            alert.setHeaderText(alert.getHeaderText() + " Le gagnant est " + mainView.getMaze().getWinner());
            alert.showAndWait();
            initgame();
        }

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