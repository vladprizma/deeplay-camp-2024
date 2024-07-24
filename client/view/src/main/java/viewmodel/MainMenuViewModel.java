package viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import utils.ViewNavigator;

public class MainMenuViewModel {
    private final BooleanProperty playButtonEnabled = new SimpleBooleanProperty(true);
    private final BooleanProperty settingsButtonEnabled = new SimpleBooleanProperty(true);

    public void onPlayButtonClicked() {
        ViewNavigator.loadView(getClass().getResource("/io/deeplay/camp/view/TestView.fxml"));
    }

    public void onSettingsButtonClicked() {
        ViewNavigator.loadView(getClass().getResource("/io/deeplay/camp/view/TestView.fxml"));
    }

    public BooleanProperty playButtonEnabledProperty() {
        return playButtonEnabled;
    }

    public BooleanProperty settingsButtonEnabledProperty() {
        return settingsButtonEnabled;
    }

}
