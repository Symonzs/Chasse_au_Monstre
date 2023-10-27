package view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Maze;

public class MonsterView extends Application {

    public static void main(String[] args) {
        launch();
    }

    public void showView() {

    }

    public static GridPane makeGameBoard(boolean[][] board) {
        GridPane gameBoard = new GridPane();
        // gameBoard.setHgap(10);
        // gameBoard.setVgap(10);

        for (int i = 0; i < board.length; i++) {
            Label columnHeader = new Label(String.valueOf(i));
            columnHeader.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            columnHeader.setAlignment(Pos.CENTER);
            gameBoard.add(columnHeader, i, 0);
        }

        for (int j = 0; j < board[0].length; j++) {
            Label rowHeader = new Label(String.valueOf((char) ('A' + j)));
            rowHeader.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            rowHeader.setAlignment(Pos.CENTER);
            gameBoard.add(rowHeader, 0, j);
        }

        for (int i = 2; i < board.length; i++) {
            for (int j = 2; j < board[i].length; j++) {
                Rectangle cell = new Rectangle(50, 50);
                if (board[i][j]) {
                    cell.setFill(javafx.scene.paint.Color.BLACK);
                } else {
                    cell.setFill(javafx.scene.paint.Color.WHITE);
                }

                cell.setStroke(javafx.scene.paint.Color.BLACK);
                cell.setStrokeWidth(1);

                gameBoard.add(cell, i, j);
            }
        }

        return gameBoard;
    }

    @Override
    public void start(Stage primaryStage) {
        Maze maze = new Maze(11, 11);

        GridPane gameBoard = MonsterView.makeGameBoard(maze.getWall());

        Scene scene = new Scene(gameBoard);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
