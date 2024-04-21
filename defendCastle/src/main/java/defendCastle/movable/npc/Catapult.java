package defendCastle.movable.npc;

/**
 * Write a description of class npc.Catapult here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Catapult extends NPC {
    private static final  int GOLD_COST =100;
    private static final int FOOD_COST = 100;
    private static final int TRAINING_SPEED_SECONDS = 60;
    private static final int HOUSING_COST = 5;
    private static final int[] TRAINING_COSTS_AND_SPEED = {GOLD_COST, FOOD_COST, HOUSING_COST, TRAINING_SPEED_SECONDS};
    private final String[] deathAnimation = {

    };


    /**
     * Constructor for objects of class npc.Catapult
     */
    public Catapult(NPCType type, double x, double y) {
        super(type, x, y, "/Misc/Buildings/Castle/castle.png");
    }
    
    public Catapult() {
    }

    @Override
    public int[] getNPCTrainingCostsAndSpeed() {
        return TRAINING_COSTS_AND_SPEED;
    }

}
