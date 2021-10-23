package ggc;

public class SelectionStatus extends Status{
    
    public double getModifierP2(int currentDate, int limitDate) {
        return limitDate - currentDate >= 2 ? 0.95 : 1.0;
    }

    public double getModifierP3(int currentDate, int limitDate) {
        int gap = currentDate - limitDate;
        return gap > 1 ? 1.0 + gap * 0.02 : 1.0; 
    }

    public double getModifierP4(int currentDate, int limitDate) {
        return 1.0 + (currentDate - limitDate) * 0.05;
    }

    @Override
    public String toString() {
        return "SELECTION";
    }

}
