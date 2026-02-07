package jroyale.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jroyale.model.IModel;
import jroyale.model.Model;
import jroyale.view.IView;
import jroyale.view.View;

import java.io.IOException;

/**
 * JavaFX App
 */
public class Main extends Application {

    private static Scene scene;
    private static final double WH_RATIO = 607.0 / 1080;

    private static final double HEIGHT = 800;
    private static final double WIDTH = HEIGHT * WH_RATIO;

    /* @Override
    public void start(Stage stage) throws IOException {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        Pane root = new Pane(canvas);
        scene = new Scene(root);

        // Creation of instances
        IModel model = Model.getIstance();
        IView view = View.getIstance(canvas, model.getRowsCount(), model.getColsCount());
        IController controller = new Controller(model, view, scene);
        
        
        stage.setScene(scene);
        stage.setTitle("JRoyale");
        stage.show();
        stage.setResizable(false);
        stage.toFront();
        stage.requestFocus();

        

        controller.start();

    } */
    
    @Override
    public void start(Stage stage) throws IOException {
        ControllerForView.getInstance().openWindow(stage);
    }

    public static void main(String[] args) {
        launch();
    }

}