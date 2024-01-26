package TCOTS.mixin;

import TCOTS.entity.misc.RotfiendExplosionBehavior;
import TCOTS.entity.necrophages.RotfiendEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true) // Enables exporting for the targets of this mixin
@Mixin(World.class)
public class WorldMixin {

    @Shadow
    private Explosion.DestructionType getDestructionType(GameRules.Key<GameRules.BooleanRule> gameRuleKey){return null;}

    @Shadow
    public GameRules getGameRules() {return null;}

    @Inject(method = "createExplosion(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/world/World$ExplosionSourceType;)Lnet/minecraft/world/explosion/Explosion;",
    at = @At("HEAD"), cancellable = true)
    private void InjectRotfiend(Entity entity, double x, double y, double z, float power, World.ExplosionSourceType explosionSourceType, CallbackInfoReturnable<Explosion> cir){
        if(entity instanceof RotfiendEntity){
            RotfiendExplosionBehavior rotfiendExplosionBehavior = new RotfiendExplosionBehavior(entity);
            cir.setReturnValue(this.createExplosion(entity, null, rotfiendExplosionBehavior, x, y, z, power, false, explosionSourceType));
        }
    }

    @Shadow
    public Explosion createExplosion(@Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, World.ExplosionSourceType explosionSourceType) {
        return this.createExplosion(entity, damageSource, behavior, x, y, z, power, createFire, explosionSourceType, true);
    }


    @Shadow
    public Explosion createExplosion(@Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, World.ExplosionSourceType explosionSourceType, boolean particles) {return null;
    }

//    @ModifyVariable(
//            method = "createExplosion(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/World$ExplosionSourceType;Z)Lnet/minecraft/world/explosion/Explosion;",
//            at = @At("STORE"), ordinal = 0)
//    private Explosion injected(Explosion value) {
//        return null;
//    }


}
