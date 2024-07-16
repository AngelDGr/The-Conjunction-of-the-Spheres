package TCOTS.mixin;

import TCOTS.items.concoctions.bombs.DimeritiumBomb;
import net.minecraft.entity.mob.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
public abstract class DimeritiumMagicBlock {
    //Enderman
    @Mixin(EndermanEntity.class)
    public abstract static class BlockEndermanTeleportation{
        @Unique
        EndermanEntity THIS = (EndermanEntity)(Object)this;
        @Inject(method = "teleportRandomly", at = @At("HEAD"), cancellable = true)
        private void magicBlockingTeleport(CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(THIS, cir);
        }
    }

    @Mixin(targets = "net.minecraft.entity.mob.EndermanEntity$TeleportTowardsPlayerGoal")
    public abstract static class BlockEndermanTeleportationTowardsPlayer{
        @Shadow @Final private EndermanEntity enderman;

        @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
        private void magicBlockingStart(CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(enderman, cir);
        }
    }

    //Shulker
    @Mixin(targets = "net.minecraft.entity.mob.ShulkerEntity$ShootBulletGoal")
    public abstract static class BlockShulkerBullet{
        @Final
        @Shadow ShulkerEntity field_7348;
        @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
        private void magicBlockingStart(CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(field_7348, cir);
        }
    }

    //Blaze
    @Mixin(targets = "net.minecraft.entity.mob.BlazeEntity$ShootFireballGoal")
    public abstract static class BlockBlazeFireball{
        @Shadow @Final private BlazeEntity blaze;

        @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
        private void magicBlockingStart(CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(blaze, cir);
        }
    }

    //Ghast
    @Mixin(targets = "net.minecraft.entity.mob.GhastEntity$ShootFireballGoal")
    public abstract static class BlockGhastFireball{
        @Shadow @Final private GhastEntity ghast;

        @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
        private void magicBlockingStart(CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(ghast, cir);
        }
    }

    //Guardian
    @Mixin(targets = "net.minecraft.entity.mob.GuardianEntity$FireBeamGoal")
    public abstract static class BlockGuardianBeamGoal{
        @Shadow @Final private GuardianEntity guardian;
        @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
        private void magicBlockingStart(CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(guardian, cir);
        }

        @Inject(method = "shouldContinue", at = @At("HEAD"), cancellable = true)
        private void magicBlockingContinue(CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(guardian, cir);
        }
    }

    //Evoker
    @Mixin(targets = "net.minecraft.entity.mob.SpellcastingIllagerEntity$CastSpellGoal")
    public abstract static class BlockCastSpellGoal {
        @Final
        @Shadow SpellcastingIllagerEntity field_7386;
        @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
        private void magicBlockingStart(CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(field_7386, cir);
        }

        @Inject(method = "shouldContinue", at = @At("HEAD"), cancellable = true)
        private void magicBlockingContinue(CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(field_7386, cir);
        }
    }

    @Mixin(targets = "net.minecraft.entity.mob.EvokerEntity$WololoGoal")
    public abstract static class BlockWololoGoal {
        @Final
        @Shadow EvokerEntity field_7268;
        @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
        private void magicBlockingStart(CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(field_7268, cir);
        }

        @Inject(method = "shouldContinue", at = @At("HEAD"), cancellable = true)
        private void magicBlockingContinue(CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(field_7268, cir);
        }
    }
}


