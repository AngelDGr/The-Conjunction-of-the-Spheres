package TCOTS.entity.misc;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.ogroids.AbstractTrollEntity;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class Troll_RockProjectileEntity extends ThrownItemEntity {
    private float damage;

    public Troll_RockProjectileEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public Troll_RockProjectileEntity(World world, LivingEntity owner, float damage) {
        super(TCOTS_Entities.TROLL_ROCK_PROJECTILE, owner, world);
        this.damage=damage;
    }

    @Override
    protected Item getDefaultItem() {
        if(this.getOwner()==null){
            return Items.COBBLESTONE;
        }

        return this.getOwner().getType() == TCOTS_Entities.ICE_TROLL? Items.PACKED_ICE: Items.COBBLESTONE;
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
            for (int i = 0; i < 8; ++i) {
                this.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK,
                        this.getOwner()!=null && this.getOwner().getType()==TCOTS_Entities.ICE_TROLL?
                                Blocks.PACKED_ICE.getDefaultState():
                                Blocks.COBBLESTONE.getDefaultState()), this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        Entity entity = entityHitResult.getEntity();

        this.playSound(TCOTS_Sounds.ROCK_PROJECTILE_IMPACT, 1, 1);


        if(
                //If hits another rock troll, and the thrower hasn't an owner, then it doesn't damage it
                (entity.getType()==TCOTS_Entities.ROCK_TROLL && this.getOwner()!=null
                        && this.getOwner().getType()==TCOTS_Entities.ROCK_TROLL && ((AbstractTrollEntity)(this.getOwner())).getOwner()==null) ||
                //If hits another ice troll, and the thrower hasn't an owner, then it doesn't damage it
                (entity.getType()==TCOTS_Entities.ICE_TROLL && this.getOwner()!=null
                        && this.getOwner().getType()==TCOTS_Entities.ICE_TROLL && ((AbstractTrollEntity)(this.getOwner())).getOwner()==null) ||
                //If hits another troll, and the owner it's the same, then it doesn't damage it
                (entity instanceof AbstractTrollEntity trollHit && this.getOwner()!=null
                        && this.getOwner() instanceof AbstractTrollEntity trollThrower && trollThrower.getOwner() == trollHit.getOwner())
        )
            return;

        double d = this.getX() - entity.getX();
        double e = this.getZ() - entity.getZ();
        if(entity instanceof LivingEntity livingEntity) {
            if(this.getOwner()!=null && this.getOwner().getType()==TCOTS_Entities.ICE_TROLL){
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 4));
            }

            livingEntity.takeKnockback(this.getOwner()!=null && this.getOwner().getType()==TCOTS_Entities.ICE_TROLL? 0.5: 2.0, d, e);
            //Push the player
            if (entity instanceof ServerPlayerEntity && !((ServerPlayerEntity) entity).isCreative()) {
                ((ServerPlayerEntity) entity).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(entity), null);
            }
        }

        entity.damage(this.getDamageSources().thrown(this, this.getOwner()), damage);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            this.playSound(TCOTS_Sounds.ROCK_PROJECTILE_IMPACT, 1, 1);
            this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
            this.discard();
        }
    }
}
