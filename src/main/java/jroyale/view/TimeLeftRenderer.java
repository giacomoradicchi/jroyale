package jroyale.view;

import javafx.scene.paint.Color;

public class TimeLeftRenderer {
    private static TimeLeftRenderer instance = null;

    private TimeLeftRenderer() {}

    public void renderTimeLeft(int secondsLeft) {
        if (secondsLeft < 0) return;
        
        String timeLeft = fromSecToTimeString(secondsLeft);

        double canvasWidth = View.getInstance().getCanvasWidth();
        double canvasHeight = View.getInstance().getCanvasHeight();

        double textSize = canvasWidth * 0.05;
        double centerX = canvasWidth * 9.0/10;
        double centerY = canvasHeight * 1.0/25;
        double boxWidth = canvasWidth * 0.167;
        double boxHeight = canvasHeight * 0.05;
        double boxArcHeight = boxHeight;
        double boxArcWidth = boxHeight;
        double alpha = 0.5;

        View.getInstance().fillScreenRoundedRect(centerX, centerY, boxWidth, boxHeight, boxArcWidth, boxArcHeight, alpha, Color.BLACK);
        View.getInstance().strokeScreenTextFromCenter(timeLeft, centerX, centerY, FontManager.getInstance().getBoldFont(textSize), Color.BLACK, 4);
        View.getInstance().fillScreenTextFromCenter(timeLeft, centerX, centerY, FontManager.getInstance().getBoldFont(textSize), Color.WHITE);
    
    }

    private String fromSecToTimeString(int secondsLeft) {
        return String.valueOf(secondsLeft / 60) + ":" + String.valueOf(secondsLeft % 60);
    }

    public static TimeLeftRenderer getInstance() {
        if (instance == null) {
            instance = new TimeLeftRenderer();
        }
        return instance;
    }
}
