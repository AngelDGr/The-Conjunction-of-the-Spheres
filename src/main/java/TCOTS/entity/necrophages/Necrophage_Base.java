package TCOTS.entity.necrophages;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.WitcherMob_Class;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;
import software.bernie.geckolib.core.animation.RawAnimation;

public class Necrophage_Base extends WitcherMob_Class {
    protected Necrophage_Base(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation RUNNING = RawAnimation.begin().thenLoop("move.running");
    public static final RawAnimation WALKING = RawAnimation.begin().thenLoop("move.walking");

    @Override
    public EntityGroup getGroup() {
        return TCOTS_Entities.NECROPHAGES;
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        return effect.getEffectType() != StatusEffects.POISON && super.canHaveStatusEffect(effect);
    }
}
