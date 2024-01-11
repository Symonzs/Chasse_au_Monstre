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
import model.Coordinate;
import model.Hunter;
import model.Maze;
import model.Observer;
import model.Subject;
import util.style.MainStyle;

public class HunterView extends PlayView implements Observer {

    private static int RECT_COL = 75;
    private static int RECT_ROW = 75;

    private Hunter hunter;

    /* Play Button : */
    private VBox playRoot;
    private GridPane playGameBoard;
    private HBox playVBoxNav;
    private Button playShotButton;
    private Button playExitButton;

    /* Waiting Button */
    private VBox waitRoot;
    private Label waitLabel;
    private Button waitButton;

    /* Error */
    private VBox errorRoot;
    private Label errorLabel;
    private Button errorButton;

    public HunterView(Integer rows, Integer cols) {
        this.hunter = new Hunter(rows, cols);

        initMazePaneSize(rows);
        initPlayView();
        initWaitView();
        showPlayScene();
    }

    public void initPlayView() {
        playRoot = new VBox();
        playRoot.setPadding(new Insets(10));

        playRoot.setAlignment(javafx.geometry.Pos.CENTER);

        playGameBoard = new GridPane();
        playGameBoard.setAlignment(Pos.CENTER);
        playShotButton = new Button(MonsterHunter.playLanguageFile.getProperty("playShotButton"));
        playExitButton = new Button(MonsterHunter.playLanguageFile.getProperty("playExitButton"));

        MainStyle.applyNormalButtonStyle(playShotButton);
        MainStyle.applyNormalButtonStyle(playExitButton);

        playVBoxNav = new HBox(playShotButton, playExitButton);
        playVBoxNav.setAlignment(Pos.CENTER);
        playVBoxNav.setSpacing(10);

        Label tourLabel = new Label("Tour : " + Maze.currentTurn);
        MainStyle.applyNormalLabelStyle(tourLabel);

        playRoot.getChildren().addAll(tourLabel, playGameBoard, playVBoxNav);
        playRoot.setBackground(new Background(MainStyle.choiceMenuBackgroundImage));

        super.setPlayScene(new Scene(playRoot));

    }

    private void initMazePaneSize(Integer row) {
        RECT_COL = (int) ((Screen.getPrimary().getBounds().getHeight() - 200) / row);
        RECT_ROW = (int) ((Screen.getPrimary().getBounds().getHeight() - 200) / row);

    }

    public void initWaitView() {
        waitRoot = new VBox();

        waitRoot.setPadding(new Insets(10));
        waitRoot.setAlignment(Pos.CENTER);
        waitLabel = new Label(MonsterHunter.playLanguageFile.getProperty("HunterViewWaitLabel"));
        MainStyle.applyNormalLabelStyle(waitLabel);
        waitButton = new Button(MonsterHunter.playLanguageFile.getProperty("waitButton"));
        MainStyle.applyLitleButtonStyle(waitButton);

        waitRoot.getChildren().addAll(waitLabel, waitButton);
        waitRoot.setBackground(new Background(MainStyle.choiceMenuBackgroundImage));

        super.setWaitScene(new Scene(waitRoot));
    }

    public void initErrorView(String message) {
        errorRoot = new VBox();
        errorLabel = new Label(message);
        errorButton = new Button(MonsterHunter.exceptionLanguageFile.getProperty("errorButton"));
    }

    public void makeGameBoard(boolean[][] wall, boolean[][] empty) {
        ImagePattern monsterTexture = new ImagePattern(
                new Image("file:" + MonsterHunter.init.getProperty("MonsterViewApparence")));
        ImagePattern wallTexture = new ImagePattern(
                new Image("file:" + MonsterHunter.init.getProperty("WallViewAsset")));
        ImagePattern groundTexture = new ImagePattern(
                new Image("file:" + MonsterHunter.init.getProperty("GroundViewAsset")));
        ImagePattern unkwonTexture = new ImagePattern(
                new Image("file:" + MonsterHunter.init.getProperty("UnknowTexture")));

        this.setTitle("Hunter View | Tour : " + Maze.currentTurn);
        this.playGameBoard.setHgap(3);
        this.playGameBoard.setVgap(3);
        this.playGameBoard.setBackground(new Background(
                new BackgroundFill(javafx.scene.paint.Color.GRAY, null, null)));

        for (int i = 0; i < wall.length; i++) {
            for (int j = 0; j < wall[i].length; j++) {
                Rectangle cell = new Rectangle(RECT_ROW, RECT_COL);
                Text text = new Text(RECT_ROW, RECT_COL, "");
                if (wall[i][j]) {
                    cell.setFill(wallTexture);
                } else {
                    ICoordinate cellCoord = new Coordinate(i, j);
                    Integer turn = hunter.getLastTurnFromCoordinate(cellCoord);
                    if (Maze.currentTurn.equals(turn)) {
                        cell.setFill(monsterTexture);
                    } else if (turn != null) {
                        text = new Text(turn.toString());
                        cell.setFill(groundTexture);
                    } else if (empty[i][j]) {
                        cell.setFill(groundTexture);
                    } else {
                        cell.setFill(unkwonTexture);
                    }
                }
                ICoordinate hunterCoord = hunter.getHunterCoord();
                if (hunterCoord != null && (i == hunterCoord.getRow() && j == hunterCoord.getCol())) {
                    cell.setStroke(javafx.scene.paint.Color.LIGHTGREEN);
                    cell.setStrokeWidth(3);
                } else {
                    cell.setStroke(wallTexture);
                    cell.setStrokeWidth(1);
                }

                StackPane stack = new StackPane();
                stack.getChildren().addAll(cell, text);

                this.playGameBoard.add(stack, j + 1, i + 1);
            }
        }
    }

    public Hunter getHunter() {
        return this.hunter;
    }

    public VBox getPlayRoot() {
        return this.playRoot;
    }

    public GridPane getPlayGameBoard() {
        return this.playGameBoard;
    }

    public Button getPlayShotButton() {
        return this.playShotButton;
    }

    public Button getPlayExitButton() {
        return this.playExitButton;
    }

    public Button getWaitButton() {
        return waitButton;
    }

    public Button getErrorButton() {
        return errorButton;
    }

    public VBox getErrorRoot() {
        return errorRoot;
    }

    public Label getErrorLabel() {
        return errorLabel;
    }

    @Override
    public void update(Subject arg0) {
        // Ne fait rien
    }

    @Override
    public void update(Subject arg0, Object arg1) {
        if (CellEvent[].class == arg1.getClass()) {
            CellEvent[] events = (CellEvent[]) arg1;
            hunter.update(events[0]);
        }
    }
}