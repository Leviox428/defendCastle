package defendCastle.movable.player.upgrades.upgradeTypes;

import defendCastle.movable.player.Player;
import defendCastle.movable.player.upgrades.PlayerUpgrade;

/**
 * Write a description of class player.items.itemTypes.Axe here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class WoodGatheringUpgrade extends PlayerUpgrade {
    /**
     * Constructor for objects of class player.items.itemTypes.Axe
     */

    @Override
    public void applyUpgrade(Player player) {
        player.increaseWoodGatheringPowerAndSpeed(0.5, 1);
    }
}
