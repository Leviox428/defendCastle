package defendCastle.buildings.buildingTypes;

import defendCastle.buildings.Building;
import defendCastle.buildings.BuildingEffect;
import defendCastle.buildings.SpecialBuilding;
import defendCastle.buildings.controllers.BuildingInfoController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import defendCastle.map.Map;
import defendCastle.movable.player.Player;

/**
 * Write a description of class Buildings.Blacksmith here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Blacksmith extends Building implements SpecialBuilding {
    private final BuildingInfoController controller;
    private final ImageView blacksmithImage;
    private final Timeline scanIfPlayerIsCloseTimeline;
    private final int[] hp = {0, 50, 75, 100, 125, 150};
    private final int[] goldCost = {10, 15, 30, 60, 130};
    private final int[] woodCost = {10, 30, 60, 120, 250};
    private final int[] stoneCost = {10, 30, 60, 120, 250};
    @SuppressWarnings("FieldCanBeLocal")
    private final String[] buildingAnimation = {
            "/Misc/Buildings/Blacksmith/UpgradeAnimation/start.png", "/Misc/Buildings/Blacksmith/UpgradeAnimation/frame2.png", "/Misc/Buildings/Blacksmith/UpgradeAnimation/frame3.png",
            "/Misc/Buildings/Blacksmith/UpgradeAnimation/frame4.png", "/Misc/Buildings/Blacksmith/UpgradeAnimation/frame5.png", "/Misc/Buildings/Blacksmith/blacksmith.png"
    };
    private final Player player;
    private boolean playerCanBuyItem;
    /**
     * Constructor for objects of class Buildings.Blacksmith
     */
    public Blacksmith(int x, int y, Map map) {
        super(x, y, 70, 70, "/Misc/Buildings/Blacksmith/blacksmith.png", map);
        super.setBuildingAnimation(this.buildingAnimation);
        super.setConstructionTime(2);
        super.setBuildingCostsAndHp(this.woodCost, this.stoneCost, this.goldCost, this.hp);

        this.player = map.getPlayer();
        this.playerCanBuyItem = false;
        this.blacksmithImage = super.getBuilding();
        Image cursorImage = new Image("Misc/MouseCursors/blacksmith_cursor.png");
        this.blacksmithImage.setOnMouseEntered(event -> this.blacksmithImage.setCursor(Cursor.cursor(cursorImage.getUrl())));

        this.blacksmithImage.setOnMouseExited(event -> this.blacksmithImage.setCursor(Cursor.DEFAULT));
        this.scanIfPlayerIsCloseTimeline = new Timeline();

        this.controller = super.getBuildingInfoController();
        this.controller.setBlacksmith(this);
        //this.controller.setGameStatus(super.getGameStatus());

        this.controller.setBuildingEffectString("You can buy player.items for you hero here.");
        this.controller.setBuildingName("Blacksmith");

        this.updateBuildingInfo();
        this.startScanningForPlayerPosition();
    }


    public boolean playerCanBuyItem() {
        return this.playerCanBuyItem;
    }

    public Player getPlayer() {
        return this.player;
    }

    private void startScanningForPlayerPosition() {
        this.scanIfPlayerIsCloseTimeline.setCycleCount(Animation.INDEFINITE);
        KeyFrame playerPositionKeyFrame = new KeyFrame(Duration.seconds(2), e -> this.checkPlayerPosition());
        this.scanIfPlayerIsCloseTimeline.getKeyFrames().add(playerPositionKeyFrame);
        this.scanIfPlayerIsCloseTimeline.play();
    }

    private void checkPlayerPosition() {
        ImageView player = this.player.getPlayerImage();
        double playerX = player.getTranslateX();
        double playerY = player.getTranslateY();
        double barracksX = this.blacksmithImage.getTranslateX();
        double barracksY = this.blacksmithImage.getTranslateY();
        double distance = this.calculateDistance(playerX, playerY, barracksX, barracksY);
        this.playerCanBuyItem = distance < 80;
    }

    private double calculateDistance(double playerX, double playerY, double barracksX, double barracksY ) {
        return Math.sqrt(Math.pow(barracksX - playerX, 2) + Math.pow(barracksY - playerY, 2));
    }

    @Override
    public void setSpecialBuildingEffect() {
        this.controller.setBuildingEffect(BuildingEffect.ITEM_STORE);
    }
}