package view.play;

import java.nio.file.Paths;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import main.MonsterHunter;
import model.Monster;
import view.game.GameView;

public class MonsterView extends PlayView {

    private static final int RECT_COL = 60;
    private static final int RECT_ROW = 60;
    private Monster monster;

    // private HBox root;
    private GridPane gameBoard;
    private Button move;
    private Button exit;

    public MonsterView(Monster monster, GameView gameView) {
        super(gameView, Paths.get(MonsterHunter.PROPERTIES.getProperty("MonsterViewApparence")).toFile());
        this.monster = monster;
        // this.setTitle("Monster View | Tour : " + Maze.turn);
        // this.root = new HBox();
        this.gameBoard = new GridPane();
        this.move = new Button("Move");
        this.exit = new Button("Exit game");

        super.display(new VBox[] { new VBox(gameBoard), new VBox(move, exit) });
        // VBox buttonsBox = new VBox(move, exit);
        // this.root.getChildren().addAll(gameBoard, buttonsBox);
        // Scene scene = new Scene(root);
        // this.setScene(scene);
    }

    public void makeGameBoard(boolean[][] board) {
        // this.setTitle("Monster View | Tour : " + Maze.turn);
        gameBoard.setHgap(3);
        gameBoard.setVgap(3);
        gameBoard.setBackground(new Background(
                new BackgroundFill(javafx.scene.paint.Color.LIGHTGRAY, null, null)));

        for (int i = 0; i < board.length; i++) {
            Label columnHeader = new Label(String.valueOf(i));
            columnHeader.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            columnHeader.setAlignment(Pos.CENTER);
            gameBoard.add(columnHeader, i + 1, 0);
        }

        for (int j = 0; j < board[0].length; j++) {
            Label rowHeader = new Label(String.valueOf((char) ('A' + j)));
            rowHeader.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            rowHeader.setAlignment(Pos.CENTER);
            gameBoard.add(rowHeader, 0, j + 1);
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Rectangle cell = new Rectangle(RECT_ROW, RECT_COL);
                Text text = new Text(RECT_ROW, RECT_COL, "");
                if (board[i][j]) {
                    cell.setFill(javafx.scene.paint.Color.BLACK);
                } else {
                    ICoordinate monsterCoord = monster.getMonsterCoord();
                    if (i == monster.getExit().getRow() && j == monster.getExit().getCol()) {
                        text = new Text("Exit");
                    } else if (i == monsterCoord.getRow() && j == monsterCoord.getCol()) {
                        text = new Text("Monster");
                    }
                    cell.setFill(javafx.scene.paint.Color.WHITE);
                }
                ICoordinate hunterCoord = monster.getHunterCoord();
                if (hunterCoord != null && (i == hunterCoord.getRow() && j == hunterCoord.getCol())) {
                    cell.setStroke(javafx.scene.paint.Color.LIGHTGREEN);
                    cell.setStrokeWidth(3);
                } else {
                    cell.setStroke(javafx.scene.paint.Color.BLACK);
                    cell.setStrokeWidth(1);
                }

                StackPane stack = new StackPane();
                stack.getChildren().addAll(cell, text);

                gameBoard.add(stack, j + 1, i + 1);
            }
        }
    }

    public Monster getMonster() {
        return this.monster;
    }

    /*
     * public HBox getRoot() {
     * return this.root;
     * }
     */

    public GridPane getGameBoard() {
        return this.gameBoard;
    }

    public Button getMoveButton() {
        return this.move;
    }

    public Button getExitButton() {
        return this.exit;
    }

}