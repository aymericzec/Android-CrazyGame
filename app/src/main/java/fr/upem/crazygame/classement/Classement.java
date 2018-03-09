package fr.upem.crazygame.classement;

import android.support.annotation.NonNull;

/**
 * Created by myfou on 04/03/2018.
 */

public class Classement implements Comparable {

    private final String name;
    private final int nbGameTotal;

    public Classement(String name, int nbGameTotal) {
        this.name = name;
        this.nbGameTotal = nbGameTotal;
    }

    public String getName() {
        return name;
    }

    public int getNbGameTotal () {
        return nbGameTotal;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Classement c=(Classement)o;
        return c.nbGameTotal - this.nbGameTotal;
    }
}
