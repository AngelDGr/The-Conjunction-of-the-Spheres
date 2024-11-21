package TCOTS.mixin;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.blocks.entity.MonsterNestBlockEntity;
import TCOTS.entity.ogroids.RockTrollEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
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
        @Inject(method = "finishUsing", at = @At("RETURN"))
        private void injectInTickDecreaseToxicity(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
            if(!world.isClient){
                if(user instanceof PlayerEntity player){
                    player.theConjunctionOfTheSpheres$decreaseToxicity(player.theConjunctionOfTheSpheres$getNormalToxicity(),false);
                    player.theConjunctionOfTheSpheres$decreaseToxicity(player.theConjunctionOfTheSpheres$getDecoctionToxicity(),true);
                }
            }
        }
    }

    //Adds particles when using in the Nest Spawner
    @Mixin(SpawnEggItem.class)
    public static abstract class SpawnEggItemMixin {


        @Shadow public abstract EntityType<?> getEntityType(ItemStack stack);

        @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
        public void InjectInMonsterNest(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir){
            BlockEntity blockEntity;
            World world = context.getWorld();
            BlockPos blockPos = context.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);
            ItemStack itemStack = context.getStack();
            if (blockState.isOf(TCOTS_Blocks.MONSTER_NEST) && (blockEntity = world.getBlockEntity(blockPos)) instanceof MonsterNestBlockEntity) {
                MonsterNestBlockEntity mobSpawnerBlockEntity = (MonsterNestBlockEntity)blockEntity;
                EntityType<?> entityType = this.getEntityType(itemStack);
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

    //Make the stick being usable to command Trolls
    @Mixin(Item.class)
    public static abstract class ItemMixin {

        @Inject(method = "useOnBlock", at = @At("TAIL"), cancellable = true)
        private void injectTrollCommanding(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir){

            if(context.getStack().isOf(Items.STICK)){
                PlayerEntity player = context.getPlayer();
                if(player!=null) {
                    List<RockTrollEntity> listFollowerTrolls =
                            player.getWorld().getEntitiesByClass(RockTrollEntity.class, player.getBoundingBox().expand(20, 10, 20),
                            troll -> troll.isFollowing() && troll.getOwner() == player);

                    if(!listFollowerTrolls.isEmpty()) {

                        listFollowerTrolls.sort(Comparator.comparing(troll -> troll.getName().getString()));

                        RockTrollEntity trollCommanded = listFollowerTrolls.get(0);
                        trollCommanded.setFollowerState(2);
                        trollCommanded.setGuardingPos(context.getBlockPos());
                        player.sendMessage(Text.translatable("tcots-witcher.gui.troll_waits", trollCommanded.getName()), true);
                        cir.setReturnValue(ActionResult.SUCCESS);
                    }
                }

            }

        }
    }

}
