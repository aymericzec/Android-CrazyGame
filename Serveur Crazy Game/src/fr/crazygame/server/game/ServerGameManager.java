package fr.crazygame.server.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.crazygame.server.Context;

public class ServerGameManager {
	//Name game to list ip search game
	private final Map<String, List<Context>> searchGames;
	
	private ServerGameManager (Map<String, List<Context>> searchGames) {
		this.searchGames = searchGames;
	}
	
	public boolean addIpToSearchGame (String nameGame, Context context) {
		
		List<Context> l;
		if ((l = searchGames.get(nameGame)) != null) {
			System.out.println(nameGame + " " + context);
			l.add(context);
			return true;
		}
		
		return false;
	}
	
	public List<Game> createGames () {
		List<Game> games = new ArrayList<Game>();
		
		for (Map.Entry<String, List<Context>> entry: searchGames.entrySet()) {
			List<Context> player = entry.getValue();
			while (player.size() >= 2) {
				Context p1 = player.get(0);
				player.remove(0);
				Context p2 = player.get(0);
				player.remove(0);
				
				Game game = new Game(p1, p2, entry.getKey());
				games.add(game);
			}
		}
		
		return games;
	}
	
	public static ServerGameManager createServerGameManager () {
		Map<String, List<Context>> searchGames = new HashMap<String, List<Context>>();
		
		searchGames.put("Morpion", new ArrayList<Context>());
		searchGames.put("MixWord", new ArrayList<Context>());
		searchGames.put("ShakeGame", new ArrayList<Context>());
		
		return new ServerGameManager(searchGames);
	}
}
