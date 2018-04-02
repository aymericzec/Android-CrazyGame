package fr.crazygame.game.morpion;

import java.nio.ByteBuffer;

import fr.crazygame.server.Context;
import fr.crazygame.server.State;

public class GameMorpion {
	private final Context player;
	private final Context adversary;
	private boolean isFinish = false;
	
	public GameMorpion(Context player, Context adversary) {
		this.player = player;
		this.adversary = adversary;
	}
	
	public Context getPlayer() {
		return player;
	}
	
	public Context getAdversary() {
		return adversary;
	}
	
	public boolean sendCell () {
		
		if (player.getIn().position() >= 3*Integer.BYTES) {
			player.getIn().flip();
			int id = player.getIn().getInt();
			
			int i = player.getIn().getInt();
			int j = player.getIn().getInt();
			
			player.getIn().compact();
			
			ByteBuffer out = adversary.getOut();
			
			out.putInt(id);
			out.putInt(i);
			out.putInt(j);
			
			player.changeState(State.WAIT_PLAYER_MORPION);
			adversary.changeState(State.GAME_MORPION);

			adversary.updateInterestOp();
			
			//1 is normal, 2 a player win
			if (id == 2) {
				isFinish = true;
			}
			
			return true;
		}		
		
		return false;
	}

	public boolean isFinish() {
		return isFinish;
	}
	
	public void deleteGame () {
		player.changeState(State.WAIT_PACKET);
		adversary.changeState(State.WAIT_PACKET);
		player.finishGameMorpion();
		adversary.finishGameMorpion();
	}
}
