package mors.tcots.registry.fabric;

import mors.fabric.tcots.items.AlchemyBookItem;
import mors.fabric.tcots.items.WitcherBestiaryItem;
import mors.tcots.TCOTS_Main;
import mors.tcots.compat.TCOTS_AccessoriesCompat;
import mors.tcots.registry.TCOTS_Items;
import mors.tcots.utils.TCOTS_Util;
import com.mojang.serialization.Codec;
import mors.fabric.tcots.compat.TCOTS_TrinketsCompat;
import mors.tcots.items.components.CustomEffectsComponent;
import mors.tcots.items.components.MonsterOilComponent;
import mors.tcots.items.components.RecipeTeacherComponent;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

import java.util.function.UnaryOperator;

@SuppressWarnings("unused")
public class TCOTS_ItemsImpl extends TCOTS_Items {

    //Item Components
    private static DataComponentType<String> REFILL_RECIPE;
    private static DataComponentType<MonsterOilComponent> MONSTER_OIL_COMPONENT;
    private static DataComponentType<Boolean> ANCHOR_RETRIEVE;
    private static DataComponentType<RecipeTeacherComponent> RECIPE_TEACHER_COMPONENT;
    private static DataComponentType<CustomEffectsComponent> CUSTOM_EFFECTS_COMPONENT;

    public static DataComponentType<String> RefillRecipe() { return REFILL_RECIPE; }
    public static DataComponentType<MonsterOilComponent> MonsterOilComponent() { return MONSTER_OIL_COMPONENT; }
    public static DataComponentType<Boolean> AnchorRetrieve() { return ANCHOR_RETRIEVE; }
    public static DataComponentType<RecipeTeacherComponent> RecipeTeacher() { return RECIPE_TEACHER_COMPONENT; }
    public static DataComponentType<CustomEffectsComponent> CustomEffects() { return CUSTOM_EFFECTS_COMPONENT; }

    public static void initDataComponents() {
        REFILL_RECIPE = registerDataComponent("refill_recipe", builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));
        MONSTER_OIL_COMPONENT = registerDataComponent("monster_oil", builder -> builder.persistent(MonsterOilComponent.CODEC));
        ANCHOR_RETRIEVE = registerDataComponent("anchor_retrieve", builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
        RECIPE_TEACHER_COMPONENT = registerDataComponent("recipe_teacher", builder -> builder.persistent(RecipeTeacherComponent.CODEC));
        CUSTOM_EFFECTS_COMPONENT = registerDataComponent("custom_effects", builder -> builder.persistent(CustomEffectsComponent.CODEC));
    }

    private static <T> DataComponentType<T> registerDataComponent(final String id, final UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, builderOperator.apply(DataComponentType.builder()).build());
    }

    public static Item createWitcherBestiary(){
        return new WitcherBestiaryItem(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "witcher_bestiary"), new Item.Properties().stacksTo(1));
    }

    public static Item createAlchemyAlmanac(){
        return new AlchemyBookItem(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "alchemy_book"), new Item.Properties().stacksTo(1));
    }

    public static Item createManticoreMedallion(){
        if(TCOTS_Util.isTrinketsLoaded())
            return TCOTS_TrinketsCompat.MANTICORE_MEDALLION;
        else if (TCOTS_Util.isAccessoriesLoaded())
            return TCOTS_AccessoriesCompat.MANTICORE_MEDALLION;
        else
            return new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    }
}