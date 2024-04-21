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
 * Write a description of class LumberMill here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LumberMill extends Building implements ResourceGenerator {
    private final Timeline woodGenerationTimeline;
    private final BuildingInfoController controller;
    private final int[] hp = {0, 50, 75, 100, 125, 150};
    private final int[] goldCost = {5, 10, 25, 55, 120};
    private final int[] woodCost = {15, 30, 60, 120, 250};
    private final int[] stoneCost = {15, 30, 60, 120, 250};
    private final int[] woodGenerationPerSecond = {0, 1, 2, 3, 4, 5};
    @SuppressWarnings("FieldCanBeLocal")
    private final String[] buildingAnimation = {
        "Misc/Buildings/LumberMill/UpgradeAnimation/start.png", "Misc/Buildings/LumberMill/UpgradeAnimation/frame2.png", "Misc/Buildings/LumberMill/UpgradeAnimation/frame3.png",
        "Misc/Buildings/LumberMill/UpgradeAnimation/frame4.png", "Misc/Buildings/LumberMill/UpgradeAnimation/frame5.png", "Misc/Buildings/LumberMill/lumberMill.png"
    };
    /**
     * Constructor for objects of class LumberMill
     */
    public LumberMill(int x, int y, Map map) {
        super(x, y, 80, 80, "/Misc/Buildings/LumberMill/lumberMill.png", map);
        super.setConstructionTime(5);
        super.setBuildingAnimation(this.buildingAnimation);
        super.setBuildingCostsAndHp(this.woodCost, this.stoneCost, this.goldCost, this.hp);
        this.controller = super.getBuildingInfoController();
        this.updateBuildingInfo();
        this.woodGenerationTimeline = new Timeline();
        this.controller.setBuildingName("LumberMill");
        this.controller.setBuildingEffect(BuildingEffect.RESOURCE_GENERATION);
    }


    @Override
    public void setBuildingEffectString() {
        this.controller.setBuildingEffectString("This building generates" + this.woodGenerationPerSecond[super.getLevel()] + " wood/s.");
    }

    @Override
    public void stopBuildingEffect() {
        this.woodGenerationTimeline.stop();
        this.woodGenerationTimeline.getKeyFrames().clear();
    }

    @Override
    public void onBuildingFinished() {
        this.woodGenerationTimeline.setCycleCount(Timeline.INDEFINITE);
        this.woodGenerationTimeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1),
                        e -> super.getGameStatus().addResource("Wood", this.woodGenerationPerSecond[super.getLevel()]))
        );
        this.woodGenerationTimeline.play();
    }

}
