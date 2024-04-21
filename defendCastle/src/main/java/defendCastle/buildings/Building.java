package defendCastle.buildings;

import defendCastle.Attackable;
import defendCastle.buildings.buildingTypes.Castle;
import defendCastle.buildings.controllers.BuildingInfoController;
import defendCastle.game.GameStatus;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import defendCastle.map.Map;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


/**
 * Write a description of class Buildings.Building here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Building implements Attackable {
    private final ImageView buildingCanBeConstructedImage;
    private int buildingLevel;
    private int constructionTimeSeconds;
    private final ImageView buildingImageContainer;
    private final Image buildingImage;
    private final BuildingConstructionZone buildingConstructionZone;
    private final BuildingInfo buildingInfo;
    private final  Map map;
    private boolean buildingCanBeAttacked;
    private int currentConstructingFrame;
    private final BuildingInfoController controller;
    private boolean buildingCanBeConstructed;
    private GameStatus gameStatus;
    private boolean buildingCanBeConstructedImageIsShown;
    private final Timeline buildingTimeline;
    private String[] buildingAnimation;
    private int[] woodCost;
    private int[] stoneCost;
    private int[] goldCost;
    private int[] hp;

    /**
     * Constructor for objects of class Buildings.Building
     */

    public Building(int x, int y, int width, int height, String buildingImagePath, Map map) {
        this.map = map;
        this.buildingLevel = 0;
        this.buildingCanBeConstructed = false;
        this.currentConstructingFrame = 0;
        this.buildingCanBeConstructedImageIsShown = false;
        this.buildingTimeline = new Timeline();
        this.buildingCanBeAttacked = false;

        try (InputStream image = getClass().getResourceAsStream(buildingImagePath)) {
            this.buildingImage = new Image(Objects.requireNonNull(image));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BuildingImageContainer container = new BuildingImageContainer(height, width, x, y);
        this.buildingImageContainer = container.getBuildingImageContainer();
        this.buildingConstructionZone = new BuildingConstructionZone(width, x, y);

        this.buildingCanBeConstructedImage = new ImageView(new Image("Misc/Buildings/constructing.png"));
        this.buildingCanBeConstructedImage.setFitWidth(this.buildingConstructionZone.getConstructionZone().getBoundsInParent().getWidth());
        this.buildingCanBeConstructedImage.setFitHeight(this.buildingConstructionZone.getConstructionZone().getBoundsInParent().getHeight() );
        this.buildingCanBeConstructedImage.setTranslateX(x - (this.buildingConstructionZone.getConstructionZone().getBoundsInParent().getWidth() / 2) + 2);
        this.buildingCanBeConstructedImage.setTranslateY(y - (this.buildingConstructionZone.getConstructionZone().getBoundsInParent().getHeight() / 2) + 2);
        this.buildingCanBeConstructedImage.setVisible(false);


        this.buildingInfo = new BuildingInfo(this.buildingConstructionZone.getConstructionZone().getTranslateX(), this.buildingConstructionZone.getConstructionZone().getTranslateY());
        this.controller = this.buildingInfo.getController();
        this.controller.setBuilding(this);

    }
    public void setBuildingCostsAndHp(int[] woodCost, int[] stoneCost, int[] goldCost, int[] hp) {
        this.woodCost = woodCost;
        this.stoneCost = stoneCost;
        this.goldCost = goldCost;
        this.hp = hp;
    }

    @Override
    public void receiveHit(int damage) {
        this.controller.receiveHit(damage);
    }

    public void setBuildingAnimation(String[] buildingAnimation) {
        this.buildingAnimation = buildingAnimation;
    }
    
    public GameStatus getGameStatus() {
        return this.gameStatus;
    }
    
    public ImageView getBuildingCanBeConstructedImage() {
        return this.buildingCanBeConstructedImage;
    }
    
    public BuildingInfoController getBuildingInfoController() {
        return this.controller;
    }
    
    public Image getBuildingImage() {
        return this.buildingImage;
    }

    public ImageView getBuilding() {
        return this.buildingImageContainer;
    }
    public SVGPath getConstructionZone() {
        return this.buildingConstructionZone.getConstructionZone();
    }
    public int getCurrentConstructingFrame() {
        return this.currentConstructingFrame;
    }

    public int getLevel() {
        return this.buildingLevel;
    }
    public double getConstructionTime() {
        return this.constructionTimeSeconds;
    }
    public void setCurrentConstructingFrame(int currentConstructingFrame) {
        this.currentConstructingFrame = currentConstructingFrame;
    }


    public void setBuildingCanBeConstructed(boolean buildingCanBeConstructed) {
        this.buildingCanBeConstructed = buildingCanBeConstructed;
        this.buildingCanBeAttacked = false;
    }
    public void setConstructionTime(int constructionTimeSeconds) {
        this.constructionTimeSeconds = constructionTimeSeconds;
    }
    
    public void hideConstructionZone() {       
        this.buildingConstructionZone.setVisible(false);
        this.buildingCanBeConstructedImage.setVisible(false);
        this.buildingImageContainer.setVisible(true);
    }
    
    public void hideBuildingInfo() {
        this.buildingInfo.setVisible(false);
    }
    
    public void addGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
        this.controller.setGameStatus(gameStatus);
    }

    public boolean buildingCanBeConstructed() {
        return this.buildingCanBeConstructed;
    }
    
    public void showBuildingCanBeConstructedImage() {
        if (!this.buildingCanBeConstructedImageIsShown) {
            this.buildingCanBeConstructedImageIsShown = true;
            this.map.addElementToMap(this.buildingCanBeConstructedImage);
        }
        this.buildingCanBeConstructedImage.setVisible(true);
        this.buildingConstructionZone.setVisible(true);
    }
               
    public void showBuildingInfo() {
        this.buildingInfo.addBuildingInfoToMap(this.map);
        this.buildingInfo.setVisible(true);
        this.buildingInfo.toFront();
    }
    public void upgrade(double buildingConstructionSpeed) {
        this.buildingImageContainer.setImage(null);

        double frameDuration = buildingConstructionSpeed / this.buildingAnimation.length;
        this.buildingTimeline.setCycleCount(1);
        for (int i = 0; i < this.buildingAnimation.length; i++) {
            int index = i;
            this.setCurrentConstructingFrame(index);
            this.buildingTimeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(frameDuration * i),
                            e -> this.buildingImageContainer.setImage(new Image(this.buildingAnimation[index])))
            );
            this.buildingImageContainer.toFront();
        }

        this.buildingTimeline.setOnFinished(event -> {
            this.buildingCanBeAttacked = true;
            this.buildingCanBeConstructed = false;
            this.currentConstructingFrame = 0;
            this.buildingLevel += 1;
            this.updateBuildingInfo();
            this.buildingTimeline.stop();
            this.buildingTimeline.getKeyFrames().clear();
            this.onBuildingFinished();
        });

        this.buildingTimeline.play();
        this.hideConstructionZone();
    }

    public boolean buildingCanBeAttacked() {
        return this.buildingCanBeAttacked;
    }

    public void onBuildingFinished() {
    }


    public void pauseBuilding() {
        if (this.buildingTimeline != null) {
            this.buildingTimeline.pause();
        }
    }

    protected void updateBuildingInfo() {
        if (this instanceof ResourceGenerator generator) {
            generator.setBuildingEffectString();
        } else if (this instanceof  SpecialBuilding special && this.buildingLevel == 1) {
            special.setSpecialBuildingEffect();
        }
        this.controller.setBuildingHP(this.hp[this.buildingLevel]);
        this.controller.setBuildingLevel(this.buildingLevel);
        if (this.buildingLevel <= 4) {
            this.controller.setGoldCost(this.goldCost[this.buildingLevel]);
            this.controller.setWoodCost(this.woodCost[this.buildingLevel]);
            this.controller.setStoneCost(this.stoneCost[this.buildingLevel]);
        } else {
            this.controller.buildingIsAtMaxLevel();
        }
    }

    public void resetBuilding() {
        if (this instanceof Castle) {
            Castle.castleDestroyed();
        }
        this.buildingCanBeAttacked = false;
        this.buildingLevel = 0;
        this.updateBuildingInfo();
        this.buildingConstructionZone.setVisible(true);
        this.buildingImageContainer.setVisible(false);
    }

}