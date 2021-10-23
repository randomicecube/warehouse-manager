package ggc;

import java.io.Serializable;

public abstract class Status implements Serializable {

    private final static int SELECTION_LIMIT = 2000;
    
    private final static int ELITE_LIMIT = 25000;

    public double getModifierP1() {
        return 0.9;
    }

    public abstract double getModifierP2(int currentDate, int limitDate);

    public abstract double getModifierP3(int currentDate, int limitDate);

    public abstract double getModifierP4(int currentDate, int limitDate);

}