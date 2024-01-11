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
            if (init.getProperty("HunterIsAnAI").equals("true")
                    && init.getProperty("MonsterIsAnAI").equals("true")) {
                playWithHunterAIAndMonsterAI();
            } else if (init.getProperty("HunterIsAnAI").equals("true")) {
                playWithHunterAI();
            } else if (init.getProperty("MonsterIsAnAI").equals("true")) {
                playWithMonsterAI();
            } else {
                playWithHuman();
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
        if (init.getProperty("HunterIsAnAI").equals("true")
                && init.getProperty("MonsterIsAnAI").equals("true")) {
            if (init.getProperty("ChoosedAlgorithm").equals("Theta*")) {
                monsterStrategy = new MazeSolver(new ThetaStarAlgorithm(), mainView.getMaze());
            } else if (init.getProperty("ChoosedAlgorithm").equals("Bidirectional A*")) {
                monsterStrategy = new MazeSolver(new BidirectionnalAlgorithm(), mainView.getMaze());
            } else {
                monsterStrategy = new MazeSolver(mainView.getMaze());
            }
            hunterStrategy = new MonsterFinder();
            hunterStrategy.initialize(mainView.getMaze().getWall().length - 1,
                    mainView.getMaze().getWall()[0].length - 1);
        } else if (init.getProperty("HunterIsAnAI").equals("true")) {
            hunterStrategy = new MonsterFinder();
            monsterController = new MonsterController(mainView.getMaze());
        } else if (init.getProperty("MonsterIsAnAI").equals("true")) {
            if (init.getProperty("ChoosedAlgorithm").equals("Theta*")) {
                monsterStrategy = new MazeSolver(new ThetaStarAlgorithm(), mainView.getMaze());
            } else if (init.getProperty("ChoosedAlgorithm").equals("Bidirectional A*")) {
                monsterStrategy = new MazeSolver(new BidirectionnalAlgorithm(), mainView.getMaze());
            } else {
                monsterStrategy = new MazeSolver(mainView.getMaze());
            }
            hunterController = new HunterController(mainView.getMaze());
        } else {
            monsterController = new MonsterController(mainView.getMaze());
            hunterController = new HunterController(mainView.getMaze());
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
                System.out.println(
                        "Coordonnées du monstre : " + monsterPosition.getRow() + " " + monsterPosition.getCol());
                mainView.getMaze().cellUpdate(new CellEvent(monsterPosition, Maze.currentTurn,
                        CellInfo.MONSTER));
                mainView.getMaze().setMonsterHasPlayed(false);
            }
        }
    }

    public void playWithHunterAIAndMonsterAI() {
        if (!gameIsFinished()) {
            if (!mainView.getMaze().getHunterHasPlayed()) {
                ICoordinate hunterPosition = hunterStrategy.play();
                mainView.getMaze().cellUpdate(new CellEvent(hunterPosition, Maze.currentTurn,
                        CellInfo.HUNTER));
                mainView.getMaze().setMonsterHasPlayed(false);
            }
        }
        if (!gameIsFinished()) {
            if (!mainView.getMaze().getMonsterHasPlayed()) {
                ICoordinate monsterPosition = monsterStrategy.play();
                mainView.getMaze().cellUpdate(new CellEvent(monsterPosition, Maze.currentTurn,
                        CellInfo.MONSTER));
                mainView.getMaze().setHunterHasPlayed(false);
            }
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