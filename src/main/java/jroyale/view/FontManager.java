package jroyale.view;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class FontManager {
    
    private static FontManager instance = null;

    private static final double REFERENCE_FONT_SIZE = 10;

    private Font regular, bold; 
    private final double regularReferenceHeight, boldReferenceHeight;

    private FontManager() {
        regular = Font.loadFont(getClass().getResourceAsStream("/jroyale/fonts/Clash_Regular.otf"), REFERENCE_FONT_SIZE);
        bold = Font.loadFont(getClass().getResourceAsStream("/jroyale/fonts/Clash_Bold.otf"), REFERENCE_FONT_SIZE); 

        Text referenceText = new Text("A"); // it will be used as reference to compute font height
        referenceText.setFont(regular);
        regularReferenceHeight = referenceText.getBoundsInLocal().getHeight();

        referenceText.setFont(bold);
        boldReferenceHeight = referenceText.getBoundsInLocal().getHeight();
    }


    public Font getRegularFont(double size) {
        return Font.font(regular.getFamily(), size);
    }

    public Font getBoldFont(double size) {
        return Font.font(bold.getFamily(), size);
    }

    public double getRegularFontSize(double targetHeight) {
        return REFERENCE_FONT_SIZE * targetHeight / regularReferenceHeight;
    }

    public double getBoldFontSize(double targetHeight) {
        return REFERENCE_FONT_SIZE * targetHeight / boldReferenceHeight;
    }

    // static methods

    public static FontManager getInstance() {
        if (instance == null) {
            instance = new FontManager();
        }

        return instance;
    }
}
