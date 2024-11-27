package TCOTS.items.concoctions.effects;

import TCOTS.world.TCOTS_DamageTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class CadaverineEffect extends StatusEffect {
    public CadaverineEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
        float damageAmount = entity instanceof IronGolemEntity ? 3f: entity instanceof ZombieEntity? 2f: 1f;

        boolean damage = entity.damage(TCOTS_DamageTypes.cadaverineDamage(entity.getWorld()), damageAmount);
        if (damage)
            entity.getArmorItems().forEach(armor -> armor.damage(3, entity.getRandom(), entity instanceof ServerPlayerEntity player? player: null));
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
