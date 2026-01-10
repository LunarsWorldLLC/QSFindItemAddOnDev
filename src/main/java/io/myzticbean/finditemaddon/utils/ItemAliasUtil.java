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

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for resolving alternative item names to official Material names.
 * This allows users to search using common aliases like "slimeball" instead of "slime_ball".
 * @author myzticbean
 */
public class ItemAliasUtil {

    private static final Map<String, String> ALIASES = new HashMap<>();

    static {
        // Slime-related
        ALIASES.put("slimeball", "SLIME_BALL");
        ALIASES.put("slime", "SLIME_BALL");

        // Magma-related
        ALIASES.put("magmacream", "MAGMA_CREAM");

        // Blaze-related
        ALIASES.put("blazerod", "BLAZE_ROD");
        ALIASES.put("blazepowder", "BLAZE_POWDER");

        // Ender-related
        ALIASES.put("enderpearl", "ENDER_PEARL");
        ALIASES.put("pearl", "ENDER_PEARL");
        ALIASES.put("endereye", "ENDER_EYE");
        ALIASES.put("eyeofender", "ENDER_EYE");

        // Ghast-related
        ALIASES.put("ghasttear", "GHAST_TEAR");

        // Spider-related
        ALIASES.put("spidereye", "SPIDER_EYE");
        ALIASES.put("fermentedspidereye", "FERMENTED_SPIDER_EYE");
        ALIASES.put("string", "STRING");
        ALIASES.put("cobweb", "COBWEB");

        // Gunpowder alternatives
        ALIASES.put("sulphur", "GUNPOWDER");
        ALIASES.put("sulfur", "GUNPOWDER");

        // Fire charge
        ALIASES.put("fireball", "FIRE_CHARGE");
        ALIASES.put("firecharge", "FIRE_CHARGE");

        // Glow-related
        ALIASES.put("glowstonematerial", "GLOWSTONE");
        ALIASES.put("glowstonedust", "GLOWSTONE_DUST");
        ALIASES.put("glowdust", "GLOWSTONE_DUST");
        ALIASES.put("glowinkac", "GLOW_INK_SAC");
        ALIASES.put("glowinksac", "GLOW_INK_SAC");

        // Ink-related
        ALIASES.put("inksac", "INK_SAC");
        ALIASES.put("ink", "INK_SAC");

        // Bone-related
        ALIASES.put("bonemeal", "BONE_MEAL");

        // Redstone-related
        ALIASES.put("redstonedust", "REDSTONE");
        ALIASES.put("redstonetorch", "REDSTONE_TORCH");
        ALIASES.put("redstoneblock", "REDSTONE_BLOCK");
        ALIASES.put("repeater", "REPEATER");
        ALIASES.put("comparator", "COMPARATOR");

        // Lapis-related
        ALIASES.put("lapislazuli", "LAPIS_LAZULI");
        ALIASES.put("lapis", "LAPIS_LAZULI");
        ALIASES.put("lapisblock", "LAPIS_BLOCK");

        // Gold-related
        ALIASES.put("goldingot", "GOLD_INGOT");
        ALIASES.put("goldblock", "GOLD_BLOCK");
        ALIASES.put("goldnugget", "GOLD_NUGGET");
        ALIASES.put("goldore", "GOLD_ORE");
        ALIASES.put("rawgold", "RAW_GOLD");

        // Iron-related
        ALIASES.put("ironingot", "IRON_INGOT");
        ALIASES.put("ironblock", "IRON_BLOCK");
        ALIASES.put("ironnugget", "IRON_NUGGET");
        ALIASES.put("ironore", "IRON_ORE");
        ALIASES.put("rawiron", "RAW_IRON");

        // Diamond-related
        ALIASES.put("diamondblock", "DIAMOND_BLOCK");
        ALIASES.put("diamondore", "DIAMOND_ORE");

        // Emerald-related
        ALIASES.put("emeraldblock", "EMERALD_BLOCK");
        ALIASES.put("emeraldore", "EMERALD_ORE");

        // Copper-related
        ALIASES.put("copperingot", "COPPER_INGOT");
        ALIASES.put("copperblock", "COPPER_BLOCK");
        ALIASES.put("copperore", "COPPER_ORE");
        ALIASES.put("rawcopper", "RAW_COPPER");

        // Netherite-related
        ALIASES.put("netheriteingot", "NETHERITE_INGOT");
        ALIASES.put("netheritescrap", "NETHERITE_SCRAP");
        ALIASES.put("netheriteblock", "NETHERITE_BLOCK");
        ALIASES.put("ancientdebris", "ANCIENT_DEBRIS");

        // Coal-related
        ALIASES.put("coalblock", "COAL_BLOCK");
        ALIASES.put("coalore", "COAL_ORE");
        ALIASES.put("charcoal", "CHARCOAL");

        // Quartz-related
        ALIASES.put("netherquartz", "QUARTZ");
        ALIASES.put("quartzblock", "QUARTZ_BLOCK");
        ALIASES.put("quartzore", "NETHER_QUARTZ_ORE");

        // Amethyst-related
        ALIASES.put("amethystshard", "AMETHYST_SHARD");
        ALIASES.put("amethystblock", "AMETHYST_BLOCK");

        // Food items
        ALIASES.put("steak", "COOKED_BEEF");
        ALIASES.put("cookedsteak", "COOKED_BEEF");
        ALIASES.put("porkchop", "PORKCHOP");
        ALIASES.put("cookedporkchop", "COOKED_PORKCHOP");
        ALIASES.put("cookedpork", "COOKED_PORKCHOP");
        ALIASES.put("goldenapple", "GOLDEN_APPLE");
        ALIASES.put("gapple", "GOLDEN_APPLE");
        ALIASES.put("notchapple", "ENCHANTED_GOLDEN_APPLE");
        ALIASES.put("godapple", "ENCHANTED_GOLDEN_APPLE");
        ALIASES.put("egapple", "ENCHANTED_GOLDEN_APPLE");
        ALIASES.put("enchantedgapple", "ENCHANTED_GOLDEN_APPLE");
        ALIASES.put("goldencarrot", "GOLDEN_CARROT");
        ALIASES.put("gcarrot", "GOLDEN_CARROT");
        ALIASES.put("cookedchicken", "COOKED_CHICKEN");
        ALIASES.put("cookedmutton", "COOKED_MUTTON");
        ALIASES.put("cookedrabbit", "COOKED_RABBIT");
        ALIASES.put("cookedsalmon", "COOKED_SALMON");
        ALIASES.put("cookedcod", "COOKED_COD");
        ALIASES.put("bakedpotato", "BAKED_POTATO");
        ALIASES.put("bread", "BREAD");
        ALIASES.put("cake", "CAKE");
        ALIASES.put("cookie", "COOKIE");
        ALIASES.put("pumpkinpie", "PUMPKIN_PIE");
        ALIASES.put("melonslice", "MELON_SLICE");
        ALIASES.put("melon", "MELON_SLICE");

        // Bucket-related
        ALIASES.put("waterbucket", "WATER_BUCKET");
        ALIASES.put("lavabucket", "LAVA_BUCKET");
        ALIASES.put("milkbucket", "MILK_BUCKET");
        ALIASES.put("milk", "MILK_BUCKET");
        ALIASES.put("powdersnowbucket", "POWDER_SNOW_BUCKET");
        ALIASES.put("axolotlbucket", "AXOLOTL_BUCKET");
        ALIASES.put("codbucket", "COD_BUCKET");
        ALIASES.put("salmonbucket", "SALMON_BUCKET");
        ALIASES.put("tropicalfishbucket", "TROPICAL_FISH_BUCKET");
        ALIASES.put("pufferfishbucket", "PUFFERFISH_BUCKET");
        ALIASES.put("tadpolebucket", "TADPOLE_BUCKET");

        // Book-related
        ALIASES.put("enchantedbook", "ENCHANTED_BOOK");
        ALIASES.put("bookandquill", "WRITABLE_BOOK");
        ALIASES.put("writtenbook", "WRITTEN_BOOK");
        ALIASES.put("writablebook", "WRITABLE_BOOK");

        // Dye-related
        ALIASES.put("bonemealdy", "BONE_MEAL");
        ALIASES.put("whitedye", "WHITE_DYE");
        ALIASES.put("orangedye", "ORANGE_DYE");
        ALIASES.put("magentadye", "MAGENTA_DYE");
        ALIASES.put("lightbluedye", "LIGHT_BLUE_DYE");
        ALIASES.put("yellowdye", "YELLOW_DYE");
        ALIASES.put("limedye", "LIME_DYE");
        ALIASES.put("pinkdye", "PINK_DYE");
        ALIASES.put("graydye", "GRAY_DYE");
        ALIASES.put("lightgraydye", "LIGHT_GRAY_DYE");
        ALIASES.put("cyandye", "CYAN_DYE");
        ALIASES.put("purpledye", "PURPLE_DYE");
        ALIASES.put("bluedye", "BLUE_DYE");
        ALIASES.put("browndye", "BROWN_DYE");
        ALIASES.put("greendye", "GREEN_DYE");
        ALIASES.put("reddye", "RED_DYE");
        ALIASES.put("blackdye", "BLACK_DYE");

        // Mob drops
        ALIASES.put("rottenflesh", "ROTTEN_FLESH");
        ALIASES.put("zombieflesh", "ROTTEN_FLESH");
        ALIASES.put("leather", "LEATHER");
        ALIASES.put("rabbitfoot", "RABBIT_FOOT");
        ALIASES.put("rabbithide", "RABBIT_HIDE");
        ALIASES.put("feather", "FEATHER");
        ALIASES.put("egg", "EGG");
        ALIASES.put("wool", "WHITE_WOOL");
        ALIASES.put("prismarinematerial", "PRISMARINE_SHARD");
        ALIASES.put("prismarineshard", "PRISMARINE_SHARD");
        ALIASES.put("prismarinecrystals", "PRISMARINE_CRYSTALS");
        ALIASES.put("shulkershell", "SHULKER_SHELL");
        ALIASES.put("phantommembrane", "PHANTOM_MEMBRANE");
        ALIASES.put("nautilusshell", "NAUTILUS_SHELL");
        ALIASES.put("heartofthesea", "HEART_OF_THE_SEA");
        ALIASES.put("totemofundying", "TOTEM_OF_UNDYING");
        ALIASES.put("totem", "TOTEM_OF_UNDYING");
        ALIASES.put("netherstar", "NETHER_STAR");
        ALIASES.put("witherskull", "WITHER_SKELETON_SKULL");
        ALIASES.put("skeletonskull", "SKELETON_SKULL");
        ALIASES.put("zombiehead", "ZOMBIE_HEAD");
        ALIASES.put("creeperhead", "CREEPER_HEAD");
        ALIASES.put("playerhead", "PLAYER_HEAD");
        ALIASES.put("dragonhead", "DRAGON_HEAD");
        ALIASES.put("piglinhead", "PIGLIN_HEAD");
        ALIASES.put("dragonegg", "DRAGON_EGG");
        ALIASES.put("elytra", "ELYTRA");
        ALIASES.put("trident", "TRIDENT");

        // Brewing-related
        ALIASES.put("glassottle", "GLASS_BOTTLE");
        ALIASES.put("glassbottle", "GLASS_BOTTLE");
        ALIASES.put("waterbottle", "POTION");
        ALIASES.put("netherwart", "NETHER_WART");
        ALIASES.put("brewingstand", "BREWING_STAND");
        ALIASES.put("cauldron", "CAULDRON");

        // Misc items
        ALIASES.put("namerag", "NAME_TAG");
        ALIASES.put("nametag", "NAME_TAG");
        ALIASES.put("lead", "LEAD");
        ALIASES.put("leash", "LEAD");
        ALIASES.put("saddle", "SADDLE");
        ALIASES.put("horsearmor", "IRON_HORSE_ARMOR");
        ALIASES.put("ironhorsearmor", "IRON_HORSE_ARMOR");
        ALIASES.put("goldenhorsearmor", "GOLDEN_HORSE_ARMOR");
        ALIASES.put("goldhorsearmor", "GOLDEN_HORSE_ARMOR");
        ALIASES.put("diamondhorsearmor", "DIAMOND_HORSE_ARMOR");
        ALIASES.put("leatherhorsearmor", "LEATHER_HORSE_ARMOR");
        ALIASES.put("endcrystal", "END_CRYSTAL");
        ALIASES.put("endercrystal", "END_CRYSTAL");
        ALIASES.put("fireworkrocket", "FIREWORK_ROCKET");
        ALIASES.put("firework", "FIREWORK_ROCKET");
        ALIASES.put("fireworkstar", "FIREWORK_STAR");
        ALIASES.put("experiencebottle", "EXPERIENCE_BOTTLE");
        ALIASES.put("xpbottle", "EXPERIENCE_BOTTLE");
        ALIASES.put("expbottle", "EXPERIENCE_BOTTLE");
        ALIASES.put("bottleoenchanting", "EXPERIENCE_BOTTLE");
        ALIASES.put("enchantingbottle", "EXPERIENCE_BOTTLE");
        ALIASES.put("clock", "CLOCK");
        ALIASES.put("watch", "CLOCK");
        ALIASES.put("compass", "COMPASS");
        ALIASES.put("map", "MAP");
        ALIASES.put("emptymap", "MAP");
        ALIASES.put("filledmap", "FILLED_MAP");
        ALIASES.put("spyglass", "SPYGLASS");
        ALIASES.put("telescope", "SPYGLASS");
        ALIASES.put("recovercompass", "RECOVERY_COMPASS");
        ALIASES.put("recoverycompass", "RECOVERY_COMPASS");
        ALIASES.put("echohard", "ECHO_SHARD");
        ALIASES.put("echoshard", "ECHO_SHARD");
        ALIASES.put("discfragment", "DISC_FRAGMENT_5");
        ALIASES.put("disc", "MUSIC_DISC_CAT");
        ALIASES.put("musicdisc", "MUSIC_DISC_CAT");

        // Wood-related (common simplifications)
        ALIASES.put("oaklog", "OAK_LOG");
        ALIASES.put("oakplank", "OAK_PLANKS");
        ALIASES.put("oakplanks", "OAK_PLANKS");
        ALIASES.put("sprucelog", "SPRUCE_LOG");
        ALIASES.put("spruceplank", "SPRUCE_PLANKS");
        ALIASES.put("spruceplanks", "SPRUCE_PLANKS");
        ALIASES.put("birchlog", "BIRCH_LOG");
        ALIASES.put("birchplank", "BIRCH_PLANKS");
        ALIASES.put("birchplanks", "BIRCH_PLANKS");
        ALIASES.put("junglelog", "JUNGLE_LOG");
        ALIASES.put("jungleplank", "JUNGLE_PLANKS");
        ALIASES.put("jungleplanks", "JUNGLE_PLANKS");
        ALIASES.put("acacialog", "ACACIA_LOG");
        ALIASES.put("acaciaplank", "ACACIA_PLANKS");
        ALIASES.put("acaciaplanks", "ACACIA_PLANKS");
        ALIASES.put("darkoaklog", "DARK_OAK_LOG");
        ALIASES.put("darkoakplank", "DARK_OAK_PLANKS");
        ALIASES.put("darkoakplanks", "DARK_OAK_PLANKS");
        ALIASES.put("mangrovelog", "MANGROVE_LOG");
        ALIASES.put("mangroveplank", "MANGROVE_PLANKS");
        ALIASES.put("mangroveplanks", "MANGROVE_PLANKS");
        ALIASES.put("cherrolog", "CHERRY_LOG");
        ALIASES.put("cherryplank", "CHERRY_PLANKS");
        ALIASES.put("cherryplanks", "CHERRY_PLANKS");
        ALIASES.put("bambooblock", "BAMBOO_BLOCK");
        ALIASES.put("bambooplank", "BAMBOO_PLANKS");
        ALIASES.put("bambooplanks", "BAMBOO_PLANKS");
        ALIASES.put("crimsonsten", "CRIMSON_STEM");
        ALIASES.put("crimsonstem", "CRIMSON_STEM");
        ALIASES.put("crimsonplank", "CRIMSON_PLANKS");
        ALIASES.put("crimsonplanks", "CRIMSON_PLANKS");
        ALIASES.put("warpedstem", "WARPED_STEM");
        ALIASES.put("warpedplank", "WARPED_PLANKS");
        ALIASES.put("warpedplanks", "WARPED_PLANKS");

        // Glass-related
        ALIASES.put("glassblock", "GLASS");
        ALIASES.put("glasspane", "GLASS_PANE");
        ALIASES.put("tintedglass", "TINTED_GLASS");

        // Stone-related
        ALIASES.put("cobble", "COBBLESTONE");
        ALIASES.put("smoothstone", "SMOOTH_STONE");
        ALIASES.put("stonebrick", "STONE_BRICKS");
        ALIASES.put("stonebricks", "STONE_BRICKS");
        ALIASES.put("mossycobble", "MOSSY_COBBLESTONE");
        ALIASES.put("mossycobblestone", "MOSSY_COBBLESTONE");
        ALIASES.put("mossystonebricks", "MOSSY_STONE_BRICKS");
        ALIASES.put("crackedstonebricks", "CRACKED_STONE_BRICKS");

        // Nether-related
        ALIASES.put("netherrack", "NETHERRACK");
        ALIASES.put("netherbrick", "NETHER_BRICKS");
        ALIASES.put("netherbricks", "NETHER_BRICKS");
        ALIASES.put("soulsand", "SOUL_SAND");
        ALIASES.put("soulsoil", "SOUL_SOIL");
        ALIASES.put("basalt", "BASALT");
        ALIASES.put("blackstone", "BLACKSTONE");
        ALIASES.put("glowstone", "GLOWSTONE");

        // End-related
        ALIASES.put("endstone", "END_STONE");
        ALIASES.put("endstonebrick", "END_STONE_BRICKS");
        ALIASES.put("endstonebricks", "END_STONE_BRICKS");
        ALIASES.put("purpurblock", "PURPUR_BLOCK");
        ALIASES.put("chorusfruit", "CHORUS_FRUIT");
        ALIASES.put("poppedchorusfruit", "POPPED_CHORUS_FRUIT");

        // Terracotta/Concrete
        ALIASES.put("terracotta", "TERRACOTTA");
        ALIASES.put("hardclay", "TERRACOTTA");
        ALIASES.put("clay", "CLAY");
        ALIASES.put("clayblock", "CLAY");
        ALIASES.put("clayball", "CLAY_BALL");
        ALIASES.put("brick", "BRICK");
        ALIASES.put("brickblock", "BRICKS");
        ALIASES.put("bricks", "BRICKS");

        // Misc blocks
        ALIASES.put("sponge", "SPONGE");
        ALIASES.put("wetsponge", "WET_SPONGE");
        ALIASES.put("obsidian", "OBSIDIAN");
        ALIASES.put("cryingobsidian", "CRYING_OBSIDIAN");
        ALIASES.put("bedrock", "BEDROCK");
        ALIASES.put("sandstone", "SANDSTONE");
        ALIASES.put("redsandstone", "RED_SANDSTONE");
        ALIASES.put("gravel", "GRAVEL");
        ALIASES.put("sand", "SAND");
        ALIASES.put("redsand", "RED_SAND");
        ALIASES.put("dirt", "DIRT");
        ALIASES.put("grass", "GRASS_BLOCK");
        ALIASES.put("grassblock", "GRASS_BLOCK");
        ALIASES.put("podzol", "PODZOL");
        ALIASES.put("mycelium", "MYCELIUM");
        ALIASES.put("mud", "MUD");
        ALIASES.put("muddbricks", "MUD_BRICKS");
        ALIASES.put("mudbricks", "MUD_BRICKS");
        ALIASES.put("packedmud", "PACKED_MUD");
        ALIASES.put("moss", "MOSS_BLOCK");
        ALIASES.put("mossblock", "MOSS_BLOCK");
        ALIASES.put("dripleaf", "BIG_DRIPLEAF");
        ALIASES.put("bigdripleaf", "BIG_DRIPLEAF");
        ALIASES.put("smalldripleaf", "SMALL_DRIPLEAF");
        ALIASES.put("sporelblossom", "SPORE_BLOSSOM");
        ALIASES.put("sporeblossom", "SPORE_BLOSSOM");
        ALIASES.put("hangingroot", "HANGING_ROOTS");
        ALIASES.put("hangingroots", "HANGING_ROOTS");
        ALIASES.put("azalea", "AZALEA");
        ALIASES.put("floweringazalea", "FLOWERING_AZALEA");
        ALIASES.put("sculk", "SCULK");
        ALIASES.put("sculksensor", "SCULK_SENSOR");
        ALIASES.put("sculkshrieker", "SCULK_SHRIEKER");
        ALIASES.put("sculkcatalyst", "SCULK_CATALYST");
        ALIASES.put("reinforceddeepslate", "REINFORCED_DEEPSLATE");

        // Chest-related
        ALIASES.put("chest", "CHEST");
        ALIASES.put("enderchest", "ENDER_CHEST");
        ALIASES.put("trappedchest", "TRAPPED_CHEST");
        ALIASES.put("barrel", "BARREL");
        ALIASES.put("shulkerbox", "SHULKER_BOX");

        // Workstation blocks
        ALIASES.put("craftingtable", "CRAFTING_TABLE");
        ALIASES.put("workbench", "CRAFTING_TABLE");
        ALIASES.put("furnace", "FURNACE");
        ALIASES.put("blastfurnace", "BLAST_FURNACE");
        ALIASES.put("smoker", "SMOKER");
        ALIASES.put("anvil", "ANVIL");
        ALIASES.put("enchantingtable", "ENCHANTING_TABLE");
        ALIASES.put("enchantmenttable", "ENCHANTING_TABLE");
        ALIASES.put("grindstone", "GRINDSTONE");
        ALIASES.put("smithingtable", "SMITHING_TABLE");
        ALIASES.put("stonecutter", "STONECUTTER");
        ALIASES.put("loom", "LOOM");
        ALIASES.put("cartographytable", "CARTOGRAPHY_TABLE");
        ALIASES.put("fletchingtable", "FLETCHING_TABLE");
        ALIASES.put("composter", "COMPOSTER");
        ALIASES.put("lectern", "LECTERN");
        ALIASES.put("beehive", "BEEHIVE");
        ALIASES.put("beenest", "BEE_NEST");
        ALIASES.put("respawnanchor", "RESPAWN_ANCHOR");
        ALIASES.put("lodestone", "LODESTONE");

        // Decoration
        ALIASES.put("torch", "TORCH");
        ALIASES.put("soultorch", "SOUL_TORCH");
        ALIASES.put("lantern", "LANTERN");
        ALIASES.put("soullantern", "SOUL_LANTERN");
        ALIASES.put("candle", "CANDLE");
        ALIASES.put("seapickle", "SEA_PICKLE");
        ALIASES.put("glowlichen", "GLOW_LICHEN");
        ALIASES.put("shroomlight", "SHROOMLIGHT");
        ALIASES.put("froglight", "OCHRE_FROGLIGHT");
        ALIASES.put("ochrefroglight", "OCHRE_FROGLIGHT");
        ALIASES.put("verdantfroglight", "VERDANT_FROGLIGHT");
        ALIASES.put("pearlescentfroglight", "PEARLESCENT_FROGLIGHT");

        // Banner/Bed
        ALIASES.put("bed", "RED_BED");
        ALIASES.put("banner", "WHITE_BANNER");
        ALIASES.put("shield", "SHIELD");

        // Armor Stand and Item Frame
        ALIASES.put("armorstand", "ARMOR_STAND");
        ALIASES.put("itemframe", "ITEM_FRAME");
        ALIASES.put("glowitemframe", "GLOW_ITEM_FRAME");
        ALIASES.put("painting", "PAINTING");

        // Rails
        ALIASES.put("rail", "RAIL");
        ALIASES.put("poweredrail", "POWERED_RAIL");
        ALIASES.put("detectorrail", "DETECTOR_RAIL");
        ALIASES.put("activatorrail", "ACTIVATOR_RAIL");
        ALIASES.put("minecart", "MINECART");
        ALIASES.put("chestminecart", "CHEST_MINECART");
        ALIASES.put("hopperminecart", "HOPPER_MINECART");
        ALIASES.put("tntminecart", "TNT_MINECART");
        ALIASES.put("furnaceminecart", "FURNACE_MINECART");

        // Boat
        ALIASES.put("boat", "OAK_BOAT");
        ALIASES.put("oakboat", "OAK_BOAT");
        ALIASES.put("spruceboat", "SPRUCE_BOAT");
        ALIASES.put("birchboat", "BIRCH_BOAT");
        ALIASES.put("jungleboat", "JUNGLE_BOAT");
        ALIASES.put("acaciaboat", "ACACIA_BOAT");
        ALIASES.put("darkoakboat", "DARK_OAK_BOAT");
        ALIASES.put("mangroveboat", "MANGROVE_BOAT");
        ALIASES.put("cherryboat", "CHERRY_BOAT");
        ALIASES.put("bambooraft", "BAMBOO_RAFT");

        // Tools with common shortenings
        ALIASES.put("dpickaxe", "DIAMOND_PICKAXE");
        ALIASES.put("dpick", "DIAMOND_PICKAXE");
        ALIASES.put("diamondpick", "DIAMOND_PICKAXE");
        ALIASES.put("dsword", "DIAMOND_SWORD");
        ALIASES.put("daxe", "DIAMOND_AXE");
        ALIASES.put("dshovel", "DIAMOND_SHOVEL");
        ALIASES.put("dhoe", "DIAMOND_HOE");
        ALIASES.put("npickaxe", "NETHERITE_PICKAXE");
        ALIASES.put("npick", "NETHERITE_PICKAXE");
        ALIASES.put("netheritepick", "NETHERITE_PICKAXE");
        ALIASES.put("nsword", "NETHERITE_SWORD");
        ALIASES.put("naxe", "NETHERITE_AXE");
        ALIASES.put("nshovel", "NETHERITE_SHOVEL");
        ALIASES.put("nhoe", "NETHERITE_HOE");
        ALIASES.put("ipickaxe", "IRON_PICKAXE");
        ALIASES.put("ipick", "IRON_PICKAXE");
        ALIASES.put("ironpick", "IRON_PICKAXE");
        ALIASES.put("isword", "IRON_SWORD");
        ALIASES.put("iaxe", "IRON_AXE");
        ALIASES.put("ishovel", "IRON_SHOVEL");
        ALIASES.put("ihoe", "IRON_HOE");

        // Armor with common shortenings
        ALIASES.put("dhelmet", "DIAMOND_HELMET");
        ALIASES.put("dchestplate", "DIAMOND_CHESTPLATE");
        ALIASES.put("dleggings", "DIAMOND_LEGGINGS");
        ALIASES.put("dboots", "DIAMOND_BOOTS");
        ALIASES.put("nhelmet", "NETHERITE_HELMET");
        ALIASES.put("nchestplate", "NETHERITE_CHESTPLATE");
        ALIASES.put("nleggings", "NETHERITE_LEGGINGS");
        ALIASES.put("nboots", "NETHERITE_BOOTS");
        ALIASES.put("ihelmet", "IRON_HELMET");
        ALIASES.put("ichestplate", "IRON_CHESTPLATE");
        ALIASES.put("ileggings", "IRON_LEGGINGS");
        ALIASES.put("iboots", "IRON_BOOTS");

        // Seeds and crops
        ALIASES.put("wheat", "WHEAT");
        ALIASES.put("wheatseeds", "WHEAT_SEEDS");
        ALIASES.put("seeds", "WHEAT_SEEDS");
        ALIASES.put("beetroot", "BEETROOT");
        ALIASES.put("beetrootseeds", "BEETROOT_SEEDS");
        ALIASES.put("potato", "POTATO");
        ALIASES.put("carrot", "CARROT");
        ALIASES.put("pumpkin", "PUMPKIN");
        ALIASES.put("pumpkinseeds", "PUMPKIN_SEEDS");
        ALIASES.put("melonblock", "MELON");
        ALIASES.put("melonseeds", "MELON_SEEDS");
        ALIASES.put("sugarcane", "SUGAR_CANE");
        ALIASES.put("cactus", "CACTUS");
        ALIASES.put("cocoa", "COCOA_BEANS");
        ALIASES.put("cocoabeans", "COCOA_BEANS");
        ALIASES.put("kelp", "KELP");
        ALIASES.put("seagrass", "SEAGRASS");
        ALIASES.put("bamboo", "BAMBOO");
        ALIASES.put("sweetberries", "SWEET_BERRIES");
        ALIASES.put("glowberries", "GLOW_BERRIES");

        // Flowers
        ALIASES.put("dandelion", "DANDELION");
        ALIASES.put("poppy", "POPPY");
        ALIASES.put("rose", "POPPY");
        ALIASES.put("blueorchid", "BLUE_ORCHID");
        ALIASES.put("allium", "ALLIUM");
        ALIASES.put("azurebluet", "AZURE_BLUET");
        ALIASES.put("redtulip", "RED_TULIP");
        ALIASES.put("orangetulip", "ORANGE_TULIP");
        ALIASES.put("whitetulip", "WHITE_TULIP");
        ALIASES.put("pinktulip", "PINK_TULIP");
        ALIASES.put("oxeyedaisy", "OXEYE_DAISY");
        ALIASES.put("cornflower", "CORNFLOWER");
        ALIASES.put("lilyofthevalley", "LILY_OF_THE_VALLEY");
        ALIASES.put("witherrose", "WITHER_ROSE");
        ALIASES.put("sunflower", "SUNFLOWER");
        ALIASES.put("lilac", "LILAC");
        ALIASES.put("peony", "PEONY");
        ALIASES.put("rosebush", "ROSE_BUSH");
        ALIASES.put("torchflower", "TORCHFLOWER");
        ALIASES.put("pitcherpod", "PITCHER_POD");
        ALIASES.put("pitcherplant", "PITCHER_PLANT");

        // Saplings
        ALIASES.put("sapling", "OAK_SAPLING");
        ALIASES.put("oaksapling", "OAK_SAPLING");
        ALIASES.put("sprucesapling", "SPRUCE_SAPLING");
        ALIASES.put("birchsapling", "BIRCH_SAPLING");
        ALIASES.put("junglesapling", "JUNGLE_SAPLING");
        ALIASES.put("acaciasapling", "ACACIA_SAPLING");
        ALIASES.put("darkoaksapling", "DARK_OAK_SAPLING");
        ALIASES.put("mangrovepropagule", "MANGROVE_PROPAGULE");
        ALIASES.put("cherrysapling", "CHERRY_SAPLING");

        // Spawn eggs (generic)
        ALIASES.put("spawnegg", "PIG_SPAWN_EGG");

        // Honey/Honeycomb
        ALIASES.put("honeycomb", "HONEYCOMB");
        ALIASES.put("honeycombblock", "HONEYCOMB_BLOCK");
        ALIASES.put("honeybottle", "HONEY_BOTTLE");
        ALIASES.put("honeyblock", "HONEY_BLOCK");

        // Goat horn
        ALIASES.put("goathorn", "GOAT_HORN");

        // Brushable items
        ALIASES.put("brush", "BRUSH");
        ALIASES.put("suspicioussand", "SUSPICIOUS_SAND");
        ALIASES.put("suspiciousgravel", "SUSPICIOUS_GRAVEL");
        ALIASES.put("potteryshard", "POTTERY_SHERD_ARCHER");
        ALIASES.put("potterysherd", "POTTERY_SHERD_ARCHER");

        // Smithing templates
        ALIASES.put("smithingtemplate", "NETHERITE_UPGRADE_SMITHING_TEMPLATE");
        ALIASES.put("netheriteupgrade", "NETHERITE_UPGRADE_SMITHING_TEMPLATE");
        ALIASES.put("armorim", "COAST_ARMOR_TRIM_SMITHING_TEMPLATE");
        ALIASES.put("armortrim", "COAST_ARMOR_TRIM_SMITHING_TEMPLATE");
    }

    /**
     * Resolves an item name alias to the official Material name.
     * If no alias is found, returns the original input.
     *
     * @param itemName The item name to resolve (case-insensitive)
     * @return The official Material name if an alias exists, otherwise the original input
     */
    public static String resolveAlias(String itemName) {
        if (itemName == null) {
            return null;
        }
        // Normalize: remove underscores and convert to lowercase for matching
        String normalizedInput = itemName.replace("_", "").toLowerCase();

        // Check if there's a direct alias
        String alias = ALIASES.get(normalizedInput);
        if (alias != null) {
            return alias;
        }

        // Return original (will be uppercased by caller for Material.getMaterial)
        return itemName;
    }

    /**
     * Checks if a Material exists for the given item name, trying alias resolution first.
     *
     * @param itemName The item name to check
     * @return The Material if found (via alias or direct match), null otherwise
     */
    public static Material resolveMaterial(String itemName) {
        if (itemName == null) {
            return null;
        }

        // First try to resolve via alias
        String resolved = resolveAlias(itemName);
        Material mat = Material.getMaterial(resolved.toUpperCase());
        if (mat != null) {
            return mat;
        }

        // If alias didn't work, try the original input directly
        return Material.getMaterial(itemName.toUpperCase());
    }
}
