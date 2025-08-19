package TCOTS.entity.misc;

import TCOTS.registry.TCOTS_Sounds;
import TCOTS.registry.TCOTS_Entities;
import TCOTS.registry.TCOTS_Items;
import TCOTS.entity.monsters.necrophages.DrownerEntity;
import TCOTS.entity.monsters.necrophages.WaterHagEntity;
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
    public WaterHag_MudBallEntity(final EntityType<? extends ThrowableItemProjectile> entityType, final Level world) {
        super(entityType, world);
    }

    public WaterHag_MudBallEntity(final Level world, final double x, final double y, final double z) {
        super(TCOTS_Entities.WaterHagMudBall(), x, y, z, world);
    }

    public WaterHag_MudBallEntity(final Level world, final LivingEntity owner, final float damage) {
        super(TCOTS_Entities.WaterHagMudBall(), owner, world);
        this.damage=damage;
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return TCOTS_Items.WATER_HAG_MUD_BALL.get();
    }

    @Override
    public void handleEntityEvent(final byte status) {
        if (status == EntityEvent.DEATH) {
            for (int i = 0; i < 8; ++i) {
                this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.MUD.defaultBlockState()), this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onHitEntity(@NotNull final EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);

        final Entity entity = entityHitResult.getEntity();
        if(entity instanceof Player){
         ((Player) entity).theConjunctionOfTheSpheres$setMudInFace(140);
        }
        this.playSound(TCOTS_Sounds.getSoundEvent("water_hag_mud_ball_hit"), 1, 1);
        final float i = entity instanceof WaterHagEntity || entity instanceof DrownerEntity ? 0 : damage;
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), i);
    }

    @Override
    protected void onHit(@NotNull final HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide) {
            this.playSound(TCOTS_Sounds.getSoundEvent("water_hag_mud_ball_hit"), 1, 1);
            this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
            this.discard();
        }
    }


}
