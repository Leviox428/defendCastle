package defendCastle.map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Write a description of class map.Resource here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Resource {
    private final Image resourceImage;
    private final ImageView resourceImageContainer;
    private boolean resourceCanBeGathered;
    private int currentGatheringFrame;
    private String[] miningAnimation;
    private final Timeline miningTimeline;
    private final Timeline growingTimeline;
    private final int growthTimeSeconds;

    /**
     * Constructor for objects of class map.Resource
     */
    public Resource(String resourceImagePath, int growthTimeSeconds) {
        this.resourceCanBeGathered = true;
        this.resourceImage = new Image(resourceImagePath);
        this.resourceImageContainer = new ImageView(this.resourceImage);
        this.currentGatheringFrame = 0;
        this.miningTimeline = new Timeline();
        this.growingTimeline = new Timeline();
        this.growthTimeSeconds = growthTimeSeconds;
    }   
    
    public ImageView getResourceImageContainer() {
        return this.resourceImageContainer;
    }
    
    public int getCurrentAnimationFrame() {
        return this.currentGatheringFrame;
    }
    
    public void mineResource(double playerGatheringSpeed) {
        this.gatheringAnimation(playerGatheringSpeed);
    }

    public boolean resourceCanBeGathered() {
        return this.resourceCanBeGathered;
    }
    
    public void pauseGathering() {
        this.miningTimeline.pause();
    }

    protected void setMiningAnimation(String[] miningAnimation) {
        this.miningAnimation = miningAnimation;
    }

    protected void setIcon(String iconImagePath) {
        Image cursorImage = new Image(iconImagePath);
        this.resourceImageContainer.setOnMouseEntered(event -> this.resourceImageContainer.setCursor(Cursor.cursor(cursorImage.getUrl())));
        this.resourceImageContainer.setOnMouseExited(event ->  this.resourceImageContainer.setCursor(Cursor.DEFAULT));
    }

    private void gatheringAnimation(double playerGatheringSpeed) {
        double frameDuration = playerGatheringSpeed / this.miningAnimation.length;
        this.miningTimeline.setCycleCount(1);
        for (int i = 0; i < this.miningAnimation.length; i++) {
            int index = i;
            this.currentGatheringFrame =index;
            this.miningTimeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(frameDuration * (i)),
                            e -> this.resourceImageContainer.setImage(new Image(this.miningAnimation[index])))
            );

        }

        this.miningTimeline.setOnFinished(event -> {
            this.miningTimeline.stop();
            this.miningTimeline.getKeyFrames().clear();
            this.resourceCanBeGathered = false;
            this.growResource();
        });
        this.miningTimeline.play();
    }

    private void growResource() {
        ArrayList<String> growingFrames = new ArrayList<>(Arrays.asList(this.miningAnimation));
        Collections.reverse(growingFrames);
        this.growingTimeline.setCycleCount(1);
        for (int i = 0; i < growingFrames.size(); i++) {
            int index = i;

            this.growingTimeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(((double) this.growthTimeSeconds / growingFrames.size()) * (i + 1)),
                            e -> this.resourceImageContainer.setImage(new Image(growingFrames.get(index))))
            );
        }
        this.growingTimeline.setOnFinished(event -> {
            this.resourceCanBeGathered = true;
            this.currentGatheringFrame = 0;
            this.resourceImageContainer.setImage(this.resourceImage);
        });
        this.growingTimeline.play();
    }
}