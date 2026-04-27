package jroyale.view.game_view.entity_view.towers;


import javafx.scene.image.Image;
import jroyale.utils.ImageUtils;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;
import jroyale.view.game_view.GameView;
import jroyale.view.game_view.entity_view.EntityView;

public class KingTowerView extends TowerView {

    private static KingTowerView instance = null;

    private final static double HEIGHT_IN_TILES = 5;
    private final static double Y_OFFSET = 0;

    private static final String RELATIVE_PATH_TO_PLAYER_IMAGE = "player/king_tower.png";
    private static final String RELATIVE_PATH_TO_OPPONENT_IMAGE = "opponent/king_tower.png";

    private KingTowerView() {}

    // instance methods

    @Override
    public EntityType getType() {
        return EntityType.KING_TOWER;
    }

    @Override
    public void renderEntity(double centreX, double centreY, double angleDirection, int currentFrame, State state,
            Side side) {

        Image img = side == Side.PLAYER ? playerTowerImage : opponentTowerImage;

        GameView.getInstance().getGUI().renderWorldImage(
            img, 
            centreX, 
            centreY + Y_OFFSET, 
            img.getWidth() * getImageScale(), 
            img.getHeight() * getImageScale(),
            false,
            1
        );
    }

    @Override
    public double getSpritesHeight() {
        return SPRITE_HEIGHT;
    }

    @Override
    protected double getHeightInTiles() {
        return HEIGHT_IN_TILES;
    }

    @Override
    public Image getPlayerTowerImage() {
        Image temp = new Image(this.getClass().getResourceAsStream(TOWER_PATH_RELATIVE_TO_RESOURCE + RELATIVE_PATH_TO_PLAYER_IMAGE));
        // TODO: farlo più robusto
        temp = ImageUtils.crop(temp, 0, 0, (int) temp.getWidth(), (int) temp.getHeight() - 30);

        return ImageUtils.enhanceOpacity(temp);
    }

    @Override
    public Image getOpponentTowerImage() {
        Image temp = new Image(this.getClass().getResourceAsStream(TOWER_PATH_RELATIVE_TO_RESOURCE + RELATIVE_PATH_TO_OPPONENT_IMAGE));
        // TODO: farlo più robusto
        temp = ImageUtils.crop(temp, 0, 0, (int) temp.getWidth(), (int) temp.getHeight() - 30);

        return ImageUtils.enhanceOpacity(temp);
    }

    // static methods

    public static EntityView getInstance() {
        if (instance == null) {
            instance = new KingTowerView();
        }
        return instance;
    }
    
}
