package io.deeplay.camp;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import utils.ViewNavigator;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        installAnything();
        ViewNavigator.setMainStage(primaryStage);
        ViewNavigator.loadView(getClass().getResource("/io/deeplay/camp/view/MainMenuView.fxml"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void installAnything() {
        Font.loadFont(getClass().getResourceAsStream("/io/deeplay/camp/view/resources/JustMeAgainDownHere.ttf"), 96);
        Font.loadFont(getClass().getResourceAsStream("/io/deeplay/camp/view/resources/Julee-Regular.ttf"), 96);
    }
}