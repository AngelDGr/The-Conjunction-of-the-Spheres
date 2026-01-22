package mors.tcots.mixin;

import mors.tcots.entity.monsters.necrophages.BullvoreEntity;
import mors.tcots.entity.monsters.ogroids.ForestTrollEntity;
import mors.tcots.items.concoctions.bombs.NorthernWindBomb;
import mors.tcots.items.concoctions.bombs.SamumBomb;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class MobEntityMixin {
    @Unique
    Mob tcots$THIS = (Mob) (Object) this;
    @Shadow
    private @Nullable LivingEntity target;

    //Samum
    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void tcots$noSamumEffectSet(final LivingEntity target, final CallbackInfo ci) {
        if (SamumBomb.checkSamumEffect(tcots$THIS)) {
            this.target = null;
            ci.cancel();
        }
    }

    //NorthernWind
    @Unique
    private boolean tcots$northernWindApplied = false;

    @Unique
    double x = -1;
    @Unique
    double z = -1;

    @Inject(method = "tick", at = @At("TAIL"))
    private void tcots$northernWindFreeze(final CallbackInfo ci) {

        if (NorthernWindBomb.checkEffect(tcots$THIS)) {

            if (x == -1) {
                x = tcots$THIS.getX();
                z = tcots$THIS.getZ();
            }

            tcots$THIS.randomTeleport(x, tcots$THIS.getY(), z, false);
            tcots$THIS.setDeltaMovement(0, 0, 0);

            if (!(tcots$THIS instanceof EnderDragon)) {
                tcots$THIS.push(0, -0.5, 0);
            }
            tcots$northernWindApplied = true;
        } else if (tcots$northernWindApplied) {
            x = -1;
            z = -1;
            tcots$northernWindApplied = false;
        }
    }

    @Inject(method = "serverAiStep", at = @At("HEAD"), cancellable = true)
    private void tcots$northernWindMove(final CallbackInfo ci) {
        if (NorthernWindBomb.checkEffect(tcots$THIS)) {
            ci.cancel();
        }
    }

    @ModifyVariable(method = "isSunBurnTick", at = @At("STORE"), ordinal = 0)
    private boolean tcots$noFireWhenFreeze(final boolean value){
        return value || NorthernWindBomb.checkEffect(tcots$THIS);
    }

    @Mixin(CropBlock.class)
    public abstract static class BullvoreDestroyCrops{

        @Inject(method = "entityInside", at = @At("HEAD"))
        private void tcots$breakCrops(final BlockState state, final Level world, final BlockPos pos, final Entity entity, final CallbackInfo ci){
            if ((entity instanceof BullvoreEntity && ((BullvoreEntity)entity).isCharging()) && world.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                world.destroyBlock(pos, true, entity);
            }
        }

    }

    @Mixin(CampfireBlock.class)
    public abstract static class ForestTrollNotGetCampfireDamage{

        @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
        private void tcots$noCampfireDamage(final BlockState state, final Level world, final BlockPos pos, final Entity entity, final CallbackInfo ci){
            if (entity instanceof ForestTrollEntity) {
                ci.cancel();
            }
        }

    }
}
