package TCOTS.items.concoctions.recipes;

import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public static HerbalTableRecipeJsonBuilder create(ItemStack herb, List<StatusEffect> PositiveEffectID) {
        return create(herb, PositiveEffectID, 40);
    }

    public static HerbalTableRecipeJsonBuilder create(ItemStack herb, List<StatusEffect> PositiveEffectID, int tickEffectTime) {
        return create(herb, PositiveEffectID, tickEffectTime, 0);
    }

    public static HerbalTableRecipeJsonBuilder create(ItemStack herb, List<StatusEffect> PositiveEffectID, int tickEffectTime, int badAmplifier) {

        return create(herb, PositiveEffectID, tickEffectTime, badAmplifier, 0);
    }

    public static HerbalTableRecipeJsonBuilder create(ItemStack herb, List<StatusEffect> PositiveEffectID, int tickEffectTime, int badAmplifier, int basePotion) {
        List<String> listPositiveEffects=new ArrayList<>();

        for(StatusEffect effect : PositiveEffectID){
            listPositiveEffects.add(Objects.requireNonNull(Registries.STATUS_EFFECT.getId(effect)).toString());
        }

        return new HerbalTableRecipeJsonBuilder(herb, listPositiveEffects, basePotion, tickEffectTime, badAmplifier);
    }

    @SuppressWarnings("unused")
    public void offerTo(RecipeExporter exporter, Identifier recipeId) {
        HerbalTableRecipe herbalTableRecipe = new HerbalTableRecipe(this.herb, this.effectsID, this.basePotion, this.tickEffectTime, this.badAmplifier);

        exporter.accept(recipeId, herbalTableRecipe, null);
    }

    public void offerTo(RecipeExporter exporter) {
        HerbalTableRecipe herbalTableRecipe = new HerbalTableRecipe(this.herb, this.effectsID, this.basePotion, this.tickEffectTime, this.badAmplifier);

        String recipeID = Registries.ITEM.getId(herb.getItem()) +"_herbal";

        exporter.accept(new Identifier(recipeID), herbalTableRecipe, null);
    }
}
