package view.main;

import java.io.File;
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
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
    private final ObservableList<String> algoListItems = FXCollections.observableArrayList();

    private Maze maze;

    /* Main Menu */
    private Pane rootMenu;
    private VBox paneMenu;
    private Text titleMenu;
    private Button playButtonMenu;
    private Button parameterButtonMenu;
    private Button quitGameButtonMenu;
    private Scene sceneMenu;

    /* Parameter Menu */
    private Scene sceneParameter;
    private VBox rootParameter;
    private HBox hBoxParameter;
    private Button quitParameter;
    private Button resetByDefault;
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
    private CheckBox checkBoxIsGeneratedMap;
    private CheckBox checkBoxAllowDiagonalMove;
    private CheckBox checkBoxMonsterIsAnAI;
    private ListView<String> algoListView;
    private CheckBox checkBoxHunterIsAnAI;
    private CheckBox checkBoxcheckBoxWarFog;

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

    private HBox mazeGeneratorHbox;
    private VBox mazeGeneratedSizeVBoxParameter;// 1
    private VBox mazeGeneratedWallPercentVBoxParameter;// 2

    public MainView(File file) {
        this.setTitle(MonsterHunter.menuLanguageFile.getProperty("StageTitle"));

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
        return checkBoxHunterIsAnAI.isSelected();
    }

    public boolean isGeneratedMap() {
        return checkBoxIsGeneratedMap.isSelected();
    }

    public boolean isAllowDiagonalMove() {
        return checkBoxAllowDiagonalMove.isSelected();
    }

    public boolean ischeckBoxWarFog() {
        return checkBoxcheckBoxWarFog.isSelected();
    }

    /* init method */
    private void initMenu() {
        rootMenu = new Pane();
        paneMenu = new VBox();
        titleMenu = new Text(MonsterHunter.menuLanguageFile.getProperty("titleMenu"));
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

        paneMenu.setBackground(new Background(MainStyle.mainbackgroundImage));

        playButtonMenu = new Button(MonsterHunter.menuLanguageFile.getProperty("playButtonMenu"));
        MainStyle.applyMainButtonStyle(playButtonMenu);

        parameterButtonMenu = new Button(MonsterHunter.menuLanguageFile.getProperty("parameterButtonMenu"));
        MainStyle.applyLitleButtonStyle(parameterButtonMenu);

        parameterButtonMenu.setLayoutX(Screen.getPrimary().getVisualBounds().getWidth() - 250);

        quitGameButtonMenu = new Button(MonsterHunter.playLanguageFile.getProperty("playExitButton"));
        MainStyle.applyLitleButtonStyle(quitGameButtonMenu);

        quitGameButtonMenu.setLayoutX(Screen.getPrimary().getVisualBounds().getWidth() - 150);
        quitGameButtonMenu.setOnAction(e -> {
            System.exit(0);
        });

        playButtonMenu.setOnAction(e -> {
            initMaze();
        });

        parameterButtonMenu.setOnAction(e -> {
            showParameterMenu();
        });

        paneMenu.getChildren().addAll(titleMenu, playButtonMenu);
        rootMenu.getChildren().addAll(paneMenu, parameterButtonMenu, quitGameButtonMenu);
        sceneMenu = new Scene(rootMenu);

    }

    private void initParameter() {
        rootParameter = new VBox();
        hBoxParameter = new HBox();

        titleParameter = new Text(MonsterHunter.menuLanguageFile.getProperty("titleParameter"));
        MainStyle.applyTitleStyle(titleParameter);

        rootParameter.setBackground(new Background(MainStyle.mainbackgroundImage));
        rootParameter.setAlignment(javafx.geometry.Pos.CENTER);

        resetByDefault = new Button(MonsterHunter.menuLanguageFile.getProperty("resetByDefault"));
        MainStyle.applyLitleButtonStyle(resetByDefault);
        resetByDefault.setOnAction(e -> {
            reInitParameters();
            this.close();
        });

        quitParameter = new Button(MonsterHunter.menuLanguageFile.getProperty("quitParameter"));
        MainStyle.applyNormalButtonStyle(quitParameter);
        quitParameter.setOnAction(e -> {
            saveParameters();
            showInitMenu();
        });

        mapVBoxParameter = new VBox();
        playerVBoxParameter = new VBox();
        languageVBoxParameter = new VBox();

        mapTitle = new Text(MonsterHunter.menuLanguageFile.getProperty("mapTitle"));
        MainStyle.applyMainTextStyle(mapTitle);

        checkBoxIsGeneratedMap = new CheckBox(MonsterHunter.menuLanguageFile.getProperty("checkBoxIsGeneratedMap"));
        MainStyle.applyCheckBoxStyle(checkBoxIsGeneratedMap);
        checkBoxIsGeneratedMap.setOnAction(e -> {
            mapListView.setDisable(checkBoxIsGeneratedMap.isSelected());
        });
        checkBoxIsGeneratedMap.setSelected(true);

        mapListView = initMapListView();
        mapListView.setDisable(checkBoxIsGeneratedMap.isSelected());

        generatedMapButtonParameter = new Button(
                MonsterHunter.menuLanguageFile.getProperty("generatedMapButtonParameter"));
        MainStyle.applyLitleButtonStyle(generatedMapButtonParameter);
        generatedMapButtonParameter.setDisable(!mapListView.isDisable());
        generatedMapButtonParameter.setOnAction(e -> {
            showGeneratedParameterMenu();
        });

        checkBoxcheckBoxWarFog = new CheckBox(MonsterHunter.menuLanguageFile.getProperty("checkBoxWarFog"));
        MainStyle.applyCheckBoxStyle(checkBoxcheckBoxWarFog);

        mapVBoxParameter.getChildren().addAll(mapTitle, checkBoxIsGeneratedMap, mapListView, checkBoxcheckBoxWarFog,
                generatedMapButtonParameter);

        playerTitle = new Text(MonsterHunter.menuLanguageFile.getProperty("playerTitle"));
        MainStyle.applyMainTextStyle(playerTitle);

        checkBoxAllowDiagonalMove = new CheckBox(
                MonsterHunter.menuLanguageFile.getProperty("checkBoxAllowDiagonalMove"));
        MainStyle.applyCheckBoxStyle(checkBoxAllowDiagonalMove);

        checkBoxMonsterIsAnAI = new CheckBox(MonsterHunter.menuLanguageFile.getProperty("checkBoxMonsterIsAnAI"));
        MainStyle.applyCheckBoxStyle(checkBoxMonsterIsAnAI);
        checkBoxMonsterIsAnAI.setOnAction(e -> {
            algoListView.setDisable(!checkBoxMonsterIsAnAI.isSelected());
        });

        algoListView = initAlgorithmListView();

        checkBoxHunterIsAnAI = new CheckBox(MonsterHunter.menuLanguageFile.getProperty("checkBoxHunterIsAnAI"));
        MainStyle.applyCheckBoxStyle(checkBoxHunterIsAnAI);

        playerVBoxParameter.getChildren().addAll(playerTitle, checkBoxAllowDiagonalMove, checkBoxMonsterIsAnAI,
                algoListView, checkBoxHunterIsAnAI);

        languageTitle = new Text(MonsterHunter.menuLanguageFile.getProperty("languageTitle"));
        MainStyle.applyMainTextStyle(languageTitle);

        langListView = initLangueListView();

        languageVBoxParameter.getChildren().addAll(languageTitle, langListView);

        setFileCheckBoxValue();

        algoListView.setDisable(!checkBoxMonsterIsAnAI.isSelected());

        playerVBoxParameter.setBackground(new Background(MainStyle.choiceMenuBackgroundImage));
        mapVBoxParameter.setBackground(new Background(MainStyle.choiceMenuBackgroundImage));
        languageVBoxParameter.setBackground(new Background(MainStyle.choiceMenuBackgroundImage));

        hBoxParameter.setAlignment(javafx.geometry.Pos.CENTER);
        playerVBoxParameter.setStyle("-fx-padding: 10;");
        mapVBoxParameter.setStyle("-fx-padding: 10;");
        languageVBoxParameter.setStyle("-fx-padding: 10;");

        hBoxParameter.getChildren().addAll(mapVBoxParameter, playerVBoxParameter, languageVBoxParameter);
        rootParameter.getChildren().addAll(quitParameter, resetByDefault, titleParameter, hBoxParameter);
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

        rootMazeGeneratorParameter.setBackground(new Background(MainStyle.mainbackgroundImage));

        mazeGeneratedGoBackButtonParameter = new Button(
                MonsterHunter.menuLanguageFile.getProperty("mazeGeneratedGoBackButtonParameter"));

        MainStyle.applyNormalButtonStyle(mazeGeneratedGoBackButtonParameter);

        mazeGeneratedGoBackButtonParameter.setBackground(new Background(MainStyle.buttonbackground));
        mazeGeneratedGoBackButtonParameter.setOnAction(e -> {
            showParameterMenu();
        });

        mazeGeneratedTitleParameter = new Text(
                MonsterHunter.menuLanguageFile.getProperty("mazeGeneratedTitleParameter"));

        MainStyle.applyTitleStyle(mazeGeneratedTitleParameter);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.rgb(0, 0, 0));
        mazeGeneratedTitleParameter.setEffect(dropShadow);
        mazeGeneratedTitleParameter.setStyle("-fx-padding: 0;");

        mazeGeneratedSizeVBoxParameter = new VBox();
        mazeGeneratedSizeVBoxParameter.setStyle("-fx-padding: 10;");

        mazeGeneratedSizeVBoxParameter.setBackground(new Background(MainStyle.choiceMenuBackgroundImage));

        mazeGeneratedSizeMapTitleParameter = new Label(
                MonsterHunter.menuLanguageFile.getProperty("mazeGeneratedSizeMapTitleParameter"));

        MainStyle.applyNormalLabelStyle(mazeGeneratedSizeMapTitleParameter);

        mazeGeneratedLineNbTitleParameter = new Label(
                MonsterHunter.menuLanguageFile.getProperty("mazeGeneratedLineNbTitleParameter"));

        MainStyle.applyLitleLabelStyle(mazeGeneratedLineNbTitleParameter);

        mazeGeneratedSliderLineNbParameter = new Slider();
        mazeGeneratedSliderLineNbParameter.setMin(Integer
                .parseInt(MonsterHunter.init.getProperty("mazeGeneratedSliderLineNbParameterMinValue")));
        mazeGeneratedSliderLineNbParameter.setMax(Integer
                .parseInt(MonsterHunter.init.getProperty("mazeGeneratedSliderLineNbParameterMaxValue")));
        mazeGeneratedSliderLineNbParameter.setValue(
                Integer.parseInt(MonsterHunter.init.getProperty("mazeGeneratedSliderLineNbParameterValue")));

        mazeGeneratedSliderLineNbParameter.setSnapToTicks(true);
        mazeGeneratedSliderLineNbParameter.setMajorTickUnit(1);
        mazeGeneratedSliderLineNbParameter.setMinorTickCount(0);
        mazeGeneratedSliderLineNbParameter.setShowTickLabels(true);
        mazeGeneratedSliderLineNbParameter.setShowTickMarks(true);

        mazeGeneratedColumnNbTitleParameter = new Label(
                MonsterHunter.menuLanguageFile.getProperty("mazeGeneratedColumnNbTitleParameter"));

        MainStyle.applyLitleLabelStyle(mazeGeneratedColumnNbTitleParameter);

        mazeGeneratedColumnNbSliceParmeter = new Slider();

        mazeGeneratedColumnNbSliceParmeter.setMin(Integer
                .parseInt(MonsterHunter.init.getProperty("mazeGeneratedColumnNbSliceParmeterMinValue")));
        mazeGeneratedColumnNbSliceParmeter.setMax(Integer
                .parseInt(MonsterHunter.init.getProperty("mazeGeneratedColumnNbSliceParmeterMaxValue")));
        mazeGeneratedColumnNbSliceParmeter.setValue(
                Integer.parseInt(MonsterHunter.init.getProperty("mazeGeneratedColumnNbSliceParmeterValue")));

        mazeGeneratedColumnNbSliceParmeter.setSnapToTicks(true);
        mazeGeneratedColumnNbSliceParmeter.setMajorTickUnit(1);
        mazeGeneratedColumnNbSliceParmeter.setMinorTickCount(0);
        mazeGeneratedColumnNbSliceParmeter.setShowTickLabels(true);
        mazeGeneratedColumnNbSliceParmeter.setShowTickMarks(true);

        mazeGeneratedSizeVBoxParameter.getChildren().addAll(mazeGeneratedSizeMapTitleParameter,
                mazeGeneratedLineNbTitleParameter, mazeGeneratedSliderLineNbParameter,
                mazeGeneratedColumnNbTitleParameter, mazeGeneratedColumnNbSliceParmeter);

        mazeGeneratedWallPercentVBoxParameter = new VBox();
        mazeGeneratedWallPercentVBoxParameter.setStyle("-fx-padding: 10;");
        mazeGeneratedWallPercentVBoxParameter.setBackground(new Background(MainStyle.choiceMenuBackgroundImage));
        mazeGeneratedWallPercentTitleParameter = new Label(
                MonsterHunter.menuLanguageFile.getProperty("mazeGeneratedWallPercentTitleParameter"));

        MainStyle.applyNormalLabelStyle(mazeGeneratedWallPercentTitleParameter);

        mazeGeneratedWallPercentSliderParameter = new Slider();
        mazeGeneratedWallPercentSliderParameter.setMin(0);
        mazeGeneratedWallPercentSliderParameter.setMax(100);
        mazeGeneratedWallPercentSliderParameter
                .setValue(Integer.parseInt(
                        MonsterHunter.init.getProperty("mazeGeneratedWallPercentSliderParameterValue")));
        mazeGeneratedWallPercentSliderParameter.setSnapToTicks(true);
        mazeGeneratedWallPercentSliderParameter.setMajorTickUnit(10);
        mazeGeneratedWallPercentSliderParameter.setMinorTickCount(5);
        mazeGeneratedWallPercentSliderParameter.setShowTickLabels(true);
        mazeGeneratedWallPercentSliderParameter.setShowTickMarks(true);

        mazeGeneratedWallPercentVBoxParameter.getChildren().addAll(mazeGeneratedWallPercentTitleParameter,
                mazeGeneratedWallPercentSliderParameter);

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
        File resources = Paths.get(MonsterHunter.init.getProperty("MapFindFolder")).toFile();
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
                .read(Paths.get(MonsterHunter.init.getProperty("LanguageSetting")).toFile());
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
        langList.getSelectionModel().select(MonsterHunter.init.getProperty("LanguageValue"));

        return langList;
    }

    private ListView<String> initAlgorithmListView() {
        ListView<String> algoList = new ListView<>();
        algoList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        algoListItems.add("A*");
        algoListItems.add("Bidirectional A*");
        algoListItems.add("Theta*");
        algoList.setPrefHeight(algoListItems.size() * 26.0);
        algoList.setMaxWidth(200);
        FXCollections.sort(algoListItems, new NumberInStringComparator());

        algoList.setItems(algoListItems);
        algoList.getSelectionModel().select(MonsterHunter.init.getProperty("ChoosedAlgorithm"));

        return algoList;
    }

    private void setFileCheckBoxValue() {
        checkBoxAllowDiagonalMove.setSelected(MonsterHunter.init.getProperty("AllowDiagonalMove").equals("true"));
        checkBoxHunterIsAnAI.setSelected(MonsterHunter.init.getProperty("HunterIsAnAI").equals("true"));
        checkBoxMonsterIsAnAI.setSelected(MonsterHunter.init.getProperty("MonsterIsAnAI").equals("true"));
        checkBoxIsGeneratedMap.setSelected(MonsterHunter.init.getProperty("IsGeneratedMap").equals("true"));
        checkBoxcheckBoxWarFog.setSelected(MonsterHunter.init.getProperty("WarFog").equals("true"));
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
        return checkBoxHunterIsAnAI.isSelected();
    }

    public boolean getMonsterIsAnAI() {
        return checkBoxMonsterIsAnAI.isSelected();
    }

    private void reInitParameters() {
        MonsterHunter.init = DataProp.read(Paths.get("./resources/config/defaultinit.conf").toFile());
        System.out.println(MonsterHunter.init.getProperty("WarFog"));
        writeParameter();
    }

    private void writeParameter() {
        DataProp.write(MonsterHunter.init, MonsterHunter.INIT_FILE.getAbsolutePath(), "");
    }

    private void saveParameters() {
        System.out.println("saved | is null : " + MonsterHunter.init.getProperty("LanguageValue") == null);

        MonsterHunter.init.setProperty("LanguageValue", langListView.getSelectionModel().getSelectedItem());
        MonsterHunter.init.setProperty("IsGeneratedMap", checkBoxIsGeneratedMap.isSelected() + "");
        MonsterHunter.init.setProperty("MonsterIsAnAI", checkBoxMonsterIsAnAI.isSelected() + "");
        MonsterHunter.init.setProperty("ChoosedAlgorithm", algoListView.getSelectionModel().getSelectedItem());
        MonsterHunter.init.setProperty("HunterIsAnAI", checkBoxHunterIsAnAI.isSelected() + "");
        MonsterHunter.init.setProperty("WarFog", checkBoxcheckBoxWarFog.isSelected() + "");
        MonsterHunter.init.setProperty("AllowDiagonalMove", checkBoxAllowDiagonalMove.isSelected() + "");
        MonsterHunter.init.setProperty("mazeGeneratedSliderLineNbParameterMinValue",
                (int) mazeGeneratedSliderLineNbParameter.getMin() + "");
        MonsterHunter.init.setProperty("mazeGeneratedSliderLineNbParameterMaxValue",
                (int) mazeGeneratedSliderLineNbParameter.getMax() + "");
        MonsterHunter.init.setProperty("mazeGeneratedSliderLineNbParameterValue",
                (int) mazeGeneratedSliderLineNbParameter.getValue() + "");
        MonsterHunter.init.setProperty("mazeGeneratedColumnNbSliceParmeterMinValue",
                (int) mazeGeneratedColumnNbSliceParmeter.getMin() + "");
        MonsterHunter.init.setProperty("mazeGeneratedColumnNbSliceParmeterMaxValue",
                (int) mazeGeneratedColumnNbSliceParmeter.getMax() + "");
        MonsterHunter.init.setProperty("mazeGeneratedColumnNbSliceParmeterValue",
                (int) mazeGeneratedColumnNbSliceParmeter.getValue() + "");
        MonsterHunter.init.setProperty("mazeGeneratedWallPercentSliderParameterValue",
                (int) mazeGeneratedWallPercentSliderParameter.getValue() + "");
        writeParameter();
    }

    public boolean isWarFogIsOn() {
        return checkBoxcheckBoxWarFog.isSelected();
    }
}
