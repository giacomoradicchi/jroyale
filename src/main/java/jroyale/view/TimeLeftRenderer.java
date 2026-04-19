package jroyale.view;

import javafx.scene.paint.Color;

public class TimeLeftRenderer {
    private static TimeLeftRenderer instance = null;

    private static final int RUNNING_OUT_TIME_LIMIT_IN_SEC = 60;
    private static final int TIME_EXCEEDING_LIMIT_IN_SEC = 10;

    private TimeLeftRenderer() {}

    public void renderTimeLeft(int secondsLeft) {
        if (secondsLeft <= 0) secondsLeft = 0;
        
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
        double lineWidth = 4;

        View.getInstance().fillScreenRoundedRect(centerX, centerY, boxWidth, boxHeight, boxArcWidth, boxArcHeight, alpha, getTextFillColor(secondsLeft));
        View.getInstance().strokeScreenTextFromCenter(timeLeft, centerX, centerY, FontManager.getInstance().getBoldFont(textSize), getTextStrokeColor(), lineWidth);
        View.getInstance().fillScreenTextFromCenter(timeLeft, centerX, centerY, FontManager.getInstance().getBoldFont(textSize), getTextFillColor(secondsLeft));
        
        if (secondsLeft > TIME_EXCEEDING_LIMIT_IN_SEC || secondsLeft == 0) return;

        centerX = View.getInstance().getScreenMapTopLeftCornerX() + View.getInstance().getScreenMapWidth()/2;
        centerY = View.getInstance().getScreenMapTopLeftCornerY() + View.getInstance().getScreenMapHeight()/2;
        textSize *= 2;

        View.getInstance().strokeScreenTextFromCenter(timeLeft, centerX, centerY, FontManager.getInstance().getBoldFont(textSize), getTextStrokeColor(), lineWidth);
        View.getInstance().fillScreenTextFromCenter(timeLeft, centerX, centerY, FontManager.getInstance().getBoldFont(textSize), getTextFillColor(secondsLeft));
        
    }

    private Color getTextFillColor(int secondsLeft) {
        if (secondsLeft < RUNNING_OUT_TIME_LIMIT_IN_SEC) return Color.RED;

        return Color.WHITE;
    }

    private Color getTextStrokeColor() {
        return Color.BLACK;
    }

    private String fromSecToTimeString(int secondsLeft) {
        int minutes = secondsLeft / 60;
        if (minutes == 0) return String.valueOf(secondsLeft % 60);

        return String.valueOf(minutes) + ":" + String.format("%02d", secondsLeft % 60);
    }

    public static TimeLeftRenderer getInstance() {
        if (instance == null) {
            instance = new TimeLeftRenderer();
        }
        return instance;
    }
}
