package TCOTS.items.concoctions.recipes;

import TCOTS.TCOTS_Main;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

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

    public void offerTo(Consumer<RecipeJsonProvider>  exporter) {

        Identifier recipeID=new Identifier(TCOTS_Main.MOD_ID, Registries.ITEM.getId(this.herb.getItem()).getPath()+"_herbal");

        HerbalTableRecipeJsonProvider herbalTableRecipe =
                new HerbalTableRecipeJsonProvider(
                        recipeID,
                        this.herb,
                        this.effectsID,
                        this.basePotion,
                        this.tickEffectTime,
                        this.badAmplifier);

        exporter.accept(herbalTableRecipe);
    }


    public static class HerbalTableRecipeJsonProvider implements RecipeJsonProvider{

        private final Identifier recipeId;
        private final ItemStack herb;
        private final List<String> effectsID;
        private final int basePotion;
        private final int tickEffectTime;
        private final int badAmplifier;

        public HerbalTableRecipeJsonProvider(
                Identifier recipeId,

                ItemStack herb,
                List<String> effectsID,
                int basePotion,
                int tickEffectTime,
                int badAmplifier
        ){
            this.recipeId=recipeId;
            this.herb=herb;
            this.effectsID=effectsID;
            this.basePotion=basePotion;
            this.tickEffectTime=tickEffectTime;
            this.badAmplifier=badAmplifier;
        }

        @Override
        public void serialize(JsonObject json) {

            json.addProperty("amplifier", this.badAmplifier);

            json.addProperty("base_potion", this.basePotion);

            //Output
            JsonObject jsonObjectOutput = new JsonObject();
            jsonObjectOutput.addProperty("id", Registries.ITEM.getId(this.herb.getItem()).toString());
            if (this.herb.getCount() > 1) {
                jsonObjectOutput.addProperty("count", this.herb.getCount());
            }
            json.add("herb", jsonObjectOutput);

            //Effects
            JsonArray jsonArrayEffects = new JsonArray();
            for (String effect : this.effectsID) {
                jsonArrayEffects.add(effect);
            }
            json.add("effects", jsonArrayEffects);

            json.addProperty("time", this.tickEffectTime);
        }

        @Override
        public Identifier getRecipeId() {
            return recipeId;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return HerbalTableRecipe.Serializer.INSTANCE;
        }

        @Nullable
        @Override
        public JsonObject toAdvancementJson() {
            return null;
        }

        @Nullable
        @Override
        public Identifier getAdvancementId() {
            return null;
        }
    }
}
