package jroyale.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import jroyale.App;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}