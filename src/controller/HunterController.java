package controller;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import main.MonsterHunter;
import model.CellEvent;
import model.Coordinate;
import model.Hunter;
import model.Maze;
import view.game.GameView;
import view.play.HunterView;

public class HunterController {

    private Maze maze;
    private HunterView view;
    private Button shot;
    private StackPane selectedStack;

    private GameView gameView;

    private boolean hunterHasPlayed = false;

    public HunterController(Maze maze, GameView gameView) {
        this.maze = maze;
        this.gameView = gameView;
        Hunter hunter = new Hunter(maze.getWall().length, maze.getWall()[0].length);
        maze.attach(hunter);
        this.view = new HunterView(hunter);
        this.makeGameBoard(view.getHunter().getKnowWall());
        this.gameView.display(view.getScene(), true);
        this.shot = view.getShotButton();
        this.shot.setOnAction(new ActionHandler());
        view.getExitButton().setOnAction(e -> {
            gameView.nextPlayScenes();
            MonsterHunter.exitedGame();
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

    private class ActionHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            if (event.getSource() == shot && selectedStack != null) {
                resetStackStroke(selectedStack);
                ICoordinate coord = new Coordinate(GridPane.getRowIndex(selectedStack) - 1,
                        GridPane.getColumnIndex(selectedStack) - 1);
                maze.cellUpdate(new CellEvent(coord, Maze.turn, CellInfo.HUNTER));
                makeGameBoard(view.getHunter().getKnowWall());
                view.getRoot().getChildren().set(0, view.getGameBoard());
                selectedStack = null;
                gameView.nextPlayScenes();
            }
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

}
