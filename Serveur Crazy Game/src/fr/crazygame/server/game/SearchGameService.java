package fr.crazygame.server.game;

import java.nio.ByteBuffer;
import java.util.Optional;

import fr.crazygame.charset.CharsetServer;

public class SearchGameService {
	private StringBuilder nameGame = new StringBuilder();
	private boolean isFinish = false;
	private ByteBuffer in;
	private int sizeNameGame;
	
	public SearchGameService (ByteBuffer in, int sizeNameGame) {
		this.in = in;
		this.sizeNameGame = sizeNameGame;
	}
	
	/**
	 * Read the data, first the length of name game and after the name of game that the player want play.
	 */
	public Optional<String> readReceiveGame () {
		if (!isFinish) {
			if (in.position() >= sizeNameGame) {	
				in.flip();
				int oldLimite = in.limit();
				in.limit(sizeNameGame);
				nameGame.append(CharsetServer.CHARSET_UTF_8.decode(in));
				in.limit(oldLimite);
				isFinish = true;
				in.compact();
				return Optional.of(this.nameGame.toString());
			} else {
				return Optional.empty();
			}
		}
		
		return Optional.of(this.nameGame.toString());
	}
	
	
	public boolean isCompleted () {
		return isFinish;
	}
	
	public int getSizeNameGame() {
		return sizeNameGame;
	}
}
