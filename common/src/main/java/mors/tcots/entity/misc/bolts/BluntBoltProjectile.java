package mors.tcots.entity.misc.bolts;

import mors.tcots.registry.TCOTS_Entities;
import mors.tcots.registry.TCOTS_Items;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BluntBoltProjectile extends WitcherBolt {
    private static final ItemStack DEFAULT_STACK = new ItemStack(TCOTS_Items.BLUNT_BOLT.get());
    public BluntBoltProjectile(final EntityType<? extends BluntBoltProjectile> type, final Level world) {
        super(type, world);
    }

    public BluntBoltProjectile(final Level world, final LivingEntity owner, final ItemStack stack, @Nullable final ItemStack weapon) {
        super(TCOTS_Entities.BluntBolt(), owner, world, stack, weapon);
        setBaseDamage(3.8);
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return DEFAULT_STACK;
    }
}
