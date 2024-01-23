package TCOTS.mixin;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(net.minecraft.server.world.ServerWorld.class)
public class ServerWorldMixin {

//    @Final
//    @Shadow
//    final List<ServerPlayerEntity> players = Lists.newArrayList();
//
//
//    @Inject(method="createExplosion", at = @At("HEAD"), cancellable = true)
//    private void injectRotfiendExplosion(Entity entity, DamageSource damageSource, ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, World.ExplosionSourceType explosionSourceType, CallbackInfoReturnable<Explosion> cir){
//
//        if(entity instanceof RotfiendEntity) {
//            World thisObject = (World) (Object) this;
//            RotfiendExplosion explosion = (RotfiendExplosion) thisObject.createExplosion(entity, damageSource, behavior, x, y, z, power, createFire, explosionSourceType, false);
//            if (!explosion.shouldDestroy()) {
//                explosion.clearAffectedBlocks();
//            }
//            for (ServerPlayerEntity serverPlayerEntity : this.players) {
//                if (!(serverPlayerEntity.squaredDistanceTo(x, y, z) < 4096.0)) continue;
//                serverPlayerEntity.networkHandler.sendPacket(new ExplosionS2CPacket(x, y, z, power, explosion.getAffectedBlocks(), explosion.getAffectedPlayers().get(serverPlayerEntity)));
//            }
//            cir.setReturnValue(explosion);
//        }
//    }

//    @ModifyVariable(method="createExplosion", at = @At("STORE"), ordinal = 0)
//    private Explosion injectRotfiendExplosion(Explosion explosion){
//        RotfiendExplosion explosionR = this.createExplosion(entity, damageSource, behavior, x, y, z, power, createFire, explosionSourceType, false);
//        return explosionR;
//    }
}
