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
    private ItemStack stack = this.getDefaultPickupItem();
    @Nullable
    private ItemStack weapon = null;
    protected WitcherBolt(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
    }

    protected WitcherBolt(EntityType<? extends AbstractArrow> type, double x, double y, double z, Level world, ItemStack stack, @Nullable ItemStack weapon) {
        this(type, world);
        this.stack = stack.copy();
        this.setCustomName(stack.get(DataComponents.CUSTOM_NAME));
        Unit unit = stack.remove(DataComponents.INTANGIBLE_PROJECTILE);
        if (unit != null) {
            this.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }

        this.setPos(x, y, z);
        if (weapon != null && world instanceof ServerLevel serverWorld) {
            if (weapon.isEmpty()) {
                throw new IllegalArgumentException("Invalid weapon firing an arrow");
            }

            this.weapon = weapon.copy();
            int i = EnchantmentHelper.getPiercingCount(serverWorld, weapon, this.stack);
            if (i > 0) {
                this.setPierceLevel((byte)i);
            }

            EnchantmentHelper.onProjectileSpawned(serverWorld, weapon, this, item -> this.weapon = null);
        }
    }

    protected WitcherBolt(EntityType<? extends AbstractArrow> type, LivingEntity owner, Level world, ItemStack stack, @Nullable ItemStack shotFrom) {
        this(type, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), world, stack, shotFrom);
        this.setOwner(owner);
    }

//    protected WitcherBolt(EntityType<? extends WitcherBolt> type, World world, ItemStack stack) {
//        super(type, world, stack);
//    }
//    protected WitcherBolt(EntityType<? extends WitcherBolt> type, double x, double y, double z, World world, ItemStack stack) {
//        this(type, world, stack);
//        this.setPosition(x, y, z);
//    }
//    protected WitcherBolt(EntityType<? extends WitcherBolt> type, LivingEntity owner, World world, ItemStack stack) {
//        this(type, owner.getX(), owner.getEyeY() - (double)0.1f, owner.getZ(), world, stack);
//        this.setOwner(owner);
//        if (owner instanceof PlayerEntity) {
//            this.pickupType = PickupPermission.ALLOWED;
//        }
//    }

}
