package jroyale.model;

import java.util.List;
import java.util.ArrayList;

class Tile {
    private List<Troop> playerTroops;
    private List<Troop> opponentTroops;

    Tile() {
        playerTroops = new ArrayList<>();
        opponentTroops = new ArrayList<>();
    }

    void addPlayerTroop(Troop playerTroop) {
        playerTroops.add(playerTroop);
    }

    void addOpponentTroop(Troop opponentTroop) {
        opponentTroops.add(opponentTroop);
    }
}
