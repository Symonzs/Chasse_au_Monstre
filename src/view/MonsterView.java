package view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Coordinate;
import model.Maze;

public class MonsterView extends Application {
    private Coordinate exit;
    private Coordinate monster;
    private Maze maze;

    public MonsterView(Maze m) {
        maze = m;
        showView();
    }

    public static void main(String[] args) {
        launch();
    }

    public void showView() {
        start(new Stage());
    }

    public GridPane makeGameBoard(boolean[][] board) {
        GridPane gameBoard = new GridPane();
        gameBoard.setHgap(2);
        gameBoard.setVgap(2);

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
                Rectangle cell = new Rectangle(60, 60);
                Text text = new Text();
                if (board[i][j]) {
                    cell.setFill(javafx.scene.paint.Color.BLACK);
                } else {
                    if (i == this.exit.getRow() && j == this.exit.getCol()) {
                        text = new Text("Exit");
                    } else if (i == this.monster.getRow() && j == this.monster.getCol()) {
                        text = new Text("Monster");
                    }
                    cell.setFill(javafx.scene.paint.Color.WHITE);
                }

                cell.setStroke(javafx.scene.paint.Color.BLACK);
                cell.setStrokeWidth(1);

                StackPane stack = new StackPane();
                stack.getChildren().addAll(cell, text);

                gameBoard.add(stack, i + 1, j + 1);
            }
        }

        return gameBoard;
    }

    // TODO Touver une soluce pour recup la sortie
    public void setExit(Coordinate c) {
        exit = c;
    }

    // TODO Touver une soluce pour recup la position du monstre
    public void setMonster(Coordinate c) {
        monster = c;
    }

    @Override
    public void start(Stage primaryStage) {
        setExit((Coordinate) maze.getExit());
        setMonster(
                (Coordinate) maze.getMonster().keySet().toArray()[maze.getMonster().keySet().toArray().length - 1]);

        GridPane gameBoard = makeGameBoard(maze.getWall());

        Scene scene = new Scene(gameBoard);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
