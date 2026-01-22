package mors.tcots.entity.misc;

import mors.tcots.registry.TCOTS_Sounds;
import mors.tcots.registry.TCOTS_Entities;
import mors.tcots.entity.monsters.ogroids.AbstractTrollEntity;
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
import org.jetbrains.annotations.NotNull;

public class Troll_RockProjectileEntity extends ThrowableItemProjectile {
    private float damage;

    public Troll_RockProjectileEntity(final EntityType<? extends ThrowableItemProjectile> entityType, final Level world) {
        super(entityType, world);
    }

    public Troll_RockProjectileEntity(final Level world, final LivingEntity owner, final float damage) {
        super(TCOTS_Entities.TrollRockProjectile(), owner, world);
        this.damage=damage;
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        if(this.getOwner()==null){
            return Items.COBBLESTONE;
        }

        return this.getOwner().getType() == TCOTS_Entities.IceTroll()? Items.PACKED_ICE: Items.COBBLESTONE;
    }

    @Override
    public void handleEntityEvent(final byte status) {
        if (status == EntityEvent.DEATH) {
            for (int i = 0; i < 8; ++i) {
                this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK,
                        this.getOwner()!=null && this.getOwner().getType()== TCOTS_Entities.IceTroll()?
                                Blocks.PACKED_ICE.defaultBlockState():
                                Blocks.COBBLESTONE.defaultBlockState()), this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onHitEntity(@NotNull final EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);

        final Entity entity = entityHitResult.getEntity();

        this.playSound(TCOTS_Sounds.getSoundEvent("rock_projectile_impact"), 1, 1);


        if(
                //If hits another rock troll, and the thrower hasn't an owner, then it doesn't damage it
                (entity.getType()== TCOTS_Entities.RockTroll() && this.getOwner()!=null
                        && this.getOwner().getType()== TCOTS_Entities.RockTroll() && ((AbstractTrollEntity)(this.getOwner())).getOwner()==null) ||
                //If hits another ice troll, and the thrower hasn't an owner, then it doesn't damage it
                (entity.getType()== TCOTS_Entities.IceTroll() && this.getOwner()!=null
                        && this.getOwner().getType()== TCOTS_Entities.IceTroll() && ((AbstractTrollEntity)(this.getOwner())).getOwner()==null) ||
                //If hits another troll, and the owner it's the same, then it doesn't damage it
                (entity instanceof final AbstractTrollEntity trollHit && this.getOwner()!=null
                        && this.getOwner() instanceof final AbstractTrollEntity trollThrower && trollThrower.getOwner() == trollHit.getOwner())
        )
            return;

        final double d = this.getX() - entity.getX();
        final double e = this.getZ() - entity.getZ();
        if(entity instanceof final LivingEntity livingEntity) {
            if(this.getOwner()!=null && this.getOwner().getType()== TCOTS_Entities.IceTroll()){
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 4));
            }

            livingEntity.knockback(this.getOwner()!=null && this.getOwner().getType()== TCOTS_Entities.IceTroll()? 0.5: 2.0, d, e);
            //Push the player
            if (entity instanceof ServerPlayer && !((ServerPlayer) entity).isCreative()) {
                ((ServerPlayer) entity).connection.send(new ClientboundSetEntityMotionPacket(entity), null);
            }
        }

        entity.hurt(this.damageSources().thrown(this, this.getOwner()), damage);
    }

    @Override
    protected void onHit(@NotNull final HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide) {
            this.playSound(TCOTS_Sounds.getSoundEvent("rock_projectile_impact"), 1, 1);
            this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
            this.discard();
        }
    }
}
