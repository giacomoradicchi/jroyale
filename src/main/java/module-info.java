module jroyale {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;

    opens jroyale.controller to javafx.fxml;
    opens jroyale.utils to com.fasterxml.jackson.databind;
    opens jroyale.model to com.fasterxml.jackson.databind;

    exports jroyale.controller;
}
