package io.myzticbean.finditemaddon.dependencies;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.myzticbean.finditemaddon.utils.log.Logger;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Integration with CosmosCore to check for claim bans
 * CosmosCore stores banned players in claim-bans.json with claim ID as key
 *
 * @author myzticbean
 */
public class CosmosCorePlugin {

    private boolean isCosmosCoreEnabled = false;
    private boolean isGriefPreventionEnabled = false;
    private Plugin cosmosCore;
    private GriefPrevention griefPrevention;
    private Map<Long, Set<UUID>> bannedClaimPlayers = new HashMap<>();
    private long lastLoadTime = 0;
    private static final long CACHE_REFRESH_INTERVAL_MS = 60000; // Refresh cache every 60 seconds

    public CosmosCorePlugin() {
        checkCosmosCorePlugin();
        checkGriefPreventionPlugin();
        if (isCosmosCoreEnabled) {
            loadBannedPlayers();
        }
    }

    private void checkCosmosCorePlugin() {
        if (Bukkit.getPluginManager().isPluginEnabled("CosmosCore")) {
            cosmosCore = Bukkit.getPluginManager().getPlugin("CosmosCore");
            isCosmosCoreEnabled = cosmosCore != null;
            if (isCosmosCoreEnabled) {
                Logger.logInfo("Found CosmosCore");
            }
        }
    }

    private void checkGriefPreventionPlugin() {
        if (Bukkit.getPluginManager().isPluginEnabled("GriefPrevention")) {
            griefPrevention = (GriefPrevention) Bukkit.getPluginManager().getPlugin("GriefPrevention");
            isGriefPreventionEnabled = griefPrevention != null;
        }
    }

    /**
     * Load banned players from CosmosCore's claim-bans.json file
     */
    private void loadBannedPlayers() {
        if (cosmosCore == null) {
            return;
        }

        File dataFile = new File(cosmosCore.getDataFolder(), "claim-bans.json");
        if (!dataFile.exists()) {
            Logger.logDebugInfo("CosmosCore claim-bans.json not found");
            return;
        }

        try (FileReader reader = new FileReader(dataFile)) {
            Type type = new TypeToken<Map<Long, Set<UUID>>>() {}.getType();
            Map<Long, Set<UUID>> loaded = new Gson().fromJson(reader, type);
            if (loaded != null) {
                bannedClaimPlayers = loaded;
                Logger.logDebugInfo("Loaded " + bannedClaimPlayers.size() + " claim ban entries from CosmosCore");
            }
            lastLoadTime = System.currentTimeMillis();
        } catch (Exception e) {
            Logger.logDebugInfo("Error loading CosmosCore claim bans: " + e.getMessage());
        }
    }

    /**
     * Refresh the cache if it's stale
     */
    private void refreshCacheIfNeeded() {
        if (System.currentTimeMillis() - lastLoadTime > CACHE_REFRESH_INTERVAL_MS) {
            loadBannedPlayers();
        }
    }

    /**
     * Check if the player is banned from the claim at the given location
     *
     * @param location The location to check
     * @param player   The player trying to access the shop
     * @return true if the player is banned from the claim at this location
     */
    public boolean isPlayerBannedFromClaim(Location location, Player player) {
        if (!isCosmosCoreEnabled || !isGriefPreventionEnabled) {
            return false;
        }

        try {
            // Refresh cache if needed
            refreshCacheIfNeeded();

            // Get the claim at this location using GriefPrevention
            Claim claim = griefPrevention.dataStore.getClaimAt(location, false, null);
            if (claim == null) {
                return false;
            }

            Long claimId = claim.getID();
            if (claimId == null) {
                return false;
            }

            // Check if there are any banned players for this claim
            Set<UUID> bannedPlayers = bannedClaimPlayers.get(claimId);
            if (bannedPlayers == null || bannedPlayers.isEmpty()) {
                return false;
            }

            // Check if this player is banned
            if (bannedPlayers.contains(player.getUniqueId())) {
                Logger.logDebugInfo("Player " + player.getName() + " is banned from CosmosCore claim at " + location);
                return true;
            }

            return false;
        } catch (Exception e) {
            Logger.logDebugInfo("Error checking CosmosCore claim ban: " + e.getMessage());
            return false;
        }
    }

    public boolean isEnabled() {
        return isCosmosCoreEnabled && isGriefPreventionEnabled;
    }
}
