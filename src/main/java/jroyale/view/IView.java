package jroyale.view;

import jroyale.controller.IControllerForView;
import jroyale.view.game_view.IGameView;
import jroyale.view.home_view.IHomeView;

public interface IView {

    public void setController(IControllerForView controller);

    public void init();

    public void update(long now);
    
    public void processOnMousePressed(double x, double y);

    public void processOnMouseDragged(double x, double y);

    public void processOnMouseReleased();

    public IMainGUI getGUI();

    // static methods

    public static IGameView getGameInstance() {
        return IGameView.getInstance();
    }

    public static IHomeView getHomeInstance() {
        return IHomeView.getInstance();
    }
}
