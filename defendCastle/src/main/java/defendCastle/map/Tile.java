package defendCastle.map;

/**
 * Write a description of class Tile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tile {
    private final static int TILE_SIZE = 16;
    private final TileType tileType;
    public Tile(TileType tileType) {
        this.tileType = tileType;
    }
    
    public String getTileImage() {
        return this.tileType.getTileImagePath();
    }

    public static int getTileSize() {
        return TILE_SIZE;
    }
}
