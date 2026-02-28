package jroyale.view;

import javafx.scene.Scene;

public interface IMouseManager {
    
    public void init(Scene scene);

    public boolean isMousePressed();

    public boolean isMouseReleased();

    public double getInitialMousePositionX();

    public double getInitialMousePositionY();

    public double getLastMousePositionX();
    
    public double getLastMousePositionY();

}
