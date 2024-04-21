package defendCastle.movable.player.upgrades.upgradeTypes;

import defendCastle.movable.player.Player;
import defendCastle.movable.player.upgrades.PlayerUpgrade;

/**
 * Write a description of class player.items.itemTypes.Boots here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MovementSpeedUpgrade extends PlayerUpgrade {
    /**
     * Constructor for objects of class player.items.itemTypes.Boots
     */



    @Override
    public void applyUpgrade(Player player) {
        player.increaseMovementSpeed(0.5);
    }
}
