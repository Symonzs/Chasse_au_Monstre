package controller;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

import java.util.Properties;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import model.CellEvent;
import model.Coordinate;
import model.Maze;
import view.play.HunterView;

public class HunterController {

    private Maze maze;
    private HunterView view;
    private Button shot;
    private StackPane selectedStack;

    public HunterController(Maze maze, Properties properties) {
        this.maze = maze;
        this.view = new HunterView(maze.getWall().length, maze.getWall()[0].length, properties);
        maze.attach(view);
        this.makeGameBoard(view.getHunter().getKnowWall(), view.getHunter().getKnowEmpty());
        view.getPlayShotButton().setOnAction(e -> {
            shot(e);
        });
        view.getPlayExitButton().setOnAction(e -> {
            maze.setGameIsClosed(true);
        });
        view.getWaitButton().setOnAction(e -> {
            maze.setIsReadyToNext(true);
        });

    }

    public void makeGameBoard(boolean[][] wall, boolean[][] empty) {
        view.makeGameBoard(wall, empty);
        for (Node node : view.getPlayGameBoard().getChildren()) {
            if (StackPane.class == node.getClass()) {
                StackPane stack = (StackPane) node;
                stack.setOnMouseClicked(new MouseHandler());
            }
        }
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

    public void shot(ActionEvent e) {
        if (selectedStack != null) {
            resetStackStroke(selectedStack);
            ICoordinate coord = new Coordinate(GridPane.getRowIndex(selectedStack) - 1,
                    GridPane.getColumnIndex(selectedStack) - 1);
            maze.cellUpdate(new CellEvent(coord, Maze.currentTurn, CellInfo.HUNTER));
            makeGameBoard(view.getHunter().getKnowWall(), view.getHunter().getKnowEmpty());
            view.getPlayRoot().getChildren().set(0, view.getPlayGameBoard());
            selectedStack = null;
            maze.setIsReadyToNext(false);
            maze.setHunterHasPlayed(true);
        }
    }

    private class MouseHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent arg0) {
            if (StackPane.class == arg0.getSource().getClass()) {
                StackPane stack = (StackPane) arg0.getSource();
                if (arg0.getClickCount() > 1) {
                    shot.fire();
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

    public HunterView getView() {
        return view;
    }
}
