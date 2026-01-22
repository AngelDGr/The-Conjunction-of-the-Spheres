package mors.tcots.registry;

import mors.tcots.TCOTS_Main;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class TCOTS_Tags {

    public static class Block {
        public static final TagKey<net.minecraft.world.level.block.Block> IGNITING_BLOCKS = createTag(Registries.BLOCK,"igniting_blocks");
        public static final TagKey<net.minecraft.world.level.block.Block> DESTROYABLE_MAGIC_BLOCKS = createTag(Registries.BLOCK, "destroyable_magic_blocks");
        public static final TagKey<net.minecraft.world.level.block.Block> NEGATES_DEVOURER_JUMP = createTag(Registries.BLOCK, "negates_devourer_jump");
    }

    public static class Entity {
        public static final TagKey<EntityType<?>> IGNITING_ENTITIES = createTag(Registries.ENTITY_TYPE, "igniting_entities");
        public static final TagKey<EntityType<?>> DIMERITIUM_REMOVAL = createTag(Registries.ENTITY_TYPE,"dimeritium_removal");
        public static final TagKey<EntityType<?>> DIMERITIUM_DAMAGE = createTag(Registries.ENTITY_TYPE,"dimeritium_damage");
        public static final TagKey<EntityType<?>> BOSS_TAG = createTag(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("c", "bosses"));

        public static final TagKey<EntityType<?>> NECROPHAGES = createTag(Registries.ENTITY_TYPE,"necrophages");
        public static final TagKey<EntityType<?>> OGROIDS = createTag(Registries.ENTITY_TYPE,"ogroids");
        public static final TagKey<EntityType<?>> SPECTERS = createTag(Registries.ENTITY_TYPE,"specters");
        public static final TagKey<EntityType<?>> VAMPIRES = createTag(Registries.ENTITY_TYPE,"vampires");
        public static final TagKey<EntityType<?>> INSECTOIDS = createTag(Registries.ENTITY_TYPE,"insectoid");
        public static final TagKey<EntityType<?>> BEASTS = createTag(Registries.ENTITY_TYPE,"beasts");
        public static final TagKey<EntityType<?>> ELEMENTA = createTag(Registries.ENTITY_TYPE,"elementa");
        public static final TagKey<EntityType<?>> HYBRIDS = createTag(Registries.ENTITY_TYPE,"hybrids");
        public static final TagKey<EntityType<?>> CURSED_ONES = createTag(Registries.ENTITY_TYPE,"cursed_ones");
        public static final TagKey<EntityType<?>> DRACONIDS = createTag(Registries.ENTITY_TYPE,"draconids");
        public static final TagKey<EntityType<?>> RELICTS = createTag(Registries.ENTITY_TYPE,"relicts");
    }

    public static class Item {
        public static final TagKey<net.minecraft.world.item.Item> DECAYING_FLESH = createTag(Registries.ITEM,"decaying_flesh");
        public static final TagKey<net.minecraft.world.item.Item> MONSTER_BLOOD = createTag(Registries.ITEM,"monster_blood");

        public static final TagKey<net.minecraft.world.item.Item> HERBS = createTag(Registries.ITEM,"herbs");
        public static final TagKey<net.minecraft.world.item.Item> LOOT_COMMON = createTag(Registries.ITEM,"loot_common");
        public static final TagKey<net.minecraft.world.item.Item> LOOT_UNCOMMON = createTag(Registries.ITEM,"loot_uncommon");
        public static final TagKey<net.minecraft.world.item.Item> LOOT_RARE = createTag(Registries.ITEM,"loot_rare");
        public static final TagKey<net.minecraft.world.item.Item> MUTAGENS = createTag(Registries.ITEM,"mutagens");

        public static class SpellEngine {
            public static final TagKey<net.minecraft.world.item.Item> SPELL_INFINITY = createTag(Registries.ITEM, TCOTS_Main.id_Spell_Engine("enchantable/spell_infinity"));

            public static final TagKey<net.minecraft.world.item.Item> CRITICAL_DAMAGE = createTag(Registries.ITEM, TCOTS_Main.id_Spell_Engine("enchantable/critical_damage"));
            public static final TagKey<net.minecraft.world.item.Item> CRITICAL_CHANCE = createTag(Registries.ITEM, TCOTS_Main.id_Spell_Engine("enchantable/critical_chance"));

            public static final TagKey<net.minecraft.world.item.Item> SPELL_NECKLACE = createTag(Registries.ITEM, TCOTS_Main.id_Spell_Engine("spell_necklace"));

        }

        public static class RPGSeries {
            public static final TagKey<net.minecraft.world.item.Item> RPG_MELEE_ARMOR = createTag(Registries.ITEM, TCOTS_Main.id_RPG_Series("armor_type/melee"));
            public static final TagKey<net.minecraft.world.item.Item> RPG_MAGIC_ARMOR = createTag(Registries.ITEM, TCOTS_Main.id_RPG_Series("armor_type/magic"));
            public static final TagKey<net.minecraft.world.item.Item> RPG_LOOT_TIER_10_ARMORS = createTag(Registries.ITEM, TCOTS_Main.id_RPG_Series("loot_tier/tier_10_armors"));

            public static final TagKey<net.minecraft.world.item.Item> MAGIC_DAMAGE_WEAPON = createTag(Registries.ITEM, TCOTS_Main.id_RPG_Series("archetype/magic_damage_weapon"));
            public static final TagKey<net.minecraft.world.item.Item> SPELL_BLADE = createTag(Registries.ITEM, TCOTS_Main.id_RPG_Series("weapon_type/spell_blade"));
        }

        public static class WitcherRPG {
            public static final TagKey<net.minecraft.world.item.Item> GLYPH_ATTACHABLE = createTag(Registries.ITEM, TCOTS_Main.id_WitcherRPG("glyph_attachable"));
            public static final TagKey<net.minecraft.world.item.Item> WITCHER_ARMOR = createTag(Registries.ITEM, TCOTS_Main.id_WitcherRPG("witcher_armor"));
            public static final TagKey<net.minecraft.world.item.Item> ENCHANTABLE_SIGN = createTag(Registries.ITEM, TCOTS_Main.id_WitcherRPG("enchantable/sign_intensity"));

            public static final TagKey<net.minecraft.world.item.Item> RUNESTONE_ATTACHABLE = createTag(Registries.ITEM, TCOTS_Main.id_WitcherRPG("runestone_attachable"));
            public static final TagKey<net.minecraft.world.item.Item> WITCHER_SWORDS = createTag(Registries.ITEM, TCOTS_Main.id_WitcherRPG("witcher_swords"));
            public static final TagKey<net.minecraft.world.item.Item> STEEL_SWORDS = createTag(Registries.ITEM, TCOTS_Main.id_WitcherRPG("steel_swords"));
            public static final TagKey<net.minecraft.world.item.Item> SILVER_SWORDS = createTag(Registries.ITEM, TCOTS_Main.id_WitcherRPG("silver_swords"));

            public static final TagKey<net.minecraft.world.item.Item> WITCHER_MEDALLIONS = createTag(Registries.ITEM, TCOTS_Main.id_WitcherRPG("witcher_medallions"));
        }


        public static class Misc {
            public static final TagKey<net.minecraft.world.item.Item> TRINKETS_NECKLACE = createTag(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("trinkets", "chest/necklace"));
            public static final TagKey<net.minecraft.world.item.Item> ACCESSORIES_NECKLACE = createTag(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("accessories", "necklace"));
        }
    }

    public static class Biome {
        //Flora
        public static final TagKey<net.minecraft.world.level.biome.Biome> CELANDINE_SPAWN = createTag(Registries.BIOME,"has_flora/celandine_spawn");
        public static final TagKey<net.minecraft.world.level.biome.Biome> VERBENA_SPAWN = createTag(Registries.BIOME,"has_flora/verbena_spawn");
        public static final TagKey<net.minecraft.world.level.biome.Biome> HAN_FIBER_SPAWN = createTag(Registries.BIOME,"has_flora/han_fiber_spawn");
        public static final TagKey<net.minecraft.world.level.biome.Biome> CROWS_EYE_SPAWN = createTag(Registries.BIOME,"has_flora/crows_eye_spawn");
        public static final TagKey<net.minecraft.world.level.biome.Biome> ARENARIA_SPAWN = createTag(Registries.BIOME,"has_flora/arenaria_spawn");
        public static final TagKey<net.minecraft.world.level.biome.Biome> PUFFBALL_SPAWN_NORMAL = createTag(Registries.BIOME,"has_flora/puffball_spawn_normal");
        public static final TagKey<net.minecraft.world.level.biome.Biome> PUFFBALL_SPAWN_TAIGA = createTag(Registries.BIOME,"has_flora/puffball_spawn_taiga");
        public static final TagKey<net.minecraft.world.level.biome.Biome> PUFFBALL_SPAWN_SWAMP = createTag(Registries.BIOME,"has_flora/puffball_spawn_swamp");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SEWANT_SPAWN_NORMAL = createTag(Registries.BIOME,"has_flora/sewant_spawn_normal");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SEWANT_SPAWN_TAIGA = createTag(Registries.BIOME,"has_flora/sewant_spawn_taiga");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SEWANT_SPAWN_DARK = createTag(Registries.BIOME,"has_flora/sewant_spawn_dark");
        public static final TagKey<net.minecraft.world.level.biome.Biome> MUSHROOM_SPAWN_OLD_GROWTH = createTag(Registries.BIOME,"has_flora/mushroom_spawn_old_growth");

        //Monsters
        public static final TagKey<net.minecraft.world.level.biome.Biome> DROWNER_SWAMP = createTag(Registries.BIOME,"has_monster/drowner_swamp");
        public static final TagKey<net.minecraft.world.level.biome.Biome> DROWNER_BEACH = createTag(Registries.BIOME,"has_monster/drowner_beach");
        public static final TagKey<net.minecraft.world.level.biome.Biome> DROWNER_WATER = createTag(Registries.BIOME,"has_monster/drowner_water");
        public static final TagKey<net.minecraft.world.level.biome.Biome> ROTFIEND = createTag(Registries.BIOME,"has_monster/rotfiend");
        public static final TagKey<net.minecraft.world.level.biome.Biome> FOGLET_SWAMP = createTag(Registries.BIOME,"has_monster/foglet_swamp");
        public static final TagKey<net.minecraft.world.level.biome.Biome> FOGLET_DARK = createTag(Registries.BIOME,"has_monster/foglet_dark");
        public static final TagKey<net.minecraft.world.level.biome.Biome> FOGLET_HILLS_FORESTS = createTag(Registries.BIOME,"has_monster/foglet_hills_forests");
        public static final TagKey<net.minecraft.world.level.biome.Biome> WATER_HAG_SWAMP = createTag(Registries.BIOME,"has_monster/water_hag_swamp");
        public static final TagKey<net.minecraft.world.level.biome.Biome> WATER_HAG_RIVER = createTag(Registries.BIOME,"has_monster/water_hag_river");
        public static final TagKey<net.minecraft.world.level.biome.Biome> GRAVE_HAG = createTag(Registries.BIOME,"has_monster/grave_hag");
        public static final TagKey<net.minecraft.world.level.biome.Biome> GHOUL = createTag(Registries.BIOME,"has_monster/ghoul");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SCURVER = createTag(Registries.BIOME,"has_monster/scurver");
        public static final TagKey<net.minecraft.world.level.biome.Biome> DEVOURER = createTag(Registries.BIOME,"has_monster/devourer");
        public static final TagKey<net.minecraft.world.level.biome.Biome> BLOEDZUIGER = createTag(Registries.BIOME,"has_monster/bloedzuiger");
        public static final TagKey<net.minecraft.world.level.biome.Biome> GRAVEIR = createTag(Registries.BIOME,"has_monster/graveir");
        public static final TagKey<net.minecraft.world.level.biome.Biome> NEKKER = createTag(Registries.BIOME,"has_monster/nekker");
        public static final TagKey<net.minecraft.world.level.biome.Biome> CYCLOPS = createTag(Registries.BIOME,"has_monster/cyclops");
        public static final TagKey<net.minecraft.world.level.biome.Biome> ROCK_TROLL = createTag(Registries.BIOME,"has_monster/rock_troll");
        public static final TagKey<net.minecraft.world.level.biome.Biome> ICE_TROLL = createTag(Registries.BIOME,"has_monster/ice_troll");
        public static final TagKey<net.minecraft.world.level.biome.Biome> FOREST_TROLL = createTag(Registries.BIOME,"has_monster/forest_troll");

        //Structures
        public static final TagKey<net.minecraft.world.level.biome.Biome> GHOUL_NEST = createTag(Registries.BIOME,"has_structure/ghoul_nest");
        public static final TagKey<net.minecraft.world.level.biome.Biome> NEKKER_NEST = createTag(Registries.BIOME,"has_structure/nekker_nest");
        public static final TagKey<net.minecraft.world.level.biome.Biome> ROCK_TROLL_CAVE = createTag(Registries.BIOME,"has_structure/rock_troll_cave");
        public static final TagKey<net.minecraft.world.level.biome.Biome> ICE_TROLL_CAVE = createTag(Registries.BIOME,"has_structure/ice_troll_cave");
        public static final TagKey<net.minecraft.world.level.biome.Biome> FOREST_TROLL_CAVE = createTag(Registries.BIOME,"has_structure/forest_troll_cave");
        public static final TagKey<net.minecraft.world.level.biome.Biome> ICE_GIANT_CAVE = createTag(Registries.BIOME,"has_structure/ice_giant_cave");
    }

    private static <T> TagKey<T> createTag(final ResourceKey<Registry<T>> registryResourceKey, final String name) {
        return createTag(registryResourceKey, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name));
    }

    private static <T> TagKey<T> createTag(final ResourceKey<Registry<T>> registryResourceKey, final ResourceLocation resourceLocation) {
        return TagKey.create(registryResourceKey, resourceLocation);
    }
}
