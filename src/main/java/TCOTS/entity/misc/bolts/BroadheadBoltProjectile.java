package TCOTS.entity.misc.bolts;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.items.TCOTS_Items;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BroadheadBoltProjectile extends WitcherBolt {
    private static final ItemStack DEFAULT_STACK = new ItemStack(TCOTS_Items.BROADHEAD_BOLT);
    public BroadheadBoltProjectile(EntityType<? extends BroadheadBoltProjectile> entityType, World world) {
        super(entityType, world, DEFAULT_STACK);
    }

    public BroadheadBoltProjectile(World world, LivingEntity owner, ItemStack stack) {
        super(TCOTS_Entities.BROADHEAD_BOLT, owner, world, stack);
        setDamage(2.8);
    }

}
