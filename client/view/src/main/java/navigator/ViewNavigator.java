package navigator;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import utils.ScreenSwitcher;

public class ViewNavigator {
    private final BooleanProperty playButtonEnabled = new SimpleBooleanProperty(true);
    private final BooleanProperty settingsButtonEnabled = new SimpleBooleanProperty(true);
    private final BooleanProperty exitButtonEnabled = new SimpleBooleanProperty(true);
    private final BooleanProperty chatButtonEnabled = new SimpleBooleanProperty(true);

    public void onPlayButtonClicked() {
        ScreenSwitcher.loadView(getClass().getResource("/io/deeplay/camp/view/TestView.fxml"));
    }

    public void onSettingsButtonClicked() {
        ScreenSwitcher.loadView(getClass().getResource("/io/deeplay/camp/view/TestView.fxml"));
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
}
