package fr.upem.crazygame.game.morpion;


import fr.upem.crazygame.game.Players;


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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
