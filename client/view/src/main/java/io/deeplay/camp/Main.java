package io.deeplay.camp;

import javafx.application.Application;
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
    }
}