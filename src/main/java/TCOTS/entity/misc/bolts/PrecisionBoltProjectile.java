package TCOTS.entity.misc.bolts;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.items.TCOTS_Items;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PrecisionBoltProjectile extends WitcherBolt{
    private static final ItemStack DEFAULT_STACK = new ItemStack(TCOTS_Items.PRECISION_BOLT);
    public PrecisionBoltProjectile(EntityType<? extends PrecisionBoltProjectile> type, World world) {
        super(type, world);
    }

    public PrecisionBoltProjectile(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack weapon) {
        super(TCOTS_Entities.PRECISION_BOLT, owner, world, stack, weapon);
        setDamage(2.5);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return DEFAULT_STACK;
    }
}
