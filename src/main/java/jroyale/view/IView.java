package jroyale.view;

import javafx.concurrent.Task;

public interface IView {

    public void init();
    
    //public Task<Void> loadAsync();

    public void update(long now);
    
    public void processOnMousePressed(double x, double y);

    public void processOnMouseDragged(double x, double y);

    public void processOnMouseReleased();

    public IMainGUI getGUI();
}
