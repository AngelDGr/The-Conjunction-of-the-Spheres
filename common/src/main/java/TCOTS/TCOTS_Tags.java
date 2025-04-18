package TCOTS;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TCOTS_Tags {
    public static final TagKey<Block> IGNITING_BLOCKS = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"igniting_blocks"));
    public static final TagKey<Block> DESTROYABLE_MAGIC_BLOCKS = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"destroyable_magic_blocks"));
    public static final TagKey<Block> NEGATES_DEVOURER_JUMP = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"negates_devourer_jump"));

    public static final TagKey<EntityType<?>> IGNITING_ENTITIES = createTag("igniting_entities");
    public static final TagKey<EntityType<?>> DIMERITIUM_REMOVAL = createTag("dimeritium_removal");
    public static final TagKey<EntityType<?>> DIMERITIUM_DAMAGE = createTag("dimeritium_damage");
    public static final TagKey<EntityType<?>> BOSS_TAG = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("c", "bosses"));
    public static final TagKey<EntityType<?>> NECROPHAGES = createTag("necrophages");
    public static final TagKey<EntityType<?>> OGROIDS = createTag("ogroids");
    public static final TagKey<EntityType<?>> SPECTERS = createTag("specters");
    public static final TagKey<EntityType<?>> VAMPIRES = createTag("vampires");
    public static final TagKey<EntityType<?>> INSECTOIDS = createTag("insectoid");
    public static final TagKey<EntityType<?>> BEASTS = createTag("beasts");
    public static final TagKey<EntityType<?>> ELEMENTA = createTag("elementa");
    public static final TagKey<EntityType<?>> HYBRIDS = createTag("hybrids");
    public static final TagKey<EntityType<?>> CURSED_ONES = createTag("cursed_ones");
    public static final TagKey<EntityType<?>> DRACONIDS = createTag("draconids");
    public static final TagKey<EntityType<?>> RELICTS = createTag("relicts");
    public static final TagKey<Item> DECAYING_FLESH = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "decaying_flesh"));
    public static final TagKey<Item> MONSTER_BLOOD = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "monster_blood"));

    public static TagKey<EntityType<?>> createTag(String name){
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,name));
    }
}
