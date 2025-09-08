package jroyale.model;

public class Model implements IModel{
    // 18x30 it's the map size, each Tile 
    // has its own List where character are inserted based on
    // their position (so the collision algoritm will be much
    // more efficient)
    
    private Tile[][] map = new Tile[30][18];

    public Model() {
        // TODO: popolare la mappa
    }

    // location tower: (8.5, 26.5)
    @Override
    public int[] getPlayerTowerPosition() {
        // TODO Auto-generated method stub
        return new int[]{};
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
    }
    
}
