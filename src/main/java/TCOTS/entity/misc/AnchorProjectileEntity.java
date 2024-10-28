package TCOTS.entity.misc;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.ogroids.IceGiantEntity;
import TCOTS.items.weapons.GiantAnchorItem;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.EntitiesUtil;
import TCOTS.utils.GeoControllersUtil;
import TCOTS.world.TCOTS_DamageTypes;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Objects;


public class AnchorProjectileEntity extends ProjectileEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected static final TrackedData<Float> FALLING_DISTANCE = DataTracker.registerData(AnchorProjectileEntity.class, TrackedDataHandlerRegistry.FLOAT);
    protected static final TrackedData<Boolean> ENCHANTED = DataTracker.registerData(AnchorProjectileEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public PersistentProjectileEntity.PickupPermission pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
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
    public AnchorProjectileEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public AnchorProjectileEntity(LivingEntity thrower, World world) {
        this(TCOTS_Entities.ANCHOR_PROJECTILE, world);
        this.setOwner(thrower);
        this.setPosition(thrower.getX(), thrower.getEyeY(), thrower.getZ());
        this.pickupType= PersistentProjectileEntity.PickupPermission.DISALLOWED;
    }

    public void setSound(SoundEvent sound) {
        this.sound = sound;
    }


    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(FALLING_DISTANCE, fallDistance);
        this.dataTracker.startTracking(ENCHANTED, false);
    }

    public void setFallingDistance(float fallingDistance) {
        this.dataTracker.set(FALLING_DISTANCE, fallingDistance);
    }

    public float getFallingDistance() {
        return this.dataTracker.get(FALLING_DISTANCE);
    }

    public void setEnchanted(boolean enchanted) {
        this.dataTracker.set(ENCHANTED, enchanted);
    }

    public boolean isEnchanted() {
        return this.dataTracker.get(ENCHANTED);
    }

    @Override
    public void setVelocity(double x, double y, double z, float speed, float divergence) {
        super.setVelocity(x, y, z, speed, divergence);
    }

    @Override
    public void setVelocityClient(double x, double y, double z) {
        super.setVelocityClient(x, y, z);
    }
    //xTODO: Fix the falling distance


    private void returnToOwnerLogic(){
        if(this.getOwner()==null || !(this.getOwner() instanceof LivingEntity owner)) return;

        //If the owner reach the distance limit
        if(this.distanceTo(owner)>20){
            GiantAnchorItem.retrieveAnchor(owner);
        }

        //If the owner doesn't have an Anchor in hand
        if((!(owner.getMainHandStack().getItem() instanceof GiantAnchorItem)
                && !(owner.getOffHandStack().getItem() instanceof GiantAnchorItem))
                && (owner.getBlockPos().toCenterPos()!=this.getBlockPos().toCenterPos())){
            GiantAnchorItem.retrieveAnchor(owner);
        }

        //If the owner it's far and was already launched
        if(this.distanceTo(owner) > 8 && this.isOnGround()) {
            GiantAnchorItem.retrieveAnchor(owner);
        }

    }

    //xTODO: Fix the immediate respawn bug-Unnecessary
    @Override
    public void tick() {
        //8 Damage outside water
        //12 Damage inside water
        if(this.getOwner()!=null && !(this.getOwner() instanceof IceGiantEntity)) this.setDamage(this.isSubmergedInWater()? 12.0f: 8.0f);

        if(!this.getWorld().isClient && (this.getOwner()==null || (this.getOwner()!=null && !this.getOwner().isAlive()))) this.discard();

        //To retrieve the anchor if it's near its owner
        if(this.getOwner()!=null && this.pickupType.equals(PersistentProjectileEntity.PickupPermission.ALLOWED)){

            List<LivingEntity> ownerDetection= this.getWorld().getEntitiesByClass(LivingEntity.class,
                    this.getBoundingBox().expand(0.5, 0.5, 0.5),
                    entity->entity==this.getOwner() && !(entity instanceof IceGiantEntity));

            if(!ownerDetection.isEmpty()) {
                this.discard();
            }

        }

        //To sync fallDistance with the client
        setFallingDistance(fallDistance);


        if(!this.isOnGround() || this.getVelocity().horizontalLengthSquared() > (double)1.0E-5f){
            this.move(MovementType.SELF, this.getVelocity());
        }

        this.returnToOwnerLogic();


        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }
        Vec3d vec3d2;
        VoxelShape voxelShape;
        super.tick();
        Vec3d vec3d = this.getVelocity();
        if (this.prevPitch == 0.0f && this.prevYaw == 0.0f) {
            double d = vec3d.horizontalLength();
            this.setYaw((float)(MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875));
            this.setPitch((float)(MathHelper.atan2(vec3d.y, d) * 57.2957763671875));
            this.prevYaw = this.getYaw();
            this.prevPitch = this.getPitch();
        }
        BlockPos blockPos = this.getBlockPos();
        BlockState blockState = this.getWorld().getBlockState(blockPos);
        if (!(blockState.isAir() || (voxelShape = blockState.getCollisionShape(this.getWorld(), blockPos)).isEmpty())) {
            vec3d2 = this.getPos();
            for (Box box : voxelShape.getBoundingBoxes()) {
                if (!box.offset(blockPos).contains(vec3d2)) continue;
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
        Vec3d vec3d3 = this.getPos();
        vec3d2 = vec3d3.add(vec3d);
        HitResult hitResult = this.getWorld().raycast(new RaycastContext(vec3d3, vec3d2, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
        if (hitResult.getType() != HitResult.Type.MISS) {
            vec3d2 = hitResult.getPos();
        }
        while (!this.isRemoved()) {
            EntityHitResult entityHitResult = this.getEntityCollision(vec3d3, vec3d2);
            if (entityHitResult != null) {
                hitResult = entityHitResult;
            }
            if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY && hitResult instanceof EntityHitResult) {
                Entity entity = ((EntityHitResult)hitResult).getEntity();
                Entity entity2 = this.getOwner();
                if (entity instanceof PlayerEntity && entity2 instanceof PlayerEntity && !((PlayerEntity)entity2).shouldDamagePlayer((PlayerEntity)entity)) {
                    hitResult = null;
                    entityHitResult = null;
                }
            }
            if (hitResult != null) {
                this.onCollision(hitResult);
                this.velocityDirty = true;
            }
            if (entityHitResult == null) break;
            hitResult = null;
        }
        vec3d = this.getVelocity();
        double e = vec3d.x;
        double f = vec3d.y;
        double g = vec3d.z;
        double h = this.getX() + e;
        double j = this.getY() + f;
        double k = this.getZ() + g;
        double l = vec3d.horizontalLength();

        this.setYaw((float)(MathHelper.atan2(e, g) * 57.2957763671875));

        this.setPitch((float)(MathHelper.atan2(f, l) * 57.2957763671875));
        this.setPitch(PersistentProjectileEntity.updateRotation(this.prevPitch, this.getPitch()));
        this.setYaw(PersistentProjectileEntity.updateRotation(this.prevYaw, this.getYaw()));
        float m = 0.99f;
//        float n = 0.05f;
        if (this.isTouchingWater()) {
            for (int o = 0; o < 4; ++o) {
                float p = 0.25f;
                this.getWorld().addParticle(ParticleTypes.BUBBLE, h - e * p, j - f * p, k - g * p, e, f, g);
            }
            m = this.getDragInWater();
        }
        this.setVelocity(vec3d.multiply(m));
        if (!this.hasNoGravity()) {
            Vec3d vec3d4 = this.getVelocity();
            this.setVelocity(vec3d4.x, vec3d4.y - (double)0.05f, vec3d4.z);
        }


        this.setPosition(h, j, k);
        this.checkBlockCollision();

        this.prevBodyYaw = this.bodyYaw;

        while (this.bodyYaw - this.prevBodyYaw < -180.0f) {
            this.prevBodyYaw -= 360.0f;
        }
        while (this.bodyYaw - this.prevBodyYaw >= 180.0f) {
            this.prevBodyYaw += 360.0f;
        }
    }

    private boolean shouldContinueFall() {
        return this.inGround && this.getWorld().isSpaceEmpty(new Box(this.getPos(), this.getPos()).expand(0.06));
    }

    private void fall() {
        this.inGround = false;
        Vec3d vec3d = this.getVelocity();
        this.setVelocity(vec3d.multiply(this.random.nextFloat() * 0.2f, this.random.nextFloat() * 0.2f, this.random.nextFloat() * 0.2f));
    }


    @Override
    public void move(MovementType movementType, Vec3d movement) {
        super.move(movementType, movement);

        if (movementType != MovementType.SELF && this.shouldContinueFall()) {
            this.fall();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        Entity entity2 = this.getOwner();
        DamageSource damageSource = TCOTS_DamageTypes.anchorDamage(this.getWorld(), this, entity2 == null ? this : entity2);
        this.dealtDamage = true;
        if (entity.damage(damageSource, this.getDamage())) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (entity instanceof LivingEntity livingEntity2) {
                if (entity2 instanceof LivingEntity) {
                    EnchantmentHelper.onUserDamaged(livingEntity2, entity2);
                    EnchantmentHelper.onTargetDamaged((LivingEntity)entity2, livingEntity2);
                }
                this.onHit(livingEntity2);
            }
        } else if (entity.getType().isIn(EntityTypeTags.DEFLECTS_TRIDENTS)) {
            this.deflect();
            return;
        }
        this.playSound(TCOTS_Sounds.ANCHOR_IMPACT, 1.0f, 1.0f);
    }

    public void deflect() {
        float f = this.random.nextFloat() * 360.0f;
        this.setVelocity(this.getVelocity().rotateY(f * ((float)Math.PI / 180)).multiply(0.5));
        this.setYaw(this.getYaw() + f);
        this.prevYaw += f;
    }


    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        this.inBlockState = this.getWorld().getBlockState(blockHitResult.getBlockPos());
        super.onBlockHit(blockHitResult);
        Vec3d vec3d = blockHitResult.getPos().subtract(this.getX(), this.getY(), this.getZ());
        this.setVelocity(vec3d);
        Vec3d vec3d2 = vec3d.normalize().multiply(0.05f);
        this.setPos(this.getX() - vec3d2.x, this.getY() - vec3d2.y, this.getZ() - vec3d2.z);

        this.playSound(this.getSound(), 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));

        this.inGround = true;
        this.shake = 7;
        this.setSound(TCOTS_Sounds.ANCHOR_IMPACT);
    }
    private static final byte FALLING_PARTICLES = 42;
    private void groundAttack(){
        if(this.getWorld().isClient || !this.isOnGround() || dealtDamage){
            return;
        }

        this.setVelocity(Vec3d.ZERO);

        EntitiesUtil.pushAndDamageEntities(
                this,
                //Damage
                Math.min(this.getDamage() + (this.getFallingDistance()*0.5f), 50),
                0.8f + (this.getFallingDistance() * 0.5f),
                3,
                1.5,
                TCOTS_DamageTypes.anchorDamage(this.getWorld(), this, this.getOwner() == null ? this : this.getOwner()),
                AnchorProjectileEntity.class,
                this.getOwner().getClass());

        this.playSound(TCOTS_Sounds.BIG_IMPACT, 1.0f, 1.0f);

        this.getWorld().sendEntityStatus(this, FALLING_PARTICLES);

        this.dealtDamage=true;
    }

    @Override
    public void onLanding() {
        this.groundAttack();

        super.onLanding();
    }

    @Override
    public void handleStatus(byte status) {
        if(status==FALLING_PARTICLES){
            double radius=0.8f + (this.getFallingDistance() * 0.5f);
            EntitiesUtil.spawnImpactParticles(this,
                    radius,
                    this.getFallingDistance(),
                    Math.max(20 + this.getFallingDistance(), 2 * Math.PI * radius));
        } else{
            super.handleStatus(status);
        }
    }

    protected SoundEvent getHitSound() {
        return TCOTS_Sounds.ANCHOR_IMPACT;
    }

    protected final SoundEvent getSound() {
        return this.sound;
    }

    protected void onHit(LivingEntity target) {
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        if (this.dealtDamage) {
            return null;
        }
        return ProjectileUtil.getEntityCollision(this.getWorld(), this, currentPosition, nextPosition, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0), this::canHit);
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.inBlockState != null) {
            nbt.put("inBlockState", NbtHelper.fromBlockState(this.inBlockState));
        }
        nbt.putByte("shake", (byte)this.shake);
        nbt.putBoolean("inGround", this.inGround);
        nbt.putDouble("damage", this.damage);
        nbt.putString("SoundEvent", Objects.requireNonNull(Registries.SOUND_EVENT.getId(this.sound)).toString());
        nbt.putBoolean("DealtDamage", this.dealtDamage);

        nbt.putByte("pickup", (byte)this.pickupType.ordinal());

        nbt.putBoolean("IsEnchanted", isEnchanted());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("inBlockState", NbtElement.COMPOUND_TYPE)) {
            this.inBlockState = NbtHelper.toBlockState(this.getWorld().createCommandRegistryWrapper(RegistryKeys.BLOCK), nbt.getCompound("inBlockState"));
        }
        this.shake = nbt.getByte("shake") & 0xFF;
        this.inGround = nbt.getBoolean("inGround");
        if (nbt.contains("damage", NbtElement.NUMBER_TYPE)) {
            this.damage = nbt.getDouble("damage");
        }
        if (nbt.contains("SoundEvent", NbtElement.STRING_TYPE)) {
            this.sound = Registries.SOUND_EVENT.getOrEmpty(new Identifier(nbt.getString("SoundEvent"))).orElse(this.getHitSound());
        }
        this.dealtDamage = nbt.getBoolean("DealtDamage");

        this.pickupType = PersistentProjectileEntity.PickupPermission.fromOrdinal(nbt.getByte("pickup"));

        this.setEnchanted(nbt.getBoolean("IsEnchanted"));
    }

    @Override
    public void setOwner(@Nullable Entity entity) {
        super.setOwner(entity);
        setPlayerAnchor(this);
    }

    private void setPlayerAnchor(@Nullable AnchorProjectileEntity anchor) {
        if(this.getOwner()==null || !(this.getOwner() instanceof LivingEntity livingEntity)){
            return;
        }

        livingEntity.theConjunctionOfTheSpheres$setAnchor(anchor);
    }

    @Override
    public void onRemoved() {
        this.setPlayerAnchor(null);
    }

    @Override
    public void remove(RemovalReason reason) {
        this.setPlayerAnchor(null);
        super.remove(reason);
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if (this.getWorld().isClient || !this.inGround || this.shake > 0) {
            return;
        }

        if(this.getOwner()==player) {
            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), TCOTS_Sounds.ANCHOR_CHAIN, player.getSoundCategory(), 1.0f, 1.0f);

            this.discard();
        }
    }
    @Override
    protected Entity.MoveEffect getMoveEffect() {
        return Entity.MoveEffect.NONE;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return (float) this.damage;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.13f;
    }

//    public void applyEnchantmentEffects(LivingEntity entity, float damageModifier) {
//        int i = EnchantmentHelper.getEquipmentLevel(Enchantments.POWER, entity);
//        this.setDamage((double)(damageModifier * 2.0f) + this.random.nextTriangular((double)this.getWorld().getDifficulty().getId() * 0.11, 0.57425));
//        if (i > 0) {
//            this.setDamage(this.getDamage() + (double)i * 0.5 + 0.5);
//        }
//        if (EnchantmentHelper.getEquipmentLevel(Enchantments.FLAME, entity) > 0) {
//            this.setOnFireFor(100);
//        }
//    }

    protected float getDragInWater() {
        return 0.8f;
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(GeoControllersUtil.genericIdleController(this));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
