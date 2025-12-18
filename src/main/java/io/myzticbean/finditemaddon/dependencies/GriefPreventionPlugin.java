package io.myzticbean.finditemaddon.dependencies;

import io.myzticbean.finditemaddon.utils.log.Logger;
import me.ryanhamshire.GPFlags.GPFlags;
import me.ryanhamshire.GPFlags.FlagManager;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Integration with GriefPrevention and GPFlags to check for locked claims
 * @author myzticbean
 */
public class GriefPreventionPlugin {

    private boolean isGriefPreventionEnabled = false;
    private boolean isGPFlagsEnabled = false;
    private GriefPrevention griefPrevention;
    private GPFlags gpFlags;

    public GriefPreventionPlugin() {
        checkGriefPreventionPlugin();
        checkGPFlagsPlugin();
    }

    private void checkGriefPreventionPlugin() {
        if (Bukkit.getPluginManager().isPluginEnabled("GriefPrevention")) {
            griefPrevention = (GriefPrevention) Bukkit.getPluginManager().getPlugin("GriefPrevention");
            isGriefPreventionEnabled = griefPrevention != null;
            if (isGriefPreventionEnabled) {
                Logger.logInfo("Found GriefPrevention");
            }
        }
    }

    private void checkGPFlagsPlugin() {
        if (Bukkit.getPluginManager().isPluginEnabled("GPFlags")) {
            gpFlags = (GPFlags) Bukkit.getPluginManager().getPlugin("GPFlags");
            isGPFlagsEnabled = gpFlags != null;
            if (isGPFlagsEnabled) {
                Logger.logInfo("Found GPFlags");
            }
        }
    }

    /**
     * Check if the player is denied entry to the claim at the given location
     * due to NoEntry or NoEnterPlayer flags from GPFlags
     *
     * @param location The location to check
     * @param player The player trying to access the shop
     * @return true if the player is denied entry to the claim
     */
    public boolean isPlayerDeniedEntry(Location location, Player player) {
        if (!isGriefPreventionEnabled || !isGPFlagsEnabled) {
            return false;
        }

        try {
            // Get the claim at the location
            Claim claim = griefPrevention.dataStore.getClaimAt(location, false, null);
            if (claim == null) {
                return false;
            }

            // Check if the player is the claim owner or has trust
            if (claim.getOwnerID() != null && claim.getOwnerID().equals(player.getUniqueId())) {
                return false;
            }

            // Check for access trust - if player has trust, they can enter
            if (claim.allowAccess(player) == null) {
                return false;
            }

            // Check for NoEntry flag
            FlagManager flagManager = gpFlags.getFlagManager();

            // Check NoEntry flag (blocks all non-trusted players)
            if (isFlagSetForClaim(flagManager, claim, "NoEntry")) {
                Logger.logDebugInfo("Shop is in claim with NoEntry flag - player denied entry");
                return true;
            }

            // Check NoEnterPlayer flag (blocks specific players or all players)
            if (isFlagSetForClaim(flagManager, claim, "NoEnterPlayer")) {
                Logger.logDebugInfo("Shop is in claim with NoEnterPlayer flag - player denied entry");
                return true;
            }

            return false;
        } catch (Exception e) {
            Logger.logDebugInfo("Error checking GriefPrevention flags: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if a specific flag is set for the claim
     */
    private boolean isFlagSetForClaim(FlagManager flagManager, Claim claim, String flagName) {
        try {
            var flagDef = flagManager.getFlagDefinitionByName(flagName);
            if (flagDef == null) {
                return false;
            }

            var flag = flagManager.getFlag(claim, flagDef);
            return flag != null && flag.getSet();
        } catch (Exception e) {
            Logger.logDebugInfo("Error checking flag " + flagName + ": " + e.getMessage());
            return false;
        }
    }

    public boolean isEnabled() {
        return isGriefPreventionEnabled && isGPFlagsEnabled;
    }
}
