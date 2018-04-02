package fr.crazygame.game.shakegame;

import java.nio.ByteBuffer;

import fr.crazygame.server.Context;
import fr.crazygame.server.State;

public class ShakeGame {
	private final Context player;
	private final Context adversary;
	private boolean isFinish = false;
	private int score;
	
	public ShakeGame(Context player, Context adversary) {
		this.player = player;
		this.adversary = adversary;
	}
	
	public Context getPlayer() {
		return player;
	}
	
	public Context getAdversary() {
		return adversary;
	}
	
	/**
	 * true if the game is finish
	 * @return
	 */
	public boolean receiveScore () {
		
		if (player.getIn().position() >= Integer.BYTES) {
			ByteBuffer in = player.getIn();
			in.flip();
			
			int score = in.getInt();
			this.score = score;
			System.out.println("son score: " + score);
			in.compact();
				
			ByteBuffer out = adversary.getOut();
			out.putInt(score);
			adversary.updateInterestOp();
			//Score du joueur adversaire
			isFinish = true;
		}
		
		return isFinish;
	}

	public boolean isFinish() {
		return isFinish;
	}
	
	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}
	
	public void deleteGame () {
		player.changeState(State.WAIT_PACKET);
		player.finishShakeWord();
	}

	public int getScore() {
		// TODO Auto-generated method stub
		return score;
	}
}
