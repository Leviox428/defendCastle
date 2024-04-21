package defendCastle.movable.player;

import defendCastle.buildings.Building;
import defendCastle.game.GameStatus;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import defendCastle.map.Resource;
import defendCastle.map.Tree;

public class PlayerAnimations {
    private final String[] actionAnimation = {
            "/Misc/Player/ActionAnimation/start.png", "/Misc/Player/ActionAnimation/frame2.png", "/Misc/Player/ActionAnimation/frame3.png", "/Misc/Player/ActionAnimation/frame4.png",
            "/Misc/Player/ActionAnimation/frame5.png", "/Misc/Player/ActionAnimation/frame6.png", "/Misc/Player/ActionAnimation/frame7.png", "/Misc/Player/ActionAnimation/end.png"
    };
    private final String[] movementAnimation = {
            "/Misc/Player/MoveAnimation/start.png", "/Misc/Player/MoveAnimation/frame2.png", "/Misc/Player/MoveAnimation/frame3.png", "/Misc/Player/MoveAnimation/frame4.png",
            "/Misc/Player/MoveAnimation/frame5.png", "/Misc/Player/MoveAnimation/frame6.png", "/Misc/Player/MoveAnimation/frame7.png", "/Misc/Player/MoveAnimation/end.png"
    };

    private final String[] idleAnimation = {
            "/Misc/Player/IdleAnimation/start.png", "/Misc/Player/IdleAnimation/frame2.png", "/Misc/Player/IdleAnimation/frame3.png", "/Misc/Player/IdleAnimation/frame4.png",
            "/Misc/Player/IdleAnimation/frame5.png", "/Misc/Player/MoveAnimation/end.png"
    };
    private final Timeline animationsTimeline;
    private final ImageView playerImage;
    private final GameStatus gameStatus;

    public PlayerAnimations(Timeline animationsTimeline, ImageView playerImage, GameStatus gameStatus) {
        this.animationsTimeline = animationsTimeline;
        this.playerImage = playerImage;
        this.gameStatus = gameStatus;
    }

    public void idleAnimation() {
        this.animationsTimeline.setCycleCount(Timeline.INDEFINITE);
        for (int i = 0; i < this.idleAnimation.length; i++) {
            int index = i;
            this.animationsTimeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(0.125 * i),
                            e -> this.playerImage.setImage(new Image(this.idleAnimation[index])))
            );
        }
        this.animationsTimeline.play();
    }

    public void movementAnimation() {
        this.animationsTimeline.setCycleCount(this.movementAnimation.length);
        for (int i = 0; i < this.movementAnimation.length; i++) {
            int index = i;
            this.animationsTimeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(0.125 * i),
                            e -> this.playerImage.setImage(new Image(this.movementAnimation[index])))
            );
        }
        this.animationsTimeline.play();
    }

    public void miningAnimation(Resource resource, double miningTimeSeconds, int playerWoodGatheringPower, int playerStoneGatheringPower) {
        this.animationsTimeline.getKeyFrames().clear();
        this.animationsTimeline.stop();
        this.animationsTimeline.setCycleCount((int)(miningTimeSeconds / (0.125 * (this.actionAnimation.length + resource.getCurrentAnimationFrame()))));
        for (int i = 0; i < this.actionAnimation.length; i++) {
            int index = i;
            this.animationsTimeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(0.125 * i),
                            e -> this.playerImage.setImage(new Image(this.actionAnimation[index])))
            );
        }

        this.animationsTimeline.setOnFinished(event -> {
            this.animationsTimeline.stop();
            this.animationsTimeline.getKeyFrames().clear();
            this.idleAnimation();
            this.playerImage.setImage(new Image("/Misc/Player/player.png"));
            if (resource instanceof Tree) {
                this.gameStatus.addResource(resource, playerWoodGatheringPower);
            } else {
                this.gameStatus.addResource(resource, playerStoneGatheringPower);
            }
        });

        this.animationsTimeline.play();
        resource.mineResource(miningTimeSeconds);
    }

    public void buildingAnimation(Building building, double playerConstructingTimeBonus) {
        this.animationsTimeline.getKeyFrames().clear();
        this.animationsTimeline.stop();
        double buildingConstructionTime = building.getConstructionTime() - playerConstructingTimeBonus;
        this.animationsTimeline.setCycleCount((int)(buildingConstructionTime / (0.125 * (this.actionAnimation.length + building.getCurrentConstructingFrame()))));
        for (int i = 0; i < this.actionAnimation.length; i++) {
            int index = i;
            this.animationsTimeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(0.125 * i),
                            e -> this.playerImage.setImage(new Image(this.actionAnimation[index])))
            );
        }

        this.animationsTimeline.setOnFinished(event -> {
            this.animationsTimeline.stop();
            this.animationsTimeline.getKeyFrames().clear();
            this.idleAnimation();
            this.playerImage.setImage(new Image("/Misc/Player/player.png"));
        });

        this.animationsTimeline.play();
        building.upgrade(buildingConstructionTime);
    }
}
