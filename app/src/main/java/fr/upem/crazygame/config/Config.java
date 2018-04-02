package fr.upem.crazygame.config;

/**
 * Created by dagama on 02/04/18.
 */

public class Config {
    private boolean volum = true;
    private boolean vibrate = true;


    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public void setVolum(boolean volum) {
        this.volum = volum;
    }

    public Boolean getVolum(){
        return this.volum;
    }

    public Boolean getVibrate(){
        return this.vibrate;
    }
}
