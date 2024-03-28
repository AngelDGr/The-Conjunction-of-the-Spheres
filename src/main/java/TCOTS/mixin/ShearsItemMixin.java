package TCOTS.mixin;

import TCOTS.items.TCOTS_Items;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShearsItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsItem.class)
public class ShearsItemMixin {

    @Inject(method = "useOnBlock", at = @At("TAIL"), cancellable = true)
    private void injectShearsAlchemyIngredients(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        ItemStack itemStack = context.getStack();
        PlayerEntity playerEntity = context.getPlayer();

        dropPetals(context, world, blockPos, block, playerEntity, itemStack, cir,
                Blocks.LILY_OF_THE_VALLEY, TCOTS_Items.LILY_OF_THE_VALLEY_PETALS, 2,4);

        dropPetals(context, world, blockPos, block, playerEntity, itemStack, cir,
                Blocks.ALLIUM, TCOTS_Items.ALLIUM_PETALS, 3,8);
    }

    @Unique
    private void dropPetals(ItemUsageContext context, World world, BlockPos blockPos, Block block, PlayerEntity playerEntity, ItemStack itemStack, CallbackInfoReturnable<ActionResult> cir, Block flower, Item petal, int min, int max){
        if(block==flower){
            int j = min + world.random.nextInt(max-min+1);
            SweetBerryBushBlock.dropStack(world, blockPos, new ItemStack(petal, j));
            world.removeBlock(blockPos, false);

            world.playSound(playerEntity, blockPos, SoundEvents.BLOCK_GROWING_PLANT_CROP, SoundCategory.BLOCKS, 1.0f, 1.0f);

            if (playerEntity != null) {itemStack.damage(1, playerEntity, player -> player.sendToolBreakStatus(context.getHand()));}

            cir.setReturnValue(ActionResult.success(world.isClient));
        }
    }
}
