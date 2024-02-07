package TCOTS.potions.recipes.recipebook;

import TCOTS.potions.recipes.AlchemyTableRecipe;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBook;
import net.minecraft.registry.DynamicRegistryManager;

@Environment(value=EnvType.CLIENT)
public class RecipeAlchemyResultCollection {
    private final DynamicRegistryManager registryManager;
    private final List<AlchemyTableRecipe> recipes;
    private final boolean singleOutput;
    private final Set<AlchemyTableRecipe> craftableRecipes = Sets.newHashSet();
    private final Set<AlchemyTableRecipe> fittingRecipes = Sets.newHashSet();
    private final Set<AlchemyTableRecipe> unlockedRecipes = Sets.newHashSet();

    public RecipeAlchemyResultCollection(DynamicRegistryManager registryManager, List<AlchemyTableRecipe> recipes) {
        this.registryManager = registryManager;
        this.recipes = ImmutableList.copyOf(recipes);
        this.singleOutput = recipes.size() <= 1 ? true : RecipeAlchemyResultCollection.shouldHaveSingleOutput(registryManager, recipes);
    }

    private static boolean shouldHaveSingleOutput(DynamicRegistryManager registryManager, List<AlchemyTableRecipe> recipes) {
        int i = recipes.size();
        ItemStack itemStack = recipes.get(0).getOutput(registryManager);
        for (int j = 1; j < i; ++j) {
            ItemStack itemStack2 = recipes.get(j).getOutput(registryManager);
            if (ItemStack.canCombine(itemStack, itemStack2)) continue;
            return false;
        }
        return true;
    }

    public DynamicRegistryManager getRegistryManager() {
        return this.registryManager;
    }

    public boolean isInitialized() {
        return !this.unlockedRecipes.isEmpty();
    }

    public void initialize(RecipeBook recipeBook) {
        for (AlchemyTableRecipe recipe : this.recipes) {
            if (!recipeBook.contains(recipe)) continue;
            this.unlockedRecipes.add(recipe);
        }
    }

    public void computeCraftables(RecipeMatcher recipeFinder, int gridWidth, int gridHeight, RecipeBook recipeBook) {
        for (AlchemyTableRecipe recipe : this.recipes) {
            boolean bl;
            boolean bl2 = bl = recipe.fits(gridWidth, gridHeight) && recipeBook.contains(recipe);
            if (bl) {
                this.fittingRecipes.add(recipe);
            } else {
                this.fittingRecipes.remove(recipe);
            }
            if (bl && recipeFinder.match(recipe, null)) {
                this.craftableRecipes.add(recipe);
                continue;
            }
            this.craftableRecipes.remove(recipe);
        }
    }

    public boolean isCraftable(AlchemyTableRecipe recipe) {
        return this.craftableRecipes.contains(recipe);
    }

    public boolean hasCraftableRecipes() {
        return !this.craftableRecipes.isEmpty();
    }

    public boolean hasFittingRecipes() {
        return !this.fittingRecipes.isEmpty();
    }

    public List<AlchemyTableRecipe> getAllRecipes() {
        return this.recipes;
    }

    public List<AlchemyTableRecipe> getResults(boolean craftableOnly) {
        ArrayList<AlchemyTableRecipe> list = Lists.newArrayList();
        Set<AlchemyTableRecipe> set = craftableOnly ? this.craftableRecipes : this.fittingRecipes;
        for (AlchemyTableRecipe recipe : this.recipes) {
            if (!set.contains(recipe)) continue;
            list.add(recipe);
        }
        return list;
    }

    public List<AlchemyTableRecipe> getRecipes(boolean craftable) {
        ArrayList<AlchemyTableRecipe> list = Lists.newArrayList();
        for (AlchemyTableRecipe recipe : this.recipes) {
            if (!this.fittingRecipes.contains(recipe) || this.craftableRecipes.contains(recipe) != craftable) continue;
            list.add(recipe);
        }
        return list;
    }

    public boolean hasSingleOutput() {
        return this.singleOutput;
    }
}