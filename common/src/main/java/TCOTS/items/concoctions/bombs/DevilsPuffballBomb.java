package TCOTS.items.concoctions.bombs;

import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.registry.TCOTS_Particles;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class DevilsPuffballBomb {
    private static final byte DEVILS_PUFFBALL_EXPLODES = 33;

    public static void explosionLogic(WitcherBombEntity bomb) {
        bomb.level().playSound(null, bomb.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), bomb.getSoundSource());
        bomb.level().broadcastEntityEvent(bomb, DEVILS_PUFFBALL_EXPLODES);
        applyLingeringPotion(bomb);
    }

    private static void applyLingeringPotion(WitcherBombEntity bomb) {
        AreaEffectCloud areaEffectCloudEntity = new AreaEffectCloud(bomb.level(), bomb.getX(), bomb.getY(), bomb.getZ());
        Entity owner = bomb.getOwner();
        if (owner instanceof LivingEntity) {
            areaEffectCloudEntity.setOwner((LivingEntity) owner);
        }
        areaEffectCloudEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 100 + (bomb.getLevel() * 100), bomb.getLevel()));
        areaEffectCloudEntity.setRadius(2.0f + (bomb.getLevel()));
        areaEffectCloudEntity.setRadiusOnUse(-0.5f);
        areaEffectCloudEntity.setWaitTime(5);
        areaEffectCloudEntity.setRadiusPerTick(-areaEffectCloudEntity.getRadius() / (float) areaEffectCloudEntity.getDuration());
        bomb.level().addFreshEntity(areaEffectCloudEntity);
    }

    public static void handleStatus(WitcherBombEntity bomb, byte status) {
        if(status==DEVILS_PUFFBALL_EXPLODES){
            bomb.level().addParticle(TCOTS_Particles.DevilsPuffballExplosionEmitter(), bomb.getX(), bomb.getY(), bomb.getZ(), 0.0, 0.0, 0.0);
        }
    }
}
