package defendCastle.movable.npc;

import defendCastle.Attackable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class NPCAnimations {
    private final Timeline NPCAnimationsTimeline;
    private final Timeline movementTimeline;
    private final ImageView NPCHitbox;
    private String[] idleAnimation;

    private String[] movementAnimation;

    private String[] attackAnimation;

    private String[] deathAnimation;

    private final NPC npc;

    public NPCAnimations(NPC npc, ImageView NPCHitbox, Timeline movementTimeline) {
        this.NPCAnimationsTimeline = new Timeline();
        this.NPCHitbox = NPCHitbox;
        this.movementTimeline = movementTimeline;
        this.npc = npc;
    }

    public void setAnimations(String[] movementAnimation, String[] idleAnimation, String[] attackAnimation, String[] deathAnimation) {
        this.idleAnimation = idleAnimation;
        this.movementAnimation = movementAnimation;
        this.attackAnimation = attackAnimation;
        this.deathAnimation = deathAnimation;
    }


    public void movementAnimation() {
        this.NPCAnimationsTimeline.stop();
        this.NPCAnimationsTimeline.getKeyFrames().clear();
        this.NPCAnimationsTimeline.setCycleCount(Timeline.INDEFINITE);
        for (int i = 0; i < this.movementAnimation.length; i++) {
            int index = i;
            this.NPCAnimationsTimeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(0.125 * i),
                            e -> this.NPCHitbox.setImage(new Image(this.movementAnimation[index])))
            );
        }
        this.NPCAnimationsTimeline.setOnFinished(event -> this.stopNPCActions());
        this.NPCAnimationsTimeline.play();
    }

    public void idleAnimation() {
        this.NPCAnimationsTimeline.setCycleCount(Timeline.INDEFINITE);
        for (int i = 0; i < this.idleAnimation.length; i++) {
            int index = i;
            this.NPCAnimationsTimeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(0.125 * i),
                            e -> this.NPCHitbox.setImage(new Image(this.idleAnimation[index])))
            );
        }
        this.NPCAnimationsTimeline.play();
    }

    public void attackAnimation(double attackSpeed, Timeline enemiesScannerTimeline, Attackable attackableObject, int damage) {
        this.NPCAnimationsTimeline.stop();
        this.NPCAnimationsTimeline.getKeyFrames().clear();
        enemiesScannerTimeline.stop();
        enemiesScannerTimeline.getKeyFrames().clear();
        double frameDuration = attackSpeed / this.attackAnimation.length;
        this.NPCAnimationsTimeline.setCycleCount(1);
        for (int i = 0; i < this.attackAnimation.length; i++) {
            int index = i;
            this.NPCAnimationsTimeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(frameDuration * (i + 1)),
                            e -> this.NPCHitbox.setImage(new Image(this.attackAnimation[index])))
            );
        }
        this.NPCAnimationsTimeline.setOnFinished(event -> {
            attackableObject.receiveHit(damage);
            this.npc.scanForEnemies();
            this.NPCAnimationsTimeline.getKeyFrames().clear();
            this.idleAnimation();

        });
        this.NPCAnimationsTimeline.play();
    }

    public void stopNPCActions() {
        if (this.movementTimeline != null) {
            this.movementTimeline.stop();
            this.movementTimeline.getKeyFrames().clear();
            this.NPCAnimationsTimeline.stop();
            this.NPCAnimationsTimeline.getKeyFrames().clear();
            this.idleAnimation();
        }
    }

    public void deathAnimation() {
        this.NPCAnimationsTimeline.stop();
        this.NPCAnimationsTimeline.getKeyFrames().clear();
        this.NPCAnimationsTimeline.setCycleCount(1);
        for (int i = 0; i < this.deathAnimation.length; i++) {
            int index = i;
            this.NPCAnimationsTimeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(0.125 * i),
                            e -> this.NPCHitbox.setImage(new Image(this.deathAnimation[index])))
            );
        }
        this.NPCAnimationsTimeline.setOnFinished(event -> {
            VBox npcContainer = npc.getNPC();
            Parent parent = npcContainer.getParent();
            if (parent instanceof Pane pane) {
                pane.getChildren().remove(npcContainer);
            }
            this.npc.removeNPC();

        });
        this.NPCAnimationsTimeline.play();
    }
}
