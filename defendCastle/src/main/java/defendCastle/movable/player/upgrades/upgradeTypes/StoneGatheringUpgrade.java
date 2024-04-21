package defendCastle.movable.player.upgrades.upgradeTypes;

import defendCastle.movable.player.Player;
import defendCastle.movable.player.upgrades.PlayerUpgrade;

/**
 * Write a description of class player.items.itemTypes.Pickaxe here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class StoneGatheringUpgrade extends PlayerUpgrade {
    /**
     * Constructor for objects of class player.items.itemTypes.Pickaxe
     */

    @Override
    public void applyUpgrade(Player player) {
        player.increaseStoneGatheringPowerAndSpeed(0.5, 1);
    }
}
