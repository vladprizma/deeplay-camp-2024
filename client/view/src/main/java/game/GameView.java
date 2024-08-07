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
import sigletonobserver.ChatString;

import java.util.List;

public class GameView {
    @FXML
    private Text text1;

    @FXML
    private Text text2;

    private static GameView instance;

    private int count1 = 2;
    private int count2 = 2;

    private int[][] board;

    public static ModelManager modelManager;

    // Создаем RadialGradient для зеленой фишки
    RadialGradient radialGradientGreen = new RadialGradient(0, 0, 0.5, 0.5, 0.8, true,
            CycleMethod.NO_CYCLE, new Stop(0, Color.WHITE), new Stop(1, Color.GREEN));

    // Создаем RadialGradient для красной фишки
    RadialGradient radialGradientRed = new RadialGradient(0, 0, 0.5, 0.5, 0.8, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.WHITE), new Stop(1, Color.RED));

    // Создаем DropShadow
    DropShadow dropShadow = new DropShadow();

    @FXML
    private GridPane gridPane; // связываем с FXML
    int player = 1;



    // Конструктор GameView и ее Инстанс
    public GameView() {}

    public static synchronized GameView getInstance() {
        if (instance == null) {
            instance = new GameView();
        }
        return instance;
    }

    // Обновление фишек на доске

    public void updateBoard(String newBoard) {
        String[] boardRows = newBoard.split("\\s+"); // Разделяем строки доски по пробелам
        int[][] stringToBoard = new int[8][8]; // Создаем массив для доски

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                char cell = boardRows[i * 8 + j].charAt(0); // Получаем символ из строки для каждой ячейки
                if (cell == '.') {
                    stringToBoard[i][j] = 0; // Если символ ".", присваиваем значение 0
                } else if (cell == '0') {
                    stringToBoard[i][j] = 1; // Если символ "0", присваиваем значение 1
                } else if (cell == 'X') {
                    stringToBoard[i][j] = -1; // Если символ "X", присваиваем значение -1
                }
            }
        }
        this.board = stringToBoard;
        placeInitialChips();
        //initializeButtons();
    }

    // Метод для инициализации кнопок
    @FXML
    public void initialize() {
        modelManager = MainMenuView.getModelManager();
        modelManager.boardModelMethod();
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
        Circle Chip = null;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++){
                if(board[row][col] != 0){
                    if (board[row][col] == 1){
                        Chip = createChip(radialGradientGreen, dropShadow);

                    } else if (board[row][col] == -1){
                        Chip = createChip(radialGradientGreen, dropShadow);
                    }
                    gridPane.add(createStackPane(Chip), col, row);
                }

            }
        }
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
}