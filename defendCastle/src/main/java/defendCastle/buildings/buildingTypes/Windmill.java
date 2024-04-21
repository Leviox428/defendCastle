package defendCastle.buildings.buildingTypes;

import defendCastle.buildings.Building;
import defendCastle.buildings.BuildingEffect;
import defendCastle.buildings.ResourceGenerator;
import defendCastle.buildings.controllers.BuildingInfoController;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.Cursor;
import defendCastle.map.Map;

/**
 * Write a description of class Farm here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Windmill extends Building implements ResourceGenerator {
    private final ImageView windmillImage;
    private final Timeline foodGenerationTimeline;
    private final BuildingInfoController controller;
    private final int[] hp = {0, 50, 75, 100, 125, 150};
    private final int[] goldCost = {5, 10, 25, 55, 120};
    private final int[] woodCost = {15, 30, 60, 120, 250};
    private final int[] stoneCost = {15, 30, 60, 120, 250};
    private final int[] foodGenerationPerSecond = {0, 1, 2, 3, 4, 5};
    @SuppressWarnings("FieldCanBeLocal")
    private final String[] buildingAnimation = {
        "Misc/Buildings/Windmill/UpgradeAnimation/start.png", "Misc/Buildings/Windmill/UpgradeAnimation/frame2.png", "Misc/Buildings/Windmill/UpgradeAnimation/frame3.png",
        "Misc/Buildings/Windmill/UpgradeAnimation/frame4.png", "Misc/Buildings/Windmill/UpgradeAnimation/frame5.png", "Misc/Buildings/Windmill/windmill.png"
    };
    /**
     * Constructor for objects of class Farm
     */
    public Windmill(int x, int y, Map map) {
        super(x, y, 50, 80, "/Misc/Buildings/Windmill/windmill.png", map);
        super.setConstructionTime(5);
        super.setBuildingAnimation(this.buildingAnimation);
        super.setBuildingCostsAndHp(this.woodCost, this.stoneCost, this.goldCost, this.hp);
        this.controller = super.getBuildingInfoController();
        this.updateBuildingInfo();
        this.foodGenerationTimeline = new Timeline();
        this.windmillImage = super.getBuilding();
        Image cursorImage = new Image("/Misc/MouseCursors/food_cursor.png");
        this.windmillImage.setOnMouseEntered(event -> this.windmillImage.setCursor(Cursor.cursor(cursorImage.getUrl())));

        this.windmillImage.setOnMouseExited(event -> this.windmillImage.setCursor(Cursor.DEFAULT));
        this.controller.setBuildingName("Windmill");
        this.controller.setBuildingEffect(BuildingEffect.RESOURCE_GENERATION);
    }


    @Override
    public void setBuildingEffectString() {
        this.controller.setBuildingEffectString("This building generates " + this.foodGenerationPerSecond[super.getLevel()] + " food/s.");
    }

    @Override
    public void stopBuildingEffect() {
        this.foodGenerationTimeline.stop();
        this.foodGenerationTimeline.getKeyFrames().clear();
    }
    @Override
    public void onBuildingFinished() {
        this.foodGenerationTimeline.setCycleCount(Timeline.INDEFINITE);
        this.foodGenerationTimeline.getKeyFrames().add(
            new KeyFrame(Duration.seconds(1),
                e -> super.getGameStatus().addResource("Food", this.foodGenerationPerSecond[super.getLevel()]))
        );
        this.foodGenerationTimeline.play();
    }
}