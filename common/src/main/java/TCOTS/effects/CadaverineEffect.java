package TCOTS.effects;

import TCOTS.utils.EntitiesUtil;
import TCOTS.registry.TCOTS_DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Zombie;

public class CadaverineEffect extends MobEffect {
    public CadaverineEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        float damageAmount = entity instanceof IronGolem ? 3f: entity instanceof Zombie? 2f: 1f;

        boolean damage = entity.hurt(TCOTS_DamageTypes.cadaverineDamage(entity.level()), damageAmount);
        if (damage)
            EntitiesUtil.damageEquipment(entity, TCOTS_DamageTypes.cadaverineDamage(entity.level()), 1,
                    EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD,
                    EquipmentSlot.BODY);

        return super.applyEffectTick(entity, amplifier);
    }



    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int i = 15 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        }
        return true;
    }

}
