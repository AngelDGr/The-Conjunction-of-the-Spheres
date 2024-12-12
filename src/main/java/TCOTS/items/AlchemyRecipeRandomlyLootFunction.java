package TCOTS.items;

import TCOTS.items.concoctions.WitcherAlcohol_Base;
import TCOTS.items.concoctions.WitcherBombs_Base;
import TCOTS.items.concoctions.WitcherMonsterOil_Base;
import TCOTS.items.concoctions.WitcherPotions_Base;
import TCOTS.utils.AlchemyFormulaUtil;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;

public class AlchemyRecipeRandomlyLootFunction extends ConditionalLootFunction {

    public final int decoctions;


    protected AlchemyRecipeRandomlyLootFunction(LootCondition[] conditions, int decoctions) {
        super(conditions);
        this.decoctions=decoctions;
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        Random random = context.getRandom();

        return getRandomFormula(stack, random, getConcoctionsID(this.decoctions));
    }

    public static List<Identifier> getConcoctionsID(){
        return getConcoctionsID(0);
    }


    public static List<Identifier> getConcoctionsID(int decoction){
        List<Identifier> listConcoctions = new ArrayList<>();

        Registries.ITEM.forEach(item ->
                {
                    if(decoction == 0) {
                        if (
                            //If it's potion (No decoction)
                                (item instanceof WitcherPotions_Base potion && !(item instanceof WitcherAlcohol_Base) && !potion.isDecoction())
                                        //If it's bomb
                                        || item instanceof WitcherBombs_Base
                                        //If it's oil
                                        || item instanceof WitcherMonsterOil_Base
                                        //If it's misc item                     //Removes Alcohest and Dwarven Spirit, recipes, so doesn't generate in the loot
                                        || (AlchemyFormulaUtil.isMiscItem(item) && item != TCOTS_Items.DWARVEN_SPIRIT && item != TCOTS_Items.ALCOHEST))

                            listConcoctions.add(Registries.ITEM.getId(item));
                    } else {
                        if(item instanceof WitcherPotions_Base potion && potion.isDecoction())
                            listConcoctions.add(Registries.ITEM.getId(item));
                    }
                }
        );

        return listConcoctions;
    }

    public static ItemStack getRandomFormula(ItemStack stack, Random random, List<Identifier> idList){
        int index = random.nextBetween(0, idList.size()-1);
        if(stack.isOf(TCOTS_Items.ALCHEMY_FORMULA)){
            stack = AlchemyFormulaUtil.setFormula(idList.get(index));
        }

        return stack;
    }

    @Override
    public LootFunctionType getType() {
        return TCOTS_Items.RANDOMIZE_FORMULA;
    }

    public static AlchemyRecipeRandomlyLootFunction.Builder create() {
        return new Builder();
    }

    public static ConditionalLootFunction.Builder<?> builder() {
        return AlchemyRecipeRandomlyLootFunction.builder(conditions -> new AlchemyRecipeRandomlyLootFunction(conditions, 0));
    }

    public static class Builder extends ConditionalLootFunction.Builder<AlchemyRecipeRandomlyLootFunction.Builder> {

        private int decoction;

        @Override
        protected AlchemyRecipeRandomlyLootFunction.Builder getThisBuilder() {
            return this;
        }

        public AlchemyRecipeRandomlyLootFunction.Builder add(int decoction) {
            this.decoction = decoction;
            return this;
        }

        @Override
        public LootFunction build() {
            return new AlchemyRecipeRandomlyLootFunction(this.getConditions(), this.decoction);
        }
    }

    public static class Serializer extends ConditionalLootFunction.Serializer<AlchemyRecipeRandomlyLootFunction> {
        @Override
        public void toJson(JsonObject jsonObject, AlchemyRecipeRandomlyLootFunction referenceLootFunction, JsonSerializationContext jsonSerializationContext) {
            jsonObject.addProperty("decoctions_only", referenceLootFunction.decoctions);
        }

        public AlchemyRecipeRandomlyLootFunction fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootCondition[] lootConditions) {

            return new AlchemyRecipeRandomlyLootFunction(lootConditions, JsonHelper.getInt(jsonObject, "decoctions_only"));
        }
    }}
