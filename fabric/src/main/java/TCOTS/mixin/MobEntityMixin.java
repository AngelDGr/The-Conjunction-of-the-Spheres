package TCOTS.mixin;

import TCOTS.entity.necrophages.BullvoreEntity;
import TCOTS.entity.ogroids.ForestTrollEntity;
import TCOTS.items.concoctions.bombs.NorthernWindBomb;
import TCOTS.items.concoctions.bombs.SamumBomb;
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
    Mob THIS = (Mob) (Object) this;
    @Shadow
    private @Nullable LivingEntity target;

    //Samum
    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void injectNoSamumEffectSet(LivingEntity target, CallbackInfo ci) {
        if (SamumBomb.checkSamumEffect(THIS)) {
            this.target = null;
            ci.cancel();
        }
    }

    //NorthernWind
    @Unique
    private boolean northernWindApplied = false;

    @Unique
    double x = -1;
    @Unique
    double z = -1;

    @Inject(method = "tick", at = @At("TAIL"))
    private void injectNorthernWindFreeze(CallbackInfo ci) {

        if (NorthernWindBomb.checkEffect(THIS)) {

            if (x == -1) {
                x = THIS.getX();
                z = THIS.getZ();
            }

            THIS.randomTeleport(x, THIS.getY(), z, false);
            THIS.setDeltaMovement(0, 0, 0);

            if (!(THIS instanceof EnderDragon)) {
                THIS.push(0, -0.5, 0);
            }
            northernWindApplied = true;
        } else if (northernWindApplied) {
            x = -1;
            z = -1;
            northernWindApplied = false;
        }
    }

    @Inject(method = "serverAiStep", at = @At("HEAD"), cancellable = true)
    private void injectNorthernWindMove(CallbackInfo ci) {
        if (NorthernWindBomb.checkEffect(THIS)) {
            ci.cancel();
        }
    }

    @ModifyVariable(method = "isSunBurnTick", at = @At("STORE"), ordinal = 0)
    private boolean injectNoFireWhenFreeze(boolean value){
        return value || NorthernWindBomb.checkEffect(THIS);
    }

    @Mixin(CropBlock.class)
    public abstract static class BullvoreDestroyCrops{

        @Inject(method = "entityInside", at = @At("HEAD"))
        private void injectNoMoonDust(BlockState state, Level world, BlockPos pos, Entity entity, CallbackInfo ci){
            if ((entity instanceof BullvoreEntity && ((BullvoreEntity)entity).isCharging()) && world.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                world.destroyBlock(pos, true, entity);
            }
        }

    }

    @Mixin(CampfireBlock.class)
    public abstract static class ForestTrollNotGetCampfireDamage{

        @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
        private void injectNoMoonDust(BlockState state, Level world, BlockPos pos, Entity entity, CallbackInfo ci){
            if (entity instanceof ForestTrollEntity) {
                ci.cancel();
            }
        }

    }
}
