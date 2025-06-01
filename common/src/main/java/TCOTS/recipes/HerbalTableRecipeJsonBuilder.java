package TCOTS.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;

public class HerbalTableRecipeJsonBuilder {

    private final ItemStack herb;
    private final List<String> effectsID;
    private final int basePotion;
    private final int tickEffectTime;
    private final int badAmplifier;
    public HerbalTableRecipeJsonBuilder(ItemStack herb, List<String> effectsID, int basePotion, int tickEffectTime, int badAmplifier) {
        this.herb=herb;
        this.effectsID = effectsID;
        this.basePotion=basePotion;
        this.tickEffectTime=tickEffectTime;
        this.badAmplifier=badAmplifier;
    }

    public static HerbalTableRecipeJsonBuilder create(ItemStack herb, List<MobEffect> PositiveEffectID) {
        return create(herb, PositiveEffectID, 40);
    }

    public static HerbalTableRecipeJsonBuilder create(ItemStack herb, List<MobEffect> PositiveEffectID, int tickEffectTime) {
        return create(herb, PositiveEffectID, tickEffectTime, 0);
    }

    public static HerbalTableRecipeJsonBuilder create(ItemStack herb, List<MobEffect> PositiveEffectID, int tickEffectTime, int badAmplifier) {

        return create(herb, PositiveEffectID, tickEffectTime, badAmplifier, 0);
    }

    public static HerbalTableRecipeJsonBuilder create(ItemStack herb, List<MobEffect> PositiveEffectID, int tickEffectTime, int badAmplifier, int basePotion) {
        List<String> listPositiveEffects=new ArrayList<>();

        for(MobEffect effect : PositiveEffectID){
            listPositiveEffects.add(Objects.requireNonNull(BuiltInRegistries.MOB_EFFECT.getKey(effect)).toString());
        }

        return new HerbalTableRecipeJsonBuilder(herb, listPositiveEffects, basePotion, tickEffectTime, badAmplifier);
    }

    @SuppressWarnings("unused")
    public void offerTo(RecipeOutput exporter, ResourceLocation recipeId) {
        HerbalTableRecipe herbalTableRecipe = new HerbalTableRecipe(this.herb, this.effectsID, this.basePotion, this.tickEffectTime, this.badAmplifier);

        exporter.accept(recipeId, herbalTableRecipe, null);
    }

    public void offerTo(RecipeOutput exporter) {
        HerbalTableRecipe herbalTableRecipe = new HerbalTableRecipe(this.herb, this.effectsID, this.basePotion, this.tickEffectTime, this.badAmplifier);

        String recipeID = BuiltInRegistries.ITEM.getKey(herb.getItem()) +"_herbal";

        exporter.accept(ResourceLocation.parse(recipeID), herbalTableRecipe, null);
    }
}
