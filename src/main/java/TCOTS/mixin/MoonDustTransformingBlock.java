package TCOTS.mixin;

import TCOTS.entity.goals.FleeWithSilverSplinters;
import TCOTS.items.concoctions.bombs.MoonDustBomb;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.client.render.entity.feature.SkinOverlayOwner;
import net.minecraft.entity.ai.goal.CreeperIgniteGoal;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
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

public class MoonDustTransformingBlock {

    @Mixin(AbstractPiglinEntity.class)
    public abstract static class BlockPiglinTransformation{
        @Unique
        AbstractPiglinEntity THIS = (AbstractPiglinEntity)(Object)this;
        @Inject(method = "shouldZombify", at = @At("HEAD"), cancellable = true)
        private void injectNoMoonDust(CallbackInfoReturnable<Boolean> cir){
            MoonDustBomb.checkEffectAndSplintersMixin(THIS, cir);
        }

    }

    @Mixin(HoglinEntity.class)
    public abstract static class BlockHoglinTransformation{
        @Unique
        HoglinEntity THIS = (HoglinEntity)(Object)this;
        @Inject(method = "canConvert", at = @At("HEAD"), cancellable = true)
        private void injectNoMoonDust(CallbackInfoReturnable<Boolean> cir){
            MoonDustBomb.checkEffectAndSplintersMixin(THIS, cir);
        }

    }

    @Mixin(ZombieEntity.class)
    public abstract static class BlockZombieTransformation{
        @Unique
        ZombieEntity THIS = (ZombieEntity)(Object)this;
        @Inject(method = "canConvertInWater", at = @At("HEAD"), cancellable = true)
        private void injectNoMoonDust(CallbackInfoReturnable<Boolean> cir){
            MoonDustBomb.checkEffectAndSplintersMixin(THIS, cir);
        }

    }

    @Mixin(HuskEntity.class)
    public abstract static class BlockHuskTransformation{
        @Unique
        HuskEntity THIS = (HuskEntity)(Object)this;
        @Inject(method = "canConvertInWater", at = @At("HEAD"), cancellable = true)
        private void injectNoMoonDust(CallbackInfoReturnable<Boolean> cir){
            MoonDustBomb.checkEffectAndSplintersMixin(THIS, cir);
        }

    }

    @Mixin(SkeletonEntity.class)
    public abstract static class BlockSkeletonTransformation{
        @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/SkeletonEntity;isAlive()Z"))
        private boolean injectNoMoonDust(SkeletonEntity instance){
            if(MoonDustBomb.checkEffectAndSplinters(instance))
                return false;

            return !instance.isRemoved() && instance.getHealth() > 0.0f;
        }

    }

    @Mixin(VillagerEntity.class)
    public abstract static class BlockVillagerTransformation {
        @Unique
        VillagerEntity THIS = (VillagerEntity)(Object)this;
        @Inject(method = "onStruckByLightning", at = @At("HEAD"), cancellable = true)
        private void injectNoMoonDust(ServerWorld world, LightningEntity lightning, CallbackInfo ci){
            if(MoonDustBomb.checkEffectAndSplinters(THIS))
                ci.cancel();
        }
    }

    @Mixin(PigEntity.class)
    public abstract static class BlockPigTransformation {
        @Unique
        PigEntity THIS = (PigEntity)(Object)this;
        @Inject(method = "onStruckByLightning", at = @At("HEAD"), cancellable = true)
        private void injectNoMoonDust(ServerWorld world, LightningEntity lightning, CallbackInfo ci){
            if(MoonDustBomb.checkEffectAndSplinters(THIS))
                ci.cancel();
        }
    }

    //Creeper
    @Mixin(CreeperIgniteGoal.class)
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
