package fr.upem.crazygame.words;


import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class load the files that contains the words in the assets folder
 */
public class LoadAssetsWords {

    private static List<String> wordsList = new ArrayList<>();

    /**
     * Read the content of the InputStream and put it in a List
     *
     * @param is Content to load
     * @throws IOException
     */
    private static void loadFromInputStream(InputStream is) throws IOException {

        try(BufferedReader buffer = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {

            String line;
            StringBuilder sb = new StringBuilder();

            while((line = buffer.readLine()) != null) {
                sb.append(line).append("\n");
            }

            wordsList.add(sb.toString());
        }
    }

    /**
     * Load a file present in the words directory of the assests directory
     *
     * @param context
     * @throws IOException
     */
    private static void loadFromAsset(Context context, String path) throws IOException
    {
        loadFromInputStream(context.getAssets().open(path));
    }

    /**
     * Load all the files that are presents in the directory words of assets
     *
     * @param context
     * @param language String that contains the language of the device
     * @return a List with all the files that have been loaded
     */
    public static List<String> loadFiles(Context context, String language) {

        String [] filenames = null;

        try {
            filenames = context.getAssets().list("words");
        } catch (IOException e) {
            return wordsList;
        }

        for(String file : filenames) {
            if(file.contains(language)) {
                try {
                    loadFromAsset(context, new File("words", file).getPath());
                } catch (IOException e) {
                    Log.w(LoadAssetsWords.class.getName(), "Cannot load the file at path words" + "/" + file);
                }
                break;
            }
        }

        return wordsList;
    }
}
