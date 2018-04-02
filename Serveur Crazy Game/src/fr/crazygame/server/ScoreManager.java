package fr.crazygame.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import fr.crazygame.charset.CharsetServer;

public class ScoreManager {
	private Map<String, Integer> scores;

	private ScoreManager(Map<String, Integer> scores) {
		this.scores = scores;
	}

	public boolean readData(SocketChannel sc, ByteBuffer buffer) throws IOException {

		//sc.read(buffer);
		System.out.println("readdata" + buffer.position());
		if (buffer.position() >= Integer.BYTES) {
			buffer.flip();
			int oldPosition = buffer.position();
			System.out.println("OldPostiion " + oldPosition + " " + buffer.limit());
			// Si on peut lire un entier, sinon on remet
			while (buffer.limit() >= Integer.BYTES) {
				oldPosition = buffer.position();
				int sizeWord = buffer.getInt();

				// Code de fin
				if (sizeWord == 400) {
					this.scores.entrySet().stream().forEach(x -> System.out.println(x.getKey() + " " + x.getValue()));
					
					//scores.clear();
					buffer.compact();
					return true; // Finis
				}
				// Si on peut lire la taille du mot plus le score sinon on remet
				if (buffer.limit() >= sizeWord - (Integer.BYTES * 2)) {
					int oldLimit = buffer.limit();
					buffer.limit(buffer.position() + sizeWord);
					String nameGame = CharsetServer.CHARSET_UTF_8
							.decode(buffer).toString().toLowerCase();
					buffer.limit(oldLimit);
					int value = buffer.getInt();
					
					if (null != scores.get(nameGame)) {
						System.out.println("ajout de valeur" + value);
						scores.merge(nameGame, value, (x,y) -> x+y);
					}

					buffer.limit(oldLimit);
				}
			}
			
			buffer.position(oldPosition);
			buffer.compact();
		}

		return false;
	}
	
	public Map<String, Integer> getScores() {
		return scores;
	}
	
	public static ScoreManager createScoreManager () {
		Map<String, Integer> scores = new HashMap<String, Integer>();
		
		scores.put("morpion", 0);
		scores.put("mixword", 0);
		scores.put("shakegame", 0);
		
		return new ScoreManager(scores);
		
	}
}
