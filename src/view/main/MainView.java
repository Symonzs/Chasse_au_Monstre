package view.main;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.MonsterHunter;
import model.Maze;
import view.play.NumberInStringComparator;

public class MainView extends Stage {
    private final ObservableList<String> mazeListItems = FXCollections.observableArrayList();
    private Maze maze;

    private Font customFont;
    private Font mainFont;

    /* Main Menu */
    private Pane rootMenu;
    private VBox paneMenu;
    private Text titleMenu;
    private Button playButtonMenu;
    private Button parameterButtonMenu;
    private Scene sceneMenu;

    /* Parameter Menu */
    private Scene sceneParameter;
    private HBox rootParameter;
    private Button quitParameter;
    private VBox mapVBoxParameter;
    private VBox playerVBoxParameter;
    private VBox languageVBoxParameter;
    private ListView<String> mapListView;
    private Text mapTitle;
    private Text playerTitle;
    private Text languageTitle;
    private TextField textServerAddress;
    private CheckBox checkBoxIsGeneratedMap;
    private CheckBox checkBoxIsMultyGame;
    private CheckBox checkBoxIsServer;
    private CheckBox checkBoxMonsterIsAnAI;
    private CheckBox checkBoxHunterISAnAi;
    private CheckBox checkBoxShowBearingWall;

    public MainView(File file) {
        System.out.println(MonsterHunter.PROPERTIES.getProperty("StylePolice"));
        System.out.println(getClass().getResourceAsStream(MonsterHunter.PROPERTIES.getProperty("StylePolice")));
        this.setTitle(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("StageTitle"));

        initFont();
        System.out.println(customFont);
        initMenu();
        initParameter();

        showInitMenu();
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

    public String getServerAddress() {
        return textServerAddress.getText();
    }

    /* init method */
    private void initMenu() {
        rootMenu = new Pane();
        paneMenu = new VBox();
        titleMenu = new Text(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("titleMenu"));
        titleMenu.setFont(customFont);
        titleMenu.setFill(Color.rgb(88, 41, 0));

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.rgb(128, 0, 0));
        titleMenu.setEffect(dropShadow);

        paneMenu.setAlignment(javafx.geometry.Pos.CENTER);

        paneMenu.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight());
        paneMenu.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight());
        paneMenu.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());

        paneMenu.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth());
        paneMenu.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth());
        paneMenu.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());

        Image backgroundImage = new Image(
                Paths.get(MonsterHunter.PROPERTIES.getProperty("MenuBckgImage")).toUri().toString());
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT, // Pas de répétition de l'image
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        paneMenu.setBackground(new Background(background));
        playButtonMenu = new Button(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("playButtonMenu"));

        playButtonMenu.setFont(mainFont);
        playButtonMenu.setStyle("-fx-padding: 0;");

        parameterButtonMenu = new Button(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("parameterButtonMenu"));
        parameterButtonMenu.setFont(mainFont);
        parameterButtonMenu.setFont(new Font(20));
        parameterButtonMenu.setStyle("-fx-padding: 0;");

        paneMenu.getChildren().addAll(titleMenu, playButtonMenu);

        rootMenu.getChildren().addAll(paneMenu, parameterButtonMenu);

        sceneMenu = new Scene(rootMenu);
        // paneMenu.prefWidthProperty().bind(sceneMenu.widthProperty());
    }

    private void initParameter() {
        rootParameter = new HBox();

        quitParameter = new Button(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("quitParameter"));

        mapVBoxParameter = new VBox();
        playerVBoxParameter = new VBox();
        languageVBoxParameter = new VBox();

        mapTitle = new Text(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("mapTitle"));
        checkBoxIsGeneratedMap = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("checkBoxIsGeneratedMap"));
        mapListView = initMapListView();
        mapListView.setDisable(!checkBoxIsGeneratedMap.isSelected());
        checkBoxShowBearingWall = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("ShowBearingWall"));
        mapVBoxParameter.getChildren().addAll(mapTitle, checkBoxIsGeneratedMap, mapListView);

        playerTitle = new Text(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("playerTitle"));
        checkBoxIsMultyGame = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("checkBoxIsMultyGame"));
        checkBoxIsServer = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("checkBoxIsServer"));
        textServerAddress = new TextField(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("textServerAddress"));
        textServerAddress.setDisable(!checkBoxIsServer.isSelected());

        checkBoxMonsterIsAnAI = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("checkBoxMonsterIsAnAI"));
        checkBoxHunterISAnAi = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("checkBoxHunterISAnAi"));

        playerVBoxParameter.getChildren().addAll(playerTitle, checkBoxIsMultyGame, textServerAddress, checkBoxIsServer,
                checkBoxMonsterIsAnAI, checkBoxHunterISAnAi);

        languageTitle = new Text(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("languageTitle"));

        languageVBoxParameter.getChildren().add(languageTitle);

        setFileCheckBoxValue();

        rootParameter = new HBox(mapVBoxParameter, playerVBoxParameter, languageVBoxParameter);
        sceneParameter = new Scene(rootParameter);
    }

    private void initMaze() {
        // TODO faire le maze avec les paramètre generer ou map déja faite.
    }

    private void initFont() {
        String fontPath = MonsterHunter.PROPERTIES.getProperty("StylePolice");
        Path absolutePath = Paths.get(fontPath);
        String absoluteFontPath = absolutePath.toUri().toString();
        customFont = Font.loadFont(absoluteFontPath, 200);
        mainFont = Font.loadFont(absoluteFontPath, 75);

    }

    private ListView<String> initMapListView() {
        ListView<String> mazeList = new ListView<>();
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

        return mazeList;
    }

    private void setFileCheckBoxValue() {
        checkBoxHunterISAnAi.setSelected(MonsterHunter.PROPERTIES.getProperty("HunterISAnAi").equals("true"));
        checkBoxMonsterIsAnAI.setSelected(MonsterHunter.PROPERTIES.getProperty("MonsterIsAnAI").equals("true"));
        checkBoxIsGeneratedMap.setSelected(MonsterHunter.PROPERTIES.getProperty("IsGeneratedMap").equals("true"));
        checkBoxIsMultyGame.setSelected(MonsterHunter.PROPERTIES.getProperty("IsMultyGame").equals("true"));
        checkBoxIsServer.setSelected(MonsterHunter.PROPERTIES.getProperty("IsServer").equals("true"));
        checkBoxShowBearingWall.setSelected(MonsterHunter.PROPERTIES.getProperty("ShowBearingWall").equals("true"));

    }
    /* show method */

    public void showInitMenu() {
        this.setFullScreen(true);
        this.setFullScreenExitHint("");
        this.setScene(sceneMenu);
    }

}
