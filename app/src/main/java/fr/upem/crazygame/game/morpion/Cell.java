package fr.upem.crazygame.game.morpion;

import fr.upem.crazygame.morpion.Players;

/**
 * Created by myfou on 15/01/2018.
 */

public class Cell {
    private final Players player;
    private final int x;
    private final int y;

    public Cell (Players player, int x, int y) {
        this.player = player;
        this.x = x;
        this.y = y;
    }

    public Players getPlayer() {
        return player;
    }
}
