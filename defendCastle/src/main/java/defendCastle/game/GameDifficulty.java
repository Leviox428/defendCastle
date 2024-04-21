package defendCastle.game;

/**
 * Enumeration class GameDifficulty - write a description of the enum class here
 * 
 * @author (your name here)
 * @version (version number or date here)
 */
public enum GameDifficulty {
    EASY(1),
    MEDIUM(2),
    HARD(3),
    ENDLESS(1);

    private final int multiplier;
    GameDifficulty(int multiplier){
        this.multiplier = multiplier;
    }

    public int getMultiplier() {
        return this.multiplier;
    }
   
}
