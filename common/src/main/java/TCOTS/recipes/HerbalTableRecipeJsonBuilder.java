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
    public HerbalTableRecipeJsonBuilder(final ItemStack herb, final List<String> effectsID, final int basePotion, final int tickEffectTime, final int badAmplifier) {
        this.herb=herb;
        this.effectsID = effectsID;
        this.basePotion=basePotion;
        this.tickEffectTime=tickEffectTime;
        this.badAmplifier=badAmplifier;
    }

    public static HerbalTableRecipeJsonBuilder create(final ItemStack herb, final List<MobEffect> PositiveEffectID) {
        return create(herb, PositiveEffectID, 40);
    }

    public static HerbalTableRecipeJsonBuilder create(final ItemStack herb, final List<MobEffect> PositiveEffectID, final int tickEffectTime) {
        return create(herb, PositiveEffectID, tickEffectTime, 0);
    }

    public static HerbalTableRecipeJsonBuilder create(final ItemStack herb, final List<MobEffect> PositiveEffectID, final int tickEffectTime, final int badAmplifier) {

        return create(herb, PositiveEffectID, tickEffectTime, badAmplifier, 0);
    }

    public static HerbalTableRecipeJsonBuilder create(final ItemStack herb, final List<MobEffect> PositiveEffectID, final int tickEffectTime, final int badAmplifier, final int basePotion) {
        final List<String> listPositiveEffects=new ArrayList<>();

        for(final MobEffect effect : PositiveEffectID){
            listPositiveEffects.add(Objects.requireNonNull(BuiltInRegistries.MOB_EFFECT.getKey(effect)).toString());
        }

        return new HerbalTableRecipeJsonBuilder(herb, listPositiveEffects, basePotion, tickEffectTime, badAmplifier);
    }

    @SuppressWarnings("unused")
    public void offerTo(final RecipeOutput exporter, final ResourceLocation recipeId) {
        final HerbalTableRecipe herbalTableRecipe = new HerbalTableRecipe(this.herb, this.effectsID, this.basePotion, this.tickEffectTime, this.badAmplifier);

        exporter.accept(recipeId, herbalTableRecipe, null);
    }

    public void offerTo(final RecipeOutput exporter) {
        final HerbalTableRecipe herbalTableRecipe = new HerbalTableRecipe(this.herb, this.effectsID, this.basePotion, this.tickEffectTime, this.badAmplifier);

        final String recipeID = BuiltInRegistries.ITEM.getKey(herb.getItem()) +"_herbal";

        exporter.accept(ResourceLocation.parse(recipeID), herbalTableRecipe, null);
    }
}
