package defendCastle.movable.npc;

import defendCastle.Attackable;
import defendCastle.buildings.Building;
import defendCastle.buildings.buildingTypes.Castle;
import defendCastle.movable.Movable;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.animation.Timeline;
import javafx.animation.Interpolator;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.transform.Scale;
import javafx.geometry.Point2D;
import defendCastle.map.Map;

import java.util.ArrayList;
/**
 * Write a description of class npc.NPC here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class NPC implements Movable<NPCType>, Attackable {
    private VBox NPC;
    private NPCController controller;
    private ImageView NPCHitbox;
    private Image NPCImage;
    private Timeline enemiesScannerTimeline;
    private Timeline movementTimeline;
    private Timeline NPCAnimationsTimeline;
    private int movementSpeed;
    private int attackRange;
    private int damage;
    private double HP;
    private double attackSpeed;
    private NPCType typeOfNPC;
    private double xPosition;
    private double yPosition;
    private boolean isSelected;
    private String NPCCurrentRotation;
    private NPCAnimations animations;

    /**
     * Constructor for objects of class npc.NPC
     */
    public NPC(NPCType typeOfNPC, double initialXPosition, double initialYPosition, String NPCImagePath) {
        this.isSelected = false;
        this.typeOfNPC = typeOfNPC;
        this.xPosition = initialXPosition;
        this.yPosition = initialYPosition;
        this.NPCImage = new Image(NPCImagePath);                                
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/NPC.fxml"));
        try {
            this.NPC = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.controller = loader.getController();
        this.controller.setHealthBarAndSelectionColor(this.typeOfNPC);
        this.NPCHitbox = this.controller.getNPCImage();
        this.NPCHitbox.setImage(this.NPCImage);        
        this.NPC.setTranslateX(this.xPosition);
        this.NPC.setTranslateY(this.yPosition);
        this.NPCCurrentRotation = "right";
    }
    
    public NPC() {
    }
    
    public void spawnNPC() {
        this.controller.setHealthBar(this.HP);
        this.enemiesScannerTimeline = new Timeline();
        this.scanForEnemies();
        this.animations.idleAnimation();
    }
    
    public Image getNPCImage() {
        return this.NPCImage;
    }
    
    public ImageView getNPCHitbox() {        
        return this.NPCHitbox;
    }

    public NPCType getNPCType() {
        return this.typeOfNPC;
    }

    public abstract int[] getNPCTrainingCostsAndSpeed();
    
    public void setNPCStats(int movementSpeed, int damage, double HP, int attackSpeed, int attackRange) {
        this.movementSpeed = movementSpeed;
        this.damage = damage;
        this.HP = HP;
        this.attackSpeed = attackSpeed;
        this.attackRange = attackRange;
    }

    
    public VBox getNPC() {
        return this.NPC;
    }

    public void attack(Attackable attackableObject) {
        this.animations.attackAnimation(this.attackSpeed, this.enemiesScannerTimeline, attackableObject, this.damage);
    }

    @Override
    public void receiveHit(int damage) {
        this.HP -= damage;
        this.controller.updateHealthBar(this.HP);
        if (this.HP <= 0) {
            Map.removeNPC(this);
            if (this.enemiesScannerTimeline != null) {
                this.enemiesScannerTimeline.stop();
                this.enemiesScannerTimeline.getKeyFrames().clear();
                this.animations.deathAnimation();
            }
        }
    }


    public void removeNPC() {
        this.NPC = null;
        this.enemiesScannerTimeline = null;
        this.animations = null;
    }

    public void setNPCAnimations(String[] movementAnimation, String[] idleAnimation, String[] attackAnimation, String[] deathAnimation) {
        this.NPCAnimationsTimeline = new Timeline();
        this.movementTimeline = new Timeline();
        this.animations = new NPCAnimations(this, this.NPCHitbox, this.movementTimeline);
        this.animations.setAnimations(movementAnimation, idleAnimation, attackAnimation, deathAnimation);
    }

    
    public void selectNPC() {
        this.controller.setSelectionTriangleVisible(true);
        this.isSelected = true;
    }
    
    public void deselectNPC() {
        this.controller.setSelectionTriangleVisible(false);
        this.isSelected = false;
    }

    public void scanForEnemies() {
        this.enemiesScannerTimeline.setCycleCount(Timeline.INDEFINITE);
        this.enemiesScannerTimeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(0.5),
                        e -> this.checkEnemiesInRange())
        );
        this.enemiesScannerTimeline.play();

    }
    
    public boolean isSelected() {
        return this.isSelected;
    }
    
    @Override
    public void move(double distance, double newPositionX, double newPositionY, NPCType type) {
        this.stopNPCActions();
        if (type== NPCType.ALLY) {
            this.enemiesScannerTimeline.stop();
            this.enemiesScannerTimeline.getKeyFrames().clear();
        }
        this.calculateRotation(newPositionX);

        this.NPCAnimationsTimeline.stop();
        this.NPCAnimationsTimeline.getKeyFrames().clear();
        
        this.movementTimeline.getKeyFrames().setAll(         
            new KeyFrame(Duration.millis(distance),
                new KeyValue(this.NPC.translateXProperty(), newPositionX, Interpolator.LINEAR),
                new KeyValue(this.NPC.translateYProperty(), newPositionY, Interpolator.LINEAR)
                )            
        );
        
        this.movementTimeline.setOnFinished(event -> {
            this.stopNPCActions();
            this.scanForEnemies();
        });
        this.movementTimeline.play();
        this.animations.movementAnimation();
    }
    
    @Override
    public double distance(double deltaX, double deltaY) {
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)) * this.movementSpeed;
    }
    
    public void stopNPCActions() {
        this.animations.stopNPCActions();
    }

    private void checkEnemiesInRange() {
        Attackable closestEnemy = null;
        ArrayList<Attackable> attackable = new ArrayList<>();

        double minDistance = Double.MAX_VALUE;
        Point2D scanningNPC = new Point2D(this.NPC.getTranslateX() + this.NPC.getWidth() / 2,this.NPC.getTranslateY() + this.NPC.getHeight() / 2);
        ArrayList<NPC> otherNPCs;
        if (this.typeOfNPC == NPCType.ALLY) {
            otherNPCs = Map.getEnemies();

        } else {
            otherNPCs = Map.getAllyNPCs();
            ArrayList<Building> buildings = Map.getBuildings();
            for (Building building : buildings) {
               if (building.buildingCanBeAttacked()) {
                   attackable.add(building);
               }
            }
        }
        attackable.addAll(otherNPCs);
        for (Attackable attackableObject : attackable) {
            Point2D objectCenter;
            if (attackableObject instanceof NPC npc) {
                VBox hitbox = npc.getNPC();
                objectCenter = new Point2D(hitbox.getTranslateX() + hitbox.getWidth() / 2, hitbox.getTranslateY() + hitbox.getHeight() / 2);
            } else if (attackableObject instanceof Building building) {
                ImageView hitbox = building.getBuilding();
                objectCenter = new Point2D(hitbox.getTranslateX() + hitbox.getFitWidth() / 2, hitbox.getTranslateY() + hitbox.getFitHeight() / 2);
            } else {
                continue;
            }
            double distance = scanningNPC.distance(objectCenter);
            if (distance <= this.attackRange && distance < minDistance) {
                minDistance = distance;
                closestEnemy = attackableObject;
            }
        }


        if (closestEnemy != null) {
            double enemyXPosition;
            if (closestEnemy instanceof Building building) {
                enemyXPosition = building.getBuilding().getX();
            } else {
                NPC npc = (NPC)closestEnemy;
                enemyXPosition = npc.getNPC().getTranslateX();
            }
            if (this.typeOfNPC == NPCType.ENEMY) {
                this.movementTimeline.pause();
            }
            this.calculateRotation(enemyXPosition);
            this.attack(closestEnemy);
        } else {
            if (!Castle.castleIsDestroyed() && this.typeOfNPC == NPCType.ENEMY) {
                this.movementTimeline.play();
                this.animations.movementAnimation();
            }
        }
    }

    private void calculateRotation(double newPositionX) {
        if (newPositionX  <= this.NPCHitbox.localToScene(0, 0).getX()  && this.NPCCurrentRotation.equals("right")) {
            this.NPCCurrentRotation = "left";
            Scale mirrorNPCImage = new Scale(-1, 1);
            mirrorNPCImage.setPivotX(this.NPCHitbox.getImage().getWidth() - this.NPCHitbox.getBoundsInParent().getWidth() / 2);
            this.NPCHitbox.getTransforms().add(mirrorNPCImage);
        }
        if (newPositionX + this.NPCHitbox.getBoundsInParent().getWidth()  >= this.NPCHitbox.localToScene(0, 0).getX() && this.NPCCurrentRotation.equals("left")) {
            this.NPCCurrentRotation = "right";
            Scale mirrorPlayerImage = new Scale(-1, 1);
            mirrorPlayerImage.setPivotX(this.NPCHitbox.getImage().getWidth() - this.NPCHitbox.getBoundsInParent().getWidth() / 2);
            this.NPCHitbox.getTransforms().add(mirrorPlayerImage);
        }
    }
}
