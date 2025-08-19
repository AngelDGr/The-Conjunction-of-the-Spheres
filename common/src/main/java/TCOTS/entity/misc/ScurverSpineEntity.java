package TCOTS.entity.misc;

import TCOTS.registry.TCOTS_Entities;
import TCOTS.registry.TCOTS_Items;
import TCOTS.registry.TCOTS_Effects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScurverSpineEntity extends AbstractArrow {
    private static final ItemStack DEFAULT_STACK = new ItemStack(TCOTS_Items.SCURVER_SPINE.get());

    public ScurverSpineEntity(final EntityType<? extends ScurverSpineEntity> entityType, final Level world) {
        super(entityType, world);
    }

    public ScurverSpineEntity(final Level world, final double x, final double y, final double z, final ItemStack stack, @Nullable final ItemStack shotFrom) {
        super(TCOTS_Entities.ScurverSpine(), x, y, z, world, stack, shotFrom);
    }

    protected ScurverSpineEntity(final EntityType<? extends AbstractArrow> type, final double x, final double y, final double z, final Level world, final ItemStack stack, @Nullable final ItemStack shotFrom) {
        super(type, x, y, z, world, stack, shotFrom);
    }

    public ScurverSpineEntity(final LivingEntity owner, final Level world, final ItemStack stack, @Nullable final ItemStack shotFrom) {
        super(TCOTS_Entities.ScurverSpine(), owner, world, stack, shotFrom);
        this.setOwner(owner);
        if (owner instanceof Player) {
            this.pickup = Pickup.ALLOWED;
        }
        setBaseDamage(4);
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return DEFAULT_STACK;
    }

    int life=0;

    @Override
    protected void doPostHurtEffects(final LivingEntity target) {
        final Entity entity = this.getEffectSource();
        target.addEffect(new MobEffectInstance(TCOTS_Effects.Bleeding(), 200, 0, false, false, true), entity);
    }



    protected void tickDespawn() {
        if(!(this.getOwner() instanceof Player)){
            ++this.life;
            if (this.life >= 200) {
                this.discard();
            }
        }
        super.tickDespawn();
    }


    @Override
    protected float getWaterInertia() {
        return 0.8f;
    }
}
