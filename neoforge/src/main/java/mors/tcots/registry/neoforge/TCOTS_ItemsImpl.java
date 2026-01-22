package mors.tcots.registry.neoforge;

import mors.tcots.TCOTS_Main;
import mors.tcots.compat.TCOTS_AccessoriesCompat;
import mors.tcots.items.components.CustomEffectsComponent;
import mors.tcots.items.components.MonsterOilComponent;
import mors.tcots.items.components.RecipeTeacherComponent;
import mors.tcots.registry.TCOTS_Items;
import mors.tcots.utils.TCOTS_Util;
import com.mojang.serialization.Codec;
import mors.neoforge.tcots.TCOTS_Registries;
import mors.neoforge.tcots.items.AlchemyBookItem;
import mors.neoforge.tcots.items.WitcherBestiaryItem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import vazkii.patchouli.common.item.PatchouliDataComponents;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@SuppressWarnings("unused")
public class TCOTS_ItemsImpl extends TCOTS_Items {

    //Item Components
    public static Supplier<DataComponentType<String>> REFILL_RECIPE;
    public static Supplier<DataComponentType<MonsterOilComponent>> MONSTER_OIL_COMPONENT;
    public static Supplier<DataComponentType<Boolean>> ANCHOR_RETRIEVE;
    public static Supplier<DataComponentType<RecipeTeacherComponent>> RECIPE_TEACHER_COMPONENT;
    public static Supplier<DataComponentType<CustomEffectsComponent>> CUSTOM_EFFECTS_COMPONENT;

    public static DataComponentType<String> RefillRecipe() { return REFILL_RECIPE.get(); }
    public static DataComponentType<MonsterOilComponent> MonsterOilComponent() { return MONSTER_OIL_COMPONENT.get(); }
    public static DataComponentType<Boolean> AnchorRetrieve() { return ANCHOR_RETRIEVE.get(); }
    public static DataComponentType<RecipeTeacherComponent> RecipeTeacher() { return RECIPE_TEACHER_COMPONENT.get(); }
    public static DataComponentType<CustomEffectsComponent> CustomEffects() { return CUSTOM_EFFECTS_COMPONENT.get(); }

    public static void initDataComponents(){
        REFILL_RECIPE = registerDataComponent("refill_recipe", builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));
        MONSTER_OIL_COMPONENT = registerDataComponent("monster_oil", builder -> builder.persistent(MonsterOilComponent.CODEC));
        ANCHOR_RETRIEVE = registerDataComponent("anchor_retrieve", builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
        RECIPE_TEACHER_COMPONENT = registerDataComponent("recipe_teacher", builder -> builder.persistent(RecipeTeacherComponent.CODEC));
        CUSTOM_EFFECTS_COMPONENT = registerDataComponent("custom_effects", builder -> builder.persistent(CustomEffectsComponent.CODEC));
    }

    private static <T> Supplier<DataComponentType<T>> registerDataComponent(final String id, final UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return TCOTS_Registries.DATA_COMPONENTS.registerComponentType(id, builderOperator);
    }

    public static Item createWitcherBestiary(){
        return new WitcherBestiaryItem(new Item.Properties().stacksTo(1)
                .component(PatchouliDataComponents.BOOK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"witcher_bestiary")));
    }

    public static Item createAlchemyAlmanac(){
        return new AlchemyBookItem(new Item.Properties().stacksTo(1)
                .component(PatchouliDataComponents.BOOK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"alchemy_book")));
    }

    public static Item createManticoreMedallion(){
        if(TCOTS_Util.isAccessoriesLoaded())
            return TCOTS_AccessoriesCompat.MANTICORE_MEDALLION;
        else
            return new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    }
}