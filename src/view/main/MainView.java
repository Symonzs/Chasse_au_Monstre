package view.main;

import java.io.File;
import java.nio.file.Paths;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.MonsterHunter;
import model.Maze;
import view.play.NumberInStringComparator;

public class MainView extends Stage {
    private final ListView<String> mazeList;
    private final ObservableList<String> mazeListItems = FXCollections.observableArrayList();
    private Maze maze;

    public MainView(File file) {
        VBox root = new VBox();
        Text title = new Text("Monster Hunter");
        Text selectMaze = new Text("Select a maze to play with : ");
        Button play = new Button("Play");
        root.getChildren().addAll(title, selectMaze);

        mazeList = new ListView<>();
        mazeList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        File resources = Paths.get(MonsterHunter.PROPERTIES.getProperty("MapFindFolder")).toFile();
        if (!resources.isDirectory()) {
            System.out.println("Le dossier resources n'existe pas");
        }
        for (File f : resources.listFiles()) {
            if (f.getName().endsWith(".csv")) {
                mazeListItems.add(f.getName().substring(0, f.getName().length() - 4));
            }
        }
        mazeList.setPrefHeight(mazeListItems.size() * 26.0);
        mazeList.setMaxWidth(200);
        FXCollections.sort(mazeListItems, new NumberInStringComparator());

        mazeList.setItems(mazeListItems);
        root.getChildren().addAll(mazeList, play);
        play.setOnAction(e -> new ActionHandler().handle());

        this.setTitle("Monster Hunter");
        this.setScene(new Scene(root, 300, 300));
    }

    public Maze getMaze() {
        return this.maze;
    }

    private class ActionHandler {

        private void handle() {
            if (mazeList.getSelectionModel().getSelectedItem() != null) {
                Maze.resetTurn();
                maze = new Maze(111, 11);
                MainView.this.close();
            }
        }
    }

}
