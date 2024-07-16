package TCOTS.items.concoctions.bombs;

import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.particles.TCOTS_Particles;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundEvents;

public class DevilsPuffballBomb {
    private static final byte DEVILS_PUFFBALL_EXPLODES = 33;

    public static void explosionLogic(WitcherBombEntity bomb) {
        bomb.getWorld().playSound(null, bomb.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, bomb.getSoundCategory());
        bomb.getWorld().sendEntityStatus(bomb, DEVILS_PUFFBALL_EXPLODES);
        applyLingeringPotion(bomb);
    }

    private static void applyLingeringPotion(WitcherBombEntity bomb) {
        AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(bomb.getWorld(), bomb.getX(), bomb.getY(), bomb.getZ());
        Entity owner = bomb.getOwner();
        if (owner instanceof LivingEntity) {
            areaEffectCloudEntity.setOwner((LivingEntity) owner);
        }
        areaEffectCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.POISON, 100 + (bomb.getLevel() * 100), bomb.getLevel()));
        areaEffectCloudEntity.setRadius(2.0f + (bomb.getLevel()));
        areaEffectCloudEntity.setRadiusOnUse(-0.5f);
        areaEffectCloudEntity.setWaitTime(5);
        areaEffectCloudEntity.setRadiusGrowth(-areaEffectCloudEntity.getRadius() / (float) areaEffectCloudEntity.getDuration());
        bomb.getWorld().spawnEntity(areaEffectCloudEntity);
    }

    public static void handleStatus(WitcherBombEntity bomb, byte status) {
        if(status==DEVILS_PUFFBALL_EXPLODES){
            bomb.getWorld().addParticle(TCOTS_Particles.DEVILS_PUFFBALL_EXPLOSION_EMITTER, bomb.getX(), bomb.getY(), bomb.getZ(), 0.0, 0.0, 0.0);
        }
    }
}
