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
import model.Hunter;
import model.Maze;
import view.play.HunterView;

public class HunterController {

    private Maze maze;
    private HunterView view;

    private Button shot;
    private StackPane selectedStack;

    private boolean hasPlayed;
    private boolean isReadyToNext;

    private boolean gameIsExited;

    public HunterController(Maze maze, Properties properties) {
        this.maze = maze;
        Hunter hunter = new Hunter(maze.getWall().length, maze.getWall()[0].length);
        maze.attach(hunter);
        this.view = new HunterView(hunter, properties);
        // this.view.getPlayGameBoard();
        this.makeGameBoard(view.getHunter().getKnowWall(), view.getHunter().getKnowEmpty());

        view.getPlayShotButton().setOnAction(e -> {
            shot(e);
        });
        view.getPlayExitButton().setOnAction(e -> {
            maze.SetgameIsExited(true);
            gameIsExited = true;
            System.out.println("ici");
            // view.close();
        });
        view.getWaitButton().setOnAction(e -> {
            isReadyToNext = true;
            hasPlayed = false;
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

    public HunterView getHunterView() {
        return this.view;
    }

    public void shot(ActionEvent e) {
        if (selectedStack != null) {
            resetStackStroke(selectedStack);
            selectedStack = null;
        }
    }

    public void update(ActionEvent e) {
        if (e.getSource() == shot && selectedStack != null) {
            resetStackStroke(selectedStack);
            ICoordinate coord = new Coordinate(GridPane.getRowIndex(selectedStack) - 1,
                    GridPane.getColumnIndex(selectedStack) - 1);
            maze.cellUpdate(new CellEvent(coord, Maze.turn, CellInfo.HUNTER));
            makeGameBoard(view.getHunter().getKnowWall(), view.getHunter().getKnowEmpty());
            view.getPlayRoot().getChildren().set(0, view.getPlayGameBoard());
            selectedStack = null;
            isReadyToNext = false;
            hasPlayed = true;
        }
    }
    /*
     * private class ActionHandler implements EventHandler<ActionEvent> {
     * 
     * @Override
     * public void handle(ActionEvent event) {
     * if (event.getSource() == shot && selectedStack != null) {
     * resetStackStroke(selectedStack);
     * ICoordinate coord = new Coordinate(GridPane.getRowIndex(selectedStack) - 1,
     * GridPane.getColumnIndex(selectedStack) - 1);
     * maze.cellUpdate(new CellEvent(coord, Maze.turn, CellInfo.HUNTER));
     * makeGameBoard(view.getHunter().getKnowWall(),
     * view.getHunter().getKnowEmpty());
     * view.getPlayRoot().getChildren().set(0, view.getPlayGameBoard());
     * selectedStack = null;
     * view.showPlayScene();
     * gameView.display(view.getPlayScene(), true);
     * try {
     * Thread.sleep(2500);
     * } catch (InterruptedException e) {
     * // TODO Auto-generated catch block
     * e.printStackTrace();
     * }
     * view.showWaitScene();
     * gameView.display(view.getWaitScene(), false);
     * }
     * }
     * 
     * }
     */

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

    public boolean hasPlayed() {
        return hasPlayed;
    }

    public boolean isReadyToNext() {
        return isReadyToNext;
    }

    public boolean isGameIsExited() {
        return gameIsExited;
    }
}
