package game;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import mainmenu.MainMenuView;
import io.deeplay.camp.ModelManager;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import observer.Observer;
import sigletonobserver.ChatString;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameView implements Observer {
    public static String splitRegex = "::";

    @FXML
    public GridPane mainBoard;

    @FXML
    private Text redScoreText;

    @FXML
    private Text greenScoreText;

    @FXML
    private Text redTimerText;

    @FXML
    private Text greenTimerText;

    @FXML
    private Text totalGameSessionText;

    @FXML
    private Text totalVictoriesText;

    @FXML
    private Text winRateText;

    private Timeline redTimer;
    private Timeline greenTimer;

    private int redTimeLeft = 120;
    private int greenTimeLeft = 120;

    private String move;

    public static ModelManager modelManager;
    private ChatString singleton;

    public GameView() {
        singleton = ChatString.getInstance();
    }

    @FXML
    public void initialize() {
        animation();
        singleton.registerObserver(this);
        modelManager = MainMenuView.getModelManager();
        modelManager.boardModelMethod();
        redTimer = createTimer(redTimerText, () -> redTimeLeft--, () -> redTimeLeft);
        greenTimer = createTimer(greenTimerText, () -> greenTimeLeft--, () -> greenTimeLeft);
    }

    private void animation() {
        for (Node node : mainBoard.getChildren()) {
            if (node instanceof Button) {
                setupGradientAnimation((Button) node);
            }
        }
    }

    private Timeline createTimer(Text timerText, Runnable onTick, Supplier<Integer> timeLeftSupplier) {
        return new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            onTick.run();
            Platform.runLater(() -> timerText.setText(formatTime(timeLeftSupplier.get())));
        }));
    }

    private String formatTime(int timeLeft) {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    private int extractCurrentPlayerId(String boardString) {
        String[] parts = boardString.split("::");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid board string format: " + boardString);
        }

        try {
            return Integer.parseInt(parts[parts.length - 1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid player ID format: " + parts[parts.length - 1], e);
        }
    }

    EventHandler<ActionEvent> timerFinishedHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            stopRedTimer(); // Остановка таймера
            makeRandomMove(); // Выполнение случайного хода
        }
    };

    private void startRedTimer(){
        if (redTimer.getStatus() == Timeline.Status.STOPPED) {
            redTimer.setCycleCount(Timeline.INDEFINITE);
            redTimer.play();
        }
        redTimer.setOnFinished(event -> {
            stopRedTimer();
            makeRandomMove();
        });
    }

    private void stopRedTimer() {
        if (redTimer.getStatus() == Timeline.Status.RUNNING) {
            redTimer.stop();
        }
    }

    private void startGreenTimer() {
        if (greenTimer.getStatus() == Timeline.Status.STOPPED) {
            greenTimer.setCycleCount(Timeline.INDEFINITE);
            greenTimer.play();
        }
        redTimer.setOnFinished(event -> {
            stopRedTimer();
            makeRandomMove();
        });
    }

    private void stopGreenTimer() {
        if (greenTimer.getStatus() == Timeline.Status.RUNNING) {
            greenTimer.stop();
        }
    }

    public void updateBoard(String boardState) {
        char[] boardArray = boardState.replaceAll(" ", "").toCharArray();
        System.out.println(boardState);
        for (int i = 0; i < boardArray.length; i++) {
            int row = i / 8;
            int col = i % 8;
            Button button = (Button) getNodeByRowColumnIndex(row, col, mainBoard);

            if (button != null) {
                switch (boardArray[i]) {
                    case '*':
                        Circle redCircle = new Circle(30, Color.rgb(255, 0, 0, 0.2));
                        redCircle.setStyle(
                                "             -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0.3, 1, 1);"
                        );
                        button.setGraphic(redCircle);
                        button.setStyle("-fx-background-color: transparent;");
                        break;
                    case '.':
                        button.setGraphic(null); // Пустая клетка
                        button.setStyle("-fx-background-color: transparent;");
                        break;
                    case 'X':
                        Circle blackCircle = new Circle(30, Color.BLACK);
                        blackCircle.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.75), 10, 0.5, 2, 2);" +
                                "-fx-fill: linear-gradient(from 0% 0% to 100% 100%, " +
                                "rgba(255, 255, 255, 0.5), rgba(0, 0, 0, 1));" +
                                "-fx-stroke: #000000;" +
                                "-fx-stroke-width: 1;");
                        button.setGraphic(blackCircle);
                        button.setStyle("-fx-background-color: transparent;");
                        break;
                    case '0':
                        Circle whiteCircle = new Circle(30, Color.WHITE);
                        button.setGraphic(whiteCircle);
                        whiteCircle.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.75), 10, 0.5, 2, 2);" +
                                        "-fx-fill: linear-gradient(from 0% 0% to 100% 100%, " +
                                        "rgba(255, 255, 255, 1), rgba(100, 100, 100, 0.5));" +
                                        "-fx-stroke: #000000;" +
                                        "-fx-stroke-width: 1;"
                        );
                        break;
                }
            }
        }
    }

    private Button getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return (Button) node;
            }
        }
        return null;
    }

    public void tileClick(ActionEvent actionEvent) {
        Button clickedButton = (Button) actionEvent.getSource();
        String tileId = clickedButton.getId();
        modelManager.moveModelMethod(tileId);
    }

    public void initializeBoard(String boardState) {
        updateBoard(boardState);
    }

    @Override
    public void update(String newString) {
        String command = newString.split(splitRegex)[0];
        switch (command) {
            case "get-board":
            case "board-after-move":
                Pattern pattern = Pattern.compile("\\{([^}]*)\\}");
                Matcher matcher = pattern.matcher(newString);

                if (matcher.find()) {
                    String extractedSubstring = matcher.group(1);
                    Platform.runLater(() -> initializeBoard(extractedSubstring));
                } else {
                    System.out.println("Подстрока не найдена.");
                }

                String[] parts = newString.split("::");
                if (parts.length >= 3) {
                    String[] scores = parts[2].split(" ");
                    if (scores.length == 2) {

                            int redScore = Integer.parseInt(scores[0]);
                            int greenScore = Integer.parseInt(scores[1]);
                            updateScores(redScore, greenScore);

                    }
                }

                int currentPlayerId = extractCurrentPlayerId(newString);

                    if (currentPlayerId == 1) {
                        greenTimeLeft = 120;
                        startRedTimer();
                        stopGreenTimer();
                    } else {
                        redTimeLeft = 120;
                        startGreenTimer();
                        stopRedTimer();
                    }

                break;
//            case "session-ggg":
//                System.out.println("evrerverv");
//                totalGameSessionText.setText(newString.split(splitRegex)[3]);
//                totalVictoriesText.setText(newString.split(splitRegex)[4]);
//                winRateText.setText(String.valueOf(
//                        (int)(Integer.parseInt(newString.split(splitRegex)[3])/Integer.parseInt(newString.split(splitRegex)[4]))));
//                break;
        }
    }

    private void updateScores(int redScore, int greenScore) {
        Platform.runLater(() -> {
            redScoreText.setText(String.valueOf(redScore));
            greenScoreText.setText(String.valueOf(greenScore));

        });
    }

    private void setupGradientAnimation(Button button) {
        Transition gradientAnimation = createGradientAnimation(button);
        button.setOnMouseEntered(event -> gradientAnimation.play());
        button.setOnMouseExited(event -> {
            gradientAnimation.stop();
            button.setStyle("-fx-background-color: transparent;");
        });
    }

    private Transition createGradientAnimation(Button button) {
        Transition gradientAnimation = new Transition() {
            {
                setCycleDuration(Duration.millis(600));
                setInterpolator(Interpolator.LINEAR);
                setCycleCount(Timeline.INDEFINITE);
                setAutoReverse(true);
            }

            @Override
            protected void interpolate(double frac) {
                Color color1Start = Color.web("#801db7");
                Color color2Start = Color.web("#48b15e");

                Color color1End = color1Start.interpolate(color2Start, frac);
                Color color2End = color2Start.interpolate(color1Start, frac);
                String style = String.format("-fx-background-color: linear-gradient(to right, %s, %s);",
                        toRgbString(color1End), toRgbString(color2End));
                button.setStyle(style);
            }

            private String toRgbString(Color color) {
                return String.format("rgba(%d, %d, %d, %.2f)",
                        Math.round(color.getRed() * 255),
                        Math.round(color.getGreen() * 255),
                        Math.round(color.getBlue() * 255),
                        color.getOpacity());
            }
        };
        return gradientAnimation;
    }

    private void makeRandomMove() {
        Random random = new Random();
        int randomRow = random.nextInt(8);
        int randomCol = random.nextInt(8);

        String tileId = "button_" + randomRow + "_" + randomCol;

        modelManager.moveModelMethod(tileId);
    }
}