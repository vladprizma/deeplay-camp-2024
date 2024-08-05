package game;

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
import mainmenu.MainMenuView;

public class GameView {
    @FXML
    private GridPane gridPane; // связываем с FXML
    int player = 1;

    // Метод для инициализации кнопок
    @FXML
    public void initialize() {

        MainMenuView.getModelManager();

        // Проверка на null, если gridPane не инициализирован
        if (gridPane == null) {
            System.err.println("GridPane не инициализирован!");
            return;
        }

        RadialGradient radialGradientGreen = new RadialGradient(0, 0, 0.5, 0.5, 0.8, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE),
                new Stop(1, Color.GREEN));
        RadialGradient radialGradientRed = new RadialGradient(0, 0, 0.5, 0.5, 0.8, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE),
                new Stop(1, Color.RED));

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Button button = new Button();
                button.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 3; -fx-pref-width: 100; -fx-pref-height: 100;");

                // Делаем col и row финальными для использования внутри лямбда-выражения
                final int finalRow = row;
                final int finalCol = col;

                // Обработчик клика
                button.setOnAction(e -> {
                    Circle chip = new Circle(35); // радиус 35 для диаметра 70
                    if(player == 1){
                        chip.setFill(radialGradientGreen);
                    } else chip.setFill(radialGradientRed);

                    chip.setStroke(Color.BLACK);
                    chip.setStrokeWidth(5);

                    DropShadow dropShadow = new DropShadow();
                    dropShadow.setColor(Color.WHITE);
                    chip.setEffect(dropShadow);
                    StackPane stackPane = new StackPane();

                    // Добавить круг в StackPane
                    stackPane.getChildren().add(chip);

                    // Удаляем кнопку и добавляем StackPane с кругом
                    gridPane.getChildren().remove(button);
                    gridPane.add(stackPane, finalCol, finalRow); // используем финальные переменные

                    if(player == 1){
                        player = 2;
                    } else player = 1;
                });

                gridPane.add(button, col, row);
            }
        }
        // Размещение начальных фишек
        placeInitialChips();
    }

    private void placeInitialChips() {
        Circle greenChip1 = new Circle(35);
        Circle greenChip2 = new Circle(35);
        // Создание эффекта RadialGradient
        RadialGradient radialGradientGreen = new RadialGradient(0, 0, 0.5, 0.5, 0.8, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE),
                new Stop(1, Color.GREEN));

        greenChip1.setFill(radialGradientGreen);
        greenChip1.setStroke(Color.BLACK);
        greenChip1.setStrokeWidth(5);
        greenChip2.setFill(radialGradientGreen);
        greenChip2.setStroke(Color.BLACK);
        greenChip2.setStrokeWidth(5);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.WHITE);
        greenChip1.setEffect(dropShadow);
        greenChip2.setEffect(dropShadow);

        // Создаем зеленую фишку
        StackPane stackPane1 = new StackPane();
        stackPane1.getChildren().add(greenChip1);
        gridPane.add(stackPane1, 3, 3); // (3,3)

        StackPane stackPane2 = new StackPane();
        stackPane2.getChildren().add(greenChip2);
        gridPane.add(stackPane2, 4, 4); // (4,4)

        Circle redChip1 = new Circle(35);
        Circle redChip2 = new Circle(35);

        // Создание эффекта RadialGradient
        RadialGradient radialGradientRed = new RadialGradient(0, 0, 0.5, 0.5, 0.8, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE),
                new Stop(1, Color.RED));

        redChip1.setFill(radialGradientRed);
        redChip1.setStroke(Color.BLACK);
        redChip1.setStrokeWidth(5);
        redChip1.setEffect(dropShadow);
        redChip2.setFill(radialGradientRed);
        redChip2.setStroke(Color.BLACK);
        redChip2.setStrokeWidth(5);
        redChip2.setEffect(dropShadow);

        StackPane stackPane3 = new StackPane();
        stackPane3.getChildren().add(redChip1);
        gridPane.add(stackPane3, 3, 4); // (3,4)

        StackPane stackPane4 = new StackPane();
        stackPane4.getChildren().add(redChip2);
        gridPane.add(stackPane4, 4, 3); // (4,3)
    }
}