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
import org.bukkit.plugin.Plugin;

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
    private FlagManager flagManager; // Cache FlagManager to avoid blocking calls

    // Cache for locked claims: claimId-playerId -> CachedClaimStatus
    private final Map<String, CachedClaimStatus> lockedClaimCache = new ConcurrentHashMap<>();
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
        // Try multiple possible plugin names
        String[] possibleNames = {"GPFlags", "GriefPreventionFlags"};
        for (String name : possibleNames) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
            if (plugin != null && plugin.isEnabled()) {
                try {
                    gpFlags = (GPFlags) plugin;
                    // Cache the FlagManager at initialization to avoid blocking calls later
                    flagManager = gpFlags.getFlagManager();
                    if (flagManager != null) {
                        isGPFlagsEnabled = true;
                        Logger.logInfo("Found GPFlags (as '" + name + "') and cached FlagManager");
                    } else {
                        Logger.logWarning("GPFlags found but FlagManager is null");
                    }
                    return;
                } catch (ClassCastException e) {
                    Logger.logDebugInfo("Plugin " + name + " found but not GPFlags type");
                } catch (Exception e) {
                    Logger.logError("Error getting FlagManager from GPFlags: " + e.getMessage());
                }
            }
        }

        // Log available plugins for debugging
        Logger.logDebugInfo("GPFlags not found. Available plugins: ");
        for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
            if (p.getName().toLowerCase().contains("flag") || p.getName().toLowerCase().contains("grief")) {
                Logger.logDebugInfo("  - " + p.getName() + " (enabled: " + p.isEnabled() + ")");
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

            // Check cache first (cache key includes player ID since access varies per player)
            String cacheKey = claimId + "-" + player.getUniqueId();
            CachedClaimStatus cachedStatus = lockedClaimCache.get(cacheKey);

            if (cachedStatus != null && !cachedStatus.isExpired()) {
                Logger.logDebugInfo("Using cached lock status for claim " + claimId + " player " + player.getName() + ": " + cachedStatus.isLocked);
                return cachedStatus.isLocked;
            }

            // Check if player has access (trust) to the claim
            // allowAccess returns null if player HAS access, error message if NOT
            String accessError = claim.allowAccess(player);
            boolean hasAccess = (accessError == null);
            Logger.logDebugInfo("Player " + player.getName() + " has access to claim " + claimId + ": " + hasAccess);
            Logger.logWarning("GP-DEBUG: After access check, hasAccess=" + hasAccess);

            // If player has access (trust), they can enter regardless of flags
            if (hasAccess) {
                Logger.logWarning("GP-DEBUG: Player has access, returning false (not denied)");
                lockedClaimCache.put(cacheKey, new CachedClaimStatus(false, System.currentTimeMillis()));
                return false;
            }

            Logger.logWarning("GP-DEBUG: Player has NO access, checking flags...");
            // Player doesn't have access - now check if NoEntry/NoEnterPlayer flags are set
            Logger.logWarning("GP-DEBUG: About to check flags for claim " + claimId);
            if (flagManager == null) {
                Logger.logWarning("GP-DEBUG: FlagManager is null!");
                return false;
            }
            Logger.logWarning("GP-DEBUG: FlagManager valid, calling getEffectiveFlag for NoEnter...");
            boolean isLocked = false;

            // Check NoEntry flag (blocks all non-trusted players)
            Flag noEntryFlag = flagManager.getEffectiveFlag(location, "NoEnter", claim);
            Logger.logWarning("GP-DEBUG: getEffectiveFlag returned: " + noEntryFlag);
            Logger.logDebugInfo("NoEntry flag result: " + (noEntryFlag != null ? "found (set=" + noEntryFlag.getSet() + ")" : "null"));
            if (noEntryFlag != null && noEntryFlag.getSet()) {
                Logger.logDebugInfo("NoEntry flag is SET for claim " + claimId + " - player denied entry");
                isLocked = true;
            }

            // Check NoEnterPlayer flag (blocks specific players by name in parameters)
            if (!isLocked) {
                Flag noEnterPlayerFlag = flagManager.getEffectiveFlag(location, "NoEnterPlayer", claim);
                if (noEnterPlayerFlag != null && noEnterPlayerFlag.getSet()) {
                    // Check if player's name is in the flag parameters
                    String params = noEnterPlayerFlag.parameters;
                    if (params != null && params.toUpperCase().contains(player.getName().toUpperCase())) {
                        Logger.logDebugInfo("NoEnterPlayer flag is SET for claim " + claimId + " with player " + player.getName() + " in params - player denied entry");
                        isLocked = true;
                    } else {
                        Logger.logDebugInfo("NoEnterPlayer flag is SET but player " + player.getName() + " not in params: " + params);
                    }
                }
            }

            // Update cache
            lockedClaimCache.put(cacheKey, new CachedClaimStatus(isLocked, System.currentTimeMillis()));
            Logger.logDebugInfo("Cached lock status for claim " + claimId + " player " + player.getName() + ": " + isLocked);

            return isLocked;
        } catch (Exception e) {
            Logger.logError("Error checking GriefPrevention flags: " + e.getMessage());
            e.printStackTrace();
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
