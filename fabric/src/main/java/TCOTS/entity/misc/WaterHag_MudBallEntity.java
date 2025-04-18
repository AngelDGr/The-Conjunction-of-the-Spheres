package TCOTS.entity.misc;

import TCOTS.entity.TCOTS_Entities_Fabric;
import TCOTS.entity.necrophages.DrownerEntity;
import TCOTS.entity.necrophages.WaterHagEntity;
import TCOTS.items.TCOTS_Items_Fabric;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class WaterHag_MudBallEntity extends ThrowableItemProjectile {

    private float damage=1;
    public WaterHag_MudBallEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
        super(entityType, world);
    }

    public WaterHag_MudBallEntity(Level world, double x, double y, double z) {
        super(TCOTS_Entities_Fabric.WATER_HAG_MUD_BALL, x, y, z, world);
    }

    public WaterHag_MudBallEntity(Level world, LivingEntity owner, float damage) {
        super(TCOTS_Entities_Fabric.WATER_HAG_MUD_BALL, owner, world);
        this.damage=damage;
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return TCOTS_Items_Fabric.WATER_HAG_MUD_BALL;
    }

    @Override
    public void handleEntityEvent(byte status) {
        if (status == EntityEvent.DEATH) {
            for (int i = 0; i < 8; ++i) {
                this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.MUD.defaultBlockState()), this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);

        Entity entity = entityHitResult.getEntity();
        if(entity instanceof Player){
         ((Player) entity).theConjunctionOfTheSpheres$setMudInFace(140);
        }
        this.playSound(TCOTS_Sounds.WATER_HAG_MUD_BALL_HIT, 1, 1);
        float i = entity instanceof WaterHagEntity || entity instanceof DrownerEntity ? 0 : damage;
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), i);
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide) {
            this.playSound(TCOTS_Sounds.WATER_HAG_MUD_BALL_HIT, 1, 1);
            this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
            this.discard();
        }
    }


}
