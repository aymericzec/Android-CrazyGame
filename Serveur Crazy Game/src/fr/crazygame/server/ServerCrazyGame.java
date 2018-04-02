package fr.crazygame.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import fr.crazygame.game.mixword.GameMixWord;
import fr.crazygame.game.morpion.GameMorpion;
import fr.crazygame.game.shakegame.ShakeGame;
import fr.crazygame.log.Logs;
import fr.crazygame.server.game.Game;
import fr.crazygame.server.game.ServerGameManager;

public class ServerCrazyGame {
	
	private static String[] MIX_WORDS_AVAILABLE = {"verre", "vert", "foire", "bleu", "trou"};
	
	
	private final ServerSocketChannel serverSocketChannel;
	private final Selector selector;
	private final Set<SelectionKey> selectedKeys;
	private final Configuration config;
	private final Logs logs;
	private final Map<String, String> users = new HashMap<String, String>();
	private final ServerGameManager serverGameManager;
	private final ScoreManager scoreManager;

	public ServerCrazyGame(ServerSocketChannel sc, Selector selector,
			Set<SelectionKey> selectedKeys, Configuration config, Logs logs,
			ServerGameManager serverGameManager, ScoreManager scoreManager) {
		this.serverSocketChannel = sc;
		this.selector = selector;
		this.selectedKeys = selectedKeys;
		this.config = config;
		this.logs = logs;
		this.serverGameManager = serverGameManager;
		this.scoreManager = scoreManager;
	}

	public Logs getLogs() {
		return logs;
	}

	private void doAccept(SelectionKey key) throws IOException {
		SocketChannel sc = serverSocketChannel.accept();
		if (sc == null) {
			return; // In case, the selector gave a bad hint
		}
		sc.configureBlocking(false);

		SelectionKey clientKey = sc.register(selector, SelectionKey.OP_READ);
		clientKey
				.attach(new Context(clientKey, config, this, serverGameManager, scoreManager));
		logs.clientConnect(sc);
	}

	private void processSelectedKeys() {
		System.out.println("Client: " + selectedKeys.size());
		for (SelectionKey key : selectedKeys) {
			if (key.isValid() && key.isAcceptable()) {
				try {
					doAccept(key);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			Context context = (Context) key.attachment();

			if (key.isValid() && key.isWritable()) {
				System.out.println("Ecriture");
				try {
					context.doWrite();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (key.isValid() && key.isReadable()) {
				System.out.println("Lecture");
				try {
					context.doRead();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}

	public Thread startCommandListener(InputStream in) {

		Thread listener = new Thread(() -> {
			Scanner sc = new Scanner(in);
			while (sc.hasNext() && !Thread.currentThread().isInterrupted()) {
				String entry = sc.next();
				// cmd.offer(entry);
				selector.wakeup();
			}

			sc.close();
		});

		listener.start();

		return listener;
	}

	public void shutdown() throws IOException {
		this.selector.close();
	}

	public void shutdownNow() throws IOException {
		for (SelectionKey key : selector.keys()) {
			if (!(key.channel() instanceof ServerSocketChannel)) {
				silentlyClose(key.channel());
			}

		}

		this.serverSocketChannel.close();
	}

	public void info() throws IOException {
		System.out.println("Nombres de clients connectés: "
				+ (selector.keys().size() - 1));
	}

	/**
	 * Method to launch the server
	 * 
	 * @throws IOException
	 */
	public void launch() throws IOException {
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		Set<SelectionKey> selectedKeys = selector.selectedKeys();

		while (!Thread.interrupted()) {
			/*
			 * String c = null; while (null != (c = cmd.poll())) { switch (c) {
			 * case "SHUTDOWN": System.err.println(
			 * "\nLe serveur n'accepte plus de nouvelle connexion\n");
			 * shutdown();
			 * 
			 * break; case "SHUTDOWN NOW":
			 * System.err.println("Arrêt du serveur"); shutdownNow();
			 * t.interrupt(); return; case "INFO": info(); break; default:
			 * System.err.println("Commande non Reconnue: " + c); break; } }
			 */
			// printKeys();
			// System.out.println("Starting select");
			selector.select(10000);
			// System.out.println("Select finished");
			// printSelectedKey();
			processSelectedKeys();
			sendGameFound();

			selectedKeys.clear();
		}
	}

	/**
	 * Add to buffer out the game found
	 */

	private void sendGameFound() {
		List<Game> games = serverGameManager.createGames();

		System.out.println("Parcours des parties " + games.size());

		for (Game game : games) {
			Context c1 = game.getC1();
			Context c2 = game.getC2();
			c1.getPlayerService().putGameToBuffer(game.getNameGame(), 1);
			c2.getPlayerService().putGameToBuffer(game.getNameGame(), 2);

			switch (game.getNameGame()) {
			case "Morpion":
				c1.foundGameMorpion(new GameMorpion(c1, c2));
				c2.foundGameMorpionWaitPlayer(new GameMorpion(c2, c1));
				break;
			case "MixWord":
				int i = new Random().nextInt(MIX_WORDS_AVAILABLE.length);
				List<String> a = Arrays.asList(MIX_WORDS_AVAILABLE[i].split(""));
				Collections.shuffle(a);
				String wordShuffle = a.stream().reduce((x, y) -> x+y).get();
			
				System.out.println(wordShuffle);
				
				c1.foundGameMixWord(new GameMixWord(c1, c2, MIX_WORDS_AVAILABLE[i]));
				c1.getPlayerService().putWordToBuffer(wordShuffle);
				c2.foundGameMixWord(new GameMixWord(c2, c1, MIX_WORDS_AVAILABLE[i]));
				c2.getPlayerService().putWordToBuffer(wordShuffle);
				break;
			case "ShakeGame":
				c1.foundGameShakeGame(new ShakeGame(c1, c2));
				c2.foundGameShakeGame(new ShakeGame(c2, c1));
				break;
			default:
				break;
			}
			
			c1.updateInterestOp();
			c2.updateInterestOp();

			logs.clientFoundGame();
		}
	}

	public static void silentlyClose(SelectableChannel sc) {
		if (sc == null)
			return;
		try {
			sc.close();
		} catch (IOException e) {
			// silently ignore
		}
	}

	/**
	 * Create an object which represent the server
	 * 
	 * @param pathConfig
	 * @return ServerCrazyGame
	 * @throws IOException
	 *             if not possible to create the server
	 */

	public static ServerCrazyGame createServerCrazyGame(Path pathConfig)
			throws IOException {
		Configuration config = Configuration.getConfig(pathConfig);
		ServerSocketChannel sc = ServerSocketChannel.open();
		sc.bind(new InetSocketAddress(config.getPort()));
		Selector selector = Selector.open();
		Set<SelectionKey> selectedKeys = selector.selectedKeys();
		Logs logs = new Logs(config.getRepLogs());
		ServerGameManager serverGameManager = ServerGameManager
				.createServerGameManager();

		ScoreManager scoreManager = ScoreManager.createScoreManager();
		
		logs.launchServer();

		System.out.println(config.getPort());

		ServerCrazyGame scg = new ServerCrazyGame(sc, selector, selectedKeys,
				config, logs, serverGameManager, scoreManager);

		return scg;
	}
}
