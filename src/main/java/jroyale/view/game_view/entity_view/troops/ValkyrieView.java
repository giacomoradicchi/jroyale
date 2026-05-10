package jroyale.view.game_view.entity_view.troops;

import java.util.Map;

import javafx.scene.image.Image;
import jroyale.utils.ImageUtils;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;
import jroyale.view.IMainGUI;
import jroyale.view.audio.AudioManager.AudioType;
import jroyale.view.game_view.IGameView;
import jroyale.view.game_view.animations.AnimationKey;
import jroyale.view.game_view.animations.Direction;
import jroyale.view.game_view.entity_view.EntityView;

public class ValkyrieView extends TroopView {

    private static ValkyrieView instance = null;

    private static Map<State, Integer> numFramesPerDirection;

    private static final Image RAW_SPELL_ICON = new Image(
        ValkyrieView.class.getResourceAsStream(
            TROOPS_PATH_RELATIVE_TO_RESOURCE + "spellIcon/valkyrie.png"
        )
    );

    private static final String TROOP_PATH = "valkyrie/";
    private static final String HEADER_NAME_FILE = "chr_valkyrie_sprite_";

    private static final String SWIRL_RELATIVE_PATH =
        "valkyrie/attack/swirl/chr_valkyrie_sprite.png";

    private static final Image SWIRL_IMAGE = new Image(
        ValkyrieView.class.getResourceAsStream(
            TROOPS_PATH_RELATIVE_TO_RESOURCE + SWIRL_RELATIVE_PATH
        )
    );

    private static final int NUM_INDEX_DIGITS = 3;

    private static final double SHIFT_X = 0;
    private static final double SHIFT_Y = -10;
    private static final double ALPHA_THRESHOLD = 0.5;
    private static final double HEIGHT_IN_TILES = 2;

    // audio time
    private static final int HIT_FRAME = 4;
    private static final int FIRST_STEP = 3;
    private static final int SECOND_STEP = 10;
    private static final double VOLUME_SFX = 0.75;
    private static final double VOLUME_WALK_SFX = 0.5;
    private static final int NUM_ATTACKS_SOUNDS = 7;

    // render layer depth
    private static final int RENDER_LAYER = 1;

    // crop offset from right
    private static final int RIGHT_CROP_PIXELS = 20;

    private static final int CROP_START_X = 0;
    private static final int CROP_START_Y = 0;

    private static final int NOT_FLIPPED = 0;
    private static final int FLIPPED = 1;

    private static final int OPACITY_MULTIPLIER = 1;

    // sprite sheet base indices for different states and sides
    private static final int PLAYER_IDLE_BASE_INDEX = 72;
    private static final int OPPONENT_IDLE_BASE_INDEX = 153;
    private static final int PLAYER_MOVE_BASE_INDEX = 0;
    private static final int OPPONENT_MOVE_BASE_INDEX = 81;
    private static final int PLAYER_ATTACK_BASE_INDEX = 271;
    private static final int OPPONENT_ATTACK_BASE_INDEX = 163;

    private final IGameView gameView = IGameView.getInstance();

    private ValkyrieView() {
        super();

        audioManager.setVolume(AudioType.VALKYRIE_ATTACK_1, VOLUME_SFX);
        audioManager.setVolume(AudioType.VALKYRIE_ATTACK_2, VOLUME_SFX);
        audioManager.setVolume(AudioType.VALKYRIE_ATTACK_3, VOLUME_SFX);
        audioManager.setVolume(AudioType.VALKYRIE_ATTACK_4, VOLUME_SFX);
        audioManager.setVolume(AudioType.VALKYRIE_ATTACK_5, VOLUME_SFX);
        audioManager.setVolume(AudioType.VALKYRIE_ATTACK_6, VOLUME_SFX);
        audioManager.setVolume(AudioType.VALKYRIE_ATTACK_7, VOLUME_SFX);
        audioManager.setVolume(AudioType.VALKYRIE_HIT, VOLUME_SFX);
        audioManager.setVolume(AudioType.VALKYRIE_DEPLOY, VOLUME_SFX);
        audioManager.setVolume(AudioType.VALKYRIE_DEPLOY_END, VOLUME_SFX);
        audioManager.setVolume(AudioType.VALKYRIE_MOVE, VOLUME_WALK_SFX);
    }

    @Override
    protected Image getRawSpellIcon() {
        return RAW_SPELL_ICON;
    }

    @Override
    protected int getPlayerIdleBaseIndex() {
        return PLAYER_IDLE_BASE_INDEX;
    }

    @Override
    protected int getOpponentIdleBaseIndex() {
        return OPPONENT_IDLE_BASE_INDEX;
    }

    @Override
    protected int getPlayerMoveBaseIndex() {
        return PLAYER_MOVE_BASE_INDEX;
    }

    @Override
    protected int getOpponentMoveBaseIndex() {
        return OPPONENT_MOVE_BASE_INDEX;
    }

    @Override
    protected int getPlayerAttackBaseIndex() {
        return PLAYER_ATTACK_BASE_INDEX;
    }

    @Override
    protected int getOpponentAttackBaseIndex() {
        return OPPONENT_ATTACK_BASE_INDEX;
    }

    @Override
    public int getNumFramesPerDirection(State state) {
        return ValkyrieView.numFramesPerDirection.get(state);
    }

    @Override
    protected String getHeaderNamePath() {
        return HEADER_NAME_FILE;
    }

    @Override
    protected String getTroopPath() {
        return TROOP_PATH;
    }

    @Override
    protected int getNumIndexDigits() {
        return NUM_INDEX_DIGITS;
    }

    @Override
    protected Image transformImage(Image image) {
        Image temp = image;

        temp = ImageUtils.enhanceOpacity(temp);

        // crop transparent pixels from right
        temp = ImageUtils.crop(
            image,
            CROP_START_X,
            CROP_START_Y,
            (int) temp.getWidth() - RIGHT_CROP_PIXELS,
            (int) temp.getHeight()
        );

        return ImageUtils.enhanceOpacity(
            temp,
            OPACITY_MULTIPLIER * ALPHA_THRESHOLD
        );
    }

    @Override
    public EntityType getType() {
        return EntityType.PEKKA;
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
        AnimationKey key = new AnimationKey(
            side,
            state,
            direction.fromAngle(angleDirection)
        );

        Image image = animationBuffer.get(key).getFrame(currentFrame);

        double width = image.getWidth() * getImageScale();
        double height = image.getHeight() * getImageScale();

        int flipped = NOT_FLIPPED;
        if (Direction.hasToFlip(angleDirection)) {
            flipped = FLIPPED;
        }

        IMainGUI gui = gameView.getGUI();

        gui.renderWorldImage(
            image,
            centreX + SHIFT_X,
            centreY + SHIFT_Y,
            Math.pow(-1, flipped) * width,
            height,
            false,
            RENDER_LAYER
        );

        if (state == State.ATTACK) {
            // swirl rendering
            gui.renderWorldImage(
                SWIRL_IMAGE,
                centreX + SHIFT_X,
                centreY + SHIFT_Y,
                width,
                height,
                false,
                RENDER_LAYER
            );
        }
    }

    @Override
    public double getSpritesHeight() {
        return SPRITES_HEIGHT;
    }

    @Override
    protected double getHeightInTiles() {
        return HEIGHT_IN_TILES;
    }

    @Override
    public void playMoveAudio(int currentFrame) {
        if (currentFrame == FIRST_STEP || currentFrame == SECOND_STEP)
            audioManager.play(AudioType.VALKYRIE_MOVE);
    }

    @Override
    public void playAttackAudio(int currentFrame) {
        if (currentFrame == START_ATTACK) {
            int randomAttack = (int) Math.floor(Math.random() * NUM_ATTACKS_SOUNDS);
            switch (randomAttack) {
                case 0:
                    audioManager.play(AudioType.VALKYRIE_ATTACK_1);
                    break;
                case 1:
                    audioManager.play(AudioType.VALKYRIE_ATTACK_2);
                    break;
                case 2:
                    audioManager.play(AudioType.VALKYRIE_ATTACK_3);
                    break;
                case 3:
                    audioManager.play(AudioType.VALKYRIE_ATTACK_4);
                    break;
                case 4:
                    audioManager.play(AudioType.VALKYRIE_ATTACK_5);
                    break;
                case 5:
                    audioManager.play(AudioType.VALKYRIE_ATTACK_6);
                    break;
                case 6:
                    audioManager.play(AudioType.VALKYRIE_ATTACK_7);
                    break;

                default:
                    break;
            }
        }
            

        if (currentFrame == HIT_FRAME)
            audioManager.play(AudioType.VALKYRIE_HIT);
    }

    @Override
    public void playDeployAudio() {
        audioManager.play(AudioType.VALKYRIE_DEPLOY);
        audioManager.play(AudioType.VALKYRIE_DEPLOY_END);
    }

    // static methods

    public static EntityView getInstance() {
        if (instance == null) {
            instance = new ValkyrieView();
        }
        return instance;
    }

    public static void setNumFramesPerDirection(
            Map<State, Integer> numFramesPerDirection
    ) {
        ValkyrieView.numFramesPerDirection = Map.copyOf(numFramesPerDirection);
    }
}