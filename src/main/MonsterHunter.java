package main;

import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;

import ai.MazeSolver;
import ai.MonsterFinder;
import ai.algorithm.BidirectionnalAlgorithm;
import ai.algorithm.ThetaStarAlgorithm;
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
    public static Properties init = DataProp.read(INIT_FILE);
    public static Properties playLanguageFile = setUpLanguageFile("play");
    public static Properties menuLanguageFile = setUpLanguageFile("menu");
    public static Properties exceptionLanguageFile = setUpLanguageFile("exception");
    private static MainView mainView;
    private static GameView gameView;

    private static MonsterController monsterController;
    private static HunterController hunterController;
    private static IMonsterStrategy monsterStrategy;
    private static IHunterStrategy hunterStrategy;

    public static void main(String[] args) {
        launch(args);
    }

    private static Properties setUpLanguageFile(String value) {
        String key = init.getProperty("LanguageValue");

        Properties languageSrcProperties = DataProp.read(Paths.get(init.getProperty("LanguageSetting")).toFile());

        return DataProp.read(Paths.get(languageSrcProperties.getProperty(key) + value + ".conf").toFile());
    }

    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        initgame();
        do {
            if (init.getProperty("HunterIsAnAI").equals("true")) {
                iaHunterTurn();
            } else {
                humanHunterTurn();
            }
            if (init.getProperty("MonsterIsAnAI").equals("true")) {
                iaMonsterTurn();
            } else {
                humanMonsterTurn();
            }
        } while (!gameIsFinished());
        exitedGame(primaryStage);
    }

    public void initgame() {
        mainView = new MainView(INIT_FILE);
        // initMusic();
        mainView.showAndWait();
        gameView = new GameView();
        if (mainView.getMaze() == null) {
            initgame();
        }
        mainView.getMaze().attach(gameView);
        if (init.getProperty("HunterIsAnAI").equals("true")) {
            hunterStrategy = new MonsterFinder();
        } else {
            hunterController = new HunterController(mainView.getMaze());
        }
        if (init.getProperty("MonsterIsAnAI").equals("true")) {
            if (init.getProperty("ChoosedAlgorithm").equals("Theta*")) {
                monsterStrategy = new MazeSolver(new ThetaStarAlgorithm(), mainView.getMaze());
            } else if (init.getProperty("ChoosedAlgorithm").equals("Bidirectional A*")) {
                monsterStrategy = new MazeSolver(new BidirectionnalAlgorithm(), mainView.getMaze());
            } else {
                monsterStrategy = new MazeSolver(mainView.getMaze());
            }
        } else {
            monsterController = new MonsterController(mainView.getMaze());
        }
    }

    public boolean gameIsFinished() {
        return mainView.getMaze().getWinner() != null || mainView.getMaze().isGameClosed();
    }

    public void humanHunterTurn() {
        if (!gameIsFinished() && !mainView.getMaze().getHunterHasPlayed()) {
            gameView.setSceneInFullScreen(hunterController.getView().getPlayScene());
        }
        if (!gameIsFinished() && mainView.getMaze().getHunterHasPlayed() && !mainView.getMaze().getIsReadyToNext()) {
            gameView.setSceneInFullScreen(hunterController.getView().getWaitScene());
            mainView.getMaze().setHunterHasPlayed(false);
        }
    }

    public void humanMonsterTurn() {
        if (!gameIsFinished() && !mainView.getMaze().getMonsterHasPlayed()) {
            gameView.setSceneInFullScreen(monsterController.getView().getPlayScene());
        }
        if (!gameIsFinished() && mainView.getMaze().getMonsterHasPlayed() && !mainView.getMaze().getIsReadyToNext()) {
            gameView.setSceneInFullScreen(monsterController.getView().getWaitScene());
            mainView.getMaze().setMonsterHasPlayed(false);
        }
    }

    public void iaHunterTurn() {
        if (!gameIsFinished() && !mainView.getMaze().getHunterHasPlayed()) {
            ICoordinate hunterPosition = hunterStrategy.play();
            mainView.getMaze().cellUpdate(new CellEvent(hunterPosition, Maze.currentTurn,
                    CellInfo.HUNTER));
            mainView.getMaze().setHunterHasPlayed(false);
        }
    }

    public void iaMonsterTurn() {
        if (!gameIsFinished() && !mainView.getMaze().getMonsterHasPlayed()) {
            ICoordinate monsterPosition = monsterStrategy.play();
            mainView.getMaze().cellUpdate(new CellEvent(monsterPosition, Maze.currentTurn,
                    CellInfo.MONSTER));
            mainView.getMaze().setMonsterHasPlayed(false);
        }
    }

    public void exitedGame(Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin de la partie");
        alert.setHeaderText("La partie est terminée.");
        if (mainView.getMaze().isGameClosed()) {
            alert.setHeaderText(alert.getHeaderText() + " La partie a été interrompue.");
            alert.showAndWait();
            System.exit(0);
        }
        if (mainView.getMaze().getWinner() != null) {
            alert.setHeaderText(alert.getHeaderText() + " Le gagnant est " + mainView.getMaze().getWinner());
            alert.showAndWait();
            try {
                start(primaryStage);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void initMusic() {
        String audioFilePath = init.getProperty("MainMusic");
        BackgroundMusicPlayer musicPlayer = new BackgroundMusicPlayer();
        musicPlayer.playAudio(audioFilePath);
    }

    public MainView getMainView() {
        return mainView;
    }

}