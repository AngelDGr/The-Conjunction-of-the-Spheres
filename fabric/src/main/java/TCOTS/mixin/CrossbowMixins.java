package TCOTS.mixin;

import TCOTS.entity.misc.ScurverSpineEntity;
import TCOTS.entity.misc.bolts.WitcherBolt;
import TCOTS.items.TCOTS_Items_Fabric;
import TCOTS.items.weapons.BoltItem;
import TCOTS.items.weapons.WitcherBaseCrossbow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CrossbowMixins {
    @Mixin(PlayerRenderer.class)
    public static class PlayerEntityRendererMixin {
        @Inject(method = "getArmPose", at = @At("TAIL"), cancellable = true)
        private static void injectKnightCrossbowPose(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir){
            ItemStack itemStack = player.getItemInHand(hand);
            if (!player.swinging && (itemStack.getItem() instanceof WitcherBaseCrossbow) && CrossbowItem.isCharged(itemStack)) {
                cir.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_HOLD);
            }
        }
    }

    @Mixin(ItemInHandRenderer.class)
    public static class HeldItemRendererMixin {
        @Inject(method = "selectionUsingItemWhileHoldingBowLike", at = @At("TAIL"), cancellable = true)
        private static void injectKnightCrossbowHandRenderer(LocalPlayer player, CallbackInfoReturnable<ItemInHandRenderer.HandRenderSelection> cir){
            ItemStack itemStack = player.getUseItem();
            InteractionHand hand = player.getUsedItemHand();
            if (itemStack.getItem() instanceof WitcherBaseCrossbow) {
                cir.setReturnValue(ItemInHandRenderer.HandRenderSelection.onlyForHand(hand));
            }
        }

        @Inject(method = "isChargedCrossbow", at = @At("TAIL"), cancellable = true)
        private static void injectKnightCrossbowCharged(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
            cir.setReturnValue((stack.getItem() instanceof WitcherBaseCrossbow) && CrossbowItem.isCharged(stack));
        }

        @Redirect(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 1))
        private boolean injectCrossbowFirstPerson(ItemStack stack, Item item) {
            return (stack.getItem() instanceof WitcherBaseCrossbow) || stack.is(item);
        }
    }

    @Mixin(CrossbowItem.class)
    public static class CrossbowItemMixin {
        @Inject(method = "getChargeDuration", at = @At("HEAD"), cancellable = true)
        private static void getPullTimeCorrectlyForAnimation(ItemStack stack, LivingEntity user, CallbackInfoReturnable<Integer> cir){
            if(stack.getItem() instanceof WitcherBaseCrossbow crossbow){
                cir.setReturnValue(crossbow.getCrossbowPullTime(stack, user));
            }
        }

        @Unique
        private static final Predicate<ItemStack> CROSSBOW_BOLTS = stack -> stack.is(TCOTS_Items_Fabric.BASE_BOLT)      ||
                                                                            stack.is(TCOTS_Items_Fabric.BLUNT_BOLT)     ||
                                                                            stack.is(TCOTS_Items_Fabric.PRECISION_BOLT) ||
                                                                            stack.is(TCOTS_Items_Fabric.EXPLODING_BOLT) ||
                                                                            stack.is(TCOTS_Items_Fabric.BROADHEAD_BOLT);

        @Inject(method = "getAllSupportedProjectiles()Ljava/util/function/Predicate;", at = @At("RETURN"), cancellable = true)
        private void insertCrossbowProjectiles(CallbackInfoReturnable<Predicate<ItemStack>> cir){
            cir.setReturnValue(cir.getReturnValue().or(CROSSBOW_BOLTS));
        }

        @Inject(method = "getSupportedHeldProjectiles()Ljava/util/function/Predicate;", at = @At("RETURN"), cancellable = true)
        private void insertCrossbowHeldProjectiles(CallbackInfoReturnable<Predicate<ItemStack>> cir){
            cir.setReturnValue(cir.getReturnValue().or(CROSSBOW_BOLTS));
        }

        @Inject(method = "createProjectile", at = @At("RETURN"), cancellable = true)
        private void injectExtraPiercing(Level world, LivingEntity shooter, ItemStack weaponStack, ItemStack arrow, boolean critical, CallbackInfoReturnable<Projectile> cir){
            boolean precisionBolt = arrow.getItem() instanceof BoltItem bolt && Objects.equals(bolt.getId(), "precision_bolt");
            if(precisionBolt) {
                if(cir.getReturnValue() instanceof AbstractArrow persistentProjectileEntity){
                    persistentProjectileEntity.setPierceLevel((byte) (persistentProjectileEntity.getPierceLevel() + 2));
                    cir.setReturnValue(persistentProjectileEntity);
                }


            }
        }
    }

    @Mixin(AbstractArrow.class)
    public static class PersistentProjectileEntityMixin {

        @Unique
        AbstractArrow THIS = (AbstractArrow)(Object)this;

        @Redirect(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setArrowCount(I)V"))
        private void redirectNoStuckArrows(LivingEntity entity, int stuckArrowCount){
            if(THIS instanceof WitcherBolt || THIS instanceof ScurverSpineEntity)
                entity.setArrowCount(entity.getArrowCount());
            else
                entity.setArrowCount(entity.getArrowCount()+1);
        }

    }
}
