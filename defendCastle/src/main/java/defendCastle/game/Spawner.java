package defendCastle.game;

import java.util.ArrayList;

import defendCastle.movable.npc.Archer;
import javafx.scene.layout.VBox;
import defendCastle.map.Map;
import defendCastle.movable.npc.Knight;
import defendCastle.movable.npc.NPC;
import defendCastle.movable.npc.NPCType;

/**
 * Write a description of class game.Spawner here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Spawner {
    private final Map spawnMap;
    private final GameDifficulty difficulty;
    /**
     * Constructor for objects of class game.Spawner
     */
    public Spawner(Map spawnMap, GameDifficulty difficulty) {
        this.spawnMap = spawnMap;
        this.difficulty = difficulty;
    }
    
    public void spawnWave(int wave) {
        ArrayList<NPC> enemyWave = new ArrayList<>();
        int knightCount = 4 * this.difficulty.getMultiplier() * wave;
        int archerCount = 2 * this.difficulty.getMultiplier() * wave;
        for (int i = 0; i < knightCount; i++) {
            NPC knight = new Knight(NPCType.ENEMY, 50, 50 + i*10);
            enemyWave.add(knight);
        }
        for (int i = 0; i < archerCount; i++) {
            NPC archer = new Archer(NPCType.ENEMY, 40,40);
            enemyWave.add(archer);
        }
        this.spawnMap.addEnemyWaveToMap(enemyWave);
        for (NPC enemy : enemyWave) {
            VBox enemyImage = enemy.getNPC();
            double initialX = enemyImage.getTranslateX();
            double initialY = enemyImage.getTranslateY();

            double destinationX = Map.getCenterOfTheMap() - (enemyImage.getBoundsInLocal().getWidth() / 2);
            double destinationY = Map.getCenterOfTheMap() - (enemyImage.getBoundsInLocal().getHeight() / 2);

            double deltaX = destinationX - initialX;
            double deltaY = destinationY - initialY;
            enemy.move(enemy.distance(deltaX, deltaY), destinationX, destinationY, NPCType.ENEMY);
        }
    }
}
