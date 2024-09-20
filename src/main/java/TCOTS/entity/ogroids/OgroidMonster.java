package TCOTS.entity.ogroids;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.WitcherMob_Class;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class OgroidMonster extends WitcherMob_Class {
    public OgroidMonster(EntityType<? extends OgroidMonster> entityType, World world) {
        super(entityType, world);
    }
    @Override
    public EntityGroup getGroup() {
        return TCOTS_Entities.OGROIDS;
    }

}
