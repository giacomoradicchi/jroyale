module jroyale {
    requires javafx.controls;
    requires javafx.fxml;

    opens jroyale to javafx.fxml;
    exports jroyale;
}
