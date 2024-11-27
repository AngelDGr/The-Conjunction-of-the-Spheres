package TCOTS.entity.misc;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.concoctions.TCOTS_Effects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ScurverSpineEntity extends PersistentProjectileEntity {
    private static final ItemStack DEFAULT_STACK = new ItemStack(TCOTS_Items.SCURVER_SPINE);

    public ScurverSpineEntity(EntityType<? extends ScurverSpineEntity> entityType, World world) {
        super(entityType, world, DEFAULT_STACK);
    }

    public ScurverSpineEntity(World world, double x, double y, double z, ItemStack stack) {
        super(TCOTS_Entities.SCURVER_SPINE, x, y, z, world, stack);
    }

    protected ScurverSpineEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack) {
        super(type, x, y, z, world, stack);
    }

    public ScurverSpineEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack) {
        this(type, owner.getX(), owner.getEyeY() - (double)0.1f, owner.getZ(), world, stack);
        this.setOwner(owner);
        if (owner instanceof PlayerEntity) {
            this.pickupType = PickupPermission.ALLOWED;
        }
        setDamage(4);
    }
    int life=0;

    @Override
    protected void onHit(LivingEntity target) {
        Entity entity = this.getEffectCause();
        target.addStatusEffect(new StatusEffectInstance(TCOTS_Effects.BLEEDING, 200, 0, false, false, true), entity);
    }



    protected void age() {
        if(!(this.getOwner() instanceof PlayerEntity)){
            ++this.life;
            if (this.life >= 200) {
                this.discard();
            }
        }
        super.age();
    }


    @Override
    protected float getDragInWater() {
        return 0.8f;
    }
}
