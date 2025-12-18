package io.myzticbean.finditemaddon.dependencies;

import io.myzticbean.finditemaddon.FindItemAddOn;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Integration with GriefPrevention and GPFlags to check for locked claims and claim bans
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
    private final Map<String, CachedClaimStatus> bannedClaimCache = new ConcurrentHashMap<>();
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
     * Checks if a player is specifically banned from the claim at the given location.
     * NOTE: GriefPrevention 16.18.4 does not have isClaimBanned() method.
     * This method always returns false - use CosmosCore integration instead for claim bans.
     *
     * @param loc The location to check for a claim
     * @param searchingPlayer The player to check for claim ban
     * @return false - GriefPrevention native claim ban not supported in this version
     */
    public boolean isPlayerBannedFromClaim(Location loc, Player searchingPlayer) {
        // GriefPrevention 16.18.4 does not have isClaimBanned() method
        // Use CosmosCore integration for claim ban checking instead
        Logger.logDebugInfo("GriefPrevention isPlayerBannedFromClaim called but not supported - use CosmosCore instead");
        return false;
    }

    /**
     * Check if the player is denied entry to the claim at the given location
     * due to NoEntry or NoEnterPlayer flags from GPFlags.
     * Requires both GriefPrevention AND GPFlags.
     *
     * @param location The location to check
     * @param player The player trying to access the shop
     * @return true if the player is denied entry to the claim
     */
    public boolean isPlayerDeniedEntry(Location location, Player player) {
        // Early return if GPFlags is not properly initialized
        if (!isGriefPreventionEnabled || !isGPFlagsEnabled || flagManager == null) {
            Logger.logDebugInfo("GPFlags check skipped - GP: " + isGriefPreventionEnabled +
                ", GPFlags: " + isGPFlagsEnabled + ", FlagManager: " + (flagManager != null));
            return false;
        }

        try {
            // Get the claim at the location - this is thread-safe
            Claim claim = griefPrevention.dataStore.getClaimAt(location, false, null);
            if (claim == null) {
                Logger.logDebugInfo("No claim found at location: " + location);
                return false;
            }

            Long claimId = claim.getID();
            if (claimId == null) {
                Logger.logDebugInfo("Claim has null ID");
                return false;
            }

            // Only skip for claim owner - they should always see their own shops
            if (claim.getOwnerID() != null && claim.getOwnerID().equals(player.getUniqueId())) {
                Logger.logDebugInfo("Player " + player.getName() + " is claim owner, not blocking");
                return false;
            }

            // Check cache first (cache key includes player ID since access varies per player)
            String cacheKey = "entry-" + claimId + "-" + player.getUniqueId();
            CachedClaimStatus cachedStatus = lockedClaimCache.get(cacheKey);

            if (cachedStatus != null && !cachedStatus.isExpired()) {
                Logger.logDebugInfo("Using cached GPFlags status for claim " + claimId + ": " + cachedStatus.isLocked);
                return cachedStatus.isLocked;
            }

            Logger.logDebugInfo("Checking GPFlags for claim " + claimId + " player " + player.getName());

            // GPFlags API is not thread-safe - must run on main thread
            // If we're already on the main thread, run directly; otherwise schedule sync
            boolean isLocked;
            if (Bukkit.isPrimaryThread()) {
                isLocked = checkGPFlagsOnMainThread(location, claim, player);
            } else {
                isLocked = checkGPFlagsSynchronously(location, claim, player);
            }

            // Update cache
            lockedClaimCache.put(cacheKey, new CachedClaimStatus(isLocked, System.currentTimeMillis()));
            Logger.logDebugInfo("GPFlags check result for claim " + claimId + ": " + isLocked);

            return isLocked;
        } catch (Exception e) {
            Logger.logError("Error checking GriefPrevention flags: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Run GPFlags check synchronously on main thread from async context.
     * Uses callSyncMethod with timeout to prevent deadlocks.
     */
    private boolean checkGPFlagsSynchronously(Location location, Claim claim, Player player) {
        try {
            Future<Boolean> future = Bukkit.getScheduler().callSyncMethod(
                FindItemAddOn.getInstance(),
                () -> checkGPFlagsOnMainThread(location, claim, player)
            );
            // 5 second timeout to prevent hanging forever
            Boolean result = future.get(5, TimeUnit.SECONDS);
            return result != null && result;
        } catch (TimeoutException e) {
            Logger.logWarning("GPFlags check timed out for claim " + claim.getID());
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.logDebugInfo("GPFlags check interrupted for claim " + claim.getID());
            return false;
        } catch (ExecutionException e) {
            Logger.logDebugInfo("Error during GPFlags sync check: " +
                (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
            return false;
        } catch (Exception e) {
            Logger.logDebugInfo("Unexpected error during GPFlags check: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check GPFlags on the main thread. Must only be called from main thread.
     * Uses getFlags(claim) for compatibility with older GPFlags versions.
     */
    private boolean checkGPFlagsOnMainThread(Location location, Claim claim, Player player) {
        boolean isLocked = false;

        try {
            // Get all flags for this claim - more compatible with different GPFlags versions
            java.util.Collection<Flag> claimFlags = flagManager.getFlags(claim);
            if (claimFlags == null || claimFlags.isEmpty()) {
                Logger.logDebugInfo("No flags found for claim " + claim.getID());
                return false;
            }

            Logger.logDebugInfo("Found " + claimFlags.size() + " flags for claim " + claim.getID());

            for (Flag flag : claimFlags) {
                if (flag == null || !flag.getSet()) {
                    continue;
                }

                String flagName = flag.getFlagDefinition() != null ? flag.getFlagDefinition().getName() : null;
                if (flagName == null) {
                    continue;
                }

                Logger.logDebugInfo("Checking flag: " + flagName);

                // Check NoEnter flag (blocks all non-trusted players)
                if ("NoEnter".equalsIgnoreCase(flagName)) {
                    String accessError = claim.allowAccess(player);
                    if (accessError != null) {
                        Logger.logDebugInfo("NoEnter flag blocks player " + player.getName());
                        isLocked = true;
                        break;
                    }
                }

                // Check NoEnterPlayer flag (blocks specific players by name)
                if ("NoEnterPlayer".equalsIgnoreCase(flagName)) {
                    String params = flag.parameters;
                    if (params != null && params.toUpperCase().contains(player.getName().toUpperCase())) {
                        String accessError = claim.allowAccess(player);
                        if (accessError != null) {
                            Logger.logDebugInfo("NoEnterPlayer flag blocks player " + player.getName());
                            isLocked = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.logDebugInfo("Error checking GPFlags: " + e.getMessage());
        }

        return isLocked;
    }

    /**
     * Clear all caches. Call this if flags or permissions are changed.
     */
    public void clearCache() {
        lockedClaimCache.clear();
        bannedClaimCache.clear();
        Logger.logDebugInfo("GriefPrevention caches cleared");
    }

    public boolean isGriefPreventionEnabled() {
        return isGriefPreventionEnabled;
    }

    public boolean isGPFlagsEnabled() {
        return isGPFlagsEnabled;
    }

    /**
     * @deprecated Use isGriefPreventionEnabled() or isGPFlagsEnabled() instead
     */
    @Deprecated
    public boolean isEnabled() {
        return isGriefPreventionEnabled;
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
