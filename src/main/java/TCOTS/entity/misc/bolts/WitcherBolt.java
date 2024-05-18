package TCOTS.entity.misc.bolts;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class WitcherBolt extends PersistentProjectileEntity {
    //xTODO: Crossbow Bolts - More damage than a arrow
    //xTODO: Blunt Crossbow Bolts - Even more damage
    //TODO: Precision Bolt - Extra piercing
    //TODO: Exploding Bolt - Explodes
    //TODO: Broadhead Bolt - Causes bleeding


    protected WitcherBolt(EntityType<? extends WitcherBolt> type, World world, ItemStack stack) {
        super(type, world, stack);
    }
    protected WitcherBolt(EntityType<? extends WitcherBolt> type, double x, double y, double z, World world, ItemStack stack) {
        this(type, world, stack);
        this.setPosition(x, y, z);
    }
    protected WitcherBolt(EntityType<? extends WitcherBolt> type, LivingEntity owner, World world, ItemStack stack) {
        this(type, owner.getX(), owner.getEyeY() - (double)0.1f, owner.getZ(), world, stack);
        this.setOwner(owner);
        if (owner instanceof PlayerEntity) {
            this.pickupType = PickupPermission.ALLOWED;
        }
    }
}
