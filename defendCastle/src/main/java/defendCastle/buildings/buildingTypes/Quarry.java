package defendCastle.buildings.buildingTypes;

import defendCastle.buildings.Building;
import defendCastle.buildings.BuildingEffect;
import defendCastle.buildings.ResourceGenerator;
import defendCastle.buildings.controllers.BuildingInfoController;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import defendCastle.map.Map;

/**
 * Write a description of class buildings.buildingTypes.Quarry here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Quarry extends Building implements ResourceGenerator {

    private final Timeline stoneGenerationTimeline;
    private final BuildingInfoController controller;
    private final int[] hp = {0, 50, 75, 100, 125, 150};
    private final int[] goldCost = {5, 10, 25, 55, 120};
    private final int[] woodCost = {15, 30, 60, 120, 250};
    private final int[] stoneCost = {15, 30, 60, 120, 250};
    private final int[] woodGenerationPerSecond = {0, 1, 2, 3, 4, 5};
    @SuppressWarnings("FieldCanBeLocal")
    private final String[] buildingAnimation = {
        "/Misc/Buildings/Quarry/UpgradeAnimation/start.png", "/Misc/Buildings/Quarry/UpgradeAnimation/frame2.png", "/Misc/Buildings/Quarry/UpgradeAnimation/frame3.png",
        "/Misc/Buildings/Quarry/UpgradeAnimation/frame4.png", "/Misc/Buildings/Quarry/UpgradeAnimation/frame5.png", "/Misc/Buildings/Quarry/quarry.png"
    };
    /**
     * Constructor for objects of class buildings.buildingTypes.Quarry
     */
    public Quarry(int x, int y, Map map) {
        super(x, y, 80, 80, "/Misc/Buildings/Quarry/quarry.png", map);
        super.setConstructionTime(5);
        super.setBuildingCostsAndHp(this.woodCost, this.stoneCost, this.goldCost, this.hp);
        super.setBuildingAnimation(this.buildingAnimation);
        this.controller = super.getBuildingInfoController();
        this.updateBuildingInfo();
        this.stoneGenerationTimeline = new Timeline();
        this.controller.setBuildingName("Quarry");
        this.controller.setBuildingEffect(BuildingEffect.RESOURCE_GENERATION);
    }


    @Override
    public void setBuildingEffectString() {
        this.controller.setBuildingEffectString("This building generates" + this.woodGenerationPerSecond[super.getLevel()] + " wood/s.");
    }

    @Override
    public void stopBuildingEffect() {
        this.stoneGenerationTimeline.stop();
        this.stoneGenerationTimeline.getKeyFrames().clear();
    }

    @Override
    public void onBuildingFinished() {
        this.stoneGenerationTimeline.setCycleCount(Timeline.INDEFINITE);
        this.stoneGenerationTimeline.getKeyFrames().add(
            new KeyFrame(Duration.seconds(1),
                e -> super.getGameStatus().addResource("Stone", this.woodGenerationPerSecond[super.getLevel()]))
        );
        this.stoneGenerationTimeline.play();
    }
    
}