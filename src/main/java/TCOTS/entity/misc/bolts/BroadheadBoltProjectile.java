package TCOTS.entity.misc.bolts;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.concoctions.TCOTS_Effects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BroadheadBoltProjectile extends WitcherBolt {
    private static final ItemStack DEFAULT_STACK = new ItemStack(TCOTS_Items.BROADHEAD_BOLT);
    public BroadheadBoltProjectile(EntityType<? extends BroadheadBoltProjectile> entityType, World world) {
        super(entityType, world);
    }

    public BroadheadBoltProjectile(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack weapon) {
        super(TCOTS_Entities.BROADHEAD_BOLT, owner, world, stack, weapon);
        setDamage(2.8);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return DEFAULT_STACK;
    }

    @Override
    protected void onHit(LivingEntity target) {
        Entity entity = this.getEffectCause();
        target.addStatusEffect(new StatusEffectInstance(TCOTS_Effects.BLEEDING, 15*20, 0, false, false, true),entity);
    }
}
