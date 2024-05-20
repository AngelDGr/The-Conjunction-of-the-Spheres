package TCOTS.mixin;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.blocks.entity.MonsterNestBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnEggItem.class)
public class SpawnEggItemMixin {

    @Shadow
    public EntityType<?> getEntityType(@Nullable NbtCompound nbt) {
        return null;
    }

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    public void InjectInMonsterNest(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir){
        BlockEntity blockEntity;
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        ItemStack itemStack = context.getStack();
        if (blockState.isOf(TCOTS_Blocks.MONSTER_NEST) && (blockEntity = world.getBlockEntity(blockPos)) instanceof MonsterNestBlockEntity) {
            MonsterNestBlockEntity mobSpawnerBlockEntity = (MonsterNestBlockEntity)blockEntity;
            EntityType<?> entityType = this.getEntityType(itemStack.getNbt());
            mobSpawnerBlockEntity.setEntityType(entityType, world.getRandom());
            blockEntity.markDirty();
            world.updateListeners(blockPos, blockState, blockState, Block.NOTIFY_ALL);
            world.emitGameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, blockPos);
            itemStack.decrement(1);
            world.syncWorldEvent(5829147, blockPos, 0);
            cir.setReturnValue(ActionResult.CONSUME);
        }

    }

}
