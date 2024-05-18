package TCOTS.entity.misc.bolts;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.items.TCOTS_Items;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BaseBoltProjectile extends WitcherBolt {

    private static final ItemStack DEFAULT_STACK = new ItemStack(TCOTS_Items.BASE_BOLT);

    public BaseBoltProjectile(EntityType<? extends BaseBoltProjectile> entityType, World world) {
        super(entityType, world, DEFAULT_STACK);
    }

    public BaseBoltProjectile(World world, LivingEntity owner, ItemStack stack) {
        super(TCOTS_Entities.BASE_BOLT, owner, world, stack);
        setDamage(3);
    }

}
