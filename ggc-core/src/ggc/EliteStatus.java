package ggc;

public class EliteStatus extends Status {
    
    public double getModifierP2(int currentDate, int limitDate) {
        return 0.9;
    }

    public double getModifierP3(int currentDate, int limitDate) {
        return 0.95;
    }

    public double getModifierP4(int currentDate, int limitDate) {
        return 1.0;
    }

    @Override
    public String toString() {
        return "ELITE";
    }

}
