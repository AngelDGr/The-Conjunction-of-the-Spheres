package mors.tcots.entity.monsters.ogroids;

import mors.tcots.entity.WitcherMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class OgroidMonster extends WitcherMob {
    public OgroidMonster(final EntityType<? extends OgroidMonster> entityType, final Level world) {
        super(entityType, world);
    }

}
