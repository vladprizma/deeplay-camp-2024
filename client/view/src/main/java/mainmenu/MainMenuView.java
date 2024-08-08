package mainmenu;

import game.GameView;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import sigletonobserver.ChatString;
import enums.ButtonEnum;
import io.deeplay.camp.ModelManager;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import javafx.util.Duration;
import navigator.ViewNavigator;
import observer.Observer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMenuView implements Observer {
    private ViewNavigator viewModel = new ViewNavigator();
    private static final String CHAT_INITIALIZER = "f9d7a792ac66a0db8557736e680a780802a21bd627bd37e72cc10eb0997fba4e";
    private static final String USER_PARSE = "04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb";

    private static final String FIRST_LINE_PARSE = "a7937b64b8caa58f03721bb6bacf5c78cb235febe0e70b1b84cd99541461a08e";
    private static final String MIDDLE_LINE_PARSE = "a4888af4e46c129c695ee32775a8c233f113c82e7cd4e6fd3cbb1fda5659f36a";
    private static final String END_LINE_PARSE = "361e48d0308f20e32dba5fb56328baf18d72ef0ccb43b84f5c262d2a6a1fc6c8";

    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public static String splitRegex = "::";

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

    @FXML
    private Button enterButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Rectangle blurBackground;

    @FXML
    private VBox loginVBox;

    @FXML
    private Button registerButton;

    @FXML
    private Button registerRegisterButton;

    @FXML
    private Button registerBackButton;

    @FXML
    private PasswordField registerConfirmPasswordField;

    @FXML
    private PasswordField registerPasswordField;

    @FXML
    private TextField registerUsernameField;

    @FXML
    private VBox registerVBox;

    @FXML
    private ImageView registerImageBackButton;

    @FXML
    private Label errorLabel;

    @FXML
    private Button botPlayButton;

    private ObservableList<String> chatMessages;

    public static ModelManager getModelManager() {
        return modelManager;
    }

    private static ModelManager modelManager;

    private List<String> chat;

    private ChatString singleton;
    private boolean isBlocked = false;
    private boolean isLogin = false;
    private boolean timeToStart = false;
    private boolean timeToChat = false;

    public MainMenuView() throws IOException {
        singleton = ChatString.getInstance();
        modelManager = new ModelManager();
    }

    private void openChat() {
        chatButton.setVisible(false);
        chatPanel.setVisible(true);
    }

    @FXML
    private void sendMessage() {
        String message = chatInput.getText();
        if (!message.isEmpty()) {
            modelManager.chatModelMethod(message);
            chatInput.clear();
        }
    }

    @FXML
    public void initialize() {
        singleton.registerObserver(this);
        animation();
        if (isLogin == false) {
            modelManager.startSessionModelMethod();
            timeToStart = true;
        }
        playButton.setOnAction(event -> onButtonClicked(ButtonEnum.PLAY));
        settingsButton.setOnAction(event -> onButtonClicked(ButtonEnum.SETTINGS));
        exitButton.setOnAction(event -> onButtonClicked(ButtonEnum.EXIT));
        chatButton.setOnAction(event -> onButtonClicked(ButtonEnum.CHAT));
        enterButton.setOnAction(event ->    onButtonClicked(ButtonEnum.ENTER));
        registerButton.setOnAction(event -> onButtonClicked(ButtonEnum.REGISTER));
        registerBackButton.setOnAction(event -> onButtonClicked(ButtonEnum.BACK));
        registerRegisterButton.setOnAction(event -> onButtonClicked(ButtonEnum.SIGNUP));
        botPlayButton.setOnAction(event -> onButtonClicked(ButtonEnum.BOT));
    }

    @FXML
    private void handleRootClick(MouseEvent event) {
        if (chatPanel.isVisible()) {
            if (!chatPanel.getBoundsInParent().contains(event.getX(), event.getY())) {
                closeChat();
            }
        }
    }

    @FXML
    private void handleChatPanelClick(MouseEvent event) {
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
            case ENTER:
                onEnterButtonClicked();
                break;
            case REGISTER:
                onRegisterButtonClicked();
                break;
            case BACK:
                onBackButtonClicked();
                break;
            case SIGNUP:
                onSignUpButtonClicked();
                break;
            case BOT:
                onBotPlayButtonClicked();
                break;
            default:
                break;
        }
    }

    private void onPlayButtonClicked() {
        setupButton(playButton, viewModel::onPlayButtonClicked, viewModel.playButtonEnabledProperty());
        viewModel.playButtonEnabledProperty().set(false);
        modelManager.startGameModelMethod();
    }

    private void onBotPlayButtonClicked() {
        setupButton(playButton, viewModel::onBotPlayButtonClicked, viewModel.botPlayButtonEnabledProperty());
        viewModel.playButtonEnabledProperty().set(false);
        modelManager.startBotGameModelMethod();
    }

    private void SessionSearched() {
        viewModel.playButtonEnabledProperty().set(true);
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
        modelManager.chatModelMethod(CHAT_INITIALIZER);
        chatMessages = FXCollections.observableArrayList();
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
                            setStyle("");
                        } else if (item.split(splitRegex).length > 1 &&
                                item.split(splitRegex)[0].equals(USER_PARSE)) {
                            setText(item.split(splitRegex)[1] + ":");
                            setStyle("-fx-background-color: transparent; " +
                                    "-fx-text-fill: white;" +
                                    "-fx-padding: 10 10 10 10;" +
                                    "-fx-font-size: 24px; " +
                                    "-fx-font-family: 'Josefin Slab SemiBold'; ");
                        } else if (isValidDate(item)) {
                            String formattedTime = formatTime(item);
                            setText(formattedTime);
                            setStyle("-fx-background-color: transparent; " +
                                    "-fx-text-fill: white;" +
                                    "-fx-padding: 10 10 10 500;" +
                                    "-fx-font-size: 24px; " +
                                    "-fx-font-family: 'Josefin Slab SemiBold' ");
                        } else if (item.split(splitRegex).length > 1 &&
                                item.split(splitRegex)[0].equals(FIRST_LINE_PARSE)) {
                            setText(item.split(splitRegex)[1]);
                            setStyle("-fx-background-radius: 0px 0px 500px 150px; " +
                                    "-fx-text-fill: white; " +
                                    "-fx-font-size: 32px; " +
                                    "-fx-background-color: rgba(255, 255, 255, 0.6); " +
                                    "-fx-font-family: 'Josefin Slab SemiBold'; ");

                        } else if (item.split(splitRegex).length > 1 &&
                                item.split(splitRegex)[0].equals(MIDDLE_LINE_PARSE)) {
                            setText(item.split(splitRegex)[1]);
                            setStyle("-fx-text-fill: white; " +
                                    "-fx-font-size: 32px; " +
                                    "-fx-background-color: rgba(255, 255, 255, 0.6); " +
                                    "-fx-font-family: 'Josefin Slab SemiBold'; ");

                        } else if (item.split(splitRegex).length > 1 &&
                                item.split(splitRegex)[0].equals(END_LINE_PARSE)) {
                            setText(item.split(splitRegex)[1]);
                            setStyle("-fx-background-radius: 50px 200px 0px 0px; " +
                                    "-fx-text-fill: white; " +
                                    "-fx-font-size: 32px; " +
                                    "-fx-background-color: rgba(255, 255, 255, 0.6); " +
                                    "-fx-font-family: 'Josefin Slab SemiBold'; ");
                        } else {
                            setText(item);
                            setStyle("-fx-background-radius: 150px 50px 500px 150px; " +
                                    "-fx-text-fill: white; " +
                                    "-fx-font-size: 32px; " +
                                    "-fx-background-color: rgba(255, 255, 255, 0.6); " +
                                    "-fx-font-family: 'Josefin Slab SemiBold'; ");
                        }
                    }
                };
            }
        });

        setupButton(settingsButton, viewModel::chatButtonEnabledProperty, viewModel.chatButtonEnabledProperty());
        openChat();
    }

    public static boolean isValidDate(String dateStr) {
        DateTimeFormatter formatter = INPUT_FORMATTER;
        try {
            LocalDateTime.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private String formatTime(String dateStr) {
        LocalDateTime dateTime = LocalDateTime.parse(dateStr, INPUT_FORMATTER);
        return dateTime.format(OUTPUT_FORMATTER);
    }

    public void sendMessages(List<String> messages) {
        Platform.runLater(() -> chatMessages.addAll(messages));
    }

    private void onExitButtonClicked() {
        viewModel.playButtonEnabledProperty().set(false);
        viewModel.settingsButtonEnabledProperty().set(false);
    }

    private void onEnterButtonClicked() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        modelManager.loginModelMethod(username + " " + password);
    }

    private void enterSuccessfull() {
        blurBackground.setVisible(false);
        loginVBox.setVisible(false);
        usernameField.setVisible(false);
        passwordField.setVisible(false);
        enterButton.setVisible(false);
        registerButton.setVisible(false);
    }

    private void onSignUpButtonClicked() {
        String username = registerUsernameField.getText();
        String password = registerPasswordField.getText();
        String confirmPassword = registerConfirmPasswordField.getText();

        try {
            validate(username, password, confirmPassword);
            modelManager.registerModelMethod(username + " " + password + " " +
                    "there could be a picture of you in here");
        } catch (ValidationException e) {
            showErrorMessage(e.getMessage());
        }
    }

    private void validate(String username, String password, String confirmPassword) throws ValidationException {
        if (username.length() < 5 || username.length() > 20) {
            throw new ValidationException("Username must be between 5 and 20 characters.");
        }

        if (password.length() < 8 || password.length() > 20) {
            throw new ValidationException("Password must be between 8 and 20 characters.");
        }

        if (!password.equals(confirmPassword)) {
            throw new ValidationException("Passwords do not match.");
        }

        if (!username.matches("[a-zA-Z0-9_]+")) {
            throw new ValidationException("Username can only contain alphanumeric characters and underscores.");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new ValidationException("Password must contain at least one uppercase letter.");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new ValidationException("Password must contain at least one lowercase letter.");
        }

        if (!password.matches(".*\\d.*")) {
            throw new ValidationException("Password must contain at least one digit.");
        }
    }

    private void showErrorMessage(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(5),
                ae -> errorLabel.setVisible(false)
        ));
        timeline.play();
    }

    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }

    private void signInSuccessfull() {
        registerRegisterButton.setVisible(false);
        registerBackButton.setVisible(false);
        registerPasswordField.setVisible(false);
        registerConfirmPasswordField.setVisible(false);
        registerUsernameField.setVisible(false);
        registerVBox.setVisible(false);
        registerImageBackButton.setVisible(false);
    }

    private void onRegisterButtonClicked() {
        blurBackground.setVisible(false);
        loginVBox.setVisible(false);
        usernameField.setVisible(false);
        passwordField.setVisible(false);
        enterButton.setVisible(false);
        registerButton.setVisible(false);

        registerRegisterButton.setVisible(true);
        registerBackButton.setVisible(true);
        registerPasswordField.setVisible(true);
        registerConfirmPasswordField.setVisible(true);
        registerUsernameField.setVisible(true);
        registerVBox.setVisible(true);
        registerImageBackButton.setVisible(true);
    }

    private void onBackButtonClicked() {
        blurBackground.setVisible(true);
        loginVBox.setVisible(true);
        usernameField.setVisible(true);
        passwordField.setVisible(true);
        enterButton.setVisible(true);
        registerButton.setVisible(true);

        registerRegisterButton.setVisible(false);
        registerBackButton.setVisible(false);
        registerPasswordField.setVisible(false);
        registerConfirmPasswordField.setVisible(false);
        registerUsernameField.setVisible(false);
        registerVBox.setVisible(false);
        registerImageBackButton.setVisible(false);
    }

    private void setupButton(Button button, Runnable actionOnClick, BooleanProperty enabledProperty) {
        button.disableProperty().bind(enabledProperty.not());
        button.setOnAction(event -> actionOnClick.run());
    }

    private void animation() {
        setupGradientAnimation(playButton);
        setupGradientAnimation(settingsButton);
        setupGradientAnimation(botPlayButton);
    }

    private void setupGradientAnimation(Button button) {
        Transition gradientAnimation = createGradientAnimation(button);
        button.setOnMouseEntered(event -> gradientAnimation.play());
        button.setOnMouseExited(event -> gradientAnimation.stop());
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

    @Override
    public void update(String newString) {
        String command = newString.split(splitRegex)[0];
        switch (command) {
            case "messages":
                List<String> parsedLogs = parseLog(newString);

                parsedLogs.removeIf(item -> item.equals(CHAT_INITIALIZER));
                Platform.runLater(() -> {
                    chatMessages.clear();
                    sendMessages(parsedLogs.reversed());
                });
                break;
            case "Please login or register":
                blurBackground.setVisible(true);
                loginVBox.setVisible(true);
                usernameField.setVisible(true);
                passwordField.setVisible(true);
                enterButton.setVisible(true);
                break;
            case "session-start":
                isLogin = true;
                enterSuccessfull();
                signInSuccessfull();
                break;
            case "session":
            case "session-bot":
                SessionSearched();
                break;
            case "User not found.":
                handleLoginError();
                break;
            case "Not unique username.":
                try {
                        throw new ValidationException("Not unique username.");
                } catch (ValidationException e) {
                    showErrorMessage("Not unique username");
                }
        }
    }

    public void handleLoginError() {
        Border errorBorder = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THIN));
        usernameField.setBorder(errorBorder);
        passwordField.setBorder(errorBorder);

        usernameField.setStyle("-fx-text-fill: red;");
        passwordField.setStyle("-fx-text-fill: red;");

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            usernameField.setBorder(null);
            passwordField.setBorder(null);

            usernameField.clear();
            passwordField.clear();

            usernameField.setStyle("-fx-text-fill: black;");
            passwordField.setStyle("-fx-text-fill: black;");
        });

        pause.play();
    }

    public static List<String> parseLog(String log) {
        List<String> result = new ArrayList<>();

        // Регулярное выражение для поиска нужных шаблонов
        String regex = "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}) (\\S+) ([^:]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(log);

        while (matcher.find()) {
            // Собираем найденные группы в строку
            String timestamp = matcher.group(1);
            String username = matcher.group(2);
            String message = matcher.group(3);

            String res = (message.equals(CHAT_INITIALIZER)) ?
                    CHAT_INITIALIZER : timestamp + " " + username + " " + message;

            if (!res.equals(CHAT_INITIALIZER)) {
                result.add(timestamp);
                result.addAll((splitString(message, 35)).reversed());
                result.add(USER_PARSE + "::" + username);
            } else {
                result.add(res);
            }
        }

        return result;
    }

    public static List<String> splitString(String text, int maxLength) {
        List<String> result = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (currentLine.length() + word.length() + 1 <= maxLength) {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                if (result.isEmpty()) {
                    result.add(END_LINE_PARSE + "::" + currentLine.toString());
                } else {
                    result.add(MIDDLE_LINE_PARSE + "::" + currentLine.toString());
                }
                currentLine = new StringBuilder(word);
            }
        }

        if (currentLine.length() > 0) {
            if (result.isEmpty()) {
                result.add(currentLine.toString());
            } else {
                result.add(FIRST_LINE_PARSE + "::" + currentLine.toString());
            }
        }

        return result;
    }
}