package TCOTS.mixin;

import TCOTS.advancements.TCOTS_Criteria;
import TCOTS.entity.WitcherMob_Class;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Shadow protected abstract void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition);

    @Inject(method = "trySleep", at = @At("HEAD"), cancellable = true)
    public void injectWitcherMobsDangerous(BlockPos pos, CallbackInfoReturnable<Either<PlayerEntity.SleepFailureReason, Unit>> cir){
        if (!this.isCreative()) {
            Vec3d vec3d = Vec3d.ofBottomCenter(pos);
            List<WitcherMob_Class> list = this.getWorld().getEntitiesByClass(WitcherMob_Class.class, new Box(vec3d.getX() - 8.0, vec3d.getY() - 5.0, vec3d.getZ() - 8.0, vec3d.getX() + 8.0, vec3d.getY() + 5.0, vec3d.getZ() + 8.0), entity -> entity.isAngryAt(this));
            if (!list.isEmpty()) {
                cir.setReturnValue(Either.left(PlayerEntity.SleepFailureReason.NOT_SAFE));
            }
        }
    }

    @Shadow
    public boolean isCreative() {
     return false;
    }
    @Unique
    ServerPlayerEntity THIS = (ServerPlayerEntity)(Object)this;
    @Inject(method = "tick", at = @At("TAIL"))
    public void injectTriggerMaxToxicity(CallbackInfo ci){
        if(this.theConjunctionOfTheSpheres$getAllToxicity() >= this.theConjunctionOfTheSpheres$getMaxToxicity()*0.9){
            TCOTS_Criteria.MAX_TOXICITY_REACHED.trigger(THIS);
        }
    }

}
