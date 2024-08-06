package game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import mainmenu.MainMenuView;

import io.deeplay.camp.ModelManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import observer.Observer;

import java.util.List;

public class GameView implements Observer {
    @FXML
    private Rectangle redScore;

    @FXML
    private Rectangle greenScore;

    @FXML
    private Text text1;

    @FXML
    private Text text2;

    @FXML
    private Text timer1;

    @FXML
    private Text timer2;

    private Timeline timeline1;
    private Timeline timeline2;

    public static String splitRegex = "::";

    private int count1 = 2;
    private int count2 = 2;

    private long BlackChips;
    private long WhiteChips;

    public static ModelManager modelManager;

    // Создаем RadialGradient для зеленой фишки
    RadialGradient radialGradientGreen = new RadialGradient(0, 0, 0.5, 0.5, 0.8, true,
            CycleMethod.NO_CYCLE, new Stop(0, Color.WHITE), new Stop(1, Color.GREEN));

    // Создаем RadialGradient для красной фишки
    RadialGradient radialGradientRed = new RadialGradient(0, 0, 0.5, 0.5, 0.8, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.WHITE), new Stop(1, Color.RED));

    @FXML
    private GridPane gridPane; // связываем с FXML
    int player = 1;

    // Метод для инициализации кнопок
    @FXML
    public void initialize() {
        placeInitialChips();
        initializeButtons();
        modelManager = MainMenuView.getModelManager();
        modelManager.boardModelMethod();
    }

    //public void receiveMessageFromServer(String message) {
    //    GameView gameView = new GameView();
    //    gameView.processBoardInformation(message);
    //}

    public void processBoardInformation(String message) {
        if (message.startsWith("get-board::")) {
            String boardNotation = message.substring("get-board::".length());
            // Теперь у вас есть boardNotation, который можно использовать дальше
            System.out.println("Received board notation: " + boardNotation);
        }
    }

    private void initializeChips() {
        RadialGradient radialGradientGreen = new RadialGradient(0, 0, 0.5, 0.5, 0.8, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE),
                new Stop(1, Color.GREEN));
        RadialGradient radialGradientRed = new RadialGradient(0, 0, 0.5, 0.5, 0.8, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE),
                new Stop(1, Color.RED));

        // Размещаем фишки на доске
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Circle chip = new Circle(35);
                chip.setFill(radialGradientGreen);
                chip.setStroke(Color.BLACK);
                chip.setStrokeWidth(5);
                DropShadow dropShadow = new DropShadow();
                dropShadow.setColor(Color.WHITE);
                chip.setEffect(dropShadow);

                StackPane stackPane = new StackPane();
                stackPane.getChildren().add(chip);

                gridPane.add(stackPane, col, row);
            }
        }
    }

    private void initializeButtons() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Button button = new Button();
                button.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 3; -fx-pref-width: 100; -fx-pref-height: 100;");

                final int finalRow = row;
                final int finalCol = col;

                button.setOnAction(e -> handleButtonClick(button, finalRow, finalCol));

                gridPane.add(button, col, row);
            }
        }
    }

    private void handleButtonClick(Button button, int row, int col) {
        Circle chip = new Circle(35);
        if (player == 1) {
            chip.setFill(radialGradientGreen);
        } else {
            chip.setFill(radialGradientRed);
        }
        chip.setStroke(Color.BLACK);
        chip.setStrokeWidth(5);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.WHITE);
        chip.setEffect(dropShadow);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(chip);

        gridPane.getChildren().remove(button);
        gridPane.add(stackPane, col, row);

        if (player == 1) {
            player = 2;
        } else {
            player = 1;
        }

        if (player == 1) {
            count1++;
        } else {
            count2++;
        }
        text1.setText(Integer.toString(count1));
        text2.setText(Integer.toString(count2));
    }

    private void placeInitialChips() {
        // Создаем DropShadow
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.WHITE);

        // Создаем и настраиваем фишки
        Circle greenChip1 = createChip(radialGradientGreen, dropShadow);
        Circle greenChip2 = createChip(radialGradientGreen, dropShadow);
        Circle redChip1 = createChip(radialGradientRed, dropShadow);
        Circle redChip2 = createChip(radialGradientRed, dropShadow);

        // Размещаем фишки на GridPane
        gridPane.add(createStackPane(greenChip1), 3, 3); // (3,3)
        gridPane.add(createStackPane(greenChip2), 4, 4); // (4,4)
        gridPane.add(createStackPane(redChip1), 3, 4); // (3,4)
        gridPane.add(createStackPane(redChip2), 4, 3); // (4,3)
    }

    private Circle createChip(RadialGradient radialGradient, DropShadow dropShadow) {
        Circle chip = new Circle(35);
        chip.setFill(radialGradient);
        chip.setStroke(Color.BLACK);
        chip.setStrokeWidth(5);
        chip.setEffect(dropShadow);
        return chip;
    }

    private StackPane createStackPane(Circle chip) {
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(chip);
        return stackPane;
    }

    public void increaseRectangles() {
    }

    @FXML
    public void handleTimerButtonClicked() {
    }

    @Override
    public void update(String newString) {
        String command = newString.split(splitRegex)[0];
        switch (command) {
            case "get-board":
                System.out.println(newString + "smth");
                break;
        }
    }
}