package mors.tcots.entity.misc.bolts;

import mors.tcots.registry.TCOTS_Entities;
import mors.tcots.registry.TCOTS_Items;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrecisionBoltProjectile extends WitcherBolt {
    private static final ItemStack DEFAULT_STACK = new ItemStack(TCOTS_Items.PRECISION_BOLT.get());
    public PrecisionBoltProjectile(final EntityType<? extends PrecisionBoltProjectile> type, final Level world) {
        super(type, world);
    }

    public PrecisionBoltProjectile(final Level world, final LivingEntity owner, final ItemStack stack, @Nullable final ItemStack weapon) {
        super(TCOTS_Entities.PrecisionBolt(), owner, world, stack, weapon);
        setBaseDamage(2.5);
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return DEFAULT_STACK;
    }
}
