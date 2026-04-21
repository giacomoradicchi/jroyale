package jroyale.view;

import javafx.stage.Stage;

public interface IMainGUI {

    public void openWindow(Stage stage);

    public void init();

    public void update(long now);
}
