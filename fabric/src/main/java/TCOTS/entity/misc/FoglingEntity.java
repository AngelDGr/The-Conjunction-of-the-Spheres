package TCOTS.entity.misc;

import TCOTS.entity.necrophages.FogletEntity;
import TCOTS.items.concoctions.bombs.NorthernWindBomb;
import TCOTS.sounds.TCOTS_Sounds;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FoglingEntity extends FogletEntity implements GeoEntity, TraceableEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Nullable
    Mob owner;
    @Nullable
    private UUID ownerUuid;

    protected static final EntityDataAccessor<Float> ALPHA_VALUE_FOGLING = SynchedEntityData.defineId(FoglingEntity.class, EntityDataSerializers.FLOAT);

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FogletEntity.Foglet_Swim(this));

        this.goalSelector.addGoal(1, new FogletEntity.Foglet_AttackWithFog<>(this, 1.0, false,60));

        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.75, 200));

        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));

        //Objectives
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, FogletEntity.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public FoglingEntity(EntityType<? extends FoglingEntity> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0f) //Amount of health that hurts you
                .add(Attributes.MOVEMENT_SPEED, 0.21f);
    }

    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    public static final byte DEATH_FOGLING_EFFECTS = 43;
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(amount>0){
            if(!this.level().isClientSide) {
                this.level().broadcastEntityEvent(this, DEATH_FOGLING_EFFECTS);
                this.playSound(TCOTS_Sounds.FOGLET_FOGLING_DISAPPEAR, 1.0f, 1.0f);
                this.dead = true;
                this.remove(Entity.RemovalReason.KILLED);
            }
        }

        return super.hurt(source, amount);
    }

    @Override
    public void handleEntityEvent(byte status) {
        if(status == DEATH_FOGLING_EFFECTS){
            this.vanishParticles();
        }
        else {
            super.handleEntityEvent(status);
        }
    }

    private void vanishParticles(){
        for (int i = 0; i < 10; i++) {
            double d = this.getX() + (double) Mth.randomBetween(this.getRandom(), -0.8F, 0.8F);
            double e = (this.getEyeY()-0.5f)+ (double) Mth.randomBetween(this.getRandom(), -1F, 1F);
            double f = this.getZ() + (double) Mth.randomBetween(this.getRandom(), -0.8F, 0.8F);
            this.level().addParticle(ParticleTypes.CLOUD, d,e,f,0,0,0);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ALPHA_VALUE_FOGLING, 0f);
    }

    public final float getAlphaValue() {
        return this.entityData.get(ALPHA_VALUE_FOGLING);
    }

    public final void setAlphaValue(float AlphaValue) {
        this.entityData.set(ALPHA_VALUE_FOGLING, AlphaValue);
    }


    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putFloat("AlphaValue", this.entityData.get(ALPHA_VALUE_FOGLING));
        if (this.ownerUuid != null) {
            nbt.putUUID("Owner", this.ownerUuid);
        }
    }
    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        this.setAlphaValue(nbt.getFloat("AlphaValue"));
        if (nbt.hasUUID("Owner")) {
            this.ownerUuid = nbt.getUUID("Owner");
        }
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public void tick() {
        if(NorthernWindBomb.checkEffect(this))
        {hurt(this.damageSources().freeze(), 1);}
        super.tick();
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Nullable
    @Override
    public Entity getOwner() {
        Entity entity;
        if (this.owner == null && this.ownerUuid != null && this.level() instanceof ServerLevel && (entity = ((ServerLevel)this.level()).getEntity(this.ownerUuid)) instanceof LivingEntity) {
            this.owner = (Mob) entity;
        }
        return this.owner;
    }

    public void setOwner(@Nullable Mob owner) {
        this.owner = owner;
        this.ownerUuid = owner == null ? null : owner.getUUID();
    }
}
