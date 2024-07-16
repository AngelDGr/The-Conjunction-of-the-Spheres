package TCOTS.items.concoctions.bombs;

import TCOTS.entity.misc.DragonsDreamCloud;
import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.particles.TCOTS_Particles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DragonsDreamBomb {
    private static final byte DRAGONS_DREAM_EXPLODES = 42;

    public static void explosionLogic(WitcherBombEntity bomb){

        bomb.getWorld().playSound(null, bomb.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, bomb.getSoundCategory());

        bomb.getWorld().sendEntityStatus(bomb, DRAGONS_DREAM_EXPLODES);

        //Put the gas cloud
        setCloud(bomb);
    }

    private static void setCloud(WitcherBombEntity bomb){
        List<LivingEntity> list = bomb.getWorld().getNonSpectatingEntities(LivingEntity.class, bomb.getBoundingBox().expand(4.0, 2.0, 4.0));
        DragonsDreamCloud dragonsDreamCloudEntity = getDragonsDreamCloud(bomb);
        dragonsDreamCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 2));
        if (!list.isEmpty()) {
            for (LivingEntity livingEntity : list) {
                double d = bomb.squaredDistanceTo(livingEntity);
                if (!(d < 16.0)) continue;
                dragonsDreamCloudEntity.setPosition(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                break;
            }
        }
        bomb.getWorld().spawnEntity(dragonsDreamCloudEntity);
    }

    @NotNull
    private static DragonsDreamCloud getDragonsDreamCloud(WitcherBombEntity bomb) {
        DragonsDreamCloud dragonsDreamCloudEntity = new DragonsDreamCloud(bomb.getWorld(), bomb.getX(), bomb.getY(), bomb.getZ(), bomb.getLevel());
        Entity entity = bomb.getOwner();
        if (entity instanceof LivingEntity) {
            dragonsDreamCloudEntity.setOwner((LivingEntity)entity);
        }
        dragonsDreamCloudEntity.setWaitTime(5);
        dragonsDreamCloudEntity.setParticleType(TCOTS_Particles.YELLOW_CLOUD);
        dragonsDreamCloudEntity.setRadius(2.0f+ bomb.getLevel());
        dragonsDreamCloudEntity.setDuration(200+(bomb.getLevel()*200));
        dragonsDreamCloudEntity.setRadiusGrowth((-dragonsDreamCloudEntity.getRadius()) / (float)dragonsDreamCloudEntity.getDuration());
        return dragonsDreamCloudEntity;
    }

    public static void handleStatus(WitcherBombEntity bomb, byte status) {
        if(status== DRAGONS_DREAM_EXPLODES){
            bomb.getWorld().addParticle(TCOTS_Particles.DRAGONS_DREAM_EXPLOSION_EMITTER, bomb.getX(), bomb.getY(), bomb.getZ(), 0.0, 0.0, 0.0);
        }
    }


}
