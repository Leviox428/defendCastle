package defendCastle.movable.player;

import defendCastle.buildings.Building;
import defendCastle.map.Tree;
import defendCastle.movable.Movable;
import defendCastle.game.GameStatus;
import javafx.scene.image.ImageView;
import javafx.animation.Timeline;
import javafx.animation.Interpolator;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.scene.image.Image;
import javafx.scene.transform.Scale;
import defendCastle.map.Resource;

/**
 * Write a description of class Player here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Player implements Movable<PlayerAction> {
    private final ImageView playerImage;
    private double playerSpeed;
    private double woodMiningTimeSeconds;
    private double stoneMiningTimeSeconds;
    private int playerWoodGatheringPower;
    private int playerStoneGatheringPower;
    private final PlayerAnimations playerAnimations;
    private final Timeline animationsTimeline;
    private final Timeline movementTimeline;
    private String currentPlayerRotation;
    private Resource currentMinedResource;
    private Building currentConstructingBuilding;
    private double playerBuildSpeedBonus;

    /**
     * Constructor for objects of class Player
     */
    public Player(double positionX, double positionY, GameStatus gameStatus) {
        this.playerSpeed = 5.0;
        this.playerBuildSpeedBonus = 0.0;
        this.woodMiningTimeSeconds = 3.0;
        this.stoneMiningTimeSeconds = 3.0;
        this.playerWoodGatheringPower = 1;
        this.playerStoneGatheringPower = 1;

        //Setting up player position and position of player's Image
        this.playerImage = new ImageView(new Image("/Misc/Player/player.png"));
        this.playerImage.setTranslateX(positionX);
        this.playerImage.setTranslateY(positionY);
        this.currentPlayerRotation = "right"; //Setting up which way is the player facing
        
        //Setting up timelines for player's animations and movement
        this.animationsTimeline = new Timeline();
        this.movementTimeline = new Timeline();
        this.playerAnimations = new PlayerAnimations(this.animationsTimeline, this.playerImage, gameStatus);
        this.playerAnimations.idleAnimation(); //Started player's idle animation
    }

    public void increaseBuildingSpeed(double bonusSpeed) {
        this.playerBuildSpeedBonus += bonusSpeed;
    }

    public void increaseStoneGatheringPowerAndSpeed(double bonusSpeed, int bonusGatheringPower) {
        this.playerStoneGatheringPower += bonusGatheringPower;
        this.stoneMiningTimeSeconds -= bonusSpeed;
    }

    public void increaseWoodGatheringPowerAndSpeed(double bonusSpeed, int bonusGatheringPower) {
        this.playerWoodGatheringPower += bonusGatheringPower;
        this.woodMiningTimeSeconds -= bonusSpeed;
    }

    public void moveToCenter(double centerX, double centerY) {
        this.moveTowardsTarget(centerX - ((double) 56 / 2), centerY - ((double) 56 / 2), PlayerAction.MOVE);
    }

    public void increaseMovementSpeed(double bonusSpeed) {
        this.playerSpeed -= bonusSpeed;
    }

    public ImageView getPlayerImage() {
        return this.playerImage;
    }

    
    @Override
    public void move(double distance, double newPositionX, double newPositionY, PlayerAction action) {
        this.calculateRotationOfPlayer(newPositionX);
        if (action == PlayerAction.GATHER  && this.currentPlayerRotation.equals("right")) {            
            newPositionX -= 20;        
        }        
        if (action == PlayerAction.GATHER && this.currentPlayerRotation.equals("left")) {
            newPositionX += 20;
        }
        
        if (action == PlayerAction.BUILD && this.currentPlayerRotation.equals("right")) {            
            newPositionX -= (this.currentConstructingBuilding.getConstructionZone().getBoundsInParent().getWidth() / 2);            
        }        
        if (action == PlayerAction.BUILD && this.currentPlayerRotation.equals("left")) {            
            newPositionX += (this.currentConstructingBuilding.getConstructionZone().getBoundsInParent().getWidth());
        }
         
        this.movementTimeline.getKeyFrames().setAll( 
            new KeyFrame(Duration.millis(distance),
                new KeyValue(this.playerImage.translateXProperty(), newPositionX, Interpolator.LINEAR),
                new KeyValue(this.playerImage.translateYProperty(), newPositionY, Interpolator.LINEAR)
                )            
        );
        this.playerAnimations.movementAnimation();
        this.movementTimeline.setOnFinished(event -> {    
            this.stopPlayerActions();
            this.playerImage.setImage(new Image("/Misc/Player/player.png"));
            switch (action) {
                case PlayerAction.GATHER:
                    if (this.currentMinedResource.resourceCanBeGathered()) {
                        double miningTimeSeconds;
                        if (currentMinedResource instanceof Tree) {
                            miningTimeSeconds = this.woodMiningTimeSeconds;
                        } else {
                            miningTimeSeconds = this.stoneMiningTimeSeconds;
                        }
                        this.animationsTimeline.getKeyFrames().clear();
                        this.playerAnimations.miningAnimation(this.currentMinedResource, miningTimeSeconds, this.playerWoodGatheringPower, this.playerStoneGatheringPower);
                    }
                    break;                
                case PlayerAction.BUILD:
                    if (this.currentConstructingBuilding.buildingCanBeConstructed()) {
                        this.animationsTimeline.getKeyFrames().clear();
                        this.playerAnimations.buildingAnimation(this.currentConstructingBuilding, this.playerBuildSpeedBonus);
                    }
                    break;                
                default:
                    this.playerAnimations.idleAnimation();
                    break;    
            }
        });
        this.movementTimeline.play();        
    }



    @Override
    public double distance(double deltaX, double deltaY) {
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)) * this.playerSpeed;
    }

    public void mineResource(Resource resource) {
        double resourceXPosition = resource.getResourceImageContainer().getLayoutX();
        double resourceYPosition = resource.getResourceImageContainer().getLayoutY() + 5;
        this.currentMinedResource = resource;
        this.moveTowardsTarget(resourceXPosition, resourceYPosition, PlayerAction.GATHER);
    }

    public void upgradeBuilding(Building building) {
        double buildingXPosition = building.getConstructionZone().getTranslateX();
        double buildingYPosition = building.getConstructionZone().getTranslateY() + 5;
        this.currentConstructingBuilding = building;
        this.moveTowardsTarget(buildingXPosition, buildingYPosition, PlayerAction.BUILD);
    }

    public void stopPlayerActions() {
        if (this.movementTimeline != null) {            
            this.movementTimeline.stop();
            this.movementTimeline.getKeyFrames().clear();
            this.animationsTimeline.stop();
            this.animationsTimeline.getKeyFrames().clear();
            this.playerAnimations.idleAnimation();
            if (this.currentMinedResource != null) {
                this.currentMinedResource.pauseGathering();
            }
            if (this.currentConstructingBuilding != null) {
                this.currentConstructingBuilding.pauseBuilding();
            }
        }
    }

    private void moveTowardsTarget(double targetX, double targetY, PlayerAction action) {
        double playerCurrentXPosition = this.playerImage.getTranslateX();
        double playerCurrentYPosition = this.playerImage.getTranslateY();
        double deltaX = targetX - playerCurrentXPosition;
        double deltaY = playerCurrentYPosition - targetY;
        this.move(this.distance(deltaX, deltaY), targetX, targetY, action);
    }

    private void calculateRotationOfPlayer(double newPositionX) {
        if (newPositionX  <= this.playerImage.getTranslateX()  && this.currentPlayerRotation.equals("right")) {
            this.currentPlayerRotation = "left";
            Scale mirrorPlayerImage = new Scale(-1, 1);
            mirrorPlayerImage.setPivotX(this.playerImage.getImage().getWidth() - (double) 56 / 2);
            this.playerImage.getTransforms().add(mirrorPlayerImage);
        }
        if (newPositionX >= this.playerImage.getTranslateX() && this.currentPlayerRotation.equals("left")) {
            this.currentPlayerRotation = "right";
            Scale mirrorPlayerImage = new Scale(-1, 1);
            mirrorPlayerImage.setPivotX(this.playerImage.getImage().getWidth() - (double) 56 / 2);
            this.playerImage.getTransforms().add(mirrorPlayerImage);
        }
    }
}
