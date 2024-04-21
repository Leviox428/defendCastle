package defendCastle.movable.player.upgrades.upgradeTypes;

import defendCastle.movable.player.Player;
import defendCastle.movable.player.upgrades.PlayerUpgrade;

/**
 * Write a description of class player.items.itemTypes.Hammer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BuildingSpeedUpgrade extends PlayerUpgrade {
    /**
     * Constructor for objects of class player.items.itemTypes.Hammer
     */

    @Override
    public void applyUpgrade(Player player) {
        player.increaseBuildingSpeed(0.5);
    }
}
