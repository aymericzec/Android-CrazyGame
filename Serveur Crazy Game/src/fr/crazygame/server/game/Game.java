package fr.crazygame.server.game;

import fr.crazygame.server.Context;

public class Game {
	
	private final Context c1;
	private final Context c2;
	private final String nameGame;
	
	public Game(Context c1, Context c2, String nameGame) {
		this.c1 = c1;
		this.c2 = c2;
		this.nameGame = nameGame;
	}
	
	public Context getC1() {
		return c1;
	}
	
	public Context getC2() {
		return c2;
	}
	
	public String getNameGame() {
		return nameGame;
	}
}
