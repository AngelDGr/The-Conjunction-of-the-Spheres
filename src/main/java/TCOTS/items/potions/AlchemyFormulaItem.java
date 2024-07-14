package TCOTS.items.potions;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class AlchemyFormulaItem extends Item {
    public AlchemyFormulaItem(Settings settings) {
        super(settings);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        if(nbtCompound!=null && nbtCompound.contains("Decoction")){
            if(nbtCompound.getBoolean("Decoction"))
                return Rarity.RARE;
        }

        return Rarity.UNCOMMON;
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


            serverPlayer.getWorld().playSoundFromEntity(null, serverPlayer, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, serverPlayer.getSoundCategory(), 1.0f, 1.0f);
            serverPlayer.unlockRecipes(List.of(recipeIdentifier));

            return TypedActionResult.consume(itemStack);
        }

        return TypedActionResult.pass(itemStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbtCompound = stack.getNbt();
        if(world != null && nbtCompound != null && nbtCompound.contains("FormulaID")){
            if(world.getRecipeManager().get(new Identifier(nbtCompound.getString("FormulaID"))).isPresent()){
                Optional<RecipeEntry<?>> recipe = world.getRecipeManager().get(new Identifier(nbtCompound.getString("FormulaID")));
                if (recipe.isEmpty()){
                    return;
                }


                Item output = recipe.get().value().getResult(null).getItem();

                if(output instanceof WitcherPotions_Base potion){
                    nbtCompound.putBoolean("Decoction", potion.isDecoction());
                }

                tooltip.add(Text.translatable("item.tcots-witcher.alchemy_formula.tooltip", output.getName()));
            }
            else {
                tooltip.add(Text.translatable("item.tcots-witcher.alchemy_formula.tooltip_n"));
            }

        }
        else {
            tooltip.add(Text.translatable("item.tcots-witcher.alchemy_formula.tooltip_n"));
        }

    }

}
