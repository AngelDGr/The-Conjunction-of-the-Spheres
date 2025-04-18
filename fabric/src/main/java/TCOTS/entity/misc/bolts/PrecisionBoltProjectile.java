package TCOTS.entity.misc.bolts;

import TCOTS.entity.TCOTS_Entities_Fabric;
import TCOTS.items.TCOTS_Items_Fabric;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrecisionBoltProjectile extends WitcherBolt{
    private static final ItemStack DEFAULT_STACK = new ItemStack(TCOTS_Items_Fabric.PRECISION_BOLT);
    public PrecisionBoltProjectile(EntityType<? extends PrecisionBoltProjectile> type, Level world) {
        super(type, world);
    }

    public PrecisionBoltProjectile(Level world, LivingEntity owner, ItemStack stack, @Nullable ItemStack weapon) {
        super(TCOTS_Entities_Fabric.PRECISION_BOLT, owner, world, stack, weapon);
        setBaseDamage(2.5);
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return DEFAULT_STACK;
    }
}
