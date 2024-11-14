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
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;


public class DrownerPuddleEntity extends Entity implements GeoEntity, Ownable {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Nullable
    private Entity owner;
    @Nullable
    private UUID ownerUuid;

    public static final RawAnimation PUDDLE_DESPAWN = RawAnimation.begin().thenPlayAndHold("misc.despawn");

    protected static final TrackedData<Boolean> DESPAWN_PUDDLE = DataTracker.registerData(DrownerPuddleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

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

    public boolean getDespawnPuddle() {
        return this.dataTracker.get(DESPAWN_PUDDLE);
    }
    public void setDespawnPuddle(boolean isSpawned) {
        this.dataTracker.set(DESPAWN_PUDDLE, isSpawned);
    }


    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(DESPAWN_PUDDLE, Boolean.FALSE);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid);
        }
        nbt.putBoolean("Despawning",this.dataTracker.get(DESPAWN_PUDDLE));
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner");
        }
        this.setDespawnPuddle(nbt.getBoolean("Despawning"));
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

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
