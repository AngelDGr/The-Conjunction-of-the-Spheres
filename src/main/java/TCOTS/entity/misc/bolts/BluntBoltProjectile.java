package TCOTS.entity.misc.bolts;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.items.TCOTS_Items;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BluntBoltProjectile extends WitcherBolt{
    private static final ItemStack DEFAULT_STACK = new ItemStack(TCOTS_Items.BLUNT_BOLT);
    public BluntBoltProjectile(EntityType<? extends BluntBoltProjectile> type, World world) {
        super(type, world);
    }

    public BluntBoltProjectile(World world, LivingEntity owner) {
        super(TCOTS_Entities.BLUNT_BOLT, owner, world);
        setDamage(3.8);
    }

    @Override
    protected ItemStack asItemStack() {
        return DEFAULT_STACK.copy();
    }
}
