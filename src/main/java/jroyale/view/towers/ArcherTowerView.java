package jroyale.view.towers;

import javafx.scene.image.Image;
import jroyale.shared.Enums.EntityType;
import jroyale.shared.Enums.Side;
import jroyale.shared.Enums.State;
import jroyale.view.EntityView;

public class ArcherTowerView extends EntityView{
    
    private static ArcherTowerView instance = null;

    private ArcherTowerView() {}

    // instance methods

    @Override
    public EntityType getType() {
        return EntityType.ARCHER_TOWER;
    }

    @Override
    public void render(double centreX, double centreY, double angleDirection, int currentFrame, State state,
            Side side) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'render'");
    }

    @Override
    protected int getPlayerIdleBaseIndex() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPlayerIdleBaseIndex'");
    }

    @Override
    protected int getOpponentIdleBaseIndex() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOpponentIdleBaseIndex'");
    }

    @Override
    protected int getPlayerMoveBaseIndex() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPlayerMoveBaseIndex'");
    }

    @Override
    protected int getOpponentMoveBaseIndex() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOpponentMoveBaseIndex'");
    }

    @Override
    protected int getPlayerAttackBaseIndex() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPlayerAttackBaseIndex'");
    }

    @Override
    protected int getOpponentAttackBaseIndex() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOpponentAttackBaseIndex'");
    }

    @Override
    public int getNumFramesPerDirection(State state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNumFramesPerDirection'");
    }

    @Override
    protected String getHeaderNamePath() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHeaderNamePath'");
    }

    @Override
    protected String getTroopPath() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTroopPath'");
    }

    @Override
    protected int getNumIndexDigits() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNumIndexDigits'");
    }

    @Override
    protected Image transformImage(Image image) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'transformImage'");
    }

    // static methods

    public static EntityView getInstance() {
        if (instance == null) {
            instance = new ArcherTowerView();
        }
        return instance;
    }
}
