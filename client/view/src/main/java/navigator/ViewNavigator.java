package navigator;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import utils.ScreenSwitcher;

public class ViewNavigator {
    private final BooleanProperty playButtonEnabled = new SimpleBooleanProperty(true);
    private final BooleanProperty settingsButtonEnabled = new SimpleBooleanProperty(true);
    private final BooleanProperty exitButtonEnabled = new SimpleBooleanProperty(true);
    private final BooleanProperty chatButtonEnabled = new SimpleBooleanProperty(true);
    private final BooleanProperty enterButtonEnabled = new SimpleBooleanProperty(true);
    private final BooleanProperty botPlayButtonEnabled = new SimpleBooleanProperty(true);


    public void onPlayButtonClicked() {
        ScreenSwitcher.loadView(getClass().getResource("/io/deeplay/camp/view/GameView.fxml"));
    }

    public void onBotPlayButtonClicked() {
        ScreenSwitcher.loadView(getClass().getResource("/io/deeplay/camp/view/GameView.fxml"));
    }

    public void onSettingsButtonClicked() {

    }

    public BooleanProperty playButtonEnabledProperty() {
        return playButtonEnabled;
    }

    public BooleanProperty settingsButtonEnabledProperty() {
        return settingsButtonEnabled;
    }

    public BooleanProperty exitButtonEnabledProperty() {
        return exitButtonEnabled;
    }

    public BooleanProperty chatButtonEnabledProperty() {
        return chatButtonEnabled;
    }

    public BooleanProperty enterButtonEnabledProperty() {
        return enterButtonEnabled;
    }

    public BooleanProperty botPlayButtonEnabledProperty() {
        return playButtonEnabled;
    }

}
