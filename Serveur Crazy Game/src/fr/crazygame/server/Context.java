package fr.crazygame.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Map;

import fr.crazygame.charset.CharsetServer;
import fr.crazygame.game.mixword.GameMixWord;
import fr.crazygame.game.morpion.GameMorpion;
import fr.crazygame.game.shakegame.ShakeGame;
import fr.crazygame.server.game.ServerGameManager;

public class Context {
	private static final int BUFF_SIZE = 256;
	private boolean close;
	private final ByteBuffer in = ByteBuffer.allocate(BUFF_SIZE);
	private final ByteBuffer out = ByteBuffer.allocate(BUFF_SIZE);
	private final SelectionKey key;
	private final SocketChannel sc;
	private final Configuration config;
	private final ServerCrazyGame scg;
	private PlayerService playerService;
	private final ServerGameManager serverGameManager;
	private int clean = 0;
	private final ScoreManager scoreManager;

	private GameMorpion gameMorpion = null;
	private GameMixWord gameMixWord = null;
	private ShakeGame shakeGame = null;

	private State state = State.WAIT_LENGTH_LANGUAGE;

	public Context(SelectionKey key, Configuration config, ServerCrazyGame scg,
			ServerGameManager serverGameManager, ScoreManager scoreManager) {
		this.key = key;
		this.sc = (SocketChannel) key.channel();
		this.config = config;
		this.scg = scg;
		this.playerService = new PlayerService(in, out);
		this.serverGameManager = serverGameManager;
		this.scoreManager = scoreManager;
	}

	public void doRead() throws IOException {
		if (-1 == sc.read(this.in)) {
			this.close = true;
		} else {
			process();
		}
		updateInterestOp();
	}

	public void doWrite() throws IOException {
		out.flip();
		System.out.println("envoie de donnée");
		sc.write(out);
		out.compact();
		//process();
		updateInterestOp();
	}

	public void updateInterestOp() {
		int interestOps = 0;

		// Read
		if (!close && in.hasRemaining()) {
			interestOps |= SelectionKey.OP_READ;
		}

		System.out.println(out.position() + " position");

		// Write
		if (out.position() != 0) {
			System.out.println("ecriture");
			interestOps |= SelectionKey.OP_WRITE;
		}

		if (interestOps == 0) {
			System.out.println("fermeture silencieuse");
			ServerCrazyGame.silentlyClose((SocketChannel) key.channel());
		} else {
			key.interestOps(interestOps);
		}
	}

	/**
	 * Change the status of player
	 */
	private boolean changeState() {
		in.flip();
		int action = in.getInt();
		System.out.println("Action " + action + new Date());

		// clean
		if (action != 1 && action != 4200 && action != 4300) {
			// Read action byte, example if receive request of mixword
			if (this.in.position() + action <= this.in.limit()) {
				int oldLimit = this.in.limit();
				this.in.limit(action + Integer.BYTES);
				this.in.get();
				this.in.limit(oldLimit);
				this.in.compact();
			} else {
				this.in.position(0);
				return false;
			}
		}

		switch (action) {
		case 1:
			state = State.WAIT_LENGTH_NAME_GAME;
			scg.getLogs().clientFirstInt(this.sc, action);
			break;
		case 4200:
			state = State.WAIT_SCORE;
			scg.getLogs().clientSubscribeScore(this.sc);
			break;
		case 4300:
			state = State.WAIT_SCORE_WORLD;
			scg.getLogs().clientAskScoreWorld(this.sc);
			break;
		default:
			break;
		}

		in.compact();
		return true;
	}

	private void process() {
		
		System.out.println(state.name() + " " + this.sc.socket().getPort());
		
		//Permet de switcher entre l'état en attente de score ou en attente de la longueur du nom de jeu
		while (true) {
			switch (state) {
			case WAIT_LENGTH_LANGUAGE:
				if (playerService.waitLengthLanguage()) {
					state = State.WAIT_LANGUAGE;
					scg.getLogs().clientReceiveLenghtLanguage(this.sc,
							playerService.getLengthLanguage());
				} else {
					break;
				}
			case WAIT_LANGUAGE:
				if (playerService.waiLanguage()) {
					scg.getLogs().clientReceiveLanguage(this.sc,
							playerService.getNameLanguage());
					state = State.WAIT_PACKET;
				} else {
					break;
				}
			case WAIT_PACKET:
				if (playerService.waitPacket()) {
					scg.getLogs().clientReceptionInt(this.sc);
					if (!changeState()) {
						break;
					}
					
					System.out.println(state.name());
					continue;
				}
				break;
			case WAIT_SCORE_WORLD:
				System.out.println("ok Score");
				Map<String, Integer> scores = scoreManager.getScores();
				
				out.putInt(scores.size());
				
				for (Map.Entry<String, Integer> entries: scores.entrySet()) {
					ByteBuffer nameGame = CharsetServer.CHARSET_UTF_8.encode(entries.getKey());
					System.out.println(nameGame.position() + " " + nameGame.limit());
					out.putInt(nameGame.limit());
					out.put(nameGame);
					out.putInt(entries.getValue());

					System.out.println(entries.getKey() + " " + entries.getValue());
				}
				
				System.out.println("Coucou");
				this.updateInterestOp();
				state = State.WAIT_PACKET;
				break;
			case WAIT_SCORE:
				try {
					scg.getLogs().clientSendScore(sc);
					scoreManager.readData(sc, in);
					
				} catch (IOException e) {
					System.err.println(e + " " + e.getMessage());
					return;
				}
				break;
			case WAIT_LENGTH_NAME_GAME:
				System.out.println(this.state);
				System.out.println(in.position() + " " + in.limit());

				if (playerService.searchLengthGame()) {
					state = State.WAIT_NAME_GAME;
					scg.getLogs().clientReceptionLengthNameGame(sc,
							this.playerService.getSizeName());
				} else {
					break;
				}
			case WAIT_NAME_GAME:
				if (playerService.searchNameGame().isPresent()) {
					this.state = State.WAIT_GAME;
				} else {
					break;
				}
			case WAIT_ADD_GAME:
				String nameGame = playerService.searchNameGame().get();
				scg.getLogs().clientReceiveNameGame(sc, nameGame);
				if (serverGameManager.addIpToSearchGame(nameGame, this)) {
					scg.getLogs().clientSearchGame(sc, nameGame);
					this.state = State.WAIT_GAME;
				}
				break;
			case WAIT_GAME:
				String nameWaitGame = playerService.searchNameGame().get();
				scg.getLogs().clientWaitGame(sc, nameWaitGame);
				break;
			case GAME_MORPION:
				boolean send = gameMorpion.sendCell();
				if (send) {
					scg.getLogs().clientPlayCellMorpion(sc);

					if (gameMorpion.isFinish()) {
						scg.getLogs().clientFinishMorpion(sc, gameMorpion);
						gameMorpion.deleteGame();
					}
				}
				break;
			case WAIT_PLAYER_MORPION:
				scg.getLogs().clientWaitTurnMorpion(sc);
				break;
			case GAME_MIXWORD:
				scg.getLogs().clientReceiveWord(sc, this.gameMixWord.getWord());

				if (gameMixWord.receiveWord()) {
					scg.getLogs().clientFinishMixWord(sc, gameMixWord);
					gameMixWord.deleteGame();
				}
				break;
			case GAME_SHAKE:
				shakeGame.receiveScore();
				
				if (shakeGame.isFinish()) {
					System.out.println(sc.socket().getInetAddress() + " " + sc.socket().getPort());
					scg.getLogs().clientReceiveScoreShake(sc, shakeGame.getScore());
					scg.getLogs().clientFinishShakeGame(sc);
					shakeGame.deleteGame();
				}
				break;
			default:
				break;
			}
			break;
		}
	}

	private void cleanBuffer() {
		if (in.position() >= clean) {
			scg.getLogs().clientCleanBuffer(sc, clean);
			in.flip();
			in.get();
			in.compact();
			state = State.WAIT_PACKET;
		}

	}

	public PlayerService getPlayerService() {
		return playerService;
	}

	public void foundGameMorpionWaitPlayer(GameMorpion gameMixWord) {
		this.state = State.WAIT_PLAYER_MORPION;
		this.gameMorpion = gameMixWord;
	}

	public void foundGameMorpion(GameMorpion gameMorpion) {
		this.state = State.GAME_MORPION;
		this.gameMorpion = gameMorpion;
	}

	public void foundGameMixWord(GameMixWord gameMixWord) {
		this.state = State.GAME_MIXWORD;
		this.gameMixWord = gameMixWord;
	}
	
	public void foundGameShakeGame(ShakeGame shakeGame) {
		this.state = State.GAME_SHAKE;
		this.shakeGame = shakeGame;
	}

	public void finishGameMorpion() {
		gameMorpion = null;
		playerService.cleanGameService();
	}

	public void finishGameMixWord() {
		gameMorpion = null;
		playerService.cleanGameService();
		scg.getLogs().clientEndMixWord(sc);
	}
	

	public void finishShakeWord() {
		shakeGame = null;
		playerService.cleanGameService();
		scg.getLogs().clientEndShake(sc);	
	}

	public ByteBuffer getIn() {
		return in;
	}

	public ByteBuffer getOut() {
		return out;
	}

	public void changeState(State state) {
		this.state = state;
	}


}
