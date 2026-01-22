package mors.tcots.mixin;

import mors.tcots.entity.goal.FleeWithSilverSplinters;
import mors.tcots.items.concoctions.bombs.MoonDustBomb;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class MoonDustTransformingBlock {

    @Mixin(AbstractPiglin.class)
    public abstract static class BlockPiglinTransformation{
        @Unique
        AbstractPiglin tcots$THIS = (AbstractPiglin)(Object)this;
        @Inject(method = "isConverting", at = @At("HEAD"), cancellable = true)
        private void tcots$noMoonDust(final CallbackInfoReturnable<Boolean> cir){
            MoonDustBomb.checkEffectAndSplintersMixin(tcots$THIS, cir);
        }

    }

    @Mixin(Hoglin.class)
    public abstract static class BlockHoglinTransformation{
        @Unique
        Hoglin tcots$THIS = (Hoglin)(Object)this;
        @Inject(method = "isConverting", at = @At("HEAD"), cancellable = true)
        private void tcots$noMoonDust(final CallbackInfoReturnable<Boolean> cir){
            MoonDustBomb.checkEffectAndSplintersMixin(tcots$THIS, cir);
        }

    }

    @Mixin(Zombie.class)
    public abstract static class BlockZombieTransformation{
        @Unique
        Zombie tcots$THIS = (Zombie)(Object)this;
        @Inject(method = "convertsInWater", at = @At("HEAD"), cancellable = true)
        private void tcots$noMoonDust(final CallbackInfoReturnable<Boolean> cir){
            MoonDustBomb.checkEffectAndSplintersMixin(tcots$THIS, cir);
        }

    }

    @Mixin(Husk.class)
    public abstract static class BlockHuskTransformation{
        @Unique
        Husk tcots$THIS = (Husk)(Object)this;
        @Inject(method = "convertsInWater", at = @At("HEAD"), cancellable = true)
        private void tcots$noMoonDust(final CallbackInfoReturnable<Boolean> cir){
            MoonDustBomb.checkEffectAndSplintersMixin(tcots$THIS, cir);
        }

    }

    @Mixin(Skeleton.class)
    public abstract static class BlockSkeletonTransformation{

        @Unique
        Skeleton tcots$THIS = (Skeleton)(Object)this;
        @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Skeleton;isAlive()Z"))
        private boolean tcots$noMoonDust(final boolean original){
            return original && !MoonDustBomb.checkEffectAndSplinters(tcots$THIS);
        }

    }

    @Mixin(Villager.class)
    public abstract static class BlockVillagerTransformation {
        @Unique
        Villager tcots$THIS = (Villager)(Object)this;
        @Inject(method = "thunderHit", at = @At("HEAD"), cancellable = true)
        private void tcots$noMoonDust(final ServerLevel world, final LightningBolt lightning, final CallbackInfo ci){
            if(MoonDustBomb.checkEffectAndSplinters(tcots$THIS))
                ci.cancel();
        }
    }

    @Mixin(Pig.class)
    public abstract static class BlockPigTransformation {
        @Unique
        Pig tcots$THIS = (Pig)(Object)this;
        @Inject(method = "thunderHit", at = @At("HEAD"), cancellable = true)
        private void tcots$noMoonDust(final ServerLevel world, final LightningBolt lightning, final CallbackInfo ci){
            if(MoonDustBomb.checkEffectAndSplinters(tcots$THIS))
                ci.cancel();
        }
    }

    //Creeper
    @Mixin(SwellGoal.class)
    public abstract static class BlockCreeperExplosion{
        @Shadow @Final private Creeper creeper;
        @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
        private void tcots$noMoonDust(final CallbackInfoReturnable<Boolean> cir){
            if(MoonDustBomb.checkSilverSplinters(creeper))
                cir.setReturnValue(false);
        }
    }

    @Mixin(Creeper.class)
    public abstract static class BlockCreeperEntityExplosion extends Monster implements PowerableMob {
        protected BlockCreeperEntityExplosion(final EntityType<? extends Monster> entityType, final Level world) {
            super(entityType, world);
        }
        @Shadow @Final private static EntityDataAccessor<Boolean> DATA_IS_IGNITED;
        @Unique
        Creeper tcots$THIS = (Creeper)(Object)this;

        @Inject(method = "registerGoals", at = @At("HEAD"))
        private void tcots$moonDustFlee(final CallbackInfo ci){
            this.goalSelector.addGoal(3, new FleeWithSilverSplinters<>(this, Player.class, 6.0f, 1.0, 1.2));
        }

        @Inject(method = "tick", at = @At("HEAD"))
        private void tcots$noMoonDust(final CallbackInfo ci){
            if(tcots$THIS.getSwellDir() > 0 && MoonDustBomb.checkSilverSplinters(tcots$THIS))
                tcots$THIS.setSwellDir(-1);

            if(tcots$THIS.isIgnited() && MoonDustBomb.checkSilverSplinters(tcots$THIS))
                this.entityData.set(DATA_IS_IGNITED, false);
        }
    }
}
