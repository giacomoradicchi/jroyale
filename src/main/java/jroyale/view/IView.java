package jroyale.view;

public interface IView {
    
    public void render(long millisecs);

    // just for debug
    public void renderCells(boolean[][] cells);
}
