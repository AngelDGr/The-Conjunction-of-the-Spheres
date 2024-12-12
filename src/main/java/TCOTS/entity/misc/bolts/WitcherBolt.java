package TCOTS.entity.misc.bolts;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.world.World;

public abstract class WitcherBolt extends PersistentProjectileEntity {
    //xTODO: Crossbow Bolts - More damage than a arrow
    //xTODO: Blunt Crossbow Bolts - Even more damage
    //xTODO: Precision Bolt - Extra piercing
    //xTODO: Exploding Bolt - Explodes
    //xTODO: Broadhead Bolt - Causes bleeding


    protected WitcherBolt(EntityType<? extends WitcherBolt> type, World world) {
        super(type, world);
    }
    protected WitcherBolt(EntityType<? extends WitcherBolt> type, double x, double y, double z, World world) {
        this(type, world);
        this.setPosition(x, y, z);
    }
    protected WitcherBolt(EntityType<? extends WitcherBolt> type, LivingEntity owner, World world) {
        this(type, owner.getX(), owner.getEyeY() - (double)0.1f, owner.getZ(), world);
        this.setOwner(owner);
        if (owner instanceof PlayerEntity) {
            this.pickupType = PickupPermission.ALLOWED;
        }
    }
}
