package TCOTS.entity.misc;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.ogroids.AbstractTrollEntity;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class Troll_RockProjectileEntity extends ThrowableItemProjectile {
    private float damage;

    public Troll_RockProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
        super(entityType, world);
    }

    public Troll_RockProjectileEntity(Level world, LivingEntity owner, float damage) {
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
    public void handleEntityEvent(byte status) {
        if (status == EntityEvent.DEATH) {
            for (int i = 0; i < 8; ++i) {
                this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK,
                        this.getOwner()!=null && this.getOwner().getType()==TCOTS_Entities.ICE_TROLL?
                                Blocks.PACKED_ICE.defaultBlockState():
                                Blocks.COBBLESTONE.defaultBlockState()), this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);

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
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 4));
            }

            livingEntity.knockback(this.getOwner()!=null && this.getOwner().getType()==TCOTS_Entities.ICE_TROLL? 0.5: 2.0, d, e);
            //Push the player
            if (entity instanceof ServerPlayer && !((ServerPlayer) entity).isCreative()) {
                ((ServerPlayer) entity).connection.send(new ClientboundSetEntityMotionPacket(entity), null);
            }
        }

        entity.hurt(this.damageSources().thrown(this, this.getOwner()), damage);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide) {
            this.playSound(TCOTS_Sounds.ROCK_PROJECTILE_IMPACT, 1, 1);
            this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
            this.discard();
        }
    }
}
