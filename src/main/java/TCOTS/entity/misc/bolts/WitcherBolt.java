package TCOTS.entity.misc.bolts;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Unit;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class WitcherBolt extends PersistentProjectileEntity {
    private ItemStack stack = this.getDefaultItemStack();
    @Nullable
    private ItemStack weapon = null;
    protected WitcherBolt(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    protected WitcherBolt(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        this(type, world);
        this.stack = stack.copy();
        this.setCustomName(stack.get(DataComponentTypes.CUSTOM_NAME));
        Unit unit = stack.remove(DataComponentTypes.INTANGIBLE_PROJECTILE);
        if (unit != null) {
            this.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        }

        this.setPosition(x, y, z);
        if (weapon != null && world instanceof ServerWorld serverWorld) {
            if (weapon.isEmpty()) {
                throw new IllegalArgumentException("Invalid weapon firing an arrow");
            }

            this.weapon = weapon.copy();
            int i = EnchantmentHelper.getProjectilePiercing(serverWorld, weapon, this.stack);
            if (i > 0) {
                this.setPierceLevel((byte)i);
            }

            EnchantmentHelper.onProjectileSpawned(serverWorld, weapon, this, item -> this.weapon = null);
        }
    }

    protected WitcherBolt(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
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

    //xTODO: Crossbow Bolts - More damage than a arrow
    //xTODO: Blunt Crossbow Bolts - Even more damage
    //xTODO: Precision Bolt - Extra piercing
    //xTODO: Exploding Bolt - Explodes
    //xTODO: Broadhead Bolt - Causes bleeding



}
