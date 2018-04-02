package fr.crazygame.launcher;

import java.io.IOException;
import java.nio.file.Paths;

import fr.crazygame.server.ServerCrazyGame;

public class Main {
	public static void main(String[] args) {
		try {
			ServerCrazyGame server = ServerCrazyGame.createServerCrazyGame(Paths.get("./Config/config.json"));
			server.launch();
		} catch (IOException e) {
			// TODO Auto-generated catch blocks
			e.printStackTrace();
		}
	}

}