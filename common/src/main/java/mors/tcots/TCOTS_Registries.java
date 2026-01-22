package mors.tcots;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;


public class TCOTS_Registries {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(TCOTS_Main.MOD_ID, Registries.MOB_EFFECT);

    public static final DeferredRegister<MapDecorationType> MAP_ICONS = DeferredRegister.create(TCOTS_Main.MOD_ID, Registries.MAP_DECORATION_TYPE);

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(TCOTS_Main.MOD_ID, Registries.ENTITY_TYPE);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(TCOTS_Main.MOD_ID, Registries.ITEM);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(TCOTS_Main.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(TCOTS_Main.MOD_ID, Registries.ARMOR_MATERIAL);

    public static final DeferredRegister<LootItemFunctionType<?>> LOOT_FUNCTIONS = DeferredRegister.create(TCOTS_Main.MOD_ID, Registries.LOOT_FUNCTION_TYPE);

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(TCOTS_Main.MOD_ID, Registries.BLOCK);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(TCOTS_Main.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(TCOTS_Main.MOD_ID, Registries.SOUND_EVENT);

    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURE = DeferredRegister.create(TCOTS_Main.MOD_ID, Registries.CONFIGURED_FEATURE);

    public static final DeferredRegister<MenuType<?>> MENU = DeferredRegister.create(TCOTS_Main.MOD_ID, Registries.MENU);

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(TCOTS_Main.MOD_ID, Registries.RECIPE_SERIALIZER);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE = DeferredRegister.create(TCOTS_Main.MOD_ID, Registries.RECIPE_TYPE);

    public static final DeferredRegister<PlacedFeature> PLACED_FEATURE = DeferredRegister.create(TCOTS_Main.MOD_ID, Registries.PLACED_FEATURE);

    public static final DeferredRegister<StructureProcessorList> PROCESSOR_LIST = DeferredRegister.create(TCOTS_Main.MOD_ID, Registries.PROCESSOR_LIST);

    public static final DeferredRegister<Feature<?>> FEATURE = DeferredRegister.create(TCOTS_Main.MOD_ID, Registries.FEATURE);

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(TCOTS_Main.MOD_ID, Registries.PARTICLE_TYPE);

    public static final DeferredRegister<CriterionTrigger<?>> CRITERIA = DeferredRegister.create(TCOTS_Main.MOD_ID, Registries.TRIGGER_TYPE);

    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(TCOTS_Main.MOD_ID, Registries.VILLAGER_PROFESSION);

    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(TCOTS_Main.MOD_ID, Registries.POINT_OF_INTEREST_TYPE);
}
