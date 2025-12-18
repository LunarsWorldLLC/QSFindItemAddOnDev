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
package io.myzticbean.finditemaddon.utils;

import io.myzticbean.finditemaddon.FindItemAddOn;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling custom item searches (ExecutableItems, etc.)
 * @author myzticbean
 */
public class CustomItemSearchUtil {

    public static final String CUSTOM_ITEM_PREFIX = "custom:";

    /**
     * Checks if the search query is a custom item search
     * @param query The search query
     * @return true if query starts with "custom:"
     */
    public static boolean isCustomItemSearch(String query) {
        return query != null && query.toLowerCase().startsWith(CUSTOM_ITEM_PREFIX);
    }

    /**
     * Extracts the custom item name from a search query
     * @param query The search query (e.g., "custom:super_sword")
     * @return The custom item name (e.g., "super_sword"), or null if invalid
     */
    @Nullable
    public static String extractCustomItemName(String query) {
        if (!isCustomItemSearch(query)) {
            return null;
        }
        String itemName = query.substring(CUSTOM_ITEM_PREFIX.length());
        return itemName.isEmpty() ? null : itemName.toLowerCase();
    }

    /**
     * Gets the ExecutableItem ID from a cleaned display name
     * @param cleanedDisplayName The cleaned display name (e.g., "super_sword")
     * @return The ExecutableItem ID, or null if not found or ExecutableItems not enabled
     */
    @Nullable
    public static String getExecutableItemId(String cleanedDisplayName) {
        if (FindItemAddOn.getExecutableItemsPlugin() == null ||
            !FindItemAddOn.getExecutableItemsPlugin().isEnabled()) {
            return null;
        }
        return FindItemAddOn.getExecutableItemsPlugin().getItemIdFromDisplayName(cleanedDisplayName);
    }

    /**
     * Checks if an ItemStack is an ExecutableItem with the specified ID
     * @param item The ItemStack to check
     * @param itemId The ExecutableItem ID to match
     * @return true if the item matches
     */
    public static boolean isCustomItemWithId(ItemStack item, String itemId) {
        if (FindItemAddOn.getExecutableItemsPlugin() == null ||
            !FindItemAddOn.getExecutableItemsPlugin().isEnabled()) {
            return false;
        }
        return FindItemAddOn.getExecutableItemsPlugin().isExecutableItem(item, itemId);
    }

    /**
     * Gets the list of custom item autocomplete entries
     * @return List of "custom:item_name" entries
     */
    public static List<String> getCustomItemAutocompleteList() {
        if (FindItemAddOn.getExecutableItemsPlugin() == null ||
            !FindItemAddOn.getExecutableItemsPlugin().isEnabled()) {
            return new ArrayList<>();
        }
        return FindItemAddOn.getExecutableItemsPlugin().getAutocompleteList();
    }
}
