package mors.tcots.mixin;

import mors.tcots.entity.WitcherMob;
import mors.tcots.registry.TCOTS_Criteria;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin extends Player {

    public ServerPlayerEntityMixin(final Level world, final BlockPos pos, final float yaw, final GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Shadow protected abstract void checkFallDamage(double heightDifference, boolean onGround, @NotNull BlockState state, @NotNull BlockPos landedPosition);

    @Inject(method = "startSleepInBed", at = @At("HEAD"), cancellable = true)
    public void tcots$injectWitcherMobsDangerous(final BlockPos pos, final CallbackInfoReturnable<Either<Player.BedSleepingProblem, Unit>> cir){
        if (!this.isCreative()) {
            final Vec3 vec3d = Vec3.atBottomCenterOf(pos);
            final List<WitcherMob> list = this.level().getEntitiesOfClass(WitcherMob.class, new AABB(vec3d.x() - 8.0, vec3d.y() - 5.0, vec3d.z() - 8.0, vec3d.x() + 8.0, vec3d.y() + 5.0, vec3d.z() + 8.0), entity -> entity.isPreventingPlayerRest(this));
            if (!list.isEmpty()) {
                cir.setReturnValue(Either.left(Player.BedSleepingProblem.NOT_SAFE));
            }
        }
    }

    @Shadow
    public boolean isCreative() {
     return false;
    }
    @Unique
    ServerPlayer tcots$THIS = (ServerPlayer)(Object)this;
    @Inject(method = "tick", at = @At("TAIL"))
    public void tcots$triggerMaxToxicity(final CallbackInfo ci){
        if(this.tcots$getAllToxicity() >= this.tcots$setMaxToxicity()*0.9){
            TCOTS_Criteria.MaxToxicityReached().trigger(tcots$THIS);
        }
    }

}
