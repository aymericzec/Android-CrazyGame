package fr.upem.crazygame.Score;

/**
 * Created by myfou on 04/03/2018.
 */

public class Score {
    private final String nameGame;
    private final int nbGamePlay;
    private final int nbGameWin;

    public Score (String nameGame, int nbGamePlay, int nbGameWin) {
        this.nbGameWin = nbGameWin;
        this.nbGamePlay = nbGamePlay;
        this.nameGame = nameGame;
    }

    public int getGame() {
        return nbGamePlay;
    }

    public int getGameWin() {
        return nbGameWin;
    }

    public String getName() {
        return nameGame;
    }
}
