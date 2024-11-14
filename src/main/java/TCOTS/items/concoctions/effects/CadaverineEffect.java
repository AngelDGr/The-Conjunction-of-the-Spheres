package TCOTS.items.concoctions.effects;

import TCOTS.utils.EntitiesUtil;
import TCOTS.world.TCOTS_DamageTypes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.IronGolemEntity;

public class CadaverineEffect extends StatusEffect {
    public CadaverineEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        float damageAmount = entity instanceof IronGolemEntity ? 3f: entity instanceof ZombieEntity? 2f: 1f;

        boolean damage = entity.damage(TCOTS_DamageTypes.cadaverineDamage(entity.getWorld()), damageAmount);
        if (damage)
            EntitiesUtil.damageEquipment(entity, TCOTS_DamageTypes.cadaverineDamage(entity.getWorld()), 1,
                    EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD,
                    EquipmentSlot.BODY);

        return super.applyUpdateEffect(entity, amplifier);
    }



    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = 15 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        }
        return true;
    }

}
