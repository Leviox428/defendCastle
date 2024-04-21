package defendCastle.map;

/**
 * Write a description of class map.Stone here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Stone extends Resource {
    private final String[] miningAnimation = {
        "/Misc/Resources/RockMiningAnimation/start.png", "/Misc/Resources/RockMiningAnimation/frame2.png", "/Misc/Resources/RockMiningAnimation/frame3.png", "/Misc/Resources/RockMiningAnimation/frame5.png",
        "/Misc/Resources/RockMiningAnimation/frame6.png"
    };
    //private final ImageView rockImage;
    /**
     * Constructor for objects of class map.Stone
     */
    public Stone() {
        super("/Misc/Resources/rock.png", 120);
        super.setMiningAnimation(this.miningAnimation);
        super.setIcon("/Misc/MouseCursors/mining_cursor.png");
    }
}
