package view.play;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import main.MonsterHunter;
import model.CellEvent;
import model.Maze;
import model.Monster;
import model.Observer;
import model.Subject;
import util.style.MainStyle;

public class MonsterView extends PlayView implements Observer {

    private static int RECT_COL = 75;
    private static int RECT_ROW = 75;

    private Monster monster;

    /* Play atribut */
    private VBox playRoot;
    private GridPane playGameBoard;
    private HBox playButtonBox;
    private Button playMoveButton;
    private Button playExitButton;

    /* Waiting atribut */

    private VBox waitRoot;
    private Label waitLabel;
    private Button waitButton;

    public MonsterView(Maze maze) {
        this.monster = new Monster(maze);
        initMazePaneSize(maze);
        initWaitingScene();
        initPlayScene();
        showPlayScene();
    }

    private void initMazePaneSize(Maze maze) {
        RECT_COL = (int) ((Screen.getPrimary().getBounds().getHeight() - 200) / maze.getHeight());
        RECT_ROW = (int) ((Screen.getPrimary().getBounds().getHeight() - 200) / maze.getHeight());

    }

    public void initPlayScene() {
        playRoot = new VBox();
        playRoot.setPadding(new Insets(10));
        playRoot.setAlignment(Pos.CENTER);
        playGameBoard = new GridPane();

        playGameBoard.setAlignment(Pos.CENTER);

        playMoveButton = new Button(MonsterHunter.playLanguageFile.getProperty("playMoveButton"));
        MainStyle.applyNormalButtonStyle(playMoveButton);

        playExitButton = new Button(MonsterHunter.playLanguageFile.getProperty("playExitButton"));
        MainStyle.applyNormalButtonStyle(playExitButton);

        playButtonBox = new HBox(playMoveButton, playExitButton);
        playButtonBox.setAlignment(Pos.CENTER);
        playButtonBox.setSpacing(10);

        playRoot.setBackground(new Background(MainStyle.choiceMenuBackgroundImage));

        playRoot.getChildren().addAll(playGameBoard, playButtonBox);

        super.setPlayScene(new Scene(playRoot));
    }

    public void initWaitingScene() {
        waitRoot = new VBox();
        waitRoot.setAlignment(Pos.CENTER);
        waitLabel = new Label(MonsterHunter.playLanguageFile.getProperty("MonsterViewWaitLabel"));

        MainStyle.applyNormalLabelStyle(waitLabel);

        waitButton = new Button(MonsterHunter.playLanguageFile.getProperty("waitButton"));

        MainStyle.applyNormalButtonStyle(waitButton);

        waitRoot.setBackground(new Background(MainStyle.choiceMenuBackgroundImage));
        waitRoot.getChildren().addAll(waitLabel, waitButton);

        super.setWaitScene(new Scene(waitRoot));
    }

    public void makeGameBoard(boolean[][] board) {
        ImagePattern monsterTexture = new ImagePattern(
                new Image("file:" + MonsterHunter.init.getProperty("MonsterViewApparence")));
        ImagePattern wallTexture = new ImagePattern(
                new Image("file:" + MonsterHunter.init.getProperty("WallViewAsset")));
        ImagePattern groundTexture = new ImagePattern(
                new Image("file:" + MonsterHunter.init.getProperty("GroundViewAsset")));
        ImagePattern exitTexture = new ImagePattern(
                new Image("file:" + MonsterHunter.init.getProperty("ExitViewAsset")));
        ImagePattern unkwonTexture = new ImagePattern(
                new Image("file:" + MonsterHunter.init.getProperty("UnknowTexture")));

        playGameBoard.setHgap(3);
        playGameBoard.setVgap(3);
        playGameBoard.setBackground(new Background(
                new BackgroundFill(javafx.scene.paint.Color.GRAY, null, null)));

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Rectangle cell = new Rectangle(RECT_ROW, RECT_COL);
                Text text = new Text(RECT_ROW, RECT_COL, "");
                if (monster.getFog()[i][j]) {
                    cell.setFill(unkwonTexture);
                } else if (board[i][j]) {
                    cell.setFill(wallTexture);
                } else {
                    ICoordinate monsterCoord = monster.getMonsterCoord();
                    if (i == monster.getExit().getRow() && j == monster.getExit().getCol()) {
                        cell.setFill(exitTexture);
                    } else if (i == monsterCoord.getRow() && j == monsterCoord.getCol()) {
                        cell.setFill(monsterTexture);

                    } else {
                        cell.setFill(groundTexture);
                    }
                }
                ICoordinate hunterCoord = monster.getHunterCoord();
                if (hunterCoord != null && (i == hunterCoord.getRow() && j == hunterCoord.getCol())) {
                    cell.setStroke(javafx.scene.paint.Color.BLACK);
                    cell.setStrokeWidth(3);
                } else {
                    cell.setStroke(javafx.scene.paint.Color.BLACK);
                    cell.setStrokeWidth(1);
                }

                StackPane stack = new StackPane();
                stack.getChildren().addAll(cell, text);

                playGameBoard.add(stack, j + 1, i + 1);
            }
        }
    }

    public Monster getMonster() {
        return this.monster;
    }

    public VBox getRoot() {
        return this.playRoot;
    }

    public GridPane getGameBoard() {
        return this.playGameBoard;
    }

    public Button getPlayMoveButton() {
        return this.playMoveButton;
    }

    public Button getExitButton() {
        return this.playExitButton;
    }

    public Button getWaitButton() {
        return waitButton;
    }

    @Override
    public void update(Subject arg0) {
        // Ne fait rien
    }

    @Override
    public void update(Subject arg0, Object arg1) {
        if (CellEvent[].class == arg1.getClass()) {
            CellEvent[] events = (CellEvent[]) arg1;
            monster.update(events[1]);
        }
    }
}