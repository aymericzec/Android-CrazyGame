package fr.crazygame.server.game;

import java.nio.ByteBuffer;
import java.util.Optional;

import fr.crazygame.server.State;


public class PlayerService {
	
	private final ByteBuffer in;
	private State state;
	private SearchGameService searchGame = null;
	
	public PlayerService (ByteBuffer in, State state) {
		this.in = in;
		this.state = state;
	}
	
	/**
	 * Wait that a packet contains 4 bytes for get a int and get the action.
	 * In is not flip in this method
	 * @return true if contains 4 bytes or more
	 */
	public boolean waitPacket() {
		return in.position() >= Integer.BYTES;
	}
	
	/**
	 * Change the status of player
	 */
	public void changeState () {
		in.flip();
		int action = in.getInt();
		
		switch (action) {
		case 1:
			state = State.WAIT_LENGTH_NAME_GAME;
		}
		
	}
	
	/**
	 * Search the name of the game that the player want play
	 * @return true if the name is found
	 */
	public boolean searchLengthGame() {
		if (this.searchGame == null) {
			if (in.limit() - in.position() > Integer.BYTES) {
				int size = in.getInt();
				state = State.WAIT_NAME_GAME;
				this.searchGame = new SearchGameService(in, size);
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Search the name of the game that the player want play
	 * @return true if the name is found
	 */
	public Optional<String> searchNameGame() {
		return this.searchNameGame();
	}
}
