package ggc;

import java.io.Serializable;

/**
 * The "in-between" Status between Normal and Elite
 */
public class SelectionStatus extends Status implements Serializable {
    
    /** @return Selection Status price modifiers for P2 */
    public double getModifierP2(int currentDate, int limitDate) {
        return limitDate - currentDate >= 2 ? 0.95 : 1.0;
    }

    /** @return Selection Status price modifiers for P3 */
    public double getModifierP3(int currentDate, int limitDate) {
        int gap = currentDate - limitDate;
        return gap > 1 ? 1.0 + gap * 0.02 : 1.0; 
    }

    /** @return Selection Status price modifiers for P4 */
    public double getModifierP4(int currentDate, int limitDate) {
        return 1.0 + (currentDate - limitDate) * 0.05;
    }

    @Override
    public String toString() {
        return "SELECTION";
    }

}
