package mors.tcots.recipes;

import mors.tcots.registry.TCOTS_Items;
import mors.tcots.items.concoctions.WitcherBombs_Base;
import mors.tcots.items.concoctions.WitcherMonsterOil_Base;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class AlchemyTableRecipeJsonBuilder {
    private final AlchemyTableRecipeCategory category;
    private final float order;
    private final List<Ingredient> ingredients;
    private final List<Integer> ingredientCount;
    private final ItemStack base;
    private final ItemStack output;

    public AlchemyTableRecipeJsonBuilder(final float order, final AlchemyTableRecipeCategory category, final List<Ingredient> ingredients, final List<Integer> ingredientCount, final ItemStack base, final ItemStack output) {
        this.order = order;
        this.category = category;
        this.ingredients = ingredients;
        this.ingredientCount = ingredientCount;
        this.base = base;
        this.output = output;
    }

    public static AlchemyTableRecipeJsonBuilder create(final float order, final Item output, final AlchemyTableRecipeCategory category, final List<ItemStack> ingredients, final ItemStack base) {
        final List<Integer> integerList = new ArrayList<>();

        final List<Ingredient> ingredientList = new ArrayList<>();

        for(final ItemStack ing: ingredients){
            integerList.add(ing.getCount());

            ingredientList.add(Ingredient.of(ing.getItem()));
        }

        return new AlchemyTableRecipeJsonBuilder(order, category, ingredientList, integerList, base, output.getDefaultInstance());
    }

    /**
    Basic recipe creation methods
     */
    public static AlchemyTableRecipeJsonBuilder create(final float order, final ItemStack output, final AlchemyTableRecipeCategory category, final List<ItemStack> ingredients, final Item base) {
        final List<Integer> integerList = new ArrayList<>();

        final List<Ingredient> ingredientList = new ArrayList<>();

        for(final ItemStack ing: ingredients){
            integerList.add(ing.getCount());

            ingredientList.add(Ingredient.of(ing.getItem()));
        }

        return new AlchemyTableRecipeJsonBuilder(order, category, ingredientList, integerList, base.getDefaultInstance(), output);
    }

    public static AlchemyTableRecipeJsonBuilder create(final float order, final Item output, final AlchemyTableRecipeCategory category, final List<ItemStack> ingredients, final Item base) {
        return create(order, output.getDefaultInstance(), category, ingredients, base);
    }

    /**
    Just creates a recipe with the category misc
     */
    public static AlchemyTableRecipeJsonBuilder createMisc(final float order, final ItemStack output, final List<ItemStack> ingredients, final Item base){
     return create(order,output, AlchemyTableRecipeCategory.MISC, ingredients, base);
    }

    /**
    Creates a recipe substance-style (With 2 items as output and using as base White Gull)
     */
    public static AlchemyTableRecipeJsonBuilder createMisc(final float order, final Item output, final List<ItemStack> ingredients){
        return create(order, new ItemStack(output,2), AlchemyTableRecipeCategory.MISC, ingredients, TCOTS_Items.WHITE_GULL.get());
    }

    /**
      Create a basic bomb, using one Gunpowder as base
     */
    public static AlchemyTableRecipeJsonBuilder createBomb(final float order, final Item output, final List<ItemStack> ingredients) {
        return create(order, output, AlchemyTableRecipeCategory.BOMBS_OILS, ingredients, Items.GUNPOWDER);
    }

    /**
    Create a basic oil, using one honeycomb as base
     */
    public static AlchemyTableRecipeJsonBuilder createOil(final float order, final Item output, final List<ItemStack> ingredients) {
        return create(order, output, AlchemyTableRecipeCategory.BOMBS_OILS, ingredients, Items.HONEYCOMB);
    }

    /**
     Creates an upgraded oil, depending on the oil level it add as first ingredient Monster Fat or Alchemy Paste
     */
    public static AlchemyTableRecipeJsonBuilder createOil(final float order, final Item output, final List<ItemStack> ingredients, final int countSpecial, final Item base) {
        int level = 0;
        if(output instanceof final WitcherMonsterOil_Base bomb){
            level = bomb.getLevel() - 1;
        }

        return createDecoctionWithLevel(order, output, level, AlchemyTableRecipeCategory.BOMBS_OILS, ingredients, base, new ItemStack(TCOTS_Items.MONSTER_FAT, countSpecial), new ItemStack(TCOTS_Items.ALCHEMY_PASTE, countSpecial));
    }

    /**
    Creates an upgraded bomb,  depending on the bomb level it add as first ingredient Stammelford's Dust or Alchemists' Powder
     */
    public static AlchemyTableRecipeJsonBuilder createBomb(final float order, final Item output, final List<ItemStack> ingredients, final Item base) {
        int level = 0;
        if(output instanceof final WitcherBombs_Base bomb){
            level = bomb.getLevel();
        }

        return createDecoctionWithLevel(order, output, level, AlchemyTableRecipeCategory.BOMBS_OILS, ingredients, base, TCOTS_Items.STAMMELFORDS_DUST.get(), TCOTS_Items.ALCHEMISTS_POWDER.get());
    }

    /**
    Create a basic potion, using a Dwarven Spirit as base
     */
    public static AlchemyTableRecipeJsonBuilder createPotion(final float order, final Item output, final List<ItemStack> ingredients) {
        return create(order, output, AlchemyTableRecipeCategory.POTIONS, ingredients, TCOTS_Items.DWARVEN_SPIRIT.get());
    }

    /**
    Creates a basic decoction potion
     */
    public static AlchemyTableRecipeJsonBuilder createDecoction(final float order, final Item output, final List<ItemStack> ingredients) {
        return create(order, output, AlchemyTableRecipeCategory.DECOCTIONS, ingredients, TCOTS_Items.DWARVEN_SPIRIT.get());
    }

    /**
    Creates an upgraded potion, depending on the level it add as first ingredient Alcohest or White Gull
     */
    public static AlchemyTableRecipeJsonBuilder createPotion(final float order, final Item output, final int level, final List<ItemStack> ingredients, final Item base) {
        return createDecoctionWithLevel(order, output, level, AlchemyTableRecipeCategory.POTIONS, ingredients, base, TCOTS_Items.ALCOHEST.get(), TCOTS_Items.WHITE_GULL.get());
    }

    /**
    Creates a decoction, using the level value to add a main, first ingredient
     */
    public static AlchemyTableRecipeJsonBuilder createDecoctionWithLevel(final float order, final Item output, final int level, final AlchemyTableRecipeCategory category, final List<ItemStack> ingredients, final Item base, final Item firstLevelMain, final Item secondLevelMain) {
        return createDecoctionWithLevel(order, output, level, category, ingredients, base, firstLevelMain.getDefaultInstance(), secondLevelMain.getDefaultInstance());
    }

    public static AlchemyTableRecipeJsonBuilder createDecoctionWithLevel(final float order, final Item output, final int level, final AlchemyTableRecipeCategory category, final List<ItemStack> ingredients, final Item base, final ItemStack firstLevelMain, final ItemStack secondLevelMain) {
        if(level == 1){
            final List<ItemStack> enhancedPotion = new ArrayList<>();
            enhancedPotion.add(firstLevelMain);
            enhancedPotion.addAll(ingredients);
            return create(order, output, category, enhancedPotion, base);
        } else if (level == 2){
            final List<ItemStack> superiorPotion = new ArrayList<>();
            superiorPotion.add(secondLevelMain);
            superiorPotion.addAll(ingredients);
            return create(order, output, category, superiorPotion, base);
        } else {
            return create(order, output, category, ingredients, base);
        }
    }


    /**
    Creates a basic splash potion, just putting one gunpowder as unique ingredient
     */
    public static AlchemyTableRecipeJsonBuilder createPotionSplash(final float order, final Item output, final Item base) {
        return create(order, output, AlchemyTableRecipeCategory.POTIONS, List.of(new ItemStack(Items.GUNPOWDER)), base);
    }

    @SuppressWarnings("unused")
    public void save(final RecipeOutput exporter, final ResourceLocation recipeId) {
        final AlchemyTableRecipe alchemyTableRecipe = new AlchemyTableRecipe(this.order, this.category, this.ingredients, this.ingredientCount, this.base, this.output);

        exporter.accept(recipeId, alchemyTableRecipe, null);
    }

    public void save(final RecipeOutput exporter) {
        final AlchemyTableRecipe alchemyTableRecipe = new AlchemyTableRecipe(this.order, this.category, this.ingredients, this.ingredientCount, this.base, this.output);

        exporter.accept(BuiltInRegistries.ITEM.getKey(output.getItem()), alchemyTableRecipe, null);
    }
}
