package io.myzticbean.finditemaddon.dependencies;

import io.myzticbean.finditemaddon.utils.log.Logger;
import me.ryanhamshire.GPFlags.GPFlags;
import me.ryanhamshire.GPFlags.Flag;
import me.ryanhamshire.GPFlags.FlagManager;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Integration with GriefPrevention and GPFlags to check for locked claims
 * @author myzticbean
 */
public class GriefPreventionPlugin {

    private boolean isGriefPreventionEnabled = false;
    private boolean isGPFlagsEnabled = false;
    private GriefPrevention griefPrevention;
    private GPFlags gpFlags;

    // Cache for locked claims: claimId -> CachedClaimStatus
    private final Map<Long, CachedClaimStatus> lockedClaimCache = new ConcurrentHashMap<>();
    private static final long CACHE_EXPIRY_MS = 60000; // 60 seconds cache

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
            Logger.logDebugInfo("GPFlags check skipped - GP enabled: " + isGriefPreventionEnabled + ", GPFlags enabled: " + isGPFlagsEnabled);
            return false;
        }

        try {
            // Get the claim at the location
            Claim claim = griefPrevention.dataStore.getClaimAt(location, false, null);
            if (claim == null) {
                Logger.logDebugInfo("No claim found at location: " + location);
                return false;
            }

            Long claimId = claim.getID();
            Logger.logDebugInfo("Checking claim ID " + claimId + " for player " + player.getName());

            // Only skip for claim owner - they should always see their own shops
            if (claim.getOwnerID() != null && claim.getOwnerID().equals(player.getUniqueId())) {
                Logger.logDebugInfo("Player " + player.getName() + " is claim owner, not blocking");
                return false;
            }

            // Check cache first
            CachedClaimStatus cachedStatus = lockedClaimCache.get(claimId);

            if (cachedStatus != null && !cachedStatus.isExpired()) {
                Logger.logDebugInfo("Using cached lock status for claim " + claimId + ": " + cachedStatus.isLocked);
                return cachedStatus.isLocked;
            }

            // Cache miss or expired - check flags
            FlagManager flagManager = gpFlags.getFlagManager();
            boolean isLocked = false;

            // Check NoEntry flag (blocks all non-owner players)
            boolean noEntryFlag = isFlagEffective(flagManager, location, "NoEntry", claim);
            Logger.logDebugInfo("NoEntry flag for claim " + claimId + ": " + noEntryFlag);
            if (noEntryFlag) {
                Logger.logDebugInfo("Shop is in claim with NoEntry flag - player denied entry");
                isLocked = true;
            }

            // Check NoEnterPlayer flag (blocks specific players or all players)
            if (!isLocked) {
                boolean noEnterPlayerFlag = isFlagEffective(flagManager, location, "NoEnterPlayer", claim);
                Logger.logDebugInfo("NoEnterPlayer flag for claim " + claimId + ": " + noEnterPlayerFlag);
                if (noEnterPlayerFlag) {
                    Logger.logDebugInfo("Shop is in claim with NoEnterPlayer flag - player denied entry");
                    isLocked = true;
                }
            }

            // Update cache
            lockedClaimCache.put(claimId, new CachedClaimStatus(isLocked, System.currentTimeMillis()));
            Logger.logDebugInfo("Cached lock status for claim " + claimId + ": " + isLocked);

            return isLocked;
        } catch (Exception e) {
            Logger.logDebugInfo("Error checking GriefPrevention flags: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if a specific flag is effective at the location
     * Uses GPFlags API: getEffectiveFlag(Location, String, Claim)
     */
    private boolean isFlagEffective(FlagManager flagManager, Location location, String flagName, Claim claim) {
        try {
            Flag flag = flagManager.getEffectiveFlag(location, flagName, claim);
            boolean isSet = flag != null && flag.getSet();
            Logger.logDebugInfo("Flag " + flagName + " at location: flag=" + flag + ", isSet=" + isSet);
            return isSet;
        } catch (Exception e) {
            Logger.logDebugInfo("Error checking flag " + flagName + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Clear the locked claim cache. Call this if flags are changed.
     */
    public void clearCache() {
        lockedClaimCache.clear();
        Logger.logDebugInfo("GriefPrevention locked claim cache cleared");
    }

    /**
     * Remove a specific claim from the cache
     */
    public void invalidateClaim(Long claimId) {
        lockedClaimCache.remove(claimId);
    }

    public boolean isEnabled() {
        return isGriefPreventionEnabled && isGPFlagsEnabled;
    }

    /**
     * Inner class to hold cached claim lock status
     */
    private static class CachedClaimStatus {
        final boolean isLocked;
        final long cachedAt;

        CachedClaimStatus(boolean isLocked, long cachedAt) {
            this.isLocked = isLocked;
            this.cachedAt = cachedAt;
        }

        boolean isExpired() {
            return System.currentTimeMillis() - cachedAt > CACHE_EXPIRY_MS;
        }
    }
}
