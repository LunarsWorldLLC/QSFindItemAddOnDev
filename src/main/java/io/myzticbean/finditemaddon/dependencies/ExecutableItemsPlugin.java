/**
 * QSFindItemAddOn: An Minecraft add-on plugin for the QuickShop Hikari
 * and Reremake Shop plugins for Spigot server platform.
 * Copyright (C) 2021  myzticbean
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.myzticbean.finditemaddon.dependencies;

import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import com.ssomar.score.api.executableitems.config.ExecutableItemInterface;
import com.ssomar.score.api.executableitems.config.ExecutableItemsManagerInterface;
import io.myzticbean.finditemaddon.utils.log.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Integration with ExecutableItems plugin
 * @author myzticbean
 */
public class ExecutableItemsPlugin {

    private boolean isExecutableItemsEnabled = false;
    private ExecutableItemsManagerInterface eiManager = null;

    // Maps cleaned display name -> ExecutableItem ID
    private final Map<String, String> displayNameToIdMap = new HashMap<>();
    // Maps ExecutableItem ID -> cleaned display name
    private final Map<String, String> idToDisplayNameMap = new HashMap<>();
    // List of autocomplete entries (custom:display_name)
    private final List<String> autocompleteList = new ArrayList<>();

    public ExecutableItemsPlugin() {
        checkExecutableItemsPlugin();
        if (isExecutableItemsEnabled) {
            loadExecutableItems();
        }
    }

    private void checkExecutableItemsPlugin() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("ExecutableItems");
        if (plugin != null && plugin.isEnabled()) {
            isExecutableItemsEnabled = true;
            eiManager = ExecutableItemsAPI.getExecutableItemsManager();
            Logger.logInfo("ExecutableItems hooked!");
        }
    }

    /**
     * Loads all ExecutableItems and creates the display name mappings
     */
    private void loadExecutableItems() {
        if (!isExecutableItemsEnabled || eiManager == null) {
            return;
        }

        List<String> itemIds = eiManager.getExecutableItemIdsList();
        Logger.logInfo("Loading " + itemIds.size() + " ExecutableItems for shop search...");

        for (String itemId : itemIds) {
            try {
                Optional<ExecutableItemInterface> eiOpt = eiManager.getExecutableItem(itemId);
                if (eiOpt.isPresent()) {
                    ExecutableItemInterface ei = eiOpt.get();
                    // Build the item to get its display name
                    ItemStack item = ei.buildItem(1, Optional.empty(), Optional.empty());
                    if (item != null && item.hasItemMeta()) {
                        ItemMeta meta = item.getItemMeta();
                        if (meta != null && meta.hasDisplayName()) {
                            String displayName = meta.getDisplayName();
                            String cleanedName = cleanDisplayName(displayName);

                            displayNameToIdMap.put(cleanedName.toLowerCase(), itemId);
                            idToDisplayNameMap.put(itemId, cleanedName);
                            autocompleteList.add("custom:" + cleanedName);

                            Logger.logDebugInfo("Loaded ExecutableItem: " + itemId + " -> custom:" + cleanedName);
                        }
                    }
                }
            } catch (Exception e) {
                Logger.logError("Failed to load ExecutableItem: " + itemId + " - " + e.getMessage());
            }
        }

        Logger.logInfo("Loaded " + autocompleteList.size() + " ExecutableItems for autocomplete");
    }

    /**
     * Cleans a display name by:
     * - Stripping all color codes
     * - Replacing spaces with underscores
     * - Removing [ and ] characters
     * @param displayName The raw display name with color codes
     * @return Cleaned display name
     */
    private String cleanDisplayName(String displayName) {
        // Strip color codes (both § and &)
        String cleaned = ChatColor.stripColor(displayName);
        // Also strip any remaining & color codes that weren't translated
        cleaned = cleaned.replaceAll("&[0-9a-fk-or]", "");
        // Also strip hex color codes like &#ffffff
        cleaned = cleaned.replaceAll("&#[0-9a-fA-F]{6}", "");
        // Replace spaces with underscores
        cleaned = cleaned.replace(" ", "_");
        // Remove [ and ] characters
        cleaned = cleaned.replace("[", "").replace("]", "");
        // Remove any double underscores that might have been created
        while (cleaned.contains("__")) {
            cleaned = cleaned.replace("__", "_");
        }
        // Trim underscores from start and end
        cleaned = cleaned.replaceAll("^_+|_+$", "");
        return cleaned;
    }

    /**
     * Gets the ExecutableItem ID from a cleaned display name
     * @param cleanedDisplayName The cleaned display name (e.g., "super_sword")
     * @return The ExecutableItem ID, or null if not found
     */
    @Nullable
    public String getItemIdFromDisplayName(String cleanedDisplayName) {
        return displayNameToIdMap.get(cleanedDisplayName.toLowerCase());
    }

    /**
     * Gets the cleaned display name from an ExecutableItem ID
     * @param itemId The ExecutableItem ID
     * @return The cleaned display name, or null if not found
     */
    @Nullable
    public String getDisplayNameFromItemId(String itemId) {
        return idToDisplayNameMap.get(itemId);
    }

    /**
     * Checks if an ItemStack is an ExecutableItem with a specific ID
     * @param itemStack The ItemStack to check
     * @param itemId The ExecutableItem ID to match
     * @return true if the ItemStack is the specified ExecutableItem
     */
    public boolean isExecutableItem(ItemStack itemStack, String itemId) {
        if (!isExecutableItemsEnabled || eiManager == null || itemStack == null) {
            return false;
        }

        Optional<ExecutableItemInterface> eiOpt = eiManager.getExecutableItem(itemStack);
        if (eiOpt.isPresent()) {
            return eiOpt.get().getId().equalsIgnoreCase(itemId);
        }
        return false;
    }

    /**
     * Gets the ExecutableItem ID from an ItemStack
     * @param itemStack The ItemStack to check
     * @return The ExecutableItem ID, or null if not an ExecutableItem
     */
    @Nullable
    public String getExecutableItemId(ItemStack itemStack) {
        if (!isExecutableItemsEnabled || eiManager == null || itemStack == null) {
            return null;
        }

        Optional<ExecutableItemInterface> eiOpt = eiManager.getExecutableItem(itemStack);
        return eiOpt.map(ExecutableItemInterface::getId).orElse(null);
    }

    /**
     * Gets the list of autocomplete entries for custom items
     * @return List of "custom:display_name" entries
     */
    public List<String> getAutocompleteList() {
        return new ArrayList<>(autocompleteList);
    }

    /**
     * @return true if ExecutableItems is enabled
     */
    public boolean isEnabled() {
        return isExecutableItemsEnabled;
    }

    /**
     * Reloads the ExecutableItems mappings
     */
    public void reload() {
        displayNameToIdMap.clear();
        idToDisplayNameMap.clear();
        autocompleteList.clear();
        if (isExecutableItemsEnabled) {
            loadExecutableItems();
        }
    }
}
