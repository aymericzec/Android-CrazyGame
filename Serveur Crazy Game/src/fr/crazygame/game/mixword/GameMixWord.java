package fr.crazygame.game.mixword;

import java.nio.ByteBuffer;

import fr.crazygame.charset.CharsetServer;
import fr.crazygame.server.Context;
import fr.crazygame.server.State;

public class GameMixWord {
	private final Context player;
	private final Context adversary;
	private boolean isFinish = false;
	private final String word;
	private final int sizeWord;
	
	public GameMixWord(Context player, Context adversary, String word) {
		this.player = player;
		this.adversary = adversary;
		this.word = word;
		this.sizeWord = CharsetServer.CHARSET_UTF_8.encode(word).limit();
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
	public boolean receiveWord () {
		
		if (player.getIn().position() >= Integer.BYTES + sizeWord) {
			ByteBuffer in = player.getIn();
			in.flip();
			int sizeWord = in.getInt();
			int tmpLimit = in.limit();
			
			in.limit(in.position() + sizeWord);
			String word = CharsetServer.CHARSET_UTF_8.decode(in).toString();
			
			in.limit(tmpLimit);
			in.compact();
			System.out.println(word);
			if (this.word.equals(word)) {
				ByteBuffer out = adversary.getOut();
				out.putInt(2);
				out.putInt(sizeWord);
				out.put(CharsetServer.CHARSET_UTF_8.encode(word));
				
				ByteBuffer outCurrent = player.getOut();
				outCurrent.putInt(1);
				isFinish = true;
			} else {
				System.out.println("Le joueur s'est trompé de mot");
				ByteBuffer outCurrent = player.getOut();
				outCurrent.putInt(3);
			}


			player.updateInterestOp();
			adversary.updateInterestOp();
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
		adversary.changeState(State.WAIT_PACKET);
		player.finishGameMixWord();
		adversary.finishGameMixWord();
	}
	
	public String getWord() {
		return word;
	}
}
