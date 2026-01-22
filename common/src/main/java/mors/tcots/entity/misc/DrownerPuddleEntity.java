package mors.tcots.entity.misc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;

public class DrownerPuddleEntity extends Entity implements GeoEntity, TraceableEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Nullable
    private Entity owner;
    @Nullable
    private UUID ownerUuid;

    public static final RawAnimation PUDDLE_DESPAWN = RawAnimation.begin().thenPlayAndHold("misc.despawn");

    protected static final EntityDataAccessor<Boolean> DESPAWN_PUDDLE = SynchedEntityData.defineId(DrownerPuddleEntity.class, EntityDataSerializers.BOOLEAN);

    public DrownerPuddleEntity(final EntityType<? extends DrownerPuddleEntity> entity, final Level world) {
        super(entity, world);
    }

    public DrownerPuddleEntity(final EntityType<? extends DrownerPuddleEntity> entity, final Level world, final double x, final double y, final double z, final LivingEntity owner) {
        this(entity, world);
        this.setOwner(owner);
        this.setPos(x, y, z);
    }


    public void setOwner(@Nullable final LivingEntity owner) {
        this.owner = owner;
        this.ownerUuid = owner == null ? null : owner.getUUID();
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

    public boolean getDespawnPuddle() {
        return this.entityData.get(DESPAWN_PUDDLE);
    }
    public void setDespawnPuddle(final boolean isSpawned) {
        this.entityData.set(DESPAWN_PUDDLE, isSpawned);
    }


    @Override
    protected void defineSynchedData(final SynchedEntityData.Builder builder) {
        builder.define(DESPAWN_PUDDLE, Boolean.FALSE);
    }

    @Override
    protected void addAdditionalSaveData(@NotNull final CompoundTag nbt) {
        if (this.ownerUuid != null) {
            nbt.putUUID("Owner", this.ownerUuid);
        }
        nbt.putBoolean("Despawning",this.entityData.get(DESPAWN_PUDDLE));
    }

    @Override
    protected void readAdditionalSaveData(final CompoundTag nbt) {
        if (nbt.hasUUID("Owner")) {
            this.ownerUuid = nbt.getUUID("Owner");
        }
        this.setDespawnPuddle(nbt.getBoolean("Despawning"));
    }


    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {

        controllers.add(DefaultAnimations.getSpawnController(this, AnimationState::getAnimatable,  36));

        //Despawn Controller
        controllers.add(
                new AnimationController<>(this, "DespawnController", 1, state -> {
                    if(this.getDespawnPuddle()) {
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
