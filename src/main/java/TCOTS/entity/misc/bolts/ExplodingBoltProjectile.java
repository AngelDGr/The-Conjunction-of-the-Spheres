package TCOTS.entity.misc.bolts;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.items.TCOTS_Items;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ExplodingBoltProjectile extends WitcherBolt {
    private static final ItemStack DEFAULT_STACK = new ItemStack(TCOTS_Items.EXPLODING_BOLT);
    public ExplodingBoltProjectile(EntityType<? extends ExplodingBoltProjectile> entityType, World world) {
        super(entityType, world, DEFAULT_STACK);
    }

    public ExplodingBoltProjectile(World world, LivingEntity owner, ItemStack stack) {
        super(TCOTS_Entities.EXPLODING_BOLT, owner, world, stack);
    }

}
