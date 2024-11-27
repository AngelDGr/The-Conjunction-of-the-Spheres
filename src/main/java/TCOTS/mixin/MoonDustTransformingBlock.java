package TCOTS.mixin;

import TCOTS.entity.goals.FleeWithSilverSplinters;
import TCOTS.items.concoctions.bombs.MoonDustBomb;
import net.minecraft.client.render.entity.feature.SkinOverlayOwner;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
public class MoonDustTransformingBlock {

    @Mixin(targets = "net.minecraft.entity.mob.AbstractPiglinEntity")
    public abstract static class BlockPiglinTransformation{
        @Unique
        AbstractPiglinEntity THIS = (AbstractPiglinEntity)(Object)this;
        @Inject(method = "shouldZombify", at = @At("HEAD"), cancellable = true)
        private void injectNoMoonDust(CallbackInfoReturnable<Boolean> cir){
            MoonDustBomb.checkEffectMixin(THIS, cir);
        }

    }

    @Mixin(HoglinEntity.class)
    public abstract static class BlockHoglinTransformation{
        @Unique
        HoglinEntity THIS = (HoglinEntity)(Object)this;
        @Inject(method = "canConvert", at = @At("HEAD"), cancellable = true)
        private void injectNoMoonDust(CallbackInfoReturnable<Boolean> cir){
            MoonDustBomb.checkEffectMixin(THIS, cir);
        }

    }

    @Mixin(targets = "net.minecraft.entity.mob.ZombieEntity")
    public abstract static class BlockZombieTransformation{
        @Unique
        ZombieEntity THIS = (ZombieEntity)(Object)this;
        @Inject(method = "canConvertInWater", at = @At("HEAD"), cancellable = true)
        private void injectNoMoonDust(CallbackInfoReturnable<Boolean> cir){
            MoonDustBomb.checkEffectMixin(THIS, cir);
        }

    }

    @Mixin(targets = "net.minecraft.entity.mob.HuskEntity")
    public abstract static class BlockHuskTransformation{
        @Unique
        HuskEntity THIS = (HuskEntity)(Object)this;
        @Inject(method = "canConvertInWater", at = @At("HEAD"), cancellable = true)
        private void injectNoMoonDust(CallbackInfoReturnable<Boolean> cir){
            MoonDustBomb.checkEffectMixin(THIS, cir);
        }

    }

    @Mixin(targets = "net.minecraft.entity.mob.SkeletonEntity")
    public abstract static class BlockSkeletonTransformation{
        @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/SkeletonEntity;isAlive()Z"))
        private boolean injectNoMoonDust(SkeletonEntity instance){
            if(MoonDustBomb.checkEffect(instance))
                return false;

            return !instance.isRemoved() && instance.getHealth() > 0.0f;
        }

    }

    //Creeper
    @Mixin(targets = "net.minecraft.entity.ai.goal.CreeperIgniteGoal")
    public abstract static class BlockCreeperExplosion{
        @Shadow @Final private CreeperEntity creeper;
        @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
        private void injectNoMoonDust(CallbackInfoReturnable<Boolean> cir){
            if(MoonDustBomb.checkSilverSplinters(creeper))
                cir.setReturnValue(false);
        }
    }

    @Mixin(CreeperEntity.class)
    public abstract static class BlockCreeperEntityExplosion extends HostileEntity implements SkinOverlayOwner {
        protected BlockCreeperEntityExplosion(EntityType<? extends HostileEntity> entityType, World world) {
            super(entityType, world);
        }
        @Shadow @Final private static TrackedData<Boolean> IGNITED;
        @Unique
        CreeperEntity THIS = (CreeperEntity)(Object)this;

        @Inject(method = "initGoals", at = @At("HEAD"))
        private void injectMoonDustRun(CallbackInfo ci){
            this.goalSelector.add(3, new FleeWithSilverSplinters<>(this, PlayerEntity.class, 6.0f, 1.0, 1.2));
        }

        @Inject(method = "tick", at = @At("HEAD"))
        private void injectNoMoonDust(CallbackInfo ci){
            if(THIS.getFuseSpeed() > 0 && MoonDustBomb.checkSilverSplinters(THIS))
                THIS.setFuseSpeed(-1);

            if(THIS.isIgnited() && MoonDustBomb.checkSilverSplinters(THIS))
                this.dataTracker.set(IGNITED, false);
        }
    }
}
