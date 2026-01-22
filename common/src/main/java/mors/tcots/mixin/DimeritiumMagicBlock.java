package mors.tcots.mixin;

import mors.tcots.entity.goal.FleeWithDimeritium;
import mors.tcots.items.concoctions.bombs.DimeritiumBomb;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public abstract class DimeritiumMagicBlock {
    //Enderman
    @Mixin(EnderMan.class)
    public abstract static class BlockEndermanTeleportation extends Monster implements NeutralMob {
        protected BlockEndermanTeleportation(final EntityType<? extends Monster> entityType, final Level world) {
            super(entityType, world);
        }

        @Unique
        EnderMan tcots$THIS = (EnderMan)(Object)this;


        @Inject(method = "teleport()Z", at = @At("HEAD"), cancellable = true)
        private void tcots$magicBlockingTeleport(final CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(tcots$THIS, cir);
        }

        @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
        private void tcots$makeTakeDamageByArrows(final DamageSource source, final float amount, final CallbackInfoReturnable<Boolean> cir){
            if (this.isInvulnerableTo(source)) {
                cir.setReturnValue(false);
            }

            if(DimeritiumBomb.checkEffect(tcots$THIS) && !(source.getDirectEntity() instanceof ThrownPotion)) {
                cir.setReturnValue(super.hurt(source, amount));
            }
        }
    }

    @Mixin(AbstractArrow.class)
    public abstract static class MakeEndermanArrowDamageable {
        @Unique
        Entity entity;

        @Inject(method = "onHitEntity", at = @At("HEAD"))
        private void tcots$getEntity(final EntityHitResult entityHitResult, final CallbackInfo ci){
            entity = entityHitResult.getEntity();
        }

        @ModifyVariable(method = "onHitEntity", at = @At("STORE"), ordinal = 0)
        private boolean tcots$makeArrowDamageable(final boolean value){
            if(entity!=null && entity instanceof final LivingEntity livingEntity){
                return value && !DimeritiumBomb.checkEffect(livingEntity);
            }

            return value;
        }
    }

    @Mixin(targets = "net.minecraft.world.entity.monster.EnderMan$EndermanLookForPlayerGoal")
    public abstract static class BlockEndermanTeleportationTowardsPlayer{
        @Shadow @Final private EnderMan enderman;

        @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
        private void tcots$magicBlockingStart(final CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(enderman, cir);
        }
    }

    //Shulker
    @Mixin(targets = "net.minecraft.world.entity.monster.Shulker$ShulkerAttackGoal")
    public abstract static class BlockShulkerBullet{
        @SuppressWarnings("all")
        @Final @Shadow Shulker field_7348;

        @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
        private void tcots$magicBlockingStart(final CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(field_7348, cir);
        }
    }

    //Blaze
    @Mixin(targets = "net.minecraft.world.entity.monster.Blaze$BlazeAttackGoal")
    public abstract static class BlockBlazeFireball{
        @Shadow @Final private Blaze blaze;

        @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
        private void tcots$magicBlockingStart(final CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(blaze, cir);
        }
    }

    //Ghast
    @Mixin(targets = "net.minecraft.world.entity.monster.Ghast$GhastShootFireballGoal")
    public abstract static class BlockGhastFireball{
        @Shadow @Final private Ghast ghast;

        @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
        private void tcots$magicBlockingStart(final CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(ghast, cir);
        }
    }

    //Guardian
    @Mixin(targets = "net.minecraft.world.entity.monster.Guardian$GuardianAttackGoal")
    public abstract static class BlockGuardianBeamGoal{
        @Shadow @Final private Guardian guardian;
        @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
        private void tcots$magicBlockingStart(final CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(guardian, cir);
        }

        @Inject(method = "canContinueToUse", at = @At("HEAD"), cancellable = true)
        private void tcots$magicBlockingContinue(final CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(guardian, cir);
        }
    }

    //Evoker
    @Mixin(targets = "net.minecraft.world.entity.monster.SpellcasterIllager$SpellcasterUseSpellGoal")
    public abstract static class BlockCastSpellGoal {
        @SuppressWarnings("all")
        @Final @Shadow SpellcasterIllager field_7386;

        @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
        private void tcots$magicBlockingStart(final CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(field_7386, cir);
        }

        @Inject(method = "canContinueToUse", at = @At("HEAD"), cancellable = true)
        private void tcots$magicBlockingContinue(final CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(field_7386, cir);
        }
    }

    @Mixin(net.minecraft.world.entity.monster.Evoker.EvokerWololoSpellGoal.class)
    public abstract static class BlockWololoGoal {
        @Final
        @Shadow Evoker field_7268;
        @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
        private void tcots$magicBlockingStart(final CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(field_7268, cir);
        }

        @Inject(method = "canContinueToUse", at = @At("HEAD"), cancellable = true)
        private void tcots$magicBlockingContinue(final CallbackInfoReturnable<Boolean> cir){
            DimeritiumBomb.checkEffectMixin(field_7268, cir);
        }
    }

    @Mixin(net.minecraft.world.entity.monster.Evoker.class)
    public abstract static class EvokerRunsFromPlayer extends SpellcasterIllager{
        protected EvokerRunsFromPlayer(final EntityType<? extends SpellcasterIllager> entityType, final Level level) {
            super(entityType, level);
        }

        @Inject(method = "registerGoals", at = @At("HEAD"))
        private void tcots$magicBlockingStart(final CallbackInfo ci){
            this.goalSelector.addGoal(1, new FleeWithDimeritium<>(this, Player.class, 10.0f, 1.0, 1.2));
        }
    }
}


