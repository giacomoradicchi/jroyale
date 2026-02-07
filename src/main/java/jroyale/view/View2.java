package jroyale.view;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class View2 implements IView2 {

    private static IView2 instance = null;
    private Stage stage;


    private static final double WH_RATIO = 607.0 / 1080;

    private static final double HEIGHT = 800;
    private static final double WIDTH = HEIGHT * WH_RATIO;

    private View2() {}

    @Override
    public void openWindow(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        Pane root = new Pane(canvas);

        
        this.stage = stage;
        
        stage.setScene(new Scene(root));
        stage.setTitle("JRoyale");
        stage.show();

    }

    public static IView2 getInstance() {
        if (instance == null) {
            instance = new View2();
        }

        return instance;
    }
    
}
