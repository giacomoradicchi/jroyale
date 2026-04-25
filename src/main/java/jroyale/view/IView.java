package jroyale.view;

public interface IView {

    public void init();

    public void update(long now);
    
    public void processOnMousePressed(double x, double y);

    public void processOnMouseDragged(double x, double y);

    public void processOnMouseReleased();

    public IMainGUI getGUI();
}
