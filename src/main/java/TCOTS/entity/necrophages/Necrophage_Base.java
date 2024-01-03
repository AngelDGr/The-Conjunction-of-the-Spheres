package TCOTS.entity.necrophages;

import TCOTS.entity.TCOTS_Entities;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;

public class Necrophage_Base extends HostileEntity {
    protected Necrophage_Base(EntityType<? extends HostileEntity> entityType, World world) {
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
