package jroyale.view;

import javafx.scene.text.Font;

public class FontManager {
    
    private static FontManager instance = null;

    private Font regular, bold; 

    private FontManager() {}

    public void init() {
        regular = Font.loadFont(getClass().getResourceAsStream("/jroyale/fonts/Clash_Regular.otf"), 0);
        bold = Font.loadFont(getClass().getResourceAsStream("/jroyale/fonts/Clash_Bold.otf"), 0); 
    }

    public Font getRegularFont(double size) {
        return Font.font(regular.getFamily(), size);
    }

    public Font getBoldFont(double size) {
        return Font.font(bold.getFamily(), size);
    }

    // static methods

    public static FontManager getInstance() {
        if (instance == null) {
            instance = new FontManager();
        }

        return instance;
    }
}
