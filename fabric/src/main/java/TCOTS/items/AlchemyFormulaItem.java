package TCOTS.items;

import TCOTS.items.components.RecipeTeacherComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

public class AlchemyFormulaItem extends Item {
    public AlchemyFormulaItem(Properties settings) {
        super(settings);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return super.isFoil(stack) || isDecoctionRecipe(stack);
    }

    public static boolean isDecoctionRecipe(ItemStack stack){
        if(!stack.has(TCOTS_Items_Fabric.RECIPE_TEACHER_COMPONENT)){
            return false;
        }

        RecipeTeacherComponent recipeTeacher = stack.get(TCOTS_Items_Fabric.RECIPE_TEACHER_COMPONENT);

        assert recipeTeacher != null;
        return recipeTeacher.isDecoction();
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(!itemStack.has(TCOTS_Items_Fabric.RECIPE_TEACHER_COMPONENT)){
            return InteractionResultHolder.pass(itemStack);
        }

        RecipeTeacherComponent recipeTeacher = itemStack.get(TCOTS_Items_Fabric.RECIPE_TEACHER_COMPONENT);

        if(recipeTeacher!=null && player instanceof ServerPlayer serverPlayer){
            ResourceLocation recipeIdentifier = ResourceLocation.parse(recipeTeacher.recipeName());

            if(serverPlayer.getRecipeBook().contains(recipeIdentifier)){
                player.displayClientMessage(Component.translatable("item.tcots_witcher.alchemy_formula.already_know").withStyle(ChatFormatting.RED), true);
                return InteractionResultHolder.pass(itemStack);
            }

            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, itemStack);

            serverPlayer.level().playSound(null, serverPlayer, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, serverPlayer.getSoundSource(), 1.0f, 1.0f);
            serverPlayer.awardRecipesByKey(List.of(recipeIdentifier));

            return InteractionResultHolder.consume(itemStack);
        }

        return InteractionResultHolder.pass(itemStack);
    }

    public static void appendTooltip(ItemStack stack, @Nullable Level world, Consumer<Component> tooltip) {
        if(!stack.has(TCOTS_Items_Fabric.RECIPE_TEACHER_COMPONENT)){
            return;
        }

        RecipeTeacherComponent recipeTeacher = stack.get(TCOTS_Items_Fabric.RECIPE_TEACHER_COMPONENT);
        if(world != null && recipeTeacher != null){
            if(world.getRecipeManager().byKey(ResourceLocation.parse(recipeTeacher.recipeName())).isPresent()){
                Optional<RecipeHolder<?>> recipe = world.getRecipeManager().byKey(ResourceLocation.parse(recipeTeacher.recipeName()));
                if (recipe.isEmpty()){
                    return;
                }

                Item output = recipe.get().value().getResultItem(null).getItem();

                Component text = Component.translatable("item.tcots_witcher.alchemy_formula.tooltip", output.getDescription()).withStyle(ChatFormatting.BLUE);

                if(isDecoctionRecipe(stack)){
                    text = Component.translatable("item.tcots_witcher.alchemy_formula.tooltip", output.getDescription()).withColor(0x41d331);
                }

                tooltip.accept(text);
            }
        }
    }
}
