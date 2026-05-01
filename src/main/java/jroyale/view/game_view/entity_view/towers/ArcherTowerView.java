package jroyale.view.game_view.entity_view.towers;

import javafx.scene.image.Image;
import jroyale.utils.ImageUtils;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;
import jroyale.view.game_view.IGameView;
import jroyale.view.game_view.entity_view.EntityView;

public class ArcherTowerView extends TowerView {
    
    private static ArcherTowerView instance = null;

    private static final double HEIGHT_IN_TILES = 4.5;

    // image crop offset from bottom
    private static final int BOTTOM_CROP_PIXELS = 30;

    // render layer depth
    private static final int RENDER_LAYER = 1;

    private static final int CROP_START_X = 0;
    private static final int CROP_START_Y = 0;

    private static final String RELATIVE_PATH_TO_PLAYER_IMAGE = "player/archer_tower.png";
    private static final String RELATIVE_PATH_TO_OPPONENT_IMAGE = "opponent/archer_tower.png";

    private final IGameView gameView = IGameView.getInstance();

    private ArcherTowerView() {}

    // instance methods

    @Override
    public EntityType getType() {
        return EntityType.ARCHER_TOWER;
    }

    @Override
    public void renderEntity(
            double centreX,
            double centreY,
            double angleDirection,
            int currentFrame,
            State state,
            Side side
    ) {
        Image img = side == Side.PLAYER ? playerTowerImage : opponentTowerImage;

        gameView.getGUI().renderWorldImage(
            img,
            centreX,
            centreY,
            img.getWidth() * getImageScale(),
            img.getHeight() * getImageScale(),
            false,
            RENDER_LAYER
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
        Image temp = new Image(
            this.getClass().getResourceAsStream(
                TOWER_PATH_RELATIVE_TO_RESOURCE + RELATIVE_PATH_TO_PLAYER_IMAGE
            )
        );

        // crop transparent pixels from bottom
        temp = ImageUtils.crop(
            temp,
            CROP_START_X,
            CROP_START_Y,
            (int) temp.getWidth(),
            (int) temp.getHeight() - BOTTOM_CROP_PIXELS
        );

        return ImageUtils.enhanceOpacity(temp);
    }

    @Override
    public Image getOpponentTowerImage() {
        Image temp = new Image(
            this.getClass().getResourceAsStream(
                TOWER_PATH_RELATIVE_TO_RESOURCE + RELATIVE_PATH_TO_OPPONENT_IMAGE
            )
        );

        // crop transparent pixels from bottom
        temp = ImageUtils.crop(
            temp,
            CROP_START_X,
            CROP_START_Y,
            (int) temp.getWidth(),
            (int) temp.getHeight() - BOTTOM_CROP_PIXELS
        );

        return ImageUtils.enhanceOpacity(temp);
    }

    // static methods

    public static EntityView getInstance() {
        if (instance == null) {
            instance = new ArcherTowerView();
        }
        return instance;
    }
}