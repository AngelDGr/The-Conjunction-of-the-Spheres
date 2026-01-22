package mors.tcots.entity.misc;

import mors.tcots.registry.TCOTS_Sounds;
import mors.tcots.registry.TCOTS_Entities;
import mors.tcots.entity.monsters.ogroids.IceGiantEntity;
import mors.tcots.items.weapons.GiantAnchorItem;
import mors.tcots.utils.TCOTS_EntitiesUtil;
import mors.tcots.utils.GeckoAnimationsUtil;
import mors.tcots.registry.TCOTS_DamageTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AnchorProjectileEntity extends Projectile implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected static final EntityDataAccessor<Float> FALLING_DISTANCE = SynchedEntityData.defineId(AnchorProjectileEntity.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Boolean> ENCHANTED = SynchedEntityData.defineId(AnchorProjectileEntity.class, EntityDataSerializers.BOOLEAN);

    public AbstractArrow.Pickup pickupType = AbstractArrow.Pickup.DISALLOWED;
    @Nullable
    private BlockState inBlockState;
    protected boolean inGround;
    protected int inGroundTime;
    public int shake;
    private double damage = 8.0;
    private SoundEvent sound = this.getHitSound();
    public boolean dealtDamage;
    public float bodyYaw;
    public float prevBodyYaw;
    public AnchorProjectileEntity(final EntityType<? extends Projectile> entityType, final Level world) {
        super(entityType, world);
    }

    public AnchorProjectileEntity(final LivingEntity thrower, final Level world) {
        this(TCOTS_Entities.AnchorProjectile(), world);
        this.setOwner(thrower);
        this.setPos(thrower.getX(), thrower.getEyeY(), thrower.getZ());
        this.pickupType= AbstractArrow.Pickup.DISALLOWED;
    }

    public void setSound(final SoundEvent sound) {
        this.sound = sound;
    }

    @Override
    protected void defineSynchedData(final SynchedEntityData.Builder builder) {
        builder.define(FALLING_DISTANCE, fallDistance);
        builder.define(ENCHANTED, false);
    }

    public void setFallingDistance(final float fallingDistance) {
        this.entityData.set(FALLING_DISTANCE, fallingDistance);
    }

    public float getFallingDistance() {
        return this.entityData.get(FALLING_DISTANCE);
    }

    public void setEnchanted(final boolean enchanted) {
        this.entityData.set(ENCHANTED, enchanted);
    }

    public boolean isEnchanted() {
        return this.entityData.get(ENCHANTED);
    }

    @Override
    public void shoot(final double x, final double y, final double z, final float speed, final float divergence) {
        super.shoot(x, y, z, speed, divergence);
    }

    @Override
    public void lerpMotion(final double x, final double y, final double z) {
        super.lerpMotion(x, y, z);
    }
    //xTODO: Fix the falling distance


    private void returnToOwnerLogic(){
        if(this.getOwner()==null || !(this.getOwner() instanceof final LivingEntity owner)) return;

        //If the owner reach the distance limit
        if(this.distanceTo(owner)>20){
            GiantAnchorItem.retrieveAnchor(owner);
        }

        //If the owner doesn't have an Anchor in hand
        if((!(owner.getMainHandItem().getItem() instanceof GiantAnchorItem)
                && !(owner.getOffhandItem().getItem() instanceof GiantAnchorItem))
                && (owner.blockPosition().getCenter()!=this.blockPosition().getCenter())){
            GiantAnchorItem.retrieveAnchor(owner);
        }

        //If the owner it's far and was already launched
        if(this.distanceTo(owner) > 8 && this.onGround()) {
            GiantAnchorItem.retrieveAnchor(owner);
        }

    }

    //xTODO: Fix the immediate respawn bug-Unnecessary
    @SuppressWarnings("all")
    @Override
    public void tick() {
        //8 Damage outside water
        //12 Damage inside water
        if(this.getOwner()!=null && !(this.getOwner() instanceof IceGiantEntity)) this.setDamage(this.isUnderWater()? 12.0f: 8.0f);

        if(!this.level().isClientSide && (this.getOwner()==null || (this.getOwner()!=null && !this.getOwner().isAlive()))) this.discard();

        //To retrieve the anchor if it's near its owner
        if(this.getOwner()!=null && this.pickupType.equals(AbstractArrow.Pickup.ALLOWED)){

            List<LivingEntity> ownerDetection= this.level().getEntitiesOfClass(LivingEntity.class,
                    this.getBoundingBox().inflate(0.5, 0.5, 0.5),
                    entity->entity==this.getOwner() && !(entity instanceof IceGiantEntity));

            if(!ownerDetection.isEmpty()) {
                this.discard();
            }

        }

        //To sync fallDistance with the client
        setFallingDistance(fallDistance);


        if(!this.onGround() || this.getDeltaMovement().horizontalDistanceSqr() > (double)1.0E-5f){
            this.move(MoverType.SELF, this.getDeltaMovement());
        }

        this.returnToOwnerLogic();


        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }
        Vec3 vec3d2;
        VoxelShape voxelShape;
        super.tick();
        Vec3 vec3d = this.getDeltaMovement();
        if (this.xRotO == 0.0f && this.yRotO == 0.0f) {
            double d = vec3d.horizontalDistance();
            this.setYRot((float)(Mth.atan2(vec3d.x, vec3d.z) * 57.2957763671875));
            this.setXRot((float)(Mth.atan2(vec3d.y, d) * 57.2957763671875));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }
        BlockPos blockPos = this.blockPosition();
        BlockState blockState = this.level().getBlockState(blockPos);
        if (!(blockState.isAir() || (voxelShape = blockState.getCollisionShape(this.level(), blockPos)).isEmpty())) {
            vec3d2 = this.position();
            for (AABB box : voxelShape.toAabbs()) {
                if (!box.move(blockPos).contains(vec3d2)) continue;
                this.inGround = true;
                break;
            }
        }
        if (this.shake > 0) {
            --this.shake;
        }
        if (this.inGround) {
            if (this.inBlockState != blockState && this.shouldContinueFall()) {
                this.fall();
            }
            ++this.inGroundTime;
            return;
        }
        this.inGroundTime = 0;
        Vec3 vec3d3 = this.position();
        vec3d2 = vec3d3.add(vec3d);
        HitResult hitResult = this.level().clip(new ClipContext(vec3d3, vec3d2, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hitResult.getType() != HitResult.Type.MISS) {
            vec3d2 = hitResult.getLocation();
        }
        while (!this.isRemoved()) {
            EntityHitResult entityHitResult = this.getEntityCollision(vec3d3, vec3d2);
            if (entityHitResult != null) {
                hitResult = entityHitResult;
            }
            if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY && hitResult instanceof EntityHitResult) {
                Entity entity = ((EntityHitResult)hitResult).getEntity();
                Entity entity2 = this.getOwner();
                if (entity instanceof Player && entity2 instanceof Player && !((Player)entity2).canHarmPlayer((Player)entity)) {
                    hitResult = null;
                    entityHitResult = null;
                }
            }
            if (hitResult != null) {
                this.onHit(hitResult);
                this.hasImpulse = true;
            }
            if (entityHitResult == null) break;
            hitResult = null;
        }
        vec3d = this.getDeltaMovement();
        double e = vec3d.x;
        double f = vec3d.y;
        double g = vec3d.z;
        double h = this.getX() + e;
        double j = this.getY() + f;
        double k = this.getZ() + g;
        double l = vec3d.horizontalDistance();

        this.setYRot((float)(Mth.atan2(e, g) * 57.2957763671875));

        this.setXRot((float)(Mth.atan2(f, l) * 57.2957763671875));
        this.setXRot(AbstractArrow.lerpRotation(this.xRotO, this.getXRot()));
        this.setYRot(AbstractArrow.lerpRotation(this.yRotO, this.getYRot()));
        float m = 0.99f;
//        float n = 0.05f;
        if (this.isInWater()) {
            for (int o = 0; o < 4; ++o) {
                float p = 0.25f;
                this.level().addParticle(ParticleTypes.BUBBLE, h - e * p, j - f * p, k - g * p, e, f, g);
            }
            m = this.getDragInWater();
        }
        this.setDeltaMovement(vec3d.scale(m));
        if (!this.isNoGravity()) {
            Vec3 vec3d4 = this.getDeltaMovement();
            this.setDeltaMovement(vec3d4.x, vec3d4.y - (double)0.05f, vec3d4.z);
        }


        this.setPos(h, j, k);
        this.checkInsideBlocks();

        this.prevBodyYaw = this.bodyYaw;

        while (this.bodyYaw - this.prevBodyYaw < -180.0f) {
            this.prevBodyYaw -= 360.0f;
        }
        while (this.bodyYaw - this.prevBodyYaw >= 180.0f) {
            this.prevBodyYaw += 360.0f;
        }
    }

    private boolean shouldContinueFall() {
        return this.inGround && this.level().noCollision(new AABB(this.position(), this.position()).inflate(0.06));
    }

    private void fall() {
        this.inGround = false;
        final Vec3 vec3d = this.getDeltaMovement();
        this.setDeltaMovement(vec3d.multiply(this.random.nextFloat() * 0.2f, this.random.nextFloat() * 0.2f, this.random.nextFloat() * 0.2f));
    }


    @Override
    public void move(@NotNull final MoverType movementType, @NotNull final Vec3 movement) {
        super.move(movementType, movement);

        if (movementType != MoverType.SELF && this.shouldContinueFall()) {
            this.fall();
        }
    }

    @Override
    protected void onHitEntity(final EntityHitResult entityHitResult) {
        final Entity entity = entityHitResult.getEntity();
        final Entity entity2 = this.getOwner();
        final DamageSource damageSource = TCOTS_DamageTypes.anchorDamage(this.level(), this, entity2 == null ? this : entity2);
        this.dealtDamage = true;
        if (entity.hurt(damageSource, this.getDamage())) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (this.level() instanceof final ServerLevel serverWorld) {
                EnchantmentHelper.doPostAttackEffectsWithItemSource(serverWorld, entity, damageSource, this.getWeaponItem());
            }

            if (entity instanceof final LivingEntity livingEntity) {
                this.knockback(livingEntity, damageSource);
                this.onHit(livingEntity);
            }
        } else if (entity.getType().is(EntityTypeTags.DEFLECTS_PROJECTILES)) {
            this.deflect();
            return;
        }
        this.playSound(TCOTS_Sounds.getSoundEvent("anchor_impact"), 1.0f, 1.0f);
    }

    public void deflect() {
        final float f = this.random.nextFloat() * 360.0f;
        this.setDeltaMovement(this.getDeltaMovement().yRot(f * ((float)Math.PI / 180)).scale(0.5));
        this.setYRot(this.getYRot() + f);
        this.yRotO += f;
    }


    @Override
    protected void onHitBlock(final BlockHitResult blockHitResult) {
        this.inBlockState = this.level().getBlockState(blockHitResult.getBlockPos());
        super.onHitBlock(blockHitResult);
        final Vec3 vec3d = blockHitResult.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vec3d);
        final Vec3 vec3d2 = vec3d.normalize().scale(0.05f);
        this.setPosRaw(this.getX() - vec3d2.x, this.getY() - vec3d2.y, this.getZ() - vec3d2.z);

        this.playSound(this.getSound(), 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));

        this.inGround = true;
        this.shake = 7;
        this.setSound(TCOTS_Sounds.getSoundEvent("anchor_impact"));
    }
    private static final byte FALLING_PARTICLES = 42;
    private void groundAttack(){
        if(this.level().isClientSide || !this.onGround() || dealtDamage){
            return;
        }

        this.setDeltaMovement(Vec3.ZERO);

        TCOTS_EntitiesUtil.pushAndDamageEntities(
                this,
                //Damage
                Math.min(this.getDamage() + (this.getFallingDistance()*0.5f), 50),
                0.8f + (this.getFallingDistance() * 0.5f),
                3,
                1.5,
                TCOTS_DamageTypes.anchorDamage(this.level(), this, this.getOwner() == null ? this : this.getOwner()),
                AnchorProjectileEntity.class,
                this.getOwner().getClass());

        this.playSound(TCOTS_Sounds.getSoundEvent("big_impact"), 1.0f, 1.0f);

        this.level().broadcastEntityEvent(this, FALLING_PARTICLES);

        this.dealtDamage=true;
    }

    @Override
    public void resetFallDistance() {
        this.groundAttack();

        super.resetFallDistance();
    }

    @Override
    public void handleEntityEvent(final byte status) {
        if(status==FALLING_PARTICLES){
            final double radius=0.8f + (this.getFallingDistance() * 0.5f);
            TCOTS_EntitiesUtil.spawnImpactParticles(this,
                    radius,
                    this.getFallingDistance(),
                    Math.max(20 + this.getFallingDistance(), 2 * Math.PI * radius));
        } else{
            super.handleEntityEvent(status);
        }
    }

    protected SoundEvent getHitSound() {
        return TCOTS_Sounds.getSoundEvent("anchor_impact");
    }

    protected final SoundEvent getSound() {
        return this.sound;
    }

    protected void knockback(final LivingEntity target, final DamageSource source) {
//        double d = (double)(
//                this.weapon != null && this.getWorld() instanceof ServerWorld serverWorld
//                        ? EnchantmentHelper.modifyKnockback(serverWorld, this.weapon, target, source, 0.0F)
//                        : 0.0F
//        );
//        if (d > 0.0) {
//            double e = Math.max(0.0, 1.0 - target.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
//            Vec3d vec3d = this.getVelocity().multiply(1.0, 0.0, 1.0).normalize().multiply(d * 0.6 * e);
//            if (vec3d.lengthSquared() > 0.0) {
//                target.addVelocity(vec3d.x, 0.1, vec3d.z);
//            }
//        }
    }

    protected void onHit(final LivingEntity target) {
    }

    @Nullable
    protected EntityHitResult getEntityCollision(final Vec3 currentPosition, final Vec3 nextPosition) {
        if (this.dealtDamage) {
            return null;
        }
        return ProjectileUtil.getEntityHitResult(this.level(), this, currentPosition, nextPosition, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0), this::canHitEntity);
    }

    @Override
    protected boolean canHitEntity(@NotNull final Entity entity) {
        return super.canHitEntity(entity);
    }

    @Override
    public void addAdditionalSaveData(@NotNull final CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        if (this.inBlockState != null) {
            nbt.put("inBlockState", NbtUtils.writeBlockState(this.inBlockState));
        }
        nbt.putByte("shake", (byte)this.shake);
        nbt.putBoolean("inGround", this.inGround);
        nbt.putDouble("damage", this.damage);
        nbt.putString("SoundEvent", Objects.requireNonNull(BuiltInRegistries.SOUND_EVENT.getKey(this.sound)).toString());
        nbt.putBoolean("DealtDamage", this.dealtDamage);

        nbt.putByte("pickup", (byte)this.pickupType.ordinal());

        nbt.putBoolean("IsEnchanted", isEnchanted());
    }

    @Override
    public void readAdditionalSaveData(@NotNull final CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("inBlockState", Tag.TAG_COMPOUND)) {
            this.inBlockState = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), nbt.getCompound("inBlockState"));
        }
        this.shake = nbt.getByte("shake") & 0xFF;
        this.inGround = nbt.getBoolean("inGround");
        if (nbt.contains("damage", Tag.TAG_ANY_NUMERIC)) {
            this.damage = nbt.getDouble("damage");
        }
        if (nbt.contains("SoundEvent", Tag.TAG_STRING)) {
            this.sound = BuiltInRegistries.SOUND_EVENT.getOptional(ResourceLocation.parse(nbt.getString("SoundEvent"))).orElse(this.getHitSound());
        }
        this.dealtDamage = nbt.getBoolean("DealtDamage");

        this.pickupType = AbstractArrow.Pickup.byOrdinal(nbt.getByte("pickup"));

        this.setEnchanted(nbt.getBoolean("IsEnchanted"));
    }

    @Override
    public void setOwner(@Nullable final Entity entity) {
        super.setOwner(entity);
        setPlayerAnchor(this);
    }

    private void setPlayerAnchor(@Nullable final AnchorProjectileEntity anchor) {
        if(this.getOwner()==null || !(this.getOwner() instanceof final LivingEntity livingEntity)){
            return;
        }

        livingEntity.tcots$setAnchor(anchor);
    }

    @Override
    public void onClientRemoval() {
        this.setPlayerAnchor(null);
    }

    @Override
    public void remove(@NotNull final RemovalReason reason) {
        this.setPlayerAnchor(null);
        super.remove(reason);
    }

    @Override
    public void playerTouch(@NotNull final Player player) {
        if (this.level().isClientSide || !this.inGround || this.shake > 0) {
            return;
        }

        if(this.getOwner()==player) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), TCOTS_Sounds.getSoundEvent("anchor_chain"), player.getSoundSource(), 1.0f, 1.0f);

            this.discard();
        }
    }
    @Override
    protected Entity.@NotNull MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    public void setDamage(final double damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return (float) this.damage;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    protected float getDragInWater() {
        return 0.8f;
    }

    @Override
    public boolean shouldRender(final double cameraX, final double cameraY, final double cameraZ) {
        return true;
    }


    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(GeckoAnimationsUtil.genericIdleController(this));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
