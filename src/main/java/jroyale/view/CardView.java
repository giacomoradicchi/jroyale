package jroyale.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import jroyale.utils.Point;

public class CardView {
    
    private final Point startingPos;
    private final Point currentPos;
    private double width, height; 

    public CardView(double x, double y, double width, double height) {
        startingPos = new Point(x, y);
        currentPos = new Point(x, y);
        this.width = width;
        this.height = height;
    }


    public void render(Image icon, Image outline) {

        // first, drawing of icon
        View2.getInstance().renderScreenImage(
            icon, 
            currentPos.getX(), 
            currentPos.getY(), 
            width, 
            height
        );
        //gc.drawImage(icon, currentPos.getX() - width/2, currentPos.getY() - height/2, width, height);

        // then, drawing of outline
        View2.getInstance().renderScreenImage(
            outline, 
            currentPos.getX(), 
            currentPos.getY(), 
            width, 
            height
        );
        //gc.drawImage(outline, currentPos.getX() - width/2, currentPos.getY() - height/2, width, height);
    }

    public void render(Image outline) {

        // drawing only outline (just for debug)
        View2.getInstance().renderScreenImage(
            outline, 
            currentPos.getX() - width/2, 
            currentPos.getY() - height/2, 
            width, 
            height
        );
        //gc.drawImage(outline, currentPos.getX() - width/2, currentPos.getY() - height/2, width, height);
    }

    public boolean isCardClicked(double mouseX, double mouseY) {
        return 
            getCurrentX() - width / 2 <= mouseX && mouseX <= getCurrentX() + width / 2
        &&  getCurrentY() - height / 2 <= mouseY && mouseY <= getCurrentY() + height / 2;
    }

    public void shiftPosition(double dx, double dy) {
        currentPos.addX(dx).addY(dy);
    }

    public double getCurrentX() {
        return currentPos.getX();
    }

    public double getCurrentY() {
        return currentPos.getY();
    }

    public double getStartX() {
        return startingPos.getX();
    }

    public double getStartY() {
        return startingPos.getY();
    }
}
