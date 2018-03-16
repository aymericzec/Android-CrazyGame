package fr.upem.crazygame.game.morpion;

import android.util.Log;

import fr.upem.crazygame.game.Players;



public class Morpion {

    private Players current;
    private Players begin;
    private Cell [][] gameBoard = new Cell[3][3];
    private String [][] colorBoard = new String[3][3];

    public Morpion (Players current, Players begin) {
        this.current = current;
        this.begin = begin;
    }

    /**
     * Initialize a morpion game with a player who begins.
     * @param current
     */
    public void initMorpion (Players current) {
        gameBoard = new Cell[3][3];
        colorBoard = new String[3][3];
        this.current = current;
    }

    public String getColor(int i, int j) {
        return colorBoard[i][j];
    }

    /**
     * Play a round with the cell and player who play.
     * @param x the row
     * @param y the column
     * @return true if the cell is not already use, false else
     */
    public boolean playAround (Players play, int x, int y) {
        if (x < 0 || x > 2 || y < 0 || y > 2) {
            throw new IllegalArgumentException("x and y must be between 0 and 2: " + x + " " + y);
        }

        if (gameBoard[x][y] != null) {
            return false;
        }
        gameBoard[x][y] = new Cell(play, x, y);

        return true;
    }

    /**
     * Check if there is a winner or null else
     * @return Players or null if there is not winner
     */
    public Players winner () {
        //Check the row
        for (int i = 0; i < gameBoard.length; i++) {
            if (gameBoard[i][0] != null && gameBoard[i][1] != null && gameBoard[i][2] != null) {
                if (gameBoard[i][0].getPlayer().equals(gameBoard[i][1].getPlayer()) && gameBoard[i][0].getPlayer().equals(gameBoard[i][2].getPlayer())) {
                    Log.d("Perdu 1", i + "");
                    colorBoard[i][0] = "red";
                    colorBoard[i][1] = "red";
                    colorBoard[i][2] = "red";
                    return gameBoard[i][0].getPlayer();
                }
            }
        }

        //Check the column
        for (int i = 0; i < gameBoard.length; i++) {
            if (gameBoard[0][i] != null && gameBoard[1][i] != null && gameBoard[2][i] != null) {
                if (gameBoard[0][i].getPlayer().equals(gameBoard[1][i].getPlayer()) && gameBoard[0][i].getPlayer().equals(gameBoard[2][i].getPlayer())) {
                    Log.d("Perdu 2", i + "");
                    colorBoard[0][i] = "red";
                    colorBoard[1][i] = "red";
                    colorBoard[2][i] = "red";
                    return gameBoard[0][i].getPlayer();
                }
            }
        }

        //Check diagonal
        for (int i = 0; i < gameBoard.length; i++) {
            if (gameBoard[0][0] != null && gameBoard[1][1] != null && gameBoard[2][2] != null) {
                Log.d("Test diagonale", gameBoard[0][0].getPlayer() + " " + gameBoard[1][1].getPlayer() + " " + gameBoard[2][2].getPlayer());
                if (gameBoard[0][0].getPlayer().equals(gameBoard[1][1].getPlayer()) && gameBoard[0][0].getPlayer().equals(gameBoard[2][2].getPlayer())) {
                    Log.d("Perdu 3", i + "");
                    colorBoard[0][0] = "red";
                    colorBoard[1][1] = "red";
                    colorBoard[2][2] = "red";
                    return gameBoard[0][0].getPlayer();
                }
            }
        }

        for (int i = 0; i < gameBoard.length; i++) {
            if (gameBoard[0][2] != null && gameBoard[1][1] != null && gameBoard[2][0] != null) {
                if (gameBoard[0][2].getPlayer().equals(gameBoard[1][1].getPlayer()) && gameBoard[0][2].getPlayer().equals(gameBoard[2][0].getPlayer())) {
                    Log.d("Perdu 4", i + "");
                    colorBoard[0][2] = "red";
                    colorBoard[1][1] = "red";
                    colorBoard[2][0] = "red";
                    return gameBoard[0][2].getPlayer();
                }
            }
        }

        Log.d("Null", "Pas perdu");
        return null;
    }

    /**
     * Return a boolean which say if the party is finished
     * @return
     */
    public boolean isFinish () {
        if (null == this.winner()) {
            for (int i = 0; i < gameBoard.length; i++) {
                for (int j = 0; j < gameBoard.length; j++) {
                    if (gameBoard[i][j] == null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
