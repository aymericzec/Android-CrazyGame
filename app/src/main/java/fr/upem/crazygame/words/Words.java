package fr.upem.crazygame.words;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Words {

    private final List<String> wordsList;

    /**
     * Constructor of this class
     *
     * @param wordsList A List that contains the words
     */
    public Words(List<String> wordsList) {
        this.wordsList = wordsList;
    }

    /**
     * Take a word in the list
     *
     * @return A word of the list
     */
    public String takeWord() {

        Random random = new Random();
        int index = random.nextInt(wordsList.size()- 1);

        return wordsList.get(index);
    }

    /**
     * Shuffle the word passed in argument
     *
     * @param word Word to shuffle
     * @return The shuffle word
     */
    public String shuffleWord(String word) {

        StringBuilder sb = new StringBuilder();
        List<Character> letters = new ArrayList<>();

        for(Character c : word.toCharArray()) {
            letters.add(c);
        }

        Collections.shuffle(letters);

        for(Character c : letters) {
            sb.append(c);
        }

        return sb.toString();
    }
}
