package mors.tcots.mixin;

import mors.tcots.registry.TCOTS_Blocks;
import mors.tcots.block.entity.MonsterNestBlockEntity;
import mors.tcots.entity.monsters.ogroids.RockTrollEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Comparator;
import java.util.List;

public class ItemsMixins {

    //Remove toxicity
    @Mixin(MilkBucketItem.class)
    public static class MilkBucketItemMixin {
        @Inject(method = "finishUsingItem", at = @At("RETURN"))
        private void tcots$injectInTickDecreaseToxicity(final ItemStack stack, final Level world, final LivingEntity user, final CallbackInfoReturnable<ItemStack> cir) {
            if(!world.isClientSide){
                if(user instanceof final Player player){
                    player.tcots$decreaseToxicity(player.tcots$getNormalToxicity(),false);
                    player.tcots$decreaseToxicity(player.tcots$getDecoctionToxicity(),true);
                }
            }
        }
    }

    //Adds particles when using in the Nest Spawner
    @Mixin(SpawnEggItem.class)
    public static abstract class SpawnEggItemMixin {
        @Shadow public abstract EntityType<?> getType(ItemStack stack);

        @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
        public void tcots$injectInMonsterNest(final UseOnContext context, final CallbackInfoReturnable<InteractionResult> cir){
            final BlockEntity blockEntity;
            final Level world = context.getLevel();
            final BlockPos blockPos = context.getClickedPos();
            final BlockState blockState = world.getBlockState(blockPos);
            final ItemStack itemStack = context.getItemInHand();
            if (blockState.is(TCOTS_Blocks.MonsterNest()) && (blockEntity = world.getBlockEntity(blockPos)) instanceof MonsterNestBlockEntity) {
                final MonsterNestBlockEntity mobSpawnerBlockEntity = (MonsterNestBlockEntity)blockEntity;
                final EntityType<?> entityType = this.getType(itemStack);
                mobSpawnerBlockEntity.setEntityId(entityType, world.getRandom());
                blockEntity.setChanged();
                world.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_ALL);
                world.gameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, blockPos);
                itemStack.shrink(1);
                world.levelEvent(5829147, blockPos, 0);
                cir.setReturnValue(InteractionResult.CONSUME);
            }

        }

    }

    //Make the stick being usable to command Trolls
    @Mixin(Item.class)
    public static abstract class ItemMixin {

        @Inject(method = "useOn", at = @At("TAIL"), cancellable = true)
        private void tcots$injectTrollCommanding(final UseOnContext context, final CallbackInfoReturnable<InteractionResult> cir){

            if(context.getItemInHand().is(Items.STICK)){
                final Player player = context.getPlayer();
                if(player!=null) {
                    final List<RockTrollEntity> listFollowerTrolls =
                            player.level().getEntitiesOfClass(RockTrollEntity.class, player.getBoundingBox().inflate(20, 10, 20),
                            troll -> troll.isFollowing() && troll.getOwner() == player);

                    if(!listFollowerTrolls.isEmpty()) {

                        listFollowerTrolls.sort(Comparator.comparing(troll -> troll.getName().getString()));

                        final RockTrollEntity trollCommanded = listFollowerTrolls.get(0);
                        trollCommanded.setFollowerState(2);
                        trollCommanded.setGuardingPos(context.getClickedPos());
                        player.displayClientMessage(Component.translatable("gui.tcots_witcher.message.troll_waits", trollCommanded.getName()), true);
                        cir.setReturnValue(InteractionResult.SUCCESS);
                    }
                }

            }

        }
    }

}
