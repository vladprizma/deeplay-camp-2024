package mainmenu;

import enums.ButtonEnum;
import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.Duration;
import navigator.ViewNavigator;

public class MainMenuView {
    private ViewNavigator viewModel = new ViewNavigator();

    @FXML
    private Button playButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button chatButton;

    @FXML
    private Pane chatPanel;

    @FXML
    private ListView<String> chatListView;

    @FXML
    private TextField chatInput;

    private ObservableList<String> chatMessages;

    private void openChat() {
        chatButton.setVisible(false);
        chatPanel.setVisible(true);
    }

    @FXML
    private void sendMessage() {
        String message = chatInput.getText();
        if (!message.isEmpty()) {
            chatListView.getItems().add(message);
            chatInput.clear();
        }
    }

    @FXML
    public void initialize() {
        animation();
        playButton.setOnAction(event -> onButtonClicked(ButtonEnum.PLAY));
        settingsButton.setOnAction(event -> onButtonClicked(ButtonEnum.SETTINGS));
        exitButton.setOnAction(event -> onButtonClicked(ButtonEnum.EXIT));
        chatButton.setOnAction(event -> onButtonClicked(ButtonEnum.CHAT));
    }

    @FXML
    private void handleRootClick(MouseEvent event) {
        if (chatPanel.isVisible()) {
            // Проверка, находится ли клик внутри панели чата
            if (!chatPanel.getBoundsInParent().contains(event.getX(), event.getY())) {
                closeChat();
            }
        }
    }

    @FXML
    private void handleChatPanelClick(MouseEvent event) {
        // Остановка распространения события, чтобы клик внутри панели чата
        // не закрывал чат
        event.consume();
    }

    private void closeChat() {
        chatPanel.setVisible(false);
        chatButton.setVisible(true);
    }

    private void onButtonClicked(ButtonEnum buttonType) {
        switch (buttonType) {
            case PLAY:
                onPlayButtonClicked();
                break;
            case SETTINGS:
                onSettingsButtonClicked();
                break;
            case CHAT:
                onChatButtonClicked();
                break;
            case EXIT:
                onExitButtonClicked();
                break;
        }
    }

    private void onPlayButtonClicked() {
        setupButton(playButton, viewModel::onPlayButtonClicked, viewModel.playButtonEnabledProperty());
        playButton.fire();
        viewModel.playButtonEnabledProperty().set(false);
        viewModel.settingsButtonEnabledProperty().set(false);
        viewModel.exitButtonEnabledProperty().set(false);
    }

    private void onSettingsButtonClicked() {
        setupButton(settingsButton, viewModel::onSettingsButtonClicked, viewModel.settingsButtonEnabledProperty());
        settingsButton.fire();
        viewModel.playButtonEnabledProperty().set(false);
        viewModel.settingsButtonEnabledProperty().set(false);
        viewModel.exitButtonEnabledProperty().set(false);
    }

    private void onChatButtonClicked() {
        chatMessages = FXCollections.observableArrayList(
                "Привет! Как дела?",
                "hello child!",
                "Сегодня отличная погода."
        );

        // Установка сообщений в ListView
        chatListView.setItems(chatMessages);
        chatListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setStyle("-fx-text-fill: black; -fx-font-size: 14px; -fx-background-color: rgba(255, 255, 255, 0.5); ");
                        } else {
                            setText(item);
                            // Применение стиля к тексту внутри ListView
                            setStyle("-fx-text-fill: black; -fx-font-size: 14px; -fx-background-color: rgba(255, 255, 255, 0.5); ");
                        }
                    }
                };
            }
        });

        setupButton(settingsButton, viewModel::chatButtonEnabledProperty, viewModel.chatButtonEnabledProperty());
        openChat();
        sendMessage();
        viewModel.playButtonEnabledProperty().set(false);
        viewModel.settingsButtonEnabledProperty().set(false);
        viewModel.exitButtonEnabledProperty().set(false);
    }

    private void onExitButtonClicked() {
        viewModel.playButtonEnabledProperty().set(false);
        viewModel.settingsButtonEnabledProperty().set(false);
    }

    private void setupButton(Button button, Runnable actionOnClick, BooleanProperty enabledProperty) {
        button.disableProperty().bind(enabledProperty.not());
        button.setOnAction(event -> actionOnClick.run());
    }

    private void animation() {
        setupGradientAnimation(playButton);
        setupGradientAnimation(settingsButton);
    }

    private void setupGradientAnimation(Button button) {
        Transition gradientAnimation = createGradientAnimation(button);
        button.setOnMouseEntered(event -> gradientAnimation.play());
        button.setOnMouseExited(event -> gradientAnimation.stop());
    }

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
}
