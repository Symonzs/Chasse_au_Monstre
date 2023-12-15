package view.main;

import java.io.File;
import java.nio.file.Paths;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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

    /* Main Menu */
    private Pane rootMenu;
    private Pane paneMenu;
    private Text titleMenu;
    private Button playButtonMenu;
    private Button parameterButtonMenu;
    private Scene sceneMenu;

    /* Parameter Menu */
    private HBox rootParameter;
    private Button quitParameter;
    private VBox mapVBoxParameter;
    private VBox playerVBoxParameter;
    private VBox languageVBoxParameter;
    private Text mapTitle;
    private Text playerTitle;
    private Text languageTitle;
    private CheckBox checkBoxIsGeneratedMap;
    private CheckBox checkBoxIsMultyGame;
    private CheckBox checkBoxIsServer;
    private TextField textServerAddress;
    private CheckBox checkBoxMonsterIsAnAI;
    private CheckBox checkBoxHunterISAnAi;
    private CheckBox checkBoxShowBearingWall;

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
        play.setOnAction(e -> {
            if (mazeList.getSelectionModel().getSelectedItem() != null) {
                Maze.resetTurn();
                maze = new Maze(MonsterHunter.PROPERTIES.getProperty("MapFindFolder")
                        + mazeList.getSelectionModel().getSelectedItem() + ".csv");
                MainView.this.close();
            }
        });

        this.setTitle(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("StageTitle"));
        this.setScene(new Scene(root, 300, 300));
    }

    /* getter */
    public Maze getMaze() {
        return this.maze;
    }

    public boolean isMonsterIsAnAI() {
        return checkBoxMonsterIsAnAI.isSelected();
    }

    public boolean isHunterIsAnAI() {
        return checkBoxHunterISAnAi.isSelected();
    }

    public boolean isMultyGame() {
        return checkBoxIsMultyGame.isSelected();
    }

    public boolean isHost() {
        return checkBoxIsServer.isSelected();
    }

    public boolean isGeneratedMap() {
        return checkBoxIsGeneratedMap.isSelected();
    }

    public boolean isShowBearingWall() {
        return checkBoxShowBearingWall.isSelected();
    }

    /* init method */
    private void initMenu() {
        rootMenu = new Pane();
        paneMenu = new Pane();
        titleMenu = new Text(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("titleMenu"));
        playButtonMenu = new Button(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("playButtonMenu"));
        parameterButtonMenu = new Button(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("parameterButtonMenu"));
        paneMenu.getChildren().addAll(titleMenu);

        rootMenu.getChildren().addAll(paneMenu, parameterButtonMenu);

        sceneMenu = new Scene(rootMenu);
    }

    private void initParameter() {
        rootParameter = new HBox();

        quitParameter = new Button(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("quitParameter"));

        mapVBoxParameter = new VBox();
        playerVBoxParameter = new VBox();
        languageVBoxParameter = new VBox();

        mapTitle = new Text(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("mapTitle"));
        playerTitle = new Text(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("playerTitle"));
        languageTitle = new Text(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("languageTitle"));

        checkBoxIsGeneratedMap = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("checkBoxIsGeneratedMap"));

        checkBoxIsMultyGame = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("checkBoxIsMultyGame"));
        checkBoxIsServer = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("checkBoxIsServer"));

        textServerAddress = new TextField(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("textServerAddress"));

        checkBoxMonsterIsAnAI = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("checkBoxMonsterIsAnAI"));
        checkBoxHunterISAnAi = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("checkBoxHunterISAnAi"));

    }

    private void initMaze() {
        // TODO faire le maze avec les paramètre generer ou map déja faite.
    }

    /* show method */

    public void showInitMenu() {
        this.setFullScreen(true);
        this.setFullScreenExitHint("");
        this.setScene(sceneMenu);

    }

}
