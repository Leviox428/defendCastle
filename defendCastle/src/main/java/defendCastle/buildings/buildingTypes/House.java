package defendCastle.buildings.buildingTypes;

import defendCastle.buildings.Building;
import defendCastle.buildings.ResourceGenerator;
import defendCastle.buildings.controllers.BuildingInfoController;
import defendCastle.map.Map;

/**
 * Write a description of class Buildings.House here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class House extends Building implements ResourceGenerator {
    private final BuildingInfoController controller;
    private final int[] hp = {0, 50, 75, 100, 125, 150};
    private final int[] goldCost = {5, 10, 25, 55, 120};
    private final int[] woodCost = {15, 30, 60, 120, 250};
    private final int[] stoneCost = {15, 30, 60, 120, 250};
    private final int[] housingProvided = {0, 5, 10, 20, 35, 55};
    @SuppressWarnings("FieldCanBeLocal")
    private final String[] buildingAnimation = {
        "Misc/Buildings/House/UpgradeAnimation/start.png", "Misc/Buildings/House/UpgradeAnimation/frame2.png", "Misc/Buildings/House/UpgradeAnimation/frame3.png",
        "Misc/Buildings/House/UpgradeAnimation/frame4.png", "Misc/Buildings/House/house.png"
    };
    /**
     * Constructor for objects of class Buildings.House
     */
    public House(int x, int y, Map map) {
        super(x, y, 50, 50, "/Misc/Buildings/House/house.png", map);
        super.setConstructionTime(5);
        super.setBuildingAnimation(this.buildingAnimation);
        super.setBuildingCostsAndHp(this.woodCost, this.stoneCost, this.goldCost, this.hp);
        this.controller = super.getBuildingInfoController();
        this.updateBuildingInfo();
        this.controller.setBuildingName("Building");
    }


    @Override
    public void setBuildingEffectString() {
        this.controller.setBuildingEffectString("This building provides space for " + this.housingProvided[super.getLevel()] + " units.");
    }

    @Override
    public void stopBuildingEffect() {

    }

    @Override
    public void onBuildingFinished() {
        super.getGameStatus().addResource("Housing", this.housingProvided[super.getLevel()]);
    }
    
}
