package defendCastle.movable.player.upgrades;

import defendCastle.movable.player.Player;

/**

 */
public abstract class PlayerUpgrade {
    private static final int MAX_UPGRADES = 4;
    private int currentNumberUpgrade;

    public PlayerUpgrade() {
        this.currentNumberUpgrade = 0;
    }

    public boolean upgradeCanBeBought() {
        if (this.currentNumberUpgrade >= MAX_UPGRADES) {
            return false;
        } else {
            this.currentNumberUpgrade++;
            return true;
        }
    }

    public String getCurrentUpgradeStatus() {
        return this.currentNumberUpgrade + "/" + MAX_UPGRADES;
    }


    public abstract void applyUpgrade(Player player);


}
