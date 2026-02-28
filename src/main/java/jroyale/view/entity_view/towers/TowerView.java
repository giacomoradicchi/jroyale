package jroyale.view.entity_view.towers;

import javafx.scene.image.Image;

import jroyale.view.entity_view.EntityView;

public abstract class TowerView extends EntityView {
    
    protected static final String TOWER_PATH_RELATIVE_TO_RESOURCE = "/jroyale/images/towers/";

    protected final Image playerTowerImage, opponentTowerImage;

    protected TowerView() {
        playerTowerImage = getPlayerTowerImage();
        opponentTowerImage = getOpponentTowerImage();
    }

    @Override
    public Image getSpellIcon() {
        return null; // towers don't have spell icons
    }

    // abstract methods

    public abstract Image getPlayerTowerImage();

    public abstract Image getOpponentTowerImage();
}
