package TCOTS.entity.misc.bolts;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.items.TCOTS_Items;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BaseBoltProjectile extends WitcherBolt {

    private static final ItemStack DEFAULT_STACK = new ItemStack(TCOTS_Items.BASE_BOLT);

    public BaseBoltProjectile(EntityType<? extends BaseBoltProjectile> entityType, World world) {
        super(entityType, world);
    }

    public BaseBoltProjectile(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack weapon) {
        super(TCOTS_Entities.BASE_BOLT, owner, world, stack, weapon);
        setDamage(3);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return DEFAULT_STACK;
    }
}
