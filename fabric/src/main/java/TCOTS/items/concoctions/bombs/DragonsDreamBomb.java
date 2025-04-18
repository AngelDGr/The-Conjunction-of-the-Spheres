package TCOTS.items.concoctions.bombs;

import TCOTS.entity.misc.DragonsDreamCloud;
import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.particles.TCOTS_Particles_Fabric;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class DragonsDreamBomb {
    private static final byte DRAGONS_DREAM_EXPLODES = 42;

    public static void explosionLogic(WitcherBombEntity bomb){

        bomb.level().playSound(null, bomb.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), bomb.getSoundSource());

        bomb.level().broadcastEntityEvent(bomb, DRAGONS_DREAM_EXPLODES);

        //Put the gas cloud
        setCloud(bomb);
    }

    private static void setCloud(WitcherBombEntity bomb){
        List<LivingEntity> list = bomb.level().getEntitiesOfClass(LivingEntity.class, bomb.getBoundingBox().inflate(4.0, 2.0, 4.0));
        DragonsDreamCloud dragonsDreamCloudEntity = getDragonsDreamCloud(bomb);
        dragonsDreamCloudEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
        if (!list.isEmpty()) {
            for (LivingEntity livingEntity : list) {
                double d = bomb.distanceToSqr(livingEntity);
                if (!(d < 16.0)) continue;
                dragonsDreamCloudEntity.setPos(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                break;
            }
        }
        bomb.level().addFreshEntity(dragonsDreamCloudEntity);
    }

    @NotNull
    private static DragonsDreamCloud getDragonsDreamCloud(WitcherBombEntity bomb) {
        DragonsDreamCloud dragonsDreamCloudEntity = new DragonsDreamCloud(bomb.level(), bomb.getX(), bomb.getY(), bomb.getZ(), bomb.getLevel());
        Entity entity = bomb.getOwner();
        if (entity instanceof LivingEntity) {
            dragonsDreamCloudEntity.setOwner((LivingEntity)entity);
        }
        dragonsDreamCloudEntity.setWaitTime(5);
        dragonsDreamCloudEntity.setParticle(TCOTS_Particles_Fabric.YELLOW_CLOUD);
        dragonsDreamCloudEntity.setRadius(2.0f+ bomb.getLevel());
        dragonsDreamCloudEntity.setDuration(200+(bomb.getLevel()*200));
        dragonsDreamCloudEntity.setRadiusPerTick((-dragonsDreamCloudEntity.getRadius()) / (float)dragonsDreamCloudEntity.getDuration());
        return dragonsDreamCloudEntity;
    }

    public static void handleStatus(WitcherBombEntity bomb, byte status) {
        if(status== DRAGONS_DREAM_EXPLODES){
            bomb.level().addParticle(TCOTS_Particles_Fabric.DRAGONS_DREAM_EXPLOSION_EMITTER, bomb.getX(), bomb.getY(), bomb.getZ(), 0.0, 0.0, 0.0);
        }
    }


}
