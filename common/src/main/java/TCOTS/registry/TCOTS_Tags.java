package TCOTS.registry;

import TCOTS.TCOTS_Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class TCOTS_Tags {
    public static final TagKey<Block> IGNITING_BLOCKS = createBlockTag("igniting_blocks");
    public static final TagKey<Block> DESTROYABLE_MAGIC_BLOCKS = createBlockTag("destroyable_magic_blocks");
    public static final TagKey<Block> NEGATES_DEVOURER_JUMP = createBlockTag("negates_devourer_jump");

    public static final TagKey<EntityType<?>> IGNITING_ENTITIES = createEntityTypeTag("igniting_entities");
    public static final TagKey<EntityType<?>> DIMERITIUM_REMOVAL = createEntityTypeTag("dimeritium_removal");
    public static final TagKey<EntityType<?>> DIMERITIUM_DAMAGE = createEntityTypeTag("dimeritium_damage");
    public static final TagKey<EntityType<?>> BOSS_TAG = createEntityTypeTag("c", "bosses");
    public static final TagKey<EntityType<?>> NECROPHAGES = createEntityTypeTag("necrophages");
    public static final TagKey<EntityType<?>> OGROIDS = createEntityTypeTag("ogroids");
    public static final TagKey<EntityType<?>> SPECTERS = createEntityTypeTag("specters");
    public static final TagKey<EntityType<?>> VAMPIRES = createEntityTypeTag("vampires");
    public static final TagKey<EntityType<?>> INSECTOIDS = createEntityTypeTag("insectoid");
    public static final TagKey<EntityType<?>> BEASTS = createEntityTypeTag("beasts");
    public static final TagKey<EntityType<?>> ELEMENTA = createEntityTypeTag("elementa");
    public static final TagKey<EntityType<?>> HYBRIDS = createEntityTypeTag("hybrids");
    public static final TagKey<EntityType<?>> CURSED_ONES = createEntityTypeTag("cursed_ones");
    public static final TagKey<EntityType<?>> DRACONIDS = createEntityTypeTag("draconids");
    public static final TagKey<EntityType<?>> RELICTS = createEntityTypeTag("relicts");
    public static final TagKey<Item> DECAYING_FLESH = createItemTag("decaying_flesh");
    public static final TagKey<Item> MONSTER_BLOOD = createItemTag("monster_blood");

    public static final TagKey<Item> HERBS = createItemTag("herbs");
    public static final TagKey<Item> LOOT_COMMON = createItemTag("loot_common");
    public static final TagKey<Item> LOOT_UNCOMMON = createItemTag("loot_uncommon");
    public static final TagKey<Item> LOOT_RARE = createItemTag("loot_rare");
    public static final TagKey<Item> MUTAGENS = createItemTag("mutagens");


    //Flora
    public static final TagKey<Biome> CELANDINE_SPAWN = createBiomeTag("has_flora/celandine_spawn");
    public static final TagKey<Biome> VERBENA_SPAWN = createBiomeTag("has_flora/verbena_spawn");
    public static final TagKey<Biome> HAN_FIBER_SPAWN = createBiomeTag("has_flora/han_fiber_spawn");
    public static final TagKey<Biome> CROWS_EYE_SPAWN = createBiomeTag("has_flora/crows_eye_spawn");
    public static final TagKey<Biome> ARENARIA_SPAWN = createBiomeTag("has_flora/arenaria_spawn");

    public static final TagKey<Biome> PUFFBALL_SPAWN_NORMAL = createBiomeTag("has_flora/puffball_spawn_normal");
    public static final TagKey<Biome> PUFFBALL_SPAWN_TAIGA = createBiomeTag("has_flora/puffball_spawn_taiga");
    public static final TagKey<Biome> PUFFBALL_SPAWN_SWAMP = createBiomeTag("has_flora/puffball_spawn_swamp");

    public static final TagKey<Biome> SEWANT_SPAWN_NORMAL = createBiomeTag("has_flora/sewant_spawn_normal");
    public static final TagKey<Biome> SEWANT_SPAWN_TAIGA = createBiomeTag("has_flora/sewant_spawn_taiga");
    public static final TagKey<Biome> SEWANT_SPAWN_DARK = createBiomeTag("has_flora/sewant_spawn_dark");

    public static final TagKey<Biome> MUSHROOM_SPAWN_OLD_GROWTH = createBiomeTag("has_flora/mushroom_spawn_old_growth");

    //Monsters
    public static final TagKey<Biome> DROWNER_SWAMP = createBiomeTag("has_monster/drowner_swamp");
    public static final TagKey<Biome> DROWNER_BEACH = createBiomeTag("has_monster/drowner_beach");
    public static final TagKey<Biome> DROWNER_WATER = createBiomeTag("has_monster/drowner_water");

    public static final TagKey<Biome> ROTFIEND = createBiomeTag("has_monster/rotfiend");

    public static final TagKey<Biome> FOGLET_SWAMP = createBiomeTag("has_monster/foglet_swamp");
    public static final TagKey<Biome> FOGLET_DARK = createBiomeTag("has_monster/foglet_dark");
    public static final TagKey<Biome> FOGLET_HILLS_FORESTS = createBiomeTag("has_monster/foglet_hills_forests");

    public static final TagKey<Biome> WATER_HAG_SWAMP = createBiomeTag("has_monster/water_hag_swamp");
    public static final TagKey<Biome> WATER_HAG_RIVER = createBiomeTag("has_monster/water_hag_river");

    public static final TagKey<Biome> GRAVE_HAG = createBiomeTag("has_monster/grave_hag");

    public static final TagKey<Biome> GHOUL = createBiomeTag("has_monster/ghoul");

    public static final TagKey<Biome> SCURVER = createBiomeTag("has_monster/scurver");

    public static final TagKey<Biome> DEVOURER = createBiomeTag("has_monster/devourer");

    public static final TagKey<Biome> BLOEDZUIGER = createBiomeTag("has_monster/bloedzuiger");

    public static final TagKey<Biome> GRAVEIR = createBiomeTag("has_monster/graveir");


    public static final TagKey<Biome> NEKKER = createBiomeTag("has_monster/nekker");

    public static final TagKey<Biome> CYCLOPS = createBiomeTag("has_monster/cyclops");

    public static final TagKey<Biome> ROCK_TROLL = createBiomeTag("has_monster/rock_troll");

    public static final TagKey<Biome> ICE_TROLL = createBiomeTag("has_monster/ice_troll");

    public static final TagKey<Biome> FOREST_TROLL = createBiomeTag("has_monster/forest_troll");

    public static TagKey<EntityType<?>> createEntityTypeTag(final String name){
        return createEntityTypeTag(TCOTS_Main.MOD_ID, name);
    }

    public static TagKey<EntityType<?>> createEntityTypeTag(final String namespace, final String name){
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(namespace, name));
    }

    public static TagKey<Biome> createBiomeTag(final String name){
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,name));
    }

    public static TagKey<Item> createItemTag(final String name){
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,name));
    }

    public static TagKey<Block> createBlockTag(final String name){
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,name));
    }
}
