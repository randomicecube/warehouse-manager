package ggc;

import java.io.Serializable;

/**
 * Abstract class representing a Status in the system
 */
public abstract class Status implements Serializable {

    /** The "border" between Normal and Selection Status is 2000 points */
    private final static int SELECTION_LIMIT = 2000;
    
    /** The "border" between Selection and Elite Status is 25000 points */
    private final static int ELITE_LIMIT = 25000;

    /** @return Status price modifiers for P1 */
    public double getModifierP1() {
        return 0.9;
    }

    /**
     * @param currentDate
     * @param limitDate
     * @return Status price modifiers for P2
     */
    public abstract double getModifierP2(int currentDate, int limitDate);

    /**
     * @param currentDate
     * @param limitDate
     * @return Status price modifiers for P3
     */
    public abstract double getModifierP3(int currentDate, int limitDate);

    /**
     * @param currentDate
     * @param limitDate
     * @return Status price modifiers for P4
     */
    public abstract double getModifierP4(int currentDate, int limitDate);

}