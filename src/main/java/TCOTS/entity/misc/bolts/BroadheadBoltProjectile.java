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

public class BroadheadBoltProjectile extends WitcherBolt {
    private static final ItemStack DEFAULT_STACK = new ItemStack(TCOTS_Items.BROADHEAD_BOLT);
    public BroadheadBoltProjectile(EntityType<? extends BroadheadBoltProjectile> entityType, World world) {
        super(entityType, world);
    }

    public BroadheadBoltProjectile(World world, LivingEntity owner) {
        super(TCOTS_Entities.BROADHEAD_BOLT, owner, world);
        setDamage(2.8);
    }

    @Override
    protected void onHit(LivingEntity target) {
        Entity entity = this.getEffectCause();
        target.addStatusEffect(new StatusEffectInstance(TCOTS_Effects.BLEEDING, 15*20, 0, false, false, true),entity);
    }

    @Override
    protected ItemStack asItemStack() {
        return DEFAULT_STACK.copy();
    }
}
