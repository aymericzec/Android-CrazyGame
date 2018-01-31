package fr.upem.crazygame.game.words;


import java.util.Locale;

public class Utils {

    /**
     * Get the language of the device
     *
     * @return The language of the device
     */
    public static String getLanguageDevice() {
        return Locale.getDefault().getLanguage();
    }
}
