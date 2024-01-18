package TCOTS.entity.misc;

import TCOTS.entity.TCOTS_Entities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.UUID;


public class DrownerPuddleEntity extends Entity implements GeoEntity, Ownable {
    @Nullable
    private Entity owner;
    @Nullable
    private UUID ownerUuid;

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


    @Override
    protected void initDataTracker() {

    }


    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner");
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(DefaultAnimations.genericIdleController(this));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


}
