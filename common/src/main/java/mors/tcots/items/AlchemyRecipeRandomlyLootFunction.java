package mors.tcots.items;

import mors.tcots.registry.TCOTS_Items;
import mors.tcots.items.concoctions.WitcherAlcohol_Base;
import mors.tcots.items.concoctions.WitcherBombs_Base;
import mors.tcots.items.concoctions.WitcherMonsterOil_Base;
import mors.tcots.items.concoctions.WitcherPotions_Base;
import mors.tcots.utils.AlchemyFormulaUtil;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

public class AlchemyRecipeRandomlyLootFunction extends LootItemConditionalFunction {

    public final int decoctions;

    public static final MapCodec<AlchemyRecipeRandomlyLootFunction> CODEC = RecordCodecBuilder
            .mapCodec(instance ->
                    AlchemyRecipeRandomlyLootFunction.commonFields(instance)
                            .and(ExtraCodecs.NON_NEGATIVE_INT.fieldOf("decoctions_only").orElse(0).forGetter(function -> function.decoctions))
                            .apply(instance, AlchemyRecipeRandomlyLootFunction::new));


    protected AlchemyRecipeRandomlyLootFunction(final List<LootItemCondition> conditions, final int decoctions) {
        super(conditions);
        this.decoctions=decoctions;
    }

    @Override
    protected @NotNull ItemStack run(@NotNull final ItemStack stack, final LootContext context) {
        final RandomSource random = context.getRandom();

        return getRandomFormula(stack, random, getConcoctionsID(this.decoctions));
    }

    public static List<ResourceLocation> getConcoctionsID(){
        return getConcoctionsID(0);
    }


    public static List<ResourceLocation> getConcoctionsID(final int decoction){
        final List<ResourceLocation> listConcoctions = new ArrayList<>();

        BuiltInRegistries.ITEM.forEach(item ->
                {
                    if(decoction == 0) {
                        if (
                            //If it's potion (No decoction)
                                (item instanceof final WitcherPotions_Base potion && !(item instanceof WitcherAlcohol_Base) && !potion.isDecoction())
                                        //If it's bomb
                                        || item instanceof WitcherBombs_Base
                                        //If it's oil
                                        || item instanceof WitcherMonsterOil_Base
                                        //If it's misc item                     //Removes Alcohest and Dwarven Spirit, recipes, so doesn't generate in the loot
                                        || (AlchemyFormulaUtil.isMiscItem(item) && item != TCOTS_Items.DWARVEN_SPIRIT && item != TCOTS_Items.ALCOHEST))

                            listConcoctions.add(BuiltInRegistries.ITEM.getKey(item));
                    } else {
                        if(item instanceof final WitcherPotions_Base potion && potion.isDecoction())
                            listConcoctions.add(BuiltInRegistries.ITEM.getKey(item));
                    }
                }
        );

        return listConcoctions;
    }

    public static ItemStack getRandomFormula(ItemStack stack, final RandomSource random, final List<ResourceLocation> idList){
        final int index = random.nextIntBetweenInclusive(0, idList.size()-1);
        if(stack.is(TCOTS_Items.ALCHEMY_FORMULA.get())){
            stack = AlchemyFormulaUtil.setFormula(idList.get(index));
        }

        return stack;
    }

    @Override
    public @NotNull LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
        return TCOTS_Items.RANDOMIZE_FORMULA.get();
    }

    public static AlchemyRecipeRandomlyLootFunction.Builder create() {
        return new AlchemyRecipeRandomlyLootFunction.Builder();
    }

    public static LootItemConditionalFunction.Builder<?> builder() {
        return AlchemyRecipeRandomlyLootFunction.simpleBuilder(conditions -> new AlchemyRecipeRandomlyLootFunction(conditions, 0));
    }

    public static class Builder extends LootItemConditionalFunction.Builder<AlchemyRecipeRandomlyLootFunction.Builder> {

        private int decoction;

        @Override
        protected AlchemyRecipeRandomlyLootFunction.@NotNull Builder getThis() {
            return this;
        }

        public AlchemyRecipeRandomlyLootFunction.Builder add(final int decoction) {
            this.decoction = decoction;
            return this;
        }

        @Override
        public @NotNull LootItemFunction build() {
            return new AlchemyRecipeRandomlyLootFunction(this.getConditions(), this.decoction);
        }
    }
}
