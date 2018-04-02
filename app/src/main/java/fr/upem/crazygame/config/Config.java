package fr.upem.crazygame.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by dagama on 02/04/18.
 */

public class Config {
    private SharedPreferences sharedPreferences;
    private static String NAME_SHARED = "config";
    private static String VIBRATE = "vibrate";
    private static String VOLUM = "volum";



    private Config (SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void setVibrate(boolean vibrate) {
        sharedPreferences.edit().putBoolean(VIBRATE, vibrate).apply();
    }

    public void setVolum(boolean volum) {
        sharedPreferences.edit().putBoolean(VOLUM, volum).apply();
    }

    public Boolean getVolum(){
        return sharedPreferences.getBoolean(VOLUM, true);
    }

    public Boolean getVibrate(){
       return sharedPreferences.getBoolean(VIBRATE, true);
    }

    public static Config createConfig (Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME_SHARED, Context.MODE_PRIVATE);
        return new Config(sharedPreferences);
    }
}
