package fr.crazygame.server;

import java.nio.ByteBuffer;
import java.util.Optional;

import fr.crazygame.charset.CharsetServer;
import fr.crazygame.server.game.SearchGameService;


public class PlayerService {
	
	private final ByteBuffer in;
	private final ByteBuffer out;
	private SearchGameService searchGameService = null;
	private String nameLanguage;
	private int lengthLanguage;
	
	public PlayerService (ByteBuffer in, ByteBuffer out) {
		this.in = in;
		this.out = out;
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
	 * Search the name of the game that the player want play
	 * @return true if the name is found
	 */
	public boolean searchLengthGame() {
		if (this.searchGameService == null) {
			if (in.position() > Integer.BYTES) {
				in.flip();
				int size = in.getInt();
				
				System.out.println(size);
				this.searchGameService = new SearchGameService(in, size);
				in.compact();
				return true;
			}
		}
		
		in.compact();
		return false;
	}
	
	/**
	 * Search the name of the game that the player want play
	 * @return true if the name is found
	 */
	public Optional<String> searchNameGame() {
		if (this.searchGameService == null) {
			throw new IllegalStateException();
		}
		
		return this.searchGameService.readReceiveGame();
	}
	
	public int getSizeName () {
		if (this.searchGameService != null ) {
			return this.searchGameService.getSizeNameGame();
		}
		
		return -1;
	}
	
	public void putGameToBuffer (String name, int begin) {
		out.putInt(1);
		ByteBuffer tmp = CharsetServer.CHARSET_UTF_8.encode(name);
		out.putInt(tmp.limit());
		out.put(tmp);
		out.putInt(begin); //1 if begin 2 else
		
		System.out.println(out.position());
	}
	
	public void putWordToBuffer (String word) {
		ByteBuffer tmp = CharsetServer.CHARSET_UTF_8.encode(word);
		out.putInt(tmp.limit());
		out.put(tmp);
	}
	
	public void cleanGameService () {
		this.searchGameService = null;
	}
	
	public boolean waitLengthLanguage() {
		if (in.position() >= Integer.BYTES) {
			in.flip();
			lengthLanguage = in.getInt();
			System.out.println("Longueur " + lengthLanguage);
			in.compact();
			return true;
		}
		
		return false;
	}
	
	public boolean waiLanguage() {
		if (in.position() >= lengthLanguage) {
			in.flip();
			
			int oldLimit = in.limit();
			in.limit(in.position() + lengthLanguage);
			
			nameLanguage = CharsetServer.CHARSET_UTF_8.decode(in).toString();
			
			in.limit(oldLimit);
			in.compact();
			return true;
		}
		
		return false;
	}

	public int getLengthLanguage() {
		return lengthLanguage;
	}
	
	public String getNameLanguage() {
		return nameLanguage;
	}
}
