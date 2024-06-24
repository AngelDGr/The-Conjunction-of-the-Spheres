package TCOTS.entity.ogroids;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.WitcherMob_Class;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

public class Ogroid_Base extends WitcherMob_Class {
    protected Ogroid_Base(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }
    @Override
    public EntityGroup getGroup() {
        return TCOTS_Entities.OGROIDS;
    }

}
