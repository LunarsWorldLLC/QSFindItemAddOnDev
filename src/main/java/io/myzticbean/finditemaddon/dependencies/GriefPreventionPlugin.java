package io.myzticbean.finditemaddon.dependencies;

import io.myzticbean.finditemaddon.utils.log.Logger;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class GriefPreventionPlugin {

    private boolean isGriefPreventionEnabled = false;

    public GriefPreventionPlugin() {
        checkGriefPreventionPlugin();
    }

    private void checkGriefPreventionPlugin() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("GriefPrevention");
        isGriefPreventionEnabled = plugin != null && plugin.isEnabled();
        if (isGriefPreventionEnabled) {
            Logger.logInfo("Found GriefPrevention");
        }
    }

    /**
     * Checks if a player is banned from the claim at the given location.
     * A player is considered "banned" if they don't have access permission to the claim.
     *
     * @param loc The location to check for a claim
     * @param searchingPlayer The player to check access for
     * @return true if the player is banned from the claim (no access), false otherwise
     */
    public boolean isPlayerBannedFromClaim(Location loc, Player searchingPlayer) {
        if (!isGriefPreventionEnabled) {
            return false;
        }

        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(loc, false, null);
        if (claim == null) {
            // No claim at this location, player is not banned
            return false;
        }

        // Check if this is the claim owner - owners are never banned from their own claim
        if (claim.getOwnerID() != null && claim.getOwnerID().equals(searchingPlayer.getUniqueId())) {
            return false;
        }

        // allowAccess returns null if access is allowed, or an error message if denied
        String accessDenied = claim.allowAccess(searchingPlayer);
        return accessDenied != null;
    }

    public boolean isEnabled() {
        return isGriefPreventionEnabled;
    }
}
