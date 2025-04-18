package TCOTS.mixin;

import TCOTS.blocks.TCOTS_Blocks_Fabric;
import TCOTS.blocks.entity.MonsterNestBlockEntity;
import TCOTS.entity.ogroids.RockTrollEntity;
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
        private void injectInTickDecreaseToxicity(ItemStack stack, Level world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
            if(!world.isClientSide){
                if(user instanceof Player player){
                    player.theConjunctionOfTheSpheres$decreaseToxicity(player.theConjunctionOfTheSpheres$getNormalToxicity(),false);
                    player.theConjunctionOfTheSpheres$decreaseToxicity(player.theConjunctionOfTheSpheres$getDecoctionToxicity(),true);
                }
            }
        }
    }

    //Adds particles when using in the Nest Spawner
    @Mixin(SpawnEggItem.class)
    public static abstract class SpawnEggItemMixin {
        @Shadow public abstract EntityType<?> getType(ItemStack stack);

        @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
        public void InjectInMonsterNest(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir){
            BlockEntity blockEntity;
            Level world = context.getLevel();
            BlockPos blockPos = context.getClickedPos();
            BlockState blockState = world.getBlockState(blockPos);
            ItemStack itemStack = context.getItemInHand();
            if (blockState.is(TCOTS_Blocks_Fabric.MONSTER_NEST) && (blockEntity = world.getBlockEntity(blockPos)) instanceof MonsterNestBlockEntity) {
                MonsterNestBlockEntity mobSpawnerBlockEntity = (MonsterNestBlockEntity)blockEntity;
                EntityType<?> entityType = this.getType(itemStack);
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
        private void injectTrollCommanding(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir){

            if(context.getItemInHand().is(Items.STICK)){
                Player player = context.getPlayer();
                if(player!=null) {
                    List<RockTrollEntity> listFollowerTrolls =
                            player.level().getEntitiesOfClass(RockTrollEntity.class, player.getBoundingBox().inflate(20, 10, 20),
                            troll -> troll.isFollowing() && troll.getOwner() == player);

                    if(!listFollowerTrolls.isEmpty()) {

                        listFollowerTrolls.sort(Comparator.comparing(troll -> troll.getName().getString()));

                        RockTrollEntity trollCommanded = listFollowerTrolls.get(0);
                        trollCommanded.setFollowerState(2);
                        trollCommanded.setGuardingPos(context.getClickedPos());
                        player.displayClientMessage(Component.translatable("tcots_witcher.gui.troll_waits", trollCommanded.getName()), true);
                        cir.setReturnValue(InteractionResult.SUCCESS);
                    }
                }

            }

        }
    }

}
