package fr.upem.crazygame.Score;

/**
 * Created by myfou on 04/03/2018.
 */

public class Score {

    private final String name;
    private final int game;
    private final int gameWin;

    public Score (String name, int game, int gameWin) {
        this.gameWin = gameWin;
        this.name = name;
        this.game = game;
    }

    public int getGame() {
        return game;
    }

    public int getGameWin() {
        return gameWin;
    }

    public String getName() {
        return name;
    }
}
