package jroyale.view.entity_view.towers;

import javafx.scene.image.Image;
import jroyale.utils.ImageUtils;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;
import jroyale.view.View2;
import jroyale.view.entity_view.EntityView;

public class ArcherTowerView extends TowerView{
    
    private static ArcherTowerView instance = null;

    private final static double SCALE = 0.39;
    private final static double Y_OFFSET = 6;

    private static final String RELATIVE_PATH_TO_PLAYER_IMAGE = "player/archer_tower.png";
    private static final String RELATIVE_PATH_TO_OPPONENT_IMAGE = "opponent/archer_tower.png";

    private ArcherTowerView() {}

    // instance methods

    @Override
    public EntityType getType() {
        return EntityType.ARCHER_TOWER;
    }

    @Override
    public void render(double centreX, double centreY, double angleDirection, int currentFrame, State state,
            Side side) {
        Image img = side == Side.PLAYER ? playerTowerImage : opponentTowerImage;

        View2.getInstance().renderWorldImage(
            img, 
            centreX, 
            centreY + Y_OFFSET, 
            img.getWidth() * SCALE, 
            img.getHeight() * SCALE
        );
    }

     @Override
    public Image getPlayerTowerImage() {
        return ImageUtils.enhanceOpacity(new Image(this.getClass().getResourceAsStream(TOWER_PATH_RELATIVE_TO_RESOURCE + RELATIVE_PATH_TO_PLAYER_IMAGE)));
    }

    @Override
    public Image getOpponentTowerImage() {
        return ImageUtils.enhanceOpacity(new Image(this.getClass().getResourceAsStream(TOWER_PATH_RELATIVE_TO_RESOURCE + RELATIVE_PATH_TO_OPPONENT_IMAGE)));
    }
    
    // static methods

    public static EntityView getInstance() {
        if (instance == null) {
            instance = new ArcherTowerView();
        }
        return instance;
    }
}
