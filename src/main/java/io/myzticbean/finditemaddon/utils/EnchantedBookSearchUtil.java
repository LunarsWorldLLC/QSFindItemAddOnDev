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

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling enchanted book searches
 * @author myzticbean
 */
public class EnchantedBookSearchUtil {

    public static final String ENCHANTED_BOOK_PREFIX = "enchanted_book:";

    private static List<String> enchantedBookAutocompleteList = null;

    /**
     * Gets a list of all enchanted book autocomplete options in the format:
     * enchanted_book:enchantment_name (e.g., enchanted_book:sharpness)
     * @return List of autocomplete strings
     */
    public static List<String> getEnchantedBookAutocompleteList() {
        if (enchantedBookAutocompleteList == null) {
            enchantedBookAutocompleteList = new ArrayList<>();
            for (Enchantment enchantment : Registry.ENCHANTMENT) {
                String enchantKey = enchantment.getKey().getKey();
                enchantedBookAutocompleteList.add(ENCHANTED_BOOK_PREFIX + enchantKey);
            }
        }
        return enchantedBookAutocompleteList;
    }

    /**
     * Checks if the search query is an enchanted book search
     * @param query The search query
     * @return true if query starts with "enchanted_book:"
     */
    public static boolean isEnchantedBookSearch(String query) {
        return query != null && query.toLowerCase().startsWith(ENCHANTED_BOOK_PREFIX);
    }

    /**
     * Extracts the enchantment name from an enchanted book search query
     * @param query The search query (e.g., "enchanted_book:sharpness")
     * @return The enchantment name (e.g., "sharpness"), or null if invalid
     */
    @Nullable
    public static String extractEnchantmentName(String query) {
        if (!isEnchantedBookSearch(query)) {
            return null;
        }
        String enchantName = query.substring(ENCHANTED_BOOK_PREFIX.length());
        return enchantName.isEmpty() ? null : enchantName.toLowerCase();
    }

    /**
     * Gets an Enchantment from its name/key
     * @param enchantmentName The enchantment name (e.g., "sharpness", "fortune")
     * @return The Enchantment, or null if not found
     */
    @Nullable
    public static Enchantment getEnchantmentByName(String enchantmentName) {
        if (enchantmentName == null || enchantmentName.isEmpty()) {
            return null;
        }
        NamespacedKey key = NamespacedKey.minecraft(enchantmentName.toLowerCase());
        return Registry.ENCHANTMENT.get(key);
    }

    /**
     * Checks if an ItemStack is an enchanted book containing a specific enchantment
     * @param item The ItemStack to check
     * @param enchantment The enchantment to look for
     * @return true if the item is an enchanted book with the specified stored enchantment
     */
    public static boolean isEnchantedBookWithEnchantment(ItemStack item, Enchantment enchantment) {
        if (item == null || item.getType() != Material.ENCHANTED_BOOK) {
            return false;
        }
        if (!(item.getItemMeta() instanceof EnchantmentStorageMeta meta)) {
            return false;
        }
        return meta.hasStoredEnchant(enchantment);
    }

    /**
     * Checks if an ItemStack is an enchanted book containing an enchantment by name
     * @param item The ItemStack to check
     * @param enchantmentName The enchantment name (e.g., "sharpness")
     * @return true if the item is an enchanted book with the specified stored enchantment
     */
    public static boolean isEnchantedBookWithEnchantment(ItemStack item, String enchantmentName) {
        Enchantment enchantment = getEnchantmentByName(enchantmentName);
        if (enchantment == null) {
            return false;
        }
        return isEnchantedBookWithEnchantment(item, enchantment);
    }
}
