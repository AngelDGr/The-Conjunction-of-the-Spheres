package TCOTS.entity.misc.bolts;

import TCOTS.registry.TCOTS_Effects;
import TCOTS.registry.TCOTS_Entities;
import TCOTS.registry.TCOTS_Items;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BroadheadBoltProjectile extends WitcherBolt {
    private static final ItemStack DEFAULT_STACK = new ItemStack(TCOTS_Items.BROADHEAD_BOLT.get());
    public BroadheadBoltProjectile(EntityType<? extends BroadheadBoltProjectile> entityType, Level world) {
        super(entityType, world);
    }

    public BroadheadBoltProjectile(Level world, LivingEntity owner, ItemStack stack, @Nullable ItemStack weapon) {
        super(TCOTS_Entities.BroadheadBolt(), owner, world, stack, weapon);
        setBaseDamage(2.8);
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return DEFAULT_STACK;
    }

    @Override
    protected void doPostHurtEffects(LivingEntity target) {
        Entity entity = this.getEffectSource();
        target.addEffect(new MobEffectInstance(TCOTS_Effects.Bleeding(), 15*20, 0, false, false, true),entity);
    }
}
