package TCOTS.entity.misc;

import TCOTS.entity.TCOTS_Entities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.UUID;


public class DrownerPuddleEntity extends Entity implements GeoEntity, Ownable {
    @Nullable
    private Entity owner;
    @Nullable
    private UUID ownerUuid;

    public static final RawAnimation PUDDLE_SPAWN = RawAnimation.begin().thenPlayAndHold("misc.puddleSpawn");
    public static final RawAnimation PUDDLE_DESPAWN = RawAnimation.begin().thenPlayAndHold("misc.puddleDespawn");

    protected static final TrackedData<Boolean> SPAWN_CONTROL = DataTracker.registerData(DrownerPuddleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public DrownerPuddleEntity(EntityType<? extends DrownerPuddleEntity> entity, World world) {
        super(entity, world);
    }


    public DrownerPuddleEntity(World world, double x, double y, double z, LivingEntity owner) {
        this(TCOTS_Entities.DROWNER_PUDDLE, world);
        this.setOwner(owner);
        this.setPosition(x, y, z);
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        this.ownerUuid = owner == null ? null : owner.getUuid();
    }
    @Nullable
    @Override
    public Entity getOwner() {
        return owner;
    }
    @Nullable
    public UUID getOwnerUUID() {
        return ownerUuid;
    }

    public boolean getSpawnControlDataTracker() {
        return this.dataTracker.get(SPAWN_CONTROL);
    }
    public void setSpawnControlDataTracker(boolean isSpawned) {
        this.dataTracker.set(SPAWN_CONTROL, isSpawned);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(SPAWN_CONTROL, Boolean.TRUE);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner");
        }
        this.setSpawnControlDataTracker(nbt.getBoolean("Spawned"));

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid);
        }
        nbt.putBoolean("Spawned",this.dataTracker.get(SPAWN_CONTROL));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        //Spawner Controller
        controllers.add(
                new AnimationController<>(this, "SpawnController", 1, state -> {
                    if (this.getSpawnControlDataTracker()){
                        state.setAnimation(PUDDLE_SPAWN);
                    }
                    else{
                        state.setAnimation(PUDDLE_DESPAWN);
                    }
                    return PlayState.CONTINUE;
                })
        );
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


}
