package jroyale.view.troops;

import javafx.scene.image.Image;
import jroyale.view.EntityView;

public class SkeletonArmyView extends SkeletonView {
    
    private static SkeletonArmyView instance;

    private static final String SPELL_ICON_RELATIVE_PATH = "spellIcon/skeleton_army.png";

    private SkeletonArmyView() {
        super();
    }

    @Override
    protected Image getRawSpellIcon() {
        return new Image(MiniPekkaView.class.getResourceAsStream(TROOPS_PATH_RELATIVE_TO_RESOURCE + SPELL_ICON_RELATIVE_PATH));
    }

    // static methods

    public static EntityView getInstance() {
        if (instance == null) 
            instance = new SkeletonArmyView();

        return instance;
    }
}
