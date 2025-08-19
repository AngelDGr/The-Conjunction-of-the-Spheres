package TCOTS.entity.misc.bolts;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class WitcherBolt extends AbstractArrow {
    @Nullable
    private ItemStack weapon = null;
    protected WitcherBolt(final EntityType<? extends AbstractArrow> entityType, final Level world) {
        super(entityType, world);
    }

    protected WitcherBolt(final EntityType<? extends AbstractArrow> type, final double x, final double y, final double z, final Level world, final ItemStack stack, @Nullable final ItemStack weapon) {
        this(type, world);
        final ItemStack stack1 = stack.copy();
        this.setCustomName(stack.get(DataComponents.CUSTOM_NAME));
        final Unit unit = stack.remove(DataComponents.INTANGIBLE_PROJECTILE);
        if (unit != null) {
            this.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }

        this.setPos(x, y, z);
        if (weapon != null && world instanceof final ServerLevel serverWorld) {
            if (weapon.isEmpty()) {
                throw new IllegalArgumentException("Invalid weapon firing an arrow");
            }

            this.weapon = weapon.copy();
            final int i = EnchantmentHelper.getPiercingCount(serverWorld, weapon, stack1);
            if (i > 0) {
                this.setPierceLevel((byte)i);
            }

            EnchantmentHelper.onProjectileSpawned(serverWorld, weapon, this, item -> this.weapon = null);
        }
    }

    protected WitcherBolt(final EntityType<? extends AbstractArrow> type, final LivingEntity owner, final Level world, final ItemStack stack, @Nullable final ItemStack shotFrom) {
        this(type, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), world, stack, shotFrom);
        this.setOwner(owner);
    }

}
