package ggc;

import java.io.Serializable;

public class NormalStatus extends Status implements Serializable {
    
    public double getModifierP2(int currentDate, int limitDate) {
        return 1.0;
    }

    public double getModifierP3(int currentDate, int limitDate) {
        return 1.0 + (currentDate - limitDate) * 0.05;
    }

    public double getModifierP4(int currentDate, int limitDate) {
        return 1.0 + (currentDate - limitDate) * 0.10;
    }

    @Override
    public String toString() {
        return "NORMAL";
    }

}
