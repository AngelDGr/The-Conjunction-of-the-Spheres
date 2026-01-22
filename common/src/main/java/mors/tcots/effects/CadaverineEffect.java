package mors.tcots.effects;

import mors.tcots.utils.TCOTS_EntitiesUtil;
import mors.tcots.registry.TCOTS_DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Zombie;

public class CadaverineEffect extends MobEffect {
    public CadaverineEffect(final MobEffectCategory category, final int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(final LivingEntity entity, final int amplifier) {
        final float damageAmount = entity instanceof IronGolem ? 3f:
                entity instanceof Zombie || TCOTS_EntitiesUtil.isNecrophage(entity)? 2f:
                        1f;

        final boolean damage = entity.hurt(TCOTS_DamageTypes.cadaverineDamage(entity.level()), damageAmount);
        if (damage)
            TCOTS_EntitiesUtil.damageEquipment(entity, TCOTS_DamageTypes.cadaverineDamage(entity.level()), 1,
                    EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD,
                    EquipmentSlot.BODY);

        return super.applyEffectTick(entity, amplifier);
    }



    @Override
    public boolean shouldApplyEffectTickThisTick(final int duration, final int amplifier) {
        final int i = 15 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        }
        return true;
    }

}
