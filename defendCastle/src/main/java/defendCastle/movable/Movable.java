package defendCastle.movable;

/**
 * Write a description of interface common.Movable here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public interface Movable<T> {
    void move(double durationMillis, double newPositionX, double newPositionY, T action);
    double distance(double deltaX, double deltaY);
}
