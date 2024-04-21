package defendCastle.map;

/**
 * Enumeration class TileType - write a description of the enum class here
 * 
 * @author (your name here)
 * @version (version number or date here)
 */
public enum TileType {
    GRASS("/Misc/tile200.png");
    private final String tileImagePath;
    TileType(String tileImagePath) {
        this.tileImagePath = tileImagePath;
    }
    
    public String getTileImagePath() {
        return this.tileImagePath;
    }
}
