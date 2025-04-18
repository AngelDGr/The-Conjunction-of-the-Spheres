package TCOTS.entity.necrophages;

import TCOTS.entity.WitcherMob_Class;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class NecrophageMonster extends WitcherMob_Class {
    public NecrophageMonster(EntityType<? extends NecrophageMonster> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        return effect.getEffect() != MobEffects.POISON && super.canBeAffected(effect);
    }
}
