package TCOTS.items.potions.bombs;

import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.items.potions.TCOTS_Effects;
import TCOTS.particles.TCOTS_Particles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.List;

public class SamumBomb {
    private static final byte SAMUM_EXPLODES = 34;

    public static void explosionLogic(WitcherBombEntity bomb){
        Explosion explosion =
                bomb.getWorld().createExplosion(
                        bomb,
                        null,
                        null,
                        bomb.getX(),
                        bomb.getY(),
                        bomb.getZ(),
                        //Level 0 -> 1.25
                        //Level 1 -> 1.50
                        //Level 2 -> 1.75
                        0.25f,
                        false,
                        World.ExplosionSourceType.BLOCK,
                        ParticleTypes.POOF,
                        ParticleTypes.POOF,
                        SoundEvents.ENTITY_GENERIC_EXPLODE
                );

        bomb.getWorld().sendEntityStatus(bomb, SAMUM_EXPLODES);

        List<LivingEntity> list = bomb.getWorld().getEntitiesByClass(LivingEntity.class, bomb.getBoundingBox().expand(3+(bomb.getLevel()*2),2,3+(bomb.getLevel()*2)),
                livingEntity -> !(livingEntity instanceof WardenEntity) && !(livingEntity instanceof ArmorStandEntity) && !(livingEntity instanceof GuardianEntity)
                        && livingEntity != bomb.getOwner());

        Entity entityCause = bomb.getEffectCause();
        for (LivingEntity entity : list) {
            //To not apply effect across walls
            if(getExposure(entity.getPos(), bomb) == 0) continue;

            if (entity instanceof PlayerEntity) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 240 + (bomb.getLevel() * 60), bomb.getLevel()), entityCause);
            } else {
                entity.addStatusEffect(new StatusEffectInstance(TCOTS_Effects.SAMUM_EFFECT, 80 + (bomb.getLevel() * 40), bomb.getLevel()), entityCause);
            }
        }

        GrapeshotBomb.destroyNests(bomb, explosion);
    }

    public static void handleStatus(WitcherBombEntity bomb, byte status) {
        if(status==SAMUM_EXPLODES){
            bomb.getWorld().addParticle(TCOTS_Particles.SAMUM_EXPLOSION_EMITTER, bomb.getX(), bomb.getY(), bomb.getZ(), 0.0, 0.0, 0.0);
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

    public static boolean checkSamumEffect(LivingEntity entity){
        return entity.hasStatusEffect(TCOTS_Effects.SAMUM_EFFECT);
    }

}
