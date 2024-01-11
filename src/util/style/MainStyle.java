package util.style;

import java.nio.file.Paths;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.MonsterHunter;

public class MainStyle {

        public static Font customFont = Font
                        .loadFont(Paths.get(MonsterHunter.init.getProperty("StylePolice")).toUri().toString(),
                                        200);
        public static Font mainFont = Font
                        .loadFont(Paths.get(MonsterHunter.init.getProperty("StylePolice")).toUri().toString(),
                                        60);
        public static Font normalFont = Font
                        .loadFont(Paths.get(MonsterHunter.init.getProperty("StylePolice")).toUri().toString(),
                                        40);
        public static Font litleFont = Font
                        .loadFont(Paths.get(MonsterHunter.init.getProperty("StylePolice")).toUri().toString(),
                                        20);

        public static BackgroundImage mainbackgroundImage = new BackgroundImage(
                        new Image(
                                        Paths.get(MonsterHunter.init.getProperty("MenuBckgImage")).toUri()
                                                        .toString()),
                        BackgroundRepeat.SPACE,
                        BackgroundRepeat.SPACE,
                        BackgroundPosition.CENTER,
                        BackgroundSize.DEFAULT);

        public static BackgroundImage buttonbackground = new BackgroundImage(
                        new Image(
                                        Paths.get(MonsterHunter.init.getProperty("ButtonTexture")).toUri()
                                                        .toString()),
                        BackgroundRepeat.SPACE,
                        BackgroundRepeat.SPACE,
                        BackgroundPosition.CENTER,
                        BackgroundSize.DEFAULT);

        public static BackgroundImage choiceMenuBackgroundImage = new BackgroundImage(
                        new Image(
                                        Paths.get(MonsterHunter.init.getProperty("MenuOptionBkg")).toUri()
                                                        .toString()),
                        BackgroundRepeat.SPACE,
                        BackgroundRepeat.SPACE,
                        BackgroundPosition.CENTER,
                        BackgroundSize.DEFAULT);

        public static void applyMainButtonStyle(Button button) {
                button.setFont(mainFont);
                applyButtonStyle(button);
        }

        public static void applyNormalButtonStyle(Button button) {
                button.setFont(normalFont);
                applyButtonStyle(button);
        }

        public static void applyLitleButtonStyle(Button button) {
                button.setFont(litleFont);
                applyButtonStyle(button);
        }

        public static void applyCustomButtonStyle(Button button) {
                button.setFont(customFont);
                applyButtonStyle(button);
        }

        private static void applyButtonStyle(Button button) {
                button.setBackground(new Background(buttonbackground));
                button.setTextFill(Color.rgb(0, 0, 0));
                button.setStyle("-fx-padding: 2;");
                GridPane.setMargin(button, new javafx.geometry.Insets(10, 25, 10, 25)); // top, right, bottom, left

        }

        public static void applyTextFieldStyle(TextField textField) {
                textField.setFont(litleFont);
                textField.setStyle("-fx-text-fill: black;");
        }

        private static void applyTextStyle(Text text) {
                text.setStyle("-fx-text-fill: black;");
        }

        public static void applyLitleTextStyle(Text text) {
                text.setFont(litleFont);
                applyTextStyle(text);
        }

        public static void applyMainTextStyle(Text text) {
                text.setFont(mainFont);
                applyTextStyle(text);
        }

        public static void applyNormalTextStyle(Text text) {
                text.setFont(normalFont);
                applyTextStyle(text);
        }

        public static void applyCustomTextStyle(Text text) {
                text.setFont(customFont);
                applyTextStyle(text);
        }

        public static void applyNormalLabelStyle(Label label) {
                label.setFont(normalFont);
                applyLabelStyle(label);
        }

        public static void applyLitleLabelStyle(Label label) {
                label.setFont(litleFont);
                applyLabelStyle(label);
        }

        private static void applyLabelStyle(Label label) {
                label.setStyle("-fx-text-fill: black;");
        }

        public static void applyCheckBoxStyle(CheckBox checkBox) {
                checkBox.setFont(litleFont);
                checkBox.setStyle("-fx-text-fill: black;");
        }

        public static void applyTitleStyle(Text title) {
                title.setFont(customFont);
                title.setFill(Color.rgb(100, 41, 0));
                DropShadow dropShadow = new DropShadow();
                dropShadow.setOffsetX(3.0);
                dropShadow.setOffsetY(3.0);
                dropShadow.setColor(Color.rgb(0, 0, 0));
                title.setEffect(dropShadow);
                title.setStyle("-fx-padding: 0;");
        }
}
