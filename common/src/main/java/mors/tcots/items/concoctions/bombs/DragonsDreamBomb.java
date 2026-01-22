package mors.tcots.items.concoctions.bombs;

import mors.tcots.entity.misc.DragonsDreamCloud;
import mors.tcots.entity.misc.WitcherBombEntity;
import mors.tcots.registry.TCOTS_Particles;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class DragonsDreamBomb {
    private static final byte DRAGONS_DREAM_EXPLODES = 42;

    public static void explosionLogic(final WitcherBombEntity bomb){

        bomb.level().playSound(null, bomb.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), bomb.getSoundSource());

        bomb.level().broadcastEntityEvent(bomb, DRAGONS_DREAM_EXPLODES);

        //Put the gas cloud
        setCloud(bomb);
    }

    private static void setCloud(final WitcherBombEntity bomb){
        final List<LivingEntity> list = bomb.level().getEntitiesOfClass(LivingEntity.class, bomb.getBoundingBox().inflate(4.0, 2.0, 4.0));
        final DragonsDreamCloud dragonsDreamCloudEntity = getDragonsDreamCloud(bomb);
        dragonsDreamCloudEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
        if (!list.isEmpty()) {
            for (final LivingEntity livingEntity : list) {
                final double d = bomb.distanceToSqr(livingEntity);
                if (!(d < 16.0)) continue;
                dragonsDreamCloudEntity.setPos(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                break;
            }
        }
        bomb.level().addFreshEntity(dragonsDreamCloudEntity);
    }

    @NotNull
    private static DragonsDreamCloud getDragonsDreamCloud(final WitcherBombEntity bomb) {
        final DragonsDreamCloud dragonsDreamCloudEntity = new DragonsDreamCloud(bomb.level(), bomb.getX(), bomb.getY(), bomb.getZ(), bomb.getLevel());
        final Entity entity = bomb.getOwner();
        if (entity instanceof LivingEntity) {
            dragonsDreamCloudEntity.setOwner((LivingEntity)entity);
        }
        dragonsDreamCloudEntity.setWaitTime(5);
        dragonsDreamCloudEntity.setParticle(TCOTS_Particles.YellowCloud());
        dragonsDreamCloudEntity.setRadius(2.0f+ bomb.getLevel());
        dragonsDreamCloudEntity.setDuration(200+(bomb.getLevel()*200));
        dragonsDreamCloudEntity.setRadiusPerTick((-dragonsDreamCloudEntity.getRadius()) / (float)dragonsDreamCloudEntity.getDuration());
        return dragonsDreamCloudEntity;
    }

    public static void handleStatus(final WitcherBombEntity bomb, final byte status) {
        if(status== DRAGONS_DREAM_EXPLODES){
            bomb.level().addParticle(TCOTS_Particles.DragonsDreamExplosionEmitter(), bomb.getX(), bomb.getY(), bomb.getZ(), 0.0, 0.0, 0.0);
        }
    }


}
