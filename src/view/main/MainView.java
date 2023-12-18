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
import javafx.scene.image.Image;
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
import util.data.DataProp;
import view.play.NumberInStringComparator;

public class MainView extends Stage {
    private final ObservableList<String> mazeListItems = FXCollections.observableArrayList();
    private final ObservableList<String> langListItems = FXCollections.observableArrayList();

    private Maze maze;

    private Font customFont;
    private Font mainFont;
    private Font normalFont;
    private Font litleFont;

    private Image panebackgroundImage = new Image(
            Paths.get(MonsterHunter.PROPERTIES.getProperty("MenuBckgImage")).toUri().toString());
    BackgroundImage panebackground = new BackgroundImage(
            panebackgroundImage,
            BackgroundRepeat.SPACE,
            BackgroundRepeat.SPACE,
            BackgroundPosition.CENTER,
            BackgroundSize.DEFAULT);

    Image buttonbackgroundImage = new Image(
            Paths.get(MonsterHunter.PROPERTIES.getProperty("ButtonTexture")).toUri().toString());
    BackgroundImage buttonbackground = new BackgroundImage(
            buttonbackgroundImage,
            BackgroundRepeat.SPACE,
            BackgroundRepeat.SPACE,
            BackgroundPosition.CENTER,
            BackgroundSize.DEFAULT);

    Image parameterChoiceMenuImage = new Image(
            Paths.get(MonsterHunter.PROPERTIES.getProperty("MenuOptionBkg")).toUri().toString());
    BackgroundImage parameterChoiceMenuBackgroundImage = new BackgroundImage(
            parameterChoiceMenuImage,
            BackgroundRepeat.SPACE,
            BackgroundRepeat.SPACE,
            BackgroundPosition.CENTER,
            BackgroundSize.DEFAULT);

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
    private Text mazeGeneratedTitleParameter;
    private Label mazeGeneratedSizeMapTitleParameter;// 1
    private Label mazeGeneratedLineNbTitleParameter;// 1
    private Slider mazeGeneratedSliderLineNbParameter;// 1
    private Label mazeGeneratedColumnNbTitleParameter;// 1
    private Slider mazeGeneratedColumnNbSliceParmeter;// 1
    private Label mazeGeneratedWallPercentTitleParameter;
    private Slider mazeGeneratedWallPercentSliderParameter;

    private VBox mazeGeneratedSizeVBoxParameter;
    private VBox mazeGeneratedWallPercentVBoxParameter;

    public MainView(File file) {
        this.setTitle(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("StageTitle"));

        initFont();
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
        titleMenu.setFill(Color.rgb(100, 41, 0));

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.rgb(128, 0, 0));
        titleMenu.setEffect(dropShadow);
        titleMenu.setStyle("-fx-padding: 0;");

        paneMenu.setAlignment(javafx.geometry.Pos.CENTER);

        paneMenu.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight());
        paneMenu.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight());
        paneMenu.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());

        paneMenu.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth());
        paneMenu.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth());
        paneMenu.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());

        paneMenu.setBackground(new Background(panebackground));

        playButtonMenu = new Button(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("playButtonMenu"));

        playButtonMenu.setBackground(null);

        playButtonMenu.setFont(mainFont);
        playButtonMenu.setStyle("-fx-padding: 0;");

        parameterButtonMenu = new Button(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("parameterButtonMenu"));
        parameterButtonMenu.setFont(mainFont);
        parameterButtonMenu.setFont(new Font(20));
        parameterButtonMenu.setStyle("-fx-padding: 0;");

        playButtonMenu.setBackground(new Background(buttonbackground));
        parameterButtonMenu.setBackground(new Background(buttonbackground));
        parameterButtonMenu.setFont(litleFont);
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
        // playButtonMenu.setLayoutX(sceneMenu.widthProperty().doubleValue() / 2);

    }

    private void initParameter() {
        rootParameter = new VBox();
        hBoxParameter = new HBox();

        titleParameter = new Text(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("titleParameter"));
        titleParameter.setFont(customFont);
        titleParameter.setFill(Color.rgb(100, 41, 0));
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.rgb(128, 0, 0));
        titleParameter.setEffect(dropShadow);
        titleParameter.setStyle("-fx-padding: 0;");

        rootParameter.setBackground(new Background(panebackground));

        quitParameter = new Button(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("quitParameter"));

        quitParameter.setBackground(new Background(buttonbackground));
        quitParameter.setFont(litleFont);
        quitParameter.setOnAction(e -> {
            showInitMenu();
        });

        rootParameter.setAlignment(javafx.geometry.Pos.CENTER);
        mapVBoxParameter = new VBox();
        playerVBoxParameter = new VBox();
        languageVBoxParameter = new VBox();

        mapTitle = new Text(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("mapTitle"));
        mapTitle.setFont(normalFont);
        mapTitle.setStyle("-fx-text-fill: black;");

        checkBoxIsGeneratedMap = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("checkBoxIsGeneratedMap"));
        checkBoxIsGeneratedMap.setOnAction(e -> {
            mapListView.setDisable(checkBoxIsGeneratedMap.isSelected());
        });
        checkBoxIsGeneratedMap.setFont(litleFont);
        checkBoxIsGeneratedMap.setStyle("-fx-text-fill: black;");
        checkBoxIsGeneratedMap.setSelected(true);

        mapListView = initMapListView();
        mapListView.setDisable(checkBoxIsGeneratedMap.isSelected());

        checkBoxShowBearingWall = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("ShowBearingWall"));
        checkBoxShowBearingWall.setFont(litleFont);
        checkBoxShowBearingWall.setStyle("-fx-text-fill: black;");
        mapVBoxParameter.getChildren().addAll(mapTitle, checkBoxIsGeneratedMap, mapListView, checkBoxShowBearingWall);

        playerTitle = new Text(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("playerTitle"));
        checkBoxIsMultyGame = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("checkBoxIsMultyGame"));
        checkBoxIsServer = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("checkBoxIsServer"));
        textServerAddress = new TextField(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("textServerAddress"));
        textServerAddress.setDisable(checkBoxIsServer.isSelected());

        playerTitle.setFont(normalFont);

        checkBoxIsMultyGame.setFont(litleFont);
        checkBoxIsMultyGame.setStyle("-fx-text-fill: black;");
        checkBoxIsMultyGame.setOnAction(e -> {
            checkBoxIsServer.setDisable(!checkBoxIsMultyGame.isSelected());
        });

        checkBoxIsServer.setFont(litleFont);
        checkBoxIsServer.setStyle("-fx-text-fill: black;");
        checkBoxIsServer.setOnAction(e -> {
            textServerAddress.setDisable(checkBoxIsServer.isSelected());
        });
        checkBoxIsServer.setDisable(!checkBoxIsMultyGame.isSelected());

        textServerAddress.setFont(litleFont);
        textServerAddress.setStyle("-fx-text-fill: black;");

        checkBoxMonsterIsAnAI = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("checkBoxMonsterIsAnAI"));
        checkBoxMonsterIsAnAI.setFont(litleFont);
        checkBoxMonsterIsAnAI.setStyle("-fx-text-fill: black;");
        checkBoxMonsterIsAnAI.setOnAction(e -> {
            checkBoxHunterISAnAi.setDisable(checkBoxMonsterIsAnAI.isSelected());
        });

        checkBoxHunterISAnAi = new CheckBox(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("checkBoxHunterISAnAi"));
        checkBoxHunterISAnAi.setFont(litleFont);
        checkBoxHunterISAnAi.setStyle("-fx-text-fill: black;");
        checkBoxHunterISAnAi.setOnAction(e -> {
            checkBoxMonsterIsAnAI.setDisable(checkBoxHunterISAnAi.isSelected());
        });

        playerVBoxParameter.getChildren().addAll(playerTitle, checkBoxIsMultyGame, checkBoxIsServer, textServerAddress,
                checkBoxMonsterIsAnAI, checkBoxHunterISAnAi);

        languageTitle = new Text(MonsterHunter.MENU_LANGUAGE_FILE.getProperty("languageTitle"));
        languageTitle.setFont(normalFont);
        languageTitle.setStyle("-fx-text-fill: black;");

        langListView = initLangueListView();

        languageVBoxParameter.getChildren().addAll(languageTitle, langListView);

        setFileCheckBoxValue();

        playerVBoxParameter.setBackground(new Background(parameterChoiceMenuBackgroundImage));
        mapVBoxParameter.setBackground(new Background(parameterChoiceMenuBackgroundImage));
        languageVBoxParameter.setBackground(new Background(parameterChoiceMenuBackgroundImage));

        hBoxParameter.setAlignment(javafx.geometry.Pos.CENTER);
        playerVBoxParameter.setStyle("-fx-padding: 50;");
        mapVBoxParameter.setStyle("-fx-padding: 50;");
        languageVBoxParameter.setStyle("-fx-padding: 50;");

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
            Stage stage = new Stage();
            stage.setTitle("Génération de la map");

            Label label1 = new Label("Choisissez la taille de la map");
            Label label2 = new Label("Nombre de ligne");
            Slider slider = new Slider();
            slider.setMin(4);
            slider.setMax(12);
            slider.setValue(8);
            slider.setShowTickLabels(true);
            slider.setShowTickMarks(true);
            slider.setMajorTickUnit(2);

            Label label3 = new Label("Nombre de colonne");
            Slider slider2 = new Slider();
            slider2.setMin(4);
            slider2.setMax(12);
            slider2.setValue(8);
            slider2.setShowTickLabels(true);
            slider2.setShowTickMarks(true);
            slider2.setMajorTickUnit(2);

            Label label4 = new Label("Choisissez le pourcentage de mur");
            Slider slider3 = new Slider();
            slider3.setMin(0);
            slider3.setMax(100);
            slider3.setValue(50);
            slider3.setShowTickLabels(true);
            slider3.setShowTickMarks(true);
            slider3.setMajorTickUnit(10);

            Button button = new Button("Générer");

            button.setOnAction(e -> {
                this.maze = new Maze((int) slider.getValue(), (int) slider2.getValue(), (int) slider3.getValue());
                stage.close();
            });

            HBox hBox = new HBox(label2, slider);
            HBox hBox2 = new HBox(label3, slider2);
            VBox vBox = new VBox(label1, hBox, hBox2, label4, slider3, button);

            Scene scene = new Scene(vBox);
            stage.setMinHeight(300);
            stage.setMinWidth(300);
            stage.setScene(scene);
            stage.showAndWait();
            stage.setOnCloseRequest(e -> {
                initMenu();
            });
        }

        this.close();
    }

    private void initFont() {
        String fontPath = MonsterHunter.PROPERTIES.getProperty("StylePolice");
        Path absolutePath = Paths.get(fontPath);
        String absoluteFontPath = absolutePath.toUri().toString();
        customFont = Font.loadFont(absoluteFontPath, 200);
        mainFont = Font.loadFont(absoluteFontPath, 75);
        litleFont = Font.loadFont(absoluteFontPath, 20);
        normalFont = Font.loadFont(absoluteFontPath, 40);
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
        this.setScene(sceneMenu);
        this.setFullScreen(true);
        this.setFullScreenExitHint("");
    }

    public void showParameterMenu() {
        this.setScene(sceneParameter);
        this.setFullScreen(true);
        this.setFullScreenExitHint("");
    }

    public boolean getHunterIsAnAi() {
        return checkBoxHunterISAnAi.isSelected();
    }

    public boolean getMonsterIsAnAI() {
        return checkBoxMonsterIsAnAI.isSelected();
    }
}
