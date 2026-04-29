package jroyale.view;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class FontManager {

    private static FontManager instance = null;

    // reference font size used to compute normalized heights
    private static final double REFERENCE_FONT_SIZE = 10;

    // reference character used to estimate font height
    private static final String REFERENCE_CHARACTER = "A";

    // font resource paths
    private static final String REGULAR_FONT_PATH = "/jroyale/fonts/Clash_Regular.otf";
    private static final String BOLD_FONT_PATH =  "/jroyale/fonts/Clash_Bold.otf";

    private Font regular;
    private Font bold;

    private final double regularReferenceHeight;
    private final double boldReferenceHeight;

    private FontManager() {
        regular = Font.loadFont(
            getClass().getResourceAsStream(REGULAR_FONT_PATH),
            REFERENCE_FONT_SIZE
        );

        bold = Font.loadFont(
            getClass().getResourceAsStream(BOLD_FONT_PATH),
            REFERENCE_FONT_SIZE
        );

        Text referenceText = new Text(REFERENCE_CHARACTER);

        // compute regular font reference height
        referenceText.setFont(regular);
        regularReferenceHeight = referenceText.getBoundsInLocal().getHeight();

        // compute bold font reference height
        referenceText.setFont(bold);
        boldReferenceHeight = referenceText.getBoundsInLocal().getHeight();
    }

    public Font getRegularFont(double size) {
        return Font.font(
            regular.getFamily(),
            size
        );
    }

    public Font getBoldFont(double size) {
        return Font.font(
            bold.getFamily(),
            size
        );
    }

    public double getRegularFontSize(double targetHeight) {
        return
            REFERENCE_FONT_SIZE
            * targetHeight
            / regularReferenceHeight;
    }

    public double getBoldFontSize(double targetHeight) {
        return
            REFERENCE_FONT_SIZE
            * targetHeight
            / boldReferenceHeight;
    }

    // static methods

    public static FontManager getInstance() {
        if (instance == null) {
            instance = new FontManager();
        }

        return instance;
    }
}