package mors.tcots.entity.monsters.necrophages;

import mors.tcots.entity.WitcherMob;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class NecrophageMonster extends WitcherMob {
    public NecrophageMonster(final EntityType<? extends NecrophageMonster> entityType, final Level world) {
        super(entityType, world);
    }

    @Override
    public boolean canBeAffected(final MobEffectInstance effect) {
        return effect.getEffect() != MobEffects.POISON && super.canBeAffected(effect);
    }
}
