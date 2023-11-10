package view.play;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.Coordinate;
import model.Hunter;
import model.Maze;

public class HunterView extends PlayView {

    private static final int RECT_COL = 60;
    private static final int RECT_ROW = 60;
    private Hunter hunter;

    private Font font;

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

    public HunterView(Hunter hunter) {
        this.font = new Font("Arial", 30);
        this.hunter = hunter;

        initPlayView();
        initWaitView();
        showPlayScene();
    }

    public void initPlayView() {
        playRoot = new VBox();
        playRoot.setPadding(new Insets(10));
        playRoot.setAlignment(Pos.CENTER);
        playGameBoard = new GridPane();
        playShotButton = new Button("Confirmer le tir");
        playShotButton.setPadding(new Insets(10));
        playShotButton.setFont(font);
        playExitButton = new Button("Quitter le jeux");
        playExitButton.setPadding(new Insets(10));
        playExitButton.setFont(font);
        playVBoxNav = new HBox(playShotButton, playExitButton);

        playRoot.getChildren().addAll(playGameBoard, playVBoxNav);

        super.setPlayScene(new Scene(playRoot));

    }

    public void initWaitView() {
        waitRoot = new VBox();

        waitRoot.setPadding(new Insets(10));
        waitRoot.setAlignment(Pos.CENTER);
        waitLabel = new Label("Vous avez joué. C'est au tour du Monstre de jouer.");
        waitLabel.setFont(font);
        waitButton = new Button("Passez au tour suivant");
        waitButton.setFont(font);
        waitButton.setPadding(new Insets(10));
        waitRoot.getChildren().addAll(waitLabel, waitButton);

        super.setWaitScene(new Scene(waitRoot));
    }

    public void makeGameBoard(boolean[][] board) {
        this.setTitle("Hunter View | Tour : " + Maze.turn);
        this.playGameBoard.setHgap(3);
        this.playGameBoard.setVgap(3);
        this.playGameBoard.setBackground(new Background(
                new BackgroundFill(javafx.scene.paint.Color.LIGHTGRAY, null, null)));

        for (int i = 0; i < board.length; i++) {
            Label columnHeader = new Label(String.valueOf(i));
            columnHeader.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            columnHeader.setAlignment(Pos.CENTER);
            this.playGameBoard.add(columnHeader, i + 1, 0);
        }

        for (int j = 0; j < board[0].length; j++) {
            Label rowHeader = new Label(String.valueOf((char) ('A' + j)));
            rowHeader.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            rowHeader.setAlignment(Pos.CENTER);
            this.playGameBoard.add(rowHeader, 0, j + 1);
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Rectangle cell = new Rectangle(RECT_ROW, RECT_COL);
                Text text = new Text(RECT_ROW, RECT_COL, "");
                if (board[i][j]) {
                    cell.setFill(javafx.scene.paint.Color.BLACK);
                } else {
                    ICoordinate cellCoord = new Coordinate(i, j);
                    Integer turn = hunter.getLastTurnFromCoordinate(cellCoord);
                    if (Maze.turn.equals(turn)) {
                        text = new Text("Monster");
                    } else if (turn != null) {
                        text = new Text(turn.toString());
                    }
                    cell.setFill(javafx.scene.paint.Color.WHITE);
                }
                ICoordinate hunterCoord = hunter.getHunterCoord();
                if (hunterCoord != null && (i == hunterCoord.getRow() && j == hunterCoord.getCol())) {
                    cell.setStroke(javafx.scene.paint.Color.LIGHTGREEN);
                    cell.setStrokeWidth(3);
                } else {
                    cell.setStroke(javafx.scene.paint.Color.BLACK);
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

}