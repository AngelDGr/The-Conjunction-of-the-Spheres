package TCOTS.registry.neoforge;

import TCOTS.TCOTS_Main;
import TCOTS.items.components.CustomEffectsComponent;
import TCOTS.items.components.MonsterOilComponent;
import TCOTS.items.components.RecipeTeacherComponent;
import TCOTS.registry.TCOTS_Items;
import com.mojang.serialization.Codec;
import neoforge.TCOTS.TCOTS_Registries;
import neoforge.TCOTS.items.AlchemyBookItem;
import neoforge.TCOTS.items.WitcherBestiaryItem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
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

    private static <T> Supplier<DataComponentType<T>> registerDataComponent(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
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
}