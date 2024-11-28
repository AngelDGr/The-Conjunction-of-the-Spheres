package TCOTS.items.concoctions;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Recipe;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlchemyFormulaItem extends Item {
    public AlchemyFormulaItem(Settings settings) {
        super(settings);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        if(isDecoction(stack)){
            return Rarity.RARE;
        }
        return Rarity.UNCOMMON;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return super.hasGlint(stack) || isDecoction(stack);
    }

    private boolean isDecoction(ItemStack stack){
        NbtCompound nbtCompound = stack.getNbt();

        if(nbtCompound!=null && nbtCompound.contains("Decoction")){
            return nbtCompound.getBoolean("Decoction");
        } else {
            return false;
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        NbtCompound nbtCompound = player.getStackInHand(hand).getNbt();
        ItemStack itemStack = player.getStackInHand(hand);

        if(nbtCompound!= null && nbtCompound.contains("FormulaID") && player instanceof ServerPlayerEntity serverPlayer){
            Identifier recipeIdentifier = new Identifier(nbtCompound.getString("FormulaID"));

            if(serverPlayer.getRecipeBook().contains(recipeIdentifier)){
                player.sendMessage(Text.translatable("item.tcots-witcher.alchemy_formula.already_know").formatted(Formatting.RED), true);
                return TypedActionResult.pass(itemStack);
            }


            serverPlayer.incrementStat(Stats.USED.getOrCreateStat(this));
            Criteria.CONSUME_ITEM.trigger(serverPlayer, itemStack);



            serverPlayer.getWorld().playSoundFromEntity(null, serverPlayer, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, serverPlayer.getSoundCategory(), 1.0f, 1.0f);
            List<Recipe<?>> list = new ArrayList<>();

            if(world.getRecipeManager().get(recipeIdentifier).isPresent())
                list.add(world.getRecipeManager().get(recipeIdentifier).get());

            serverPlayer.unlockRecipes(list);

            return TypedActionResult.consume(itemStack);
        }

        return TypedActionResult.pass(itemStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbtCompound = stack.getNbt();
        if(world != null && nbtCompound != null && nbtCompound.contains("FormulaID")){
            if(world.getRecipeManager().get(new Identifier(nbtCompound.getString("FormulaID"))).isPresent()){
                Optional<? extends Recipe<?>> recipe = world.getRecipeManager().get(new Identifier(nbtCompound.getString("FormulaID")));
                if (recipe.isEmpty()){
                    return;
                }

                Item output = recipe.get().getOutput(null).getItem();

                if(output instanceof WitcherPotions_Base potion){
                    nbtCompound.putBoolean("Decoction", potion.isDecoction());
                }

                Text text = Text.translatable("item.tcots-witcher.alchemy_formula.tooltip", output.getName()).formatted(Formatting.BLUE);

//                0x41d331
                if(isDecoction(stack)){
                    text = Text.translatable("item.tcots-witcher.alchemy_formula.tooltip", output.getName()).setStyle(Style.EMPTY.withColor(0x41d331));


                }

                tooltip.add(text);
            }
        }
    }
}
