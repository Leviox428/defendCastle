package defendCastle.movable.npc;

/**
 * Write a description of class npc.Archer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Archer extends NPC {
    private final String[] movementAnimation = {
            "/Misc/NPCS/Archer/MovementAnimation/run_1.png", "/Misc/NPCS/Archer/MovementAnimation/run_2.png", "/Misc/NPCS/Archer/MovementAnimation/run_3.png",
            "/Misc/NPCS/Archer/MovementAnimation/run_4.png", "/Misc/NPCS/Archer/MovementAnimation/run_5.png", "/Misc/NPCS/Archer/MovementAnimation/run_6.png",
            "/Misc/NPCS/Archer/MovementAnimation/run_7.png","/Misc/NPCS/Archer/MovementAnimation/run_8.png"
    };
    private final String[] idleAnimation = {
            "/Misc/NPCS/Archer/IdleAnimation/ready_1.png", "/Misc/NPCS/Archer/IdleAnimation/ready_2.png", "/Misc/NPCS/Archer/IdleAnimation/ready_1.png", "/Misc/NPCS/Archer/IdleAnimation/ready_2.png"
    };
    private final String[] attackAnimation = {
            "/Misc/NPCS/Archer/AttackAnimation/attack1.png", "/Misc/NPCS/Archer/AttackAnimation/attack2.png", "/Misc/NPCS/Archer/AttackAnimation/attack3.png", "/Misc/NPCS/Archer/AttackAnimation/attack4.png", "/Misc/NPCS/Archer/AttackAnimation/attack5.png",
            "/Misc/NPCS/Archer/AttackAnimation/attack6.png", "/Misc/NPCS/Archer/AttackAnimation/attack7.png", "/Misc/NPCS/Archer/AttackAnimation/attack8.png", "/Misc/NPCS/Archer/AttackAnimation/attack9.png", "/Misc/NPCS/Archer/AttackAnimation/attack10.png",
            "/Misc/NPCS/Archer/AttackAnimation/attack11.png",
    };

    private final String[] deathAnimation = {
            "/Misc/NPCS/Archer/DeathAnimation/death1.png", "/Misc/NPCS/Archer/DeathAnimation/death2.png", "/Misc/NPCS/Archer/DeathAnimation/death3.png", "/Misc/NPCS/Archer/DeathAnimation/death4.png", "/Misc/NPCS/Archer/DeathAnimation/death5.png",
            "/Misc/NPCS/Archer/DeathAnimation/death6.png", "/Misc/NPCS/Archer/DeathAnimation/death7.png", "/Misc/NPCS/Archer/DeathAnimation/death8.png", "/Misc/NPCS/Archer/DeathAnimation/death9.png", "/Misc/NPCS/Archer/DeathAnimation/death10.png"
    };
    private static final  int GOLD_COST = 15;
    private static final int FOOD_COST = 15;
    private static final int TRAINING_SPEED_SECONDS = 5;
    private static final int HOUSING_COST = 1;
    private static final int[] TRAINING_COSTS_AND_SPEED = {GOLD_COST, FOOD_COST, HOUSING_COST, TRAINING_SPEED_SECONDS};
    /**
     * Constructor for objects of class npc.Archer
     */
    public Archer(NPCType type, double x, double y) {
        super(type, x, y, "/Misc/NPCS/Archer/archer.png");
        super.setNPCStats(10, 20, 80, 1, 150);
        super.setNPCAnimations(this.movementAnimation, this.idleAnimation, this.attackAnimation, this.deathAnimation);

    }
    
    public Archer() {
    }


    @Override
    public int[] getNPCTrainingCostsAndSpeed() {
        return TRAINING_COSTS_AND_SPEED;
    }
    

}
