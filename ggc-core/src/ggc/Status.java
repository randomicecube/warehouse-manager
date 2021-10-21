package ggc;

public class Status {
    // TODO - implement Status (and its subclasses)

    private int _points = 0;

    public void updatePoints(int points) {
        _points += points;
    }

    public int getPoints() {
        return _points;
    }
}
