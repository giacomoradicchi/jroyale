module jroyale {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;

    opens jroyale.controller to javafx.fxml;
    exports jroyale.controller;
}
