package TCOTS.entity.necrophages;

import TCOTS.entity.TCOTS_Entities;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

public class Necrophage_Base extends PathAwareEntity implements Monster {
    protected Necrophage_Base(EntityType<? extends PathAwareEntity> entityType, World world) {
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

    @Override
    public void tickMovement() {
        this.tickHandSwing();
        this.updateDespawnCounter();
        super.tickMovement();
    }

    protected void updateDespawnCounter() {
        float f = this.getBrightnessAtEyes();
        if (f > 0.5F) {
            this.despawnCounter += 2;
        }

    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

}
