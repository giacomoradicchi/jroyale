package jroyale.view.game_view.ui;

import javafx.scene.paint.Color;
import jroyale.view.FontManager;
import jroyale.view.IMainGUI;
import jroyale.view.game_view.IGameView;

public class TimeLeftRenderer {
    private static TimeLeftRenderer instance = null;

    private static final int RUNNING_OUT_TIME_LIMIT_IN_SEC = 60;
    private static final int TIME_EXCEEDING_LIMIT_IN_SEC = 10;
    private static final double NORMALIZED_TEXT_SIZE = 0.05;
    private static final double NORMALIZED_CENTER_X = 9.0/10;
    private static final double NORMALIZED_CENTER_Y = 1.0/25;
    private static final double NORMALIZED_BOX_WIDTH = 0.167;
    private static final double NORMALIZED_BOX_HEIGHT = 0.05;
    private static final double NORMALIZED_LINE_WIDTH = 0.01;
    private static final double ALPHA_BACKGROUND = 0.5;

    private TimeLeftRenderer() {}

    public void renderTimeLeft(int secondsLeft, double alpha) {
        
        if (secondsLeft < 0) secondsLeft = 0;
        
        String timeLeft = fromSecToTimeString(secondsLeft + 1); // adding + 1 so it will be displayed values starting from 1 instead of 0

        IGameView view = IGameView.getInstance();
        IMainGUI gui = view.getGUI();

        double canvasWidth = gui.getCanvasWidth();
        double canvasHeight = gui.getCanvasHeight();

        double textSize = canvasWidth * NORMALIZED_TEXT_SIZE;
        double centerX = canvasWidth * NORMALIZED_CENTER_X;
        double centerY = canvasHeight * NORMALIZED_CENTER_Y;
        double boxWidth = canvasWidth * NORMALIZED_BOX_WIDTH;
        double boxHeight = canvasHeight * NORMALIZED_BOX_HEIGHT;
        double boxArcHeight = boxHeight;
        double boxArcWidth = boxHeight;
        double lineWidth = canvasWidth * NORMALIZED_LINE_WIDTH;

        gui.fillScreenRoundedRect(centerX, centerY, boxWidth, boxHeight, boxArcWidth, boxArcHeight, ALPHA_BACKGROUND * alpha, getTextFillColor(secondsLeft));

        gui.strokeScreenTextFromCenter(timeLeft, centerX, centerY, FontManager.getInstance().getBoldFont(textSize), getTextStrokeColor(), lineWidth, alpha);
        gui.fillScreenTextFromCenter(timeLeft, centerX, centerY, FontManager.getInstance().getBoldFont(textSize), getTextFillColor(secondsLeft), alpha);
        
        if (secondsLeft >= TIME_EXCEEDING_LIMIT_IN_SEC) return;

        centerX = view.getScreenMapTopLeftCornerX() + view.getScreenMapWidth()/2;
        centerY = view.getScreenMapTopLeftCornerY() + view.getScreenMapHeight()/2;
        textSize *= 2;

        gui.strokeScreenTextFromCenter(timeLeft, centerX, centerY, FontManager.getInstance().getBoldFont(textSize), getTextStrokeColor(), lineWidth, alpha);
        gui.fillScreenTextFromCenter(timeLeft, centerX, centerY, FontManager.getInstance().getBoldFont(textSize), getTextFillColor(secondsLeft), alpha);
        
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
