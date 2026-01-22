package jroyale;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jroyale.controller.Controller;
import jroyale.controller.IController;
import jroyale.model.IModel;
import jroyale.model.Model;
import jroyale.view.IView;
import jroyale.view.View;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static final double ORIGINAL_RATIO = 607.0 / 1080;

    @Override
    public void start(Stage stage) throws IOException {
        double height = 800;
        double width = height * ORIGINAL_RATIO; 
        Canvas canvas = new Canvas(width, height);
        Pane root = new Pane(canvas);
        scene = new Scene(root);

        // Creation of instances
        IModel model = Model.getIstance();
        IView view = View.getIstance(canvas, model.getRowsCount(), model.getColsCount());
        IController controller = new Controller(model, view, scene);
        
        
        stage.setScene(scene);
        stage.setTitle("JRoyale");
        stage.show();
        stage.setResizable(true);

        

        controller.start();
    } 

    public static void main(String[] args) {
        launch();
    }

}