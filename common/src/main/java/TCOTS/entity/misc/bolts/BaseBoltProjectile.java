package TCOTS.entity.misc.bolts;

import TCOTS.registry.TCOTS_Entities;
import TCOTS.registry.TCOTS_Items;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BaseBoltProjectile extends WitcherBolt {

    private static final ItemStack DEFAULT_STACK = new ItemStack(TCOTS_Items.KNIGHT_CROSSBOW.get());

    public BaseBoltProjectile(final EntityType<? extends BaseBoltProjectile> entityType, final Level world) {
        super(entityType, world);
    }

    public BaseBoltProjectile(final Level world, final LivingEntity owner, final ItemStack stack, @Nullable final ItemStack weapon) {
        super(TCOTS_Entities.BaseBolt(), owner, world, stack, weapon);
        setBaseDamage(3);
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return DEFAULT_STACK;
    }
}
