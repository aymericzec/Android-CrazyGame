package fr.upem.crazygame.game.mixwords;


/**
 * Modèle de MixWords
 */

public class MixWords {

    private final String mixWord;
    private final String[] wordOrder;
    private final String[] keyboard;

    public  MixWords(String mixWord) {
        this.mixWord = mixWord;
        wordOrder = new String[mixWord.length()];
        keyboard = mixWord.split("");
    }

    public int addCaracter (String c) {
        int i = 0;

        for(String letter : wordOrder) {
            if(letter == null) {
                 wordOrder[i] = c;
                 return i;
            }
            i++;
        }

        return -1;
    }

    public void removeCaracter (int i) {
        wordOrder[i] = null;
    }

    public String getWord () {
        StringBuilder sb = new StringBuilder();

        for (String s: wordOrder) {
            sb.append(s);
        }

        return sb.toString();
    }
}
