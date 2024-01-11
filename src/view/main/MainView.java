package view.main;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
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
import util.data.DataProp;
import util.style.MainStyle;
import view.play.NumberInStringComparator;

public class MainView extends Stage {
    private final ObservableList<String> mazeListItems = FXCollections.observableArrayList();
    private final ObservableList<String> langListItems = FXCollections.observableArrayList();

    private Maze maze;

    /* Main Menu */
    private Pane rootMenu;
    private VBox paneMenu;
    private Text titleMenu;
    private Button playButtonMenu;
    private Button parameterButtonMenu;
    private Scene sceneMenu;

    /* Parameter Menu */
    private Scene sceneParameter;
    private VBox rootParameter;
    private HBox hBoxParameter;
    private Button quitParameter;
    private Text titleParameter;
    private VBox mapVBoxParameter;
    private VBox playerVBoxParameter;
    private VBox languageVBoxParameter;
    private ListView<String> mapListView;
    private Button generatedMapButtonParameter;
    private Text mapTitle;
    private Text playerTitle;
    private Text languageTitle;
    private ListView<String> langListView;
    private TextField textServerAddress;
    private CheckBox checkBoxIsGeneratedMap;
    private CheckBox checkBoxIsMultyGame;
    private CheckBox checkBoxIsServer;
    private CheckBox checkBoxMonsterIsAnAI;
    private CheckBox checkBoxHunterISAnAi;
    private CheckBox checkBoxShowBearingWall;

    /* generated maze setting */
    private Scene mazeGeneratedSceneParameter;
    private VBox rootMazeGeneratorParameter;

    private Button mazeGeneratedGoBackButtonParameter; // 0
    private Text mazeGeneratedTitleParameter;// 0

    private Label mazeGeneratedSizeMapTitleParameter;// 1
    private Label mazeGeneratedLineNbTitleParameter;// 1
    private Slider mazeGeneratedSliderLineNbParameter;// 1
    private Label mazeGeneratedColumnNbTitleParameter;// 1
    private Slider mazeGeneratedColumnNbSliceParmeter;// 1

    private Label mazeGeneratedWallPercentTitleParameter;// 2
    private Slider mazeGeneratedWallPercentSliderParameter;// 2
    private CheckBox mazeGeneratedIsGermanCheckBoxParameter;// 2

    private HBox mazeGeneratorHbox;
    private VBox mazeGeneratedSizeVBoxParameter;// 1
    private VBox mazeGeneratedWallPercentVBoxParameter;// 2

    public MainView(File file) {
        this.setTitle(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("StageTitle"));

        initMenu();
        initParameter();
        initMazeGeneratorParamter();

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
        titleMenu.setFont(MainStyle.customFont);
        titleMenu.setFill(Color.rgb(100, 41, 0));

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.rgb(0, 0, 0));
        titleMenu.setEffect(dropShadow);
        titleMenu.setStyle("-fx-padding: 0;");

        paneMenu.setAlignment(javafx.geometry.Pos.CENTER);

        paneMenu.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight());
        paneMenu.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight());
        paneMenu.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());

        paneMenu.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth());
        paneMenu.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth());
        paneMenu.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());

        paneMenu.setBackground(new Background(MonsterHunter.mainbackgroundImage));

        playButtonMenu = new Button(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("playButtonMenu"));

        MainStyle.applyMainButtonStyle(playButtonMenu);

        parameterButtonMenu = new Button(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("parameterButtonMenu"));
        MainStyle.applyLitleButtonStyle(parameterButtonMenu);

        parameterButtonMenu.setLayoutX(1650);

        playButtonMenu.setOnAction(e -> {
            initMaze();
        });

        parameterButtonMenu.setOnAction(e -> {
            showParameterMenu();
        });

        paneMenu.getChildren().addAll(titleMenu, playButtonMenu);
        rootMenu.getChildren().addAll(paneMenu, parameterButtonMenu);
        sceneMenu = new Scene(rootMenu);

    }

    private void initParameter() {
        rootParameter = new VBox();
        hBoxParameter = new HBox();

        titleParameter = new Text(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("titleParameter"));
        MainStyle.applyTitleStyle(titleParameter);

        rootParameter.setBackground(new Background(MonsterHunter.mainbackgroundImage));

        quitParameter = new Button(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("quitParameter"));
        MainStyle.applyNormalButtonStyle(quitParameter);

        quitParameter.setOnAction(e -> {
            showInitMenu();
        });

        rootParameter.setAlignment(javafx.geometry.Pos.CENTER);
        mapVBoxParameter = new VBox();
        playerVBoxParameter = new VBox();
        languageVBoxParameter = new VBox();

        mapTitle = new Text(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("mapTitle"));
        MainStyle.applyMainTextStyle(mapTitle);

        checkBoxIsGeneratedMap = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("checkBoxIsGeneratedMap"));
        checkBoxIsGeneratedMap.setOnAction(e -> {
            mapListView.setDisable(checkBoxIsGeneratedMap.isSelected());
        });

        MainStyle.applyCheckBoxStyle(checkBoxIsGeneratedMap);

        checkBoxIsGeneratedMap.setSelected(true);

        mapListView = initMapListView();
        mapListView.setDisable(checkBoxIsGeneratedMap.isSelected());
        generatedMapButtonParameter = new Button(
                MonsterHunter.MENU_LANGUAGE_FILE.getProperty("generatedMapButtonParameter"));
        generatedMapButtonParameter.setDisable(!mapListView.isDisable());

        MainStyle.applyLitleButtonStyle(generatedMapButtonParameter);

        generatedMapButtonParameter.setOnAction(e -> {
            showGeneratedParameterMenu();
        });

        checkBoxShowBearingWall = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("ShowBearingWall"));

        MainStyle.applyCheckBoxStyle(checkBoxShowBearingWall);

        mapVBoxParameter.getChildren().addAll(mapTitle, checkBoxIsGeneratedMap, mapListView, checkBoxShowBearingWall,
                generatedMapButtonParameter);

        playerTitle = new Text(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("playerTitle"));
        checkBoxIsMultyGame = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("checkBoxIsMultyGame"));
        checkBoxIsServer = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("checkBoxIsServer"));
        textServerAddress = new TextField(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("textServerAddress"));
        textServerAddress.setDisable(checkBoxIsServer.isSelected());

        MainStyle.applyMainTextStyle(playerTitle);

        MainStyle.applyCheckBoxStyle(checkBoxIsMultyGame);

        checkBoxIsMultyGame.setOnAction(e -> {
            checkBoxIsServer.setDisable(!checkBoxIsMultyGame.isSelected());
        });

        MainStyle.applyCheckBoxStyle(checkBoxIsServer);

        checkBoxIsServer.setOnAction(e -> {
            textServerAddress.setDisable(checkBoxIsServer.isSelected());
        });
        checkBoxIsServer.setDisable(!checkBoxIsMultyGame.isSelected());

        MainStyle.applyTextFieldStyle(textServerAddress);

        checkBoxMonsterIsAnAI = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("checkBoxMonsterIsAnAI"));

        MainStyle.applyCheckBoxStyle(checkBoxMonsterIsAnAI);

        checkBoxMonsterIsAnAI.setOnAction(e -> {
            checkBoxHunterISAnAi.setDisable(checkBoxMonsterIsAnAI.isSelected());
        });

        checkBoxHunterISAnAi = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("checkBoxHunterISAnAi"));

        MainStyle.applyCheckBoxStyle(checkBoxHunterISAnAi);

        checkBoxHunterISAnAi.setOnAction(e -> {
            checkBoxMonsterIsAnAI.setDisable(checkBoxHunterISAnAi.isSelected());
        });

        playerVBoxParameter.getChildren().addAll(playerTitle, checkBoxIsMultyGame, checkBoxIsServer, textServerAddress,
                checkBoxMonsterIsAnAI, checkBoxHunterISAnAi);

        languageTitle = new Text(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("languageTitle"));

        MainStyle.applyMainTextStyle(languageTitle);

        langListView = initLangueListView();

        languageVBoxParameter.getChildren().addAll(languageTitle, langListView);

        setFileCheckBoxValue();

        playerVBoxParameter.setBackground(new Background(MonsterHunter.choiceMenuBackgroundImage));
        mapVBoxParameter.setBackground(new Background(MonsterHunter.choiceMenuBackgroundImage));
        languageVBoxParameter.setBackground(new Background(MonsterHunter.choiceMenuBackgroundImage));

        hBoxParameter.setAlignment(javafx.geometry.Pos.CENTER);
        playerVBoxParameter.setStyle("-fx-padding: 10;");
        mapVBoxParameter.setStyle("-fx-padding: 10;");
        languageVBoxParameter.setStyle("-fx-padding: 10;");

        hBoxParameter.getChildren().addAll(mapVBoxParameter, playerVBoxParameter, languageVBoxParameter);
        rootParameter.getChildren().addAll(quitParameter, titleParameter, hBoxParameter);
        sceneParameter = new Scene(rootParameter);
    }

    private void initMaze() {
        if (!isGeneratedMap()) {
            try {
                this.maze = new Maze(mapListView.getSelectionModel().getSelectedItem() + ".csv");
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.showAndWait();
                return;
            }
        } else {

            this.maze = new Maze((int) mazeGeneratedSliderLineNbParameter.getValue(),
                    (int) mazeGeneratedColumnNbSliceParmeter.getValue(),
                    (int) mazeGeneratedWallPercentSliderParameter.getValue());

        }

        this.close();
    }

    private void initMazeGeneratorParamter() {

        rootMazeGeneratorParameter = new VBox();
        rootMazeGeneratorParameter.setAlignment(javafx.geometry.Pos.CENTER);

        rootMazeGeneratorParameter.setBackground(new Background(MonsterHunter.mainbackgroundImage));

        mazeGeneratedGoBackButtonParameter = new Button(
                MonsterHunter.MENU_LANGUAGE_FILE.getProperty("mazeGeneratedGoBackButtonParameter"));

        MainStyle.applyNormalButtonStyle(mazeGeneratedGoBackButtonParameter);

        mazeGeneratedGoBackButtonParameter.setBackground(new Background(MonsterHunter.buttonbackground));
        mazeGeneratedGoBackButtonParameter.setOnAction(e -> {
            showParameterMenu();
        });

        mazeGeneratedTitleParameter = new Text(
                MonsterHunter.MENU_LANGUAGE_FILE.getProperty("mazeGeneratedTitleParameter"));

        MainStyle.applyTitleStyle(mazeGeneratedTitleParameter);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.rgb(0, 0, 0));
        mazeGeneratedTitleParameter.setEffect(dropShadow);
        mazeGeneratedTitleParameter.setStyle("-fx-padding: 0;");

        mazeGeneratedSizeVBoxParameter = new VBox();
        mazeGeneratedSizeVBoxParameter.setStyle("-fx-padding: 10;");

        mazeGeneratedSizeVBoxParameter.setBackground(new Background(MonsterHunter.choiceMenuBackgroundImage));

        mazeGeneratedSizeMapTitleParameter = new Label(
                MonsterHunter.MENU_LANGUAGE_FILE.getProperty("mazeGeneratedSizeMapTitleParameter"));

        MainStyle.applyNormalLabelStyle(mazeGeneratedSizeMapTitleParameter);

        mazeGeneratedLineNbTitleParameter = new Label(
                MonsterHunter.MENU_LANGUAGE_FILE.getProperty("mazeGeneratedLineNbTitleParameter"));

        MainStyle.applyLitleLabelStyle(mazeGeneratedLineNbTitleParameter);

        mazeGeneratedSliderLineNbParameter = new Slider();
        mazeGeneratedSliderLineNbParameter.setMin(Integer
                .parseInt(MonsterHunter.PROPERTIES.getProperty("mazeGeneratedSliderLineNbParameterMinValue")));
        mazeGeneratedSliderLineNbParameter.setMax(Integer
                .parseInt(MonsterHunter.PROPERTIES.getProperty("mazeGeneratedSliderLineNbParameterMaxValue")));
        mazeGeneratedSliderLineNbParameter.setValue(
                Integer.parseInt(MonsterHunter.PROPERTIES.getProperty("mazeGeneratedSliderLineNbParameterValue")));

        mazeGeneratedSliderLineNbParameter.setMajorTickUnit(2);
        mazeGeneratedSliderLineNbParameter.setShowTickLabels(true);
        mazeGeneratedSliderLineNbParameter.setShowTickMarks(true);

        mazeGeneratedColumnNbTitleParameter = new Label(
                MonsterHunter.MENU_LANGUAGE_FILE.getProperty("mazeGeneratedColumnNbTitleParameter"));

        MainStyle.applyLitleLabelStyle(mazeGeneratedColumnNbTitleParameter);

        mazeGeneratedColumnNbSliceParmeter = new Slider();

        mazeGeneratedColumnNbSliceParmeter.setMin(Integer
                .parseInt(MonsterHunter.PROPERTIES.getProperty("mazeGeneratedColumnNbSliceParmeterMinValue")));
        mazeGeneratedColumnNbSliceParmeter.setMax(Integer
                .parseInt(MonsterHunter.PROPERTIES.getProperty("mazeGeneratedColumnNbSliceParmeterMaxValue")));
        mazeGeneratedColumnNbSliceParmeter.setValue(
                Integer.parseInt(MonsterHunter.PROPERTIES.getProperty("mazeGeneratedColumnNbSliceParmeterValue")));

        mazeGeneratedColumnNbSliceParmeter.setShowTickLabels(true);
        mazeGeneratedColumnNbSliceParmeter.setShowTickMarks(true);
        mazeGeneratedColumnNbSliceParmeter.setMajorTickUnit(2);

        mazeGeneratedSizeVBoxParameter.getChildren().addAll(mazeGeneratedSizeMapTitleParameter,
                mazeGeneratedLineNbTitleParameter, mazeGeneratedSliderLineNbParameter,
                mazeGeneratedColumnNbTitleParameter, mazeGeneratedColumnNbSliceParmeter);

        mazeGeneratedWallPercentVBoxParameter = new VBox();
        mazeGeneratedWallPercentVBoxParameter.setStyle("-fx-padding: 10;");
        mazeGeneratedWallPercentVBoxParameter.setBackground(new Background(MonsterHunter.choiceMenuBackgroundImage));
        mazeGeneratedWallPercentTitleParameter = new Label(
                MonsterHunter.MENU_LANGUAGE_FILE.getProperty("mazeGeneratedWallPercentTitleParameter"));

        MainStyle.applyNormalLabelStyle(mazeGeneratedWallPercentTitleParameter);

        mazeGeneratedWallPercentSliderParameter = new Slider();
        mazeGeneratedWallPercentSliderParameter.setMin(0);
        mazeGeneratedWallPercentSliderParameter.setMax(100);
        mazeGeneratedWallPercentSliderParameter
                .setValue(Integer.parseInt(
                        MonsterHunter.PROPERTIES.getProperty("mazeGeneratedWallPercentSliderParameterValue")));
        mazeGeneratedWallPercentSliderParameter.setShowTickLabels(true);
        mazeGeneratedWallPercentSliderParameter.setShowTickMarks(true);
        mazeGeneratedWallPercentSliderParameter.setMajorTickUnit(10);

        mazeGeneratedIsGermanCheckBoxParameter = new CheckBox(
                MonsterHunter.MENU_LANGUAGE_FILE.getProperty("mazeGeneratedIsGermanCheckBoxParameter"));
        mazeGeneratedIsGermanCheckBoxParameter
                .setSelected(Boolean.parseBoolean(MonsterHunter.PROPERTIES.getProperty("IsGermanMaze")));

        MainStyle.applyCheckBoxStyle(mazeGeneratedIsGermanCheckBoxParameter);

        mazeGeneratedWallPercentVBoxParameter.getChildren().addAll(mazeGeneratedWallPercentTitleParameter,
                mazeGeneratedWallPercentSliderParameter, mazeGeneratedIsGermanCheckBoxParameter);

        mazeGeneratorHbox = new HBox();
        mazeGeneratorHbox.setAlignment(javafx.geometry.Pos.CENTER);

        mazeGeneratorHbox.getChildren().addAll(mazeGeneratedSizeVBoxParameter, mazeGeneratedWallPercentVBoxParameter);
        rootMazeGeneratorParameter.getChildren().addAll(mazeGeneratedGoBackButtonParameter,
                mazeGeneratedTitleParameter, mazeGeneratorHbox);
        mazeGeneratedSceneParameter = new Scene(rootMazeGeneratorParameter);
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

    private ListView<String> initLangueListView() {
        ListView<String> langList = new ListView<>();
        langList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Properties langvalue = DataProp
                .read(Paths.get(MonsterHunter.PROPERTIES.getProperty("LanguageSetting")).toFile());
        for (Object obj : langvalue.keySet()) {
            if (obj instanceof String) {
                String str = (String) obj;
                langListItems.add(str);
            }
        }
        langList.setPrefHeight(langListItems.size() * 26.0);
        langList.setMaxWidth(200);
        FXCollections.sort(langListItems, new NumberInStringComparator());
        langList.setItems(langListItems);

        return langList;
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
        showScenneInFullScreen(sceneMenu);
    }

    public void showParameterMenu() {
        showScenneInFullScreen(sceneParameter);
    }

    public void showGeneratedParameterMenu() {
        showScenneInFullScreen(mazeGeneratedSceneParameter);
    }

    private void showScenneInFullScreen(Scene scene) {
        setScene(scene);
        setFullScreen(true);
        setFullScreenExitHint("");
    }

    public boolean getHunterIsAnAi() {
        return checkBoxHunterISAnAi.isSelected();
    }

    public boolean getMonsterIsAnAI() {
        return checkBoxMonsterIsAnAI.isSelected();
    }
}
