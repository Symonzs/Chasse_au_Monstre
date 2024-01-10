package controller;

import java.util.InputMismatchException;
import java.util.Properties;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import main.MonsterHunter;
import model.CellEvent;
import model.Coordinate;
import model.Maze;
import model.Monster;
import view.play.MonsterView;

public class MonsterController {

    private Maze maze;
    private MonsterView view;
    private Button move;
    private StackPane selectedStack;

    private boolean hasPlayed = false;
    private boolean isReadyToNext = false;

    private boolean diagonalMovesAllowed;

    public MonsterController(Maze maze, Properties properties, boolean diagonalMovesAllowed) {
        this.maze = maze;
        this.view = new MonsterView(maze, properties);
        this.diagonalMovesAllowed = diagonalMovesAllowed;
        maze.attach(view);
        this.makeGameBoard(view.getMonster().getWall());
        view.getExitButton().setOnAction(e -> {
            maze.setGameIsClosed(true);
        });
        view.getPlayMoveButton().setOnAction(new ActionHandler());
        view.getWaitButton().setOnAction(e -> {
            maze.setIsReadyToNext(true);
        });
    }

    public void makeGameBoard(boolean[][] board) {
        view.makeGameBoard(board);
        for (Node node : view.getGameBoard().getChildren()) {
            if (StackPane.class == node.getClass()) {
                StackPane stack = (StackPane) node;
                stack.setOnMouseClicked(new MouseHandler());
            }
        }
    }

    public boolean coordIsNeighbor(Monster monster, ICoordinate coordinate2) {
        Integer row1 = monster.getMonsterCoord().getRow();
        Integer col1 = monster.getMonsterCoord().getCol();

        Integer row2 = coordinate2.getRow();
        Integer col2 = coordinate2.getCol();

        if (diagonalMovesAllowed) {
            if (row1.equals(row2)) {
                return col1.equals(col2 + 1) ^ col1.equals(col2 - 1);
            }
            if (col1.equals(col2)) {
                return row1.equals(row2 + 1) ^ row1.equals(row2 - 1);
            }
            return row1.equals(row2 + 1) && col1.equals(col2 + 1)
                    || row1.equals(row2 + 1) && col1.equals(col2 - 1)
                    || row1.equals(row2 - 1) && col1.equals(col2 + 1)
                    || row1.equals(row2 - 1) && col1.equals(col2 - 1);
        } else {
            if (row1.equals(row2)) {
                return col1.equals(col2 + 1) ^ col1.equals(col2 - 1);
            }
            if (col1.equals(col2)) {
                return row1.equals(row2 + 1) ^ row1.equals(row2 - 1);
            }
        }
        return false;
    }

    public boolean coordIsDifferent(Monster monster, ICoordinate newCoord) {
        return !monster.getMonsterCoord().equals(newCoord);
    }

    public boolean cellIsWall(Monster monster, ICoordinate newCoord) {
        return monster.getWall()[newCoord.getRow()][newCoord.getCol()];
    }

    public boolean isAllowedToMove(Monster monster, ICoordinate newCoord) {
        if (!coordIsDifferent(monster, newCoord)) {
            throw new InputMismatchException("Le monstre ne peut pas rester sur place");
        }
        if (!coordIsNeighbor(monster, newCoord)) {
            throw new InputMismatchException("La case n'est pas atteignable par le monstre");
        }
        if (cellIsWall(monster, newCoord)) {
            throw new InputMismatchException("La case contient un mur");
        }
        return true;
    }

    protected static void resetStackStroke(StackPane stack) {
        Rectangle selectedCell = (Rectangle) stack.getChildren().get(0);
        if (selectedCell != null) {
            selectedCell.setStroke(javafx.scene.paint.Color.BLACK);
            selectedCell.setStrokeWidth(1);
        }
    }

    protected static void amplifyStackStroke(StackPane stack) {
        Rectangle selectedCell = (Rectangle) stack.getChildren().get(0);
        if (selectedCell != null) {
            selectedCell.setStroke(javafx.scene.paint.Color.RED);
            selectedCell.setStrokeWidth(3);
        }
    }

    private class ActionHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            if (selectedStack != null) {
                resetStackStroke(selectedStack);
                ICoordinate coord = new Coordinate(GridPane.getRowIndex(selectedStack) - 1,
                        GridPane.getColumnIndex(selectedStack) - 1);
                try {
                    isAllowedToMove(view.getMonster(), coord);
                } catch (InputMismatchException ex) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Attention");
                    alert.setHeaderText(ex.getMessage());
                    alert.showAndWait();
                    return;
                }
                maze.cellUpdate(new CellEvent(coord, Maze.currentTurn, CellInfo.MONSTER));
                makeGameBoard(view.getMonster().getWall());
                view.getRoot().getChildren().set(0, view.getGameBoard());
                selectedStack = null;
                maze.setIsReadyToNext(false);
                maze.setMonsterHasPlayed(true);
            }
        }

    }

    private class MouseHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent arg0) {
            if (StackPane.class == arg0.getSource().getClass()) {
                StackPane stack = (StackPane) arg0.getSource();
                if (arg0.getClickCount() > 1) {
                    move.fire();
                    return;
                }
                if (selectedStack == stack) {
                    resetStackStroke(selectedStack);
                    selectedStack = null;
                } else {
                    if (selectedStack != null) {
                        resetStackStroke(selectedStack);
                    }
                    selectedStack = stack;
                    amplifyStackStroke(selectedStack);
                }
            }
        }

    }

    public MonsterView getView() {
        return this.view;
    }

    public boolean hasPlayed() {
        return hasPlayed;
    }

    public boolean isReadyToNext() {
        return isReadyToNext;
    }
}
