package TCOTS.entity.necrophages;

import TCOTS.entity.WitcherMob_Class;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.world.World;

public class NecrophageMonster extends WitcherMob_Class {
    public NecrophageMonster(EntityType<? extends NecrophageMonster> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        return effect.getEffectType() != StatusEffects.POISON && super.canHaveStatusEffect(effect);
    }
}
