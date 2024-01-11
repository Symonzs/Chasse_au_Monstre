package view.play;

import java.util.Properties;

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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.CellEvent;
import model.Maze;
import model.Monster;
import model.Observer;
import model.Subject;

public class MonsterView extends PlayView implements Observer {

    private static final int RECT_COL = 75;
    private static final int RECT_ROW = 75;

    private Monster monster;

    private Font font;

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

    private Properties properties;

    public MonsterView(Maze maze, Properties properties, boolean warFogIsOn) {
        this.monster = new Monster(maze, warFogIsOn);
        this.properties = properties;

        this.font = new Font("Arial", 24);

        initWaitingScene();
        initPlayScene();
        showPlayScene();
    }

    public void initPlayScene() {
        playRoot = new VBox();
        playRoot.setPadding(new Insets(10));
        playRoot.setAlignment(Pos.CENTER);
        playGameBoard = new GridPane();
        playMoveButton = new Button("Confirmer le déplacement.");
        playMoveButton.setPadding(new Insets(10));
        playMoveButton.setFont(font);
        playExitButton = new Button("Quitter le jeux.");
        playExitButton.setPadding(new Insets(10));
        playExitButton.setFont(font);
        playButtonBox = new HBox(playMoveButton, playExitButton);

        playRoot.getChildren().addAll(playGameBoard, playButtonBox);

        super.setPlayScene(new Scene(playRoot));
    }

    public void initWaitingScene() {
        waitRoot = new VBox();
        waitRoot.setAlignment(Pos.CENTER);
        waitLabel = new Label("Vous avez joué. C'est au tour du Chasseur de jouer.");
        waitLabel.setFont(font);
        waitButton = new Button("Passez au tour suivant");
        waitLabel.setFont(font);
        waitButton.setPadding(new Insets(10));

        waitRoot.getChildren().addAll(waitLabel, waitButton);

        super.setWaitScene(new Scene(waitRoot));
    }

    public void makeGameBoard(boolean[][] board) {
        ImagePattern monsterTexture = new ImagePattern(
                new Image("file:" + properties.getProperty("MonsterViewApparence")));
        ImagePattern wallTexture = new ImagePattern(new Image("file:" + properties.getProperty("WallViewAsset")));
        ImagePattern groundTexture = new ImagePattern(new Image("file:" + properties.getProperty("GroundViewAsset")));
        ImagePattern exitTexture = new ImagePattern(new Image("file:" + properties.getProperty("ExitViewAsset")));
        ImagePattern unkwonTexture = new ImagePattern(new Image("file:" + properties.getProperty("UnknowTexture")));

        playGameBoard.setHgap(3);
        playGameBoard.setVgap(3);
        playGameBoard.setBackground(new Background(
                new BackgroundFill(javafx.scene.paint.Color.GRAY, null, null)));

        for (int i = 0; i < board.length; i++) {
            Label columnHeader = new Label(String.valueOf(i));
            columnHeader.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            columnHeader.setAlignment(Pos.CENTER);
            playGameBoard.add(columnHeader, i + 1, 0);
        }

        for (int j = 0; j < board[0].length; j++) {
            Label rowHeader = new Label(String.valueOf((char) ('A' + j)));
            rowHeader.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            rowHeader.setAlignment(Pos.CENTER);
            playGameBoard.add(rowHeader, 0, j + 1);
        }

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
                    cell.setStroke(javafx.scene.paint.Color.BROWN);
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