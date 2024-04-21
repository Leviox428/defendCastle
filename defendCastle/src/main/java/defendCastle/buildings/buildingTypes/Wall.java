package defendCastle.buildings.buildingTypes;

import defendCastle.buildings.Building;
import defendCastle.buildings.controllers.BuildingInfoController;
import defendCastle.map.Map;

/**
 * Write a description of class buildings.buildingTypes.Wall here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Wall extends Building {
    private final BuildingInfoController controller;
    private final int[] hp = {0, 50, 75, 100, 125, 150};
    private final int[] goldCost = {5, 10, 25, 55, 120};
    private final int[] woodCost = {15, 30, 60, 120, 250};
    private final int[] stoneCost = {15, 30, 60, 120, 250};
    @SuppressWarnings("FieldCanBeLocal")
    private final String[] buildingAnimation = {
        "/Misc/Buildings/Wall/UpgradeAnimation/start.png", "/Misc/Buildings/Wall/UpgradeAnimation/frame2.png", "/Misc/Buildings/Wall/UpgradeAnimation/frame3.png",
        "/Misc/Buildings/Wall/UpgradeAnimation/frame4.png", "/Misc/Buildings/Wall/wall.png"
    };
    /**
     * Constructor for objects of class buildings.buildingTypes.Wall
     */
    public Wall(int x, int y, Map map) {
        super(x, y, 32, 32, "/Misc/Buildings/Wall/wall.png", map);
        super.setConstructionTime(3);
        super.setBuildingAnimation(this.buildingAnimation);
        super.setBuildingCostsAndHp(this.woodCost, this.stoneCost, this.goldCost, this.hp);
        this.controller = super.getBuildingInfoController();
        this.updateBuildingInfo();
        this.controller.setBuildingName("Wall");
        this.controller.setBuildingEffectString("Protects your castle from enemies");
    }
    
}