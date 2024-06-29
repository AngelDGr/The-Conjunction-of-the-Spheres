package TCOTS.entity.necrophages;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.WitcherMob_Class;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

public class NecrophageMonster extends WitcherMob_Class {
    public NecrophageMonster(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public EntityGroup getGroup() {
        return TCOTS_Entities.NECROPHAGES;
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        return effect.getEffectType() != StatusEffects.POISON && super.canHaveStatusEffect(effect);
    }
}
