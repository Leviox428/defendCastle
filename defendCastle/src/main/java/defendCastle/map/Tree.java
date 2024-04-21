package defendCastle.map;

/**
 * Write a description of class map.Tree here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tree extends Resource {
    private final String[] miningAnimation = {
        "/Misc/Resources/TreeChoppingAnimation/start.png", "/Misc/Resources/TreeChoppingAnimation/frame2.png", "/Misc/Resources/TreeChoppingAnimation/frame3.png", "/Misc/Resources/TreeChoppingAnimation/frame5.png",
        "/Misc/Resources/TreeChoppingAnimation/frame6.png"
    };
    /**
     * Constructor for objects of class map.Tree
     */
    public Tree() {        
        super("/Misc/Resources/tree.png", 60);
        super.setMiningAnimation(this.miningAnimation);
        super.setIcon("/Misc/MouseCursors/wood_gathering_cursor.png");
    }

}
