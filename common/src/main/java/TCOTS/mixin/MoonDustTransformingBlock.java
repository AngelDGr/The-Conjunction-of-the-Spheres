package TCOTS.mixin;

import TCOTS.entity.goals.FleeWithSilverSplinters;
import TCOTS.items.concoctions.bombs.MoonDustBomb;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class MoonDustTransformingBlock {

    @Mixin(AbstractPiglin.class)
    public abstract static class BlockPiglinTransformation{
        @Unique
        AbstractPiglin THIS = (AbstractPiglin)(Object)this;
        @Inject(method = "isConverting", at = @At("HEAD"), cancellable = true)
        private void injectNoMoonDust(CallbackInfoReturnable<Boolean> cir){
            MoonDustBomb.checkEffectAndSplintersMixin(THIS, cir);
        }

    }

    @Mixin(Hoglin.class)
    public abstract static class BlockHoglinTransformation{
        @Unique
        Hoglin THIS = (Hoglin)(Object)this;
        @Inject(method = "isConverting", at = @At("HEAD"), cancellable = true)
        private void injectNoMoonDust(CallbackInfoReturnable<Boolean> cir){
            MoonDustBomb.checkEffectAndSplintersMixin(THIS, cir);
        }

    }

    @Mixin(Zombie.class)
    public abstract static class BlockZombieTransformation{
        @Unique
        Zombie THIS = (Zombie)(Object)this;
        @Inject(method = "convertsInWater", at = @At("HEAD"), cancellable = true)
        private void injectNoMoonDust(CallbackInfoReturnable<Boolean> cir){
            MoonDustBomb.checkEffectAndSplintersMixin(THIS, cir);
        }

    }

    @Mixin(Husk.class)
    public abstract static class BlockHuskTransformation{
        @Unique
        Husk THIS = (Husk)(Object)this;
        @Inject(method = "convertsInWater", at = @At("HEAD"), cancellable = true)
        private void injectNoMoonDust(CallbackInfoReturnable<Boolean> cir){
            MoonDustBomb.checkEffectAndSplintersMixin(THIS, cir);
        }

    }

    @Mixin(Skeleton.class)
    public abstract static class BlockSkeletonTransformation{
        @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Skeleton;isAlive()Z"))
        private boolean injectNoMoonDust(Skeleton instance){
            if(MoonDustBomb.checkEffectAndSplinters(instance))
                return false;

            return !instance.isRemoved() && instance.getHealth() > 0.0f;
        }

    }

    @Mixin(Villager.class)
    public abstract static class BlockVillagerTransformation {
        @Unique
        Villager THIS = (Villager)(Object)this;
        @Inject(method = "thunderHit", at = @At("HEAD"), cancellable = true)
        private void injectNoMoonDust(ServerLevel world, LightningBolt lightning, CallbackInfo ci){
            if(MoonDustBomb.checkEffectAndSplinters(THIS))
                ci.cancel();
        }
    }

    @Mixin(Pig.class)
    public abstract static class BlockPigTransformation {
        @Unique
        Pig THIS = (Pig)(Object)this;
        @Inject(method = "thunderHit", at = @At("HEAD"), cancellable = true)
        private void injectNoMoonDust(ServerLevel world, LightningBolt lightning, CallbackInfo ci){
            if(MoonDustBomb.checkEffectAndSplinters(THIS))
                ci.cancel();
        }
    }

    //Creeper
    @Mixin(SwellGoal.class)
    public abstract static class BlockCreeperExplosion{
        @Shadow @Final private Creeper creeper;
        @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
        private void injectNoMoonDust(CallbackInfoReturnable<Boolean> cir){
            if(MoonDustBomb.checkSilverSplinters(creeper))
                cir.setReturnValue(false);
        }
    }

    @Mixin(Creeper.class)
    public abstract static class BlockCreeperEntityExplosion extends Monster implements PowerableMob {
        protected BlockCreeperEntityExplosion(EntityType<? extends Monster> entityType, Level world) {
            super(entityType, world);
        }
        @Shadow @Final private static EntityDataAccessor<Boolean> DATA_IS_IGNITED;
        @Unique
        Creeper THIS = (Creeper)(Object)this;

        @Inject(method = "registerGoals", at = @At("HEAD"))
        private void injectMoonDustRun(CallbackInfo ci){
            this.goalSelector.addGoal(3, new FleeWithSilverSplinters<>(this, Player.class, 6.0f, 1.0, 1.2));
        }

        @Inject(method = "tick", at = @At("HEAD"))
        private void injectNoMoonDust(CallbackInfo ci){
            if(THIS.getSwellDir() > 0 && MoonDustBomb.checkSilverSplinters(THIS))
                THIS.setSwellDir(-1);

            if(THIS.isIgnited() && MoonDustBomb.checkSilverSplinters(THIS))
                this.entityData.set(DATA_IS_IGNITED, false);
        }
    }
}
