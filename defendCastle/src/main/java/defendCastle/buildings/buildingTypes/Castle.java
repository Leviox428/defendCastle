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
 * Write a description of class Buildings.Castle here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Castle extends Building implements ResourceGenerator {
    private final ImageView castleImage;
    private final Timeline goldGenerationTimeline;
    private final BuildingInfoController controller;
    private final int[] hp = {0, 100, 150, 200, 250, 300};
    private final int[] goldCost = {10, 25, 62, 156, 390};
    private final int[] woodCost = {15, 40, 90, 200, 450};
    private final int[] stoneCost = {15, 40, 90, 200, 450};
    private final int[] goldGenerationPerSecond = {0, 1, 2, 4, 8, 16};
    private final String[] buildingAnimation = {
        "Misc/Buildings/Castle/UpgradeAnimation/start.png", "Misc/Buildings/Castle/UpgradeAnimation/frame2.png", "Misc/Buildings/Castle/UpgradeAnimation/frame3.png",
        "Misc/Buildings/Castle/UpgradeAnimation/frame4.png", "Misc/Buildings/Castle/UpgradeAnimation/frame5.png", "Misc/Buildings/Castle/castle.png"
    };
    private static boolean CASTLE_IS_DESTROYED = false;
    private final Map map;
    /**
     * Constructor for objects of class Buildings.Castle
     */
    public Castle(int x, int y, Map map) {
        super(x, y, 100, 100, "/Misc/Buildings/Castle/castle.png", map);
        super.setConstructionTime(2);
        super.setBuildingAnimation(this.buildingAnimation);
        super.setBuildingCostsAndHp(this.woodCost, this.stoneCost, this.goldCost, this.hp);
        this.controller = super.getBuildingInfoController();
        this.map = map;
        this.updateBuildingInfo();
        this.goldGenerationTimeline = new Timeline();
        this.castleImage = super.getBuilding();
        Image cursorImage = new Image("Misc/MouseCursors/gold_cursor.png");
        this.castleImage.setOnMouseEntered(event -> this.castleImage.setCursor(Cursor.cursor(cursorImage.getUrl())));
        this.castleImage.setOnMouseExited(event -> this.castleImage.setCursor(Cursor.DEFAULT));
        this.controller.setBuildingName("Castle");
        this.controller.setBuildingEffect(BuildingEffect.RESOURCE_GENERATION);
    }    



    @Override
    public void setBuildingEffectString() {
        this.controller.setBuildingEffectString("This building generates " + this.goldGenerationPerSecond[super.getLevel()] + " gold/s.");
    }

    @Override
    public void stopBuildingEffect() {
        this.goldGenerationTimeline.stop();
        this.goldGenerationTimeline.getKeyFrames().clear();
    }
    @Override
    public void onBuildingFinished() {
        this.map.generateAllStructures();
        this.goldGenerationTimeline.setCycleCount(Timeline.INDEFINITE);
        this.goldGenerationTimeline.getKeyFrames().add(
            new KeyFrame(Duration.seconds(1),
                e -> super.getGameStatus().addResource("Gold", this.goldGenerationPerSecond[super.getLevel()]))
        );
        this.goldGenerationTimeline.play();
    }

    public static void castleDestroyed() {
        CASTLE_IS_DESTROYED = true;
    }

    public static boolean castleIsDestroyed() {
        return CASTLE_IS_DESTROYED;
    }
}