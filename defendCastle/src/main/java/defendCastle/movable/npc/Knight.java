package defendCastle.movable.npc;

/**
 * Write a description of class npc.Knight here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Knight extends NPC {
    private final String[] movementAnimation = {
        "/Misc/NPCS/Knight/MovementAnimation/run_1.png", "/Misc/NPCS/Knight/MovementAnimation/run_2.png", "/Misc/NPCS/Knight/MovementAnimation/run_3.png",
        "/Misc/NPCS/Knight/MovementAnimation/run_4.png", "/Misc/NPCS/Knight/MovementAnimation/run_5.png", "/Misc/NPCS/Knight/MovementAnimation/run_6.png"
    };
    private final String[] idleAnimation = {
        "/Misc/NPCS/Knight/IdleAnimation/ready_1.png", "/Misc/NPCS/Knight/IdleAnimation/ready_2.png", "/Misc/NPCS/Knight/IdleAnimation/ready_3.png"
    };
    private final String[] attackAnimation = {
        "/Misc/NPCS/Knight/AttackAnimation/attack1_1.png", "/Misc/NPCS/Knight/AttackAnimation/attack1_2.png", "/Misc/NPCS/Knight/AttackAnimation/attack1_3.png",
        "/Misc/NPCS/Knight/AttackAnimation/attack1_4.png", "/Misc/NPCS/Knight/AttackAnimation/attack1_5.png", "/Misc/NPCS/Knight/AttackAnimation/attack1_6.png",
    };
    private final String[] deathAnimation = {
        "/Misc/NPCS/Knight/DeathAnimation/fall_back_1.png", "/Misc/NPCS/Knight/DeathAnimation/fall_back_2.png", "/Misc/NPCS/Knight/DeathAnimation/fall_back_3.png", "/Misc/NPCS/Knight/DeathAnimation/fall_back_4.png", "/Misc/NPCS/Knight/DeathAnimation/fall_back_5.png"
    };
    
    private static final  int GOLD_COST = 10;
    private static final int FOOD_COST = 10;
    private static final int TRAINING_SPEED_SECONDS = 3;
    private static final int HOUSING_COST = 1;
    private static final int[] TRAINING_COSTS_AND_SPEED = {GOLD_COST, FOOD_COST, HOUSING_COST, TRAINING_SPEED_SECONDS};
    /**
     * Constructor for objects of class npc.Knight
     */
    public Knight(NPCType type, double x, double y) {
        super(type, x, y, "/Misc/NPCS/Knight/AttackAnimation/attack1_1.png");
        super.setNPCStats(10, 20, 120.0, 1, 25);
        super.setNPCAnimations(this.movementAnimation, this.idleAnimation, this.attackAnimation, this.deathAnimation);

    }
    
    public Knight() {
    }
    

    @Override
    public int[] getNPCTrainingCostsAndSpeed() {
        return TRAINING_COSTS_AND_SPEED;
    }
    
}
