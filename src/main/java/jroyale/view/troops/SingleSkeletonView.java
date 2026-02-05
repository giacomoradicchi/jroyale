package jroyale.view.troops;

import javafx.scene.image.Image;

public class SingleSkeletonView extends SkeletonView {
    
    private static SkeletonView instance;

    private static final String SPELL_ICON_RELATIVE_PATH = "spellIcon/single_skeleton.png";

    private SingleSkeletonView() {
        super();
    }

    public static TroopView getIstance() {
        if (instance == null) 
            instance = new SingleSkeletonView();

        return instance;
    }

    @Override
    protected Image getRawSpellIcon() {
        return new Image(MiniPekkaView.class.getResourceAsStream(TROOPS_PATH_RELATIVE_TO_RESOURCE + SPELL_ICON_RELATIVE_PATH));
    }
}
