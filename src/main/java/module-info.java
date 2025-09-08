module jroyale {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens jroyale to javafx.fxml;
    opens jroyale.controller to javafx.fxml;
    exports jroyale;
}
