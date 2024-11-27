package TCOTS.entity.misc;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.necrophages.DrownerEntity;
import TCOTS.entity.necrophages.WaterHagEntity;
import TCOTS.items.TCOTS_Items;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class WaterHag_MudBallEntity extends ThrownItemEntity {

    private float damage=1;
    public WaterHag_MudBallEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public WaterHag_MudBallEntity(World world, double x, double y, double z) {
        super(TCOTS_Entities.WATER_HAG_MUD_BALL, x, y, z, world);
    }

    public WaterHag_MudBallEntity(World world, LivingEntity owner, float damage) {
        super(TCOTS_Entities.WATER_HAG_MUD_BALL, owner, world);
        this.damage=damage;
    }

    @Override
    protected Item getDefaultItem() {
        return TCOTS_Items.WATER_HAG_MUD_BALL;
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
            for (int i = 0; i < 8; ++i) {
                this.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.MUD.getDefaultState()), this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        Entity entity = entityHitResult.getEntity();
        if(entity instanceof PlayerEntity){
         ((PlayerEntity) entity).theConjunctionOfTheSpheres$setMudInFace(140);
        }
        this.playSound(TCOTS_Sounds.WATER_HAG_MUD_BALL_HIT, 1, 1);
        float i = entity instanceof WaterHagEntity || entity instanceof DrownerEntity ? 0 : damage;
        entity.damage(this.getDamageSources().thrown(this, this.getOwner()), i);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            this.playSound(TCOTS_Sounds.WATER_HAG_MUD_BALL_HIT, 1, 1);
            this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
            this.discard();
        }
    }


}
