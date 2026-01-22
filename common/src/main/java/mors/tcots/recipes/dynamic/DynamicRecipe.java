package mors.tcots.recipes.dynamic;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingBookCategory;

import java.util.List;

public interface DynamicRecipe {

    JsonObject export();

    void saveDynamic(final List<DynamicRecipe> recipes, final String suffix);

    default void save(final List<DynamicRecipe> recipes){
        this.save(recipes, id());
    };

    default void save(final List<DynamicRecipe> recipes, final ResourceLocation name){
        this.save(recipes, name.toString());
    };

    default void save(final List<DynamicRecipe> recipes, final String name){
        this.id(ResourceLocation.tryParse(name));
        recipes.add(this);
    }

    default CraftingBookCategory determineBookCategory(final RecipeCategory category) {
        return switch (category) {
            case BUILDING_BLOCKS -> CraftingBookCategory.BUILDING;
            case TOOLS, COMBAT -> CraftingBookCategory.EQUIPMENT;
            case REDSTONE -> CraftingBookCategory.REDSTONE;
            default -> CraftingBookCategory.MISC;
        };
    }

    RecipeCategory category();

    ResourceLocation id();

    DynamicRecipe id(ResourceLocation id);
}
