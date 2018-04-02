package fr.crazygame.log;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.crazygame.game.mixword.GameMixWord;
import fr.crazygame.game.morpion.GameMorpion;

public class Logs {
	private final String rep;
	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	public Logs(String rep) {
		this.rep = rep;
	}

	public void writePath(String message) {
		try {
			Path path = Paths.get(rep + format.format(new Date()));
			Files.write(path, message.getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE,
					StandardOpenOption.WRITE);
		} catch (IOException e) {
			System.err.println("Error file logs: " + e);
		}
	}


	public void launchServer () {
		writePath(new Date() + ": Lancement du serveur\n");
	}

	public void stopServer() {
		writePath(new Date() + ": Arr�t du serveur\n");
	}
	
	public void clientConnect (SocketChannel sc) {
		writePath("Un client s'est connect� � l'addresse: " + sc.socket().getInetAddress() + "\n");
	}
	
	public void clientDisconnect (SocketChannel sc) {
		writePath(new Date() + "Un client s'est d�connect� � l'addresse: " + sc.socket().getInetAddress() + "\n");
	}
	
	public void clientSearchGame (SocketChannel sc, String nameGame) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " Recherche une partie pour " + nameGame + "\n");
	}

	public void clientFirstInt(SocketChannel sc, int action) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " Action demand�e: " + action + "\n");
	}

	public void clientReceptionInt(SocketChannel sc) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " Integer receptionn�\n");
	}

	public void clientReceptionLengthNameGame(SocketChannel sc, int length) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " Longueur du nom: " + length +  "\n");
	}

	public void clientReceiveNameGame(SocketChannel sc, String nameGame) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " Nom: " + nameGame +  "\n");
	}

	public void clientWaitGame(SocketChannel sc, String nameWaitGame) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " est en attente d'une partie pour " + nameWaitGame +  "\n");
	}

	public void clientFoundGame() {
		writePath(new Date() + " " + "Une partie � �t� cr�e\n");
	}

	public void clientPlayCellMorpion(SocketChannel sc) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " a jou� un coup au morpion\n");
		
	}

	public void clientWaitTurnMorpion(SocketChannel sc) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " attends son tour au morpion\n");
		
	}

	public void clientFinishMorpion(SocketChannel sc, GameMorpion gameMorpion) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " a mis fin � la partie de Morpion\n");
	}

	public void clientReceiveLenghtLanguage(SocketChannel sc, int length) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " a envoy� une longueur de taille de laguage " + length + "\n");
	}

	public void clientReceiveLanguage(SocketChannel sc, String nameLanguage) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " a envoy� le langage " + nameLanguage + "\n");
		
	}

	public void clientReceiveWord(SocketChannel sc, String word) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " doit trouv� le mot " + word + "\n");
	}

	public void clientFinishMixWord(SocketChannel sc, GameMixWord gameMixWord) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " a mis fin � la partie de MixWord avec le mot " + gameMixWord.getWord() + "\n");
		
	}

	public void clientEndMixWord(SocketChannel sc) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " nettoyage de la partie MixWord\n");
		
	}

	public void clientCleanBuffer(SocketChannel sc, int clean) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " Nettoyage de " + clean + "octects");
		
	}

	public void clientSubscribeScore(SocketChannel sc) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " est pr�t � envoy� des scores de jeux\n");
		
	}

	public void clientSendScore(SocketChannel sc) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " a envoy� des donn�es de jeux\n");
	}

	public void clientEndShake(SocketChannel sc) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " nettoyage de la partie ShakeGame\n");
	}

	public void clientReceiveScoreShake(SocketChannel sc, int score) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " a re�u un score de ShakeGame " + score +  "\n");
	}

	public void clientFinishShakeGame(SocketChannel sc) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " � finie la partie ShakeGame\n");
	}

	public void clientAskScoreWorld(SocketChannel sc) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " � demand� les scores mondiaux\n");
		
	}

	public void clientCleanGame(SocketChannel sc) {
		writePath(new Date() + " " + sc.socket().getInetAddress() + " � supprim� le jeux auquel il jouait. Un joueur adversaire s'est probablement deconnect�\n");
	}
}
