package TCOTS.entity.misc;


import TCOTS.entity.necrophages.FogletEntity;
import TCOTS.items.potions.bombs.NorthernWindBomb;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;

import java.util.UUID;

public class FoglingEntity extends FogletEntity implements GeoEntity, Ownable {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    @Nullable
    MobEntity owner;
    @Nullable
    private UUID ownerUuid;

    protected static final TrackedData<Float> ALPHA_VALUE_FOGLING = DataTracker.registerData(FoglingEntity.class, TrackedDataHandlerRegistry.FLOAT);

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new FogletEntity.Foglet_Swim(this));

        this.goalSelector.add(1, new FogletEntity.Foglet_AttackWithFog<>(this, 1.0, false,60));

        this.goalSelector.add(2, new WanderAroundGoal(this, 0.75, 200));

        this.goalSelector.add(3, new LookAroundGoal(this));

        //Objectives
        this.targetSelector.add(1, new RevengeGoal(this, FogletEntity.class));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public FoglingEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.21f);
    }

    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if(amount>0){
            if(this.getWorld().isClient){
                vanishParticles();
            }
            if(!this.getWorld().isClient) {
                this.playSound(TCOTS_Sounds.FOGLET_FOGLING_DISAPPEAR, 1.0f, 1.0f);
                this.dead = true;
                this.remove(Entity.RemovalReason.KILLED);
            }
        }

        return super.damage(source, amount);
    }

    private void vanishParticles(){
        for (int i = 0; i < 10; i++) {
            double d = this.getX() + (double) MathHelper.nextBetween(this.getRandom(), -0.8F, 0.8F);
            double e = (this.getEyeY()-0.5f)+ (double) MathHelper.nextBetween(this.getRandom(), -1F, 1F);
            double f = this.getZ() + (double) MathHelper.nextBetween(this.getRandom(), -0.8F, 0.8F);
            this.getWorld().addParticle(ParticleTypes.CLOUD, d,e,f,0,0,0);
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ALPHA_VALUE_FOGLING, 0f);
    }

    public final float getAlphaValue() {
        return this.dataTracker.get(ALPHA_VALUE_FOGLING);
    }

    public final void setAlphaValue(float AlphaValue) {
        this.dataTracker.set(ALPHA_VALUE_FOGLING, AlphaValue);
    }


    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("AlphaValue", this.dataTracker.get(ALPHA_VALUE_FOGLING));
        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid);
        }
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.setAlphaValue(nbt.getFloat("AlphaValue"));
        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner");
        }
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    public void tick() {
        if(NorthernWindBomb.checkEffect(this))
        {damage(this.getDamageSources().freeze(), 1);}
        super.tick();
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public boolean isAffectedBySplashPotions() {
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
        if (this.owner == null && this.ownerUuid != null && this.getWorld() instanceof ServerWorld && (entity = ((ServerWorld)this.getWorld()).getEntity(this.ownerUuid)) instanceof LivingEntity) {
            this.owner = (MobEntity) entity;
        }
        return this.owner;
    }

    public void setOwner(@Nullable MobEntity owner) {
        this.owner = owner;
        this.ownerUuid = owner == null ? null : owner.getUuid();
    }
}
