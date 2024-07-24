package utils;

import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import viewmodel.MainMenuViewModel;

public class MainMenuController {
    private MainMenuViewModel viewModel = new MainMenuViewModel();

    @FXML
    private Button playButton;

    @FXML
    private Button settingsButton;

    private Transition createGradientAnimation(Button button) {
        Transition gradientAnimation = new Transition() {
            {
                setCycleDuration(Duration.millis(600)); // Уменьшенная продолжительность анимации для увеличения скорости в 5 раз
                setInterpolator(Interpolator.LINEAR);
                setCycleCount(Timeline.INDEFINITE);
                setAutoReverse(true);
            }

            @Override
            protected void interpolate(double frac) {
                Color color1Start = Color.web("#801db7");
                Color color2Start = Color.web("#48b15e");
                // Меняем цвета местами для переливания с лева на право
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

    @FXML
    private void initialize() {
        playButton.disableProperty().bind(viewModel.playButtonEnabledProperty().not());
        playButton.setOnAction(event -> viewModel.onPlayButtonClicked());

        Transition gradientAnimation = createGradientAnimation(playButton);
        playButton.setOnMouseEntered(event -> gradientAnimation.play());
        playButton.setOnMouseExited(event -> gradientAnimation.stop());
    }

    @FXML
    private void settingsButtonClick() {
        settingsButton.disableProperty().bind(viewModel.settingsButtonEnabledProperty().not());
        settingsButton.setOnAction(event -> viewModel.onSettingsButtonClicked());

        Transition gradientAnimation = createGradientAnimation(settingsButton);
        settingsButton.setOnMouseEntered(event -> gradientAnimation.play());
        settingsButton.setOnMouseExited(event -> gradientAnimation.stop());
    }
}
