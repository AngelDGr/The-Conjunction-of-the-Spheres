package TCOTS.items.concoctions;

import TCOTS.items.TCOTS_Items;
import TCOTS.items.components.RecipeTeacherComponent;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class AlchemyFormulaItem extends Item {
    public AlchemyFormulaItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return super.hasGlint(stack) || isDecoctionRecipe(stack);
    }

    public static boolean isDecoctionRecipe(ItemStack stack){
        if(!stack.contains(TCOTS_Items.RECIPE_TEACHER_COMPONENT)){
            return false;
        }

        RecipeTeacherComponent recipeTeacher = stack.get(TCOTS_Items.RECIPE_TEACHER_COMPONENT);

        assert recipeTeacher != null;
        return recipeTeacher.isDecoction();
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if(!itemStack.contains(TCOTS_Items.RECIPE_TEACHER_COMPONENT)){
            return TypedActionResult.pass(itemStack);
        }

        RecipeTeacherComponent recipeTeacher = itemStack.get(TCOTS_Items.RECIPE_TEACHER_COMPONENT);

        if(recipeTeacher!=null && player instanceof ServerPlayerEntity serverPlayer){
            Identifier recipeIdentifier = Identifier.of(recipeTeacher.recipeName());

            if(serverPlayer.getRecipeBook().contains(recipeIdentifier)){
                player.sendMessage(Text.translatable("item.tcots-witcher.alchemy_formula.already_know").formatted(Formatting.RED), true);
                return TypedActionResult.pass(itemStack);
            }

            serverPlayer.incrementStat(Stats.USED.getOrCreateStat(this));
            Criteria.CONSUME_ITEM.trigger(serverPlayer, itemStack);

            serverPlayer.getWorld().playSoundFromEntity(null, serverPlayer, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, serverPlayer.getSoundCategory(), 1.0f, 1.0f);
            serverPlayer.unlockRecipes(List.of(recipeIdentifier));

            return TypedActionResult.consume(itemStack);
        }

        return TypedActionResult.pass(itemStack);
    }

    public static void appendTooltip(ItemStack stack, @Nullable World world, Consumer<Text> tooltip) {
        if(!stack.contains(TCOTS_Items.RECIPE_TEACHER_COMPONENT)){
            return;
        }

        RecipeTeacherComponent recipeTeacher = stack.get(TCOTS_Items.RECIPE_TEACHER_COMPONENT);
        if(world != null && recipeTeacher != null){
            if(world.getRecipeManager().get(Identifier.of(recipeTeacher.recipeName())).isPresent()){
                Optional<RecipeEntry<?>> recipe = world.getRecipeManager().get(Identifier.of(recipeTeacher.recipeName()));
                if (recipe.isEmpty()){
                    return;
                }

                Item output = recipe.get().value().getResult(null).getItem();

                Text text = Text.translatable("item.tcots-witcher.alchemy_formula.tooltip", output.getName()).formatted(Formatting.BLUE);

                if(isDecoctionRecipe(stack)){
                    text = Text.translatable("item.tcots-witcher.alchemy_formula.tooltip", output.getName()).withColor(0x41d331);
                }

                tooltip.accept(text);
            }
        }
    }
}
