package TCOTS.mixin;

import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Explosion.class)
public class WorldMixin {

//    @Inject(method =
//            "createExplosion(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/World$ExplosionSourceType;Z)Lnet/minecraft/world/explosion/Explosion;",
//            at = @At("TAIL"), cancellable = true)
//    private void injectRotfiendExplosion(Entity entity, DamageSource damageSource, ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, World.ExplosionSourceType explosionSourceType, boolean particles, CallbackInfoReturnable<Explosion> cir){
//        if(entity instanceof RotfiendEntity){
//            World thisObject = (World)(Object)this;
//            System.out.println("MixinTriggered");
//            RotfiendExplosion explosion = new RotfiendExplosion(thisObject, entity, damageSource, behavior, x, y, z, power, createFire, Explosion.DestructionType.KEEP);
//            explosion.collectBlocksAndDamageEntities();
//            explosion.affectWorld(particles);
//            cir.setReturnValue(explosion);
//        }
//    }
//    @Inject(method = "createExplosion", at = @At("HEAD"))
//    private void injectRotfiendExplosion(Entity entity, DamageSource damageSource, ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, World.ExplosionSourceType explosionSourceType, CallbackInfoReturnable<Explosion> cir){
//
//    }


}
