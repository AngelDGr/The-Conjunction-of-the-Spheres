package TCOTS.items.potions.bombs;

import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.items.potions.TCOTS_Effects;
import TCOTS.particles.TCOTS_Particles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

public class MoonDustBomb {
    //xTODO: Block Drowned, Piglin, Zoglin, Stray transforming, Block Creeper explosion

    private static final byte MOON_DUST_EXPLODES = 41;

    public static void explosionLogic(WitcherBombEntity bomb){

        bomb.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1,1);

        bomb.getWorld().sendEntityStatus(bomb, MOON_DUST_EXPLODES);

        List<LivingEntity> list = bomb.getWorld().getEntitiesByClass(LivingEntity.class, bomb.getBoundingBox().expand(3+(bomb.getLevel()*2),2,3+(bomb.getLevel()*2)),
                entity ->
                        !(entity instanceof WardenEntity) && !(entity instanceof ArmorStandEntity)
                                && entity.isAlive()
                                && entity != bomb.getOwner());


        Entity entityCause = bomb.getEffectCause();
        for(LivingEntity entity: list){
            //To not apply effect across walls
            if(getExposure(entity.getPos(), bomb) == 0) continue;

            //Applies moon dust effect to entity
            entity.addStatusEffect(new StatusEffectInstance(TCOTS_Effects.MOON_DUST_EFFECT, bomb.getLevel() < 1 ? 400 : 800, bomb.getLevel()), entityCause);
        }
    }

    private static float getExposure(Vec3d source, Entity entity) {
        Box box = entity.getBoundingBox();
        double d = 1.0 / ((box.maxX - box.minX) * 2.0 + 1.0);
        double e = 1.0 / ((box.maxY - box.minY) * 2.0 + 1.0);
        double f = 1.0 / ((box.maxZ - box.minZ) * 2.0 + 1.0);
        double g = (1.0 - Math.floor(1.0 / d) * d) / 2.0;
        double h = (1.0 - Math.floor(1.0 / f) * f) / 2.0;
        if (d < 0.0 || e < 0.0 || f < 0.0) {
            return 0.0f;
        }
        int i = 0;
        int j = 0;
        for (double k = 0.0; k <= 1.0; k += d) {
            for (double l = 0.0; l <= 1.0; l += e) {
                for (double m = 0.0; m <= 1.0; m += f) {
                    double n = MathHelper.lerp(k, box.minX, box.maxX);
                    double o = MathHelper.lerp(l, box.minY, box.maxY);
                    double p = MathHelper.lerp(m, box.minZ, box.maxZ);
                    Vec3d vec3d = new Vec3d(n + g, o, p + h);
                    if (entity.getWorld().raycast(new RaycastContext(vec3d, source, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity)).getType() == HitResult.Type.MISS) {
                        ++i;
                    }
                    ++j;
                }
            }
        }
        return (float)i / (float)j;
    }

    public static void handleStatus(WitcherBombEntity bomb, byte status) {
        if(status== MOON_DUST_EXPLODES){
            bomb.getWorld().addParticle(TCOTS_Particles.MOON_DUST_EXPLOSION_EMITTER, bomb.getX(), bomb.getY(), bomb.getZ(), 0.0, 0.0, 0.0);
        }
    }

    public static boolean checkEffect(LivingEntity entity){
        return MoonDustBomb.checkOnlyEffect(entity) || entity.theConjunctionOfTheSpheres$hasSilverSplinters();
    }

    public static boolean checkOnlyEffect(LivingEntity entity){
        return entity.hasStatusEffect(TCOTS_Effects.MOON_DUST_EFFECT);
    }

    public static boolean checkSilverSplinters(LivingEntity entity){
        return entity.theConjunctionOfTheSpheres$hasSilverSplinters();
    }

    public static void checkEffectMixin(LivingEntity entity, CallbackInfoReturnable<Boolean> cir){
        if(MoonDustBomb.checkEffect(entity))
            cir.setReturnValue(false);
    }

}
