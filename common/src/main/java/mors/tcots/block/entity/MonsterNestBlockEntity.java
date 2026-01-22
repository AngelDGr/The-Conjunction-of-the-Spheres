package mors.tcots.block.entity;

import mors.tcots.registry.TCOTS_Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.Spawner;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

@SuppressWarnings("unused")
public class MonsterNestBlockEntity extends BlockEntity implements GeoBlockEntity, Spawner {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public MonsterNestBlockEntity(final BlockEntityType<?> entityType, final BlockPos pos, final BlockState state) {
        super(entityType, pos, state);
    }

    private final BaseSpawner logicMonsterNest = new BaseSpawner(){

        @Override
        public void broadcastEvent(final Level world, @NotNull final BlockPos pos, final int status) {
            world.blockEvent(pos, TCOTS_Blocks.MonsterNest(), status, 0);
        }

        @Override
        public void setNextSpawnData(@Nullable final Level world, @NotNull final BlockPos pos, @NotNull final SpawnData spawnEntry) {
            super.setNextSpawnData(world, pos, spawnEntry);
            if (world != null) {
                final BlockState blockState = world.getBlockState(pos);
                world.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_INVISIBLE);
            }
        }

        @Override
        public @NotNull CompoundTag save(final CompoundTag nbt) {
            this.spawnRange = 3;
            this.requiredPlayerRange = 64;
            this.minSpawnDelay = 1200;
            this.maxSpawnDelay = 2000;

            nbt.putShort("Delay", (short)this.spawnDelay);
            nbt.putShort("MinSpawnDelay", (short)this.minSpawnDelay);
            nbt.putShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
            nbt.putShort("SpawnCount", (short)this.spawnCount);
            nbt.putShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
            nbt.putShort("RequiredPlayerRange", (short)this.requiredPlayerRange);
            nbt.putShort("SpawnRange", (short)this.spawnRange);

            if (this.nextSpawnData != null) {
                nbt.put(SPAWN_DATA_TAG,
                        SpawnData.CODEC.encodeStart(NbtOps.INSTANCE, this.nextSpawnData).result().orElseThrow(()
                                -> new IllegalStateException("Invalid SpawnData")));
            }
            nbt.put("SpawnPotentials", SpawnData.LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.spawnPotentials).result().orElseThrow());
            return nbt;
        }

        @Override
        public void serverTick(@NotNull final ServerLevel world, @NotNull final BlockPos pos) {
            if (this.isNearPlayer(world, pos)) {
                if (this.spawnDelay == -1) {
                    this.delay(world, pos);
                }

                if (this.spawnDelay > 0) {
                    this.spawnDelay--;
                } else {
                    boolean bl = false;
                    final RandomSource random = world.getRandom();
                    final SpawnData mobSpawnerEntry = this.getOrCreateNextSpawnData(world, random, pos);

                    for (int i = 0; i < this.spawnCount; i++) {
                        final CompoundTag nbtCompound = mobSpawnerEntry.getEntityToSpawn();
                        final Optional<EntityType<?>> optional = EntityType.by(nbtCompound);
                        if (optional.isEmpty()) {
                            this.delay(world, pos);
                            return;
                        }

                        final ListTag nbtList = nbtCompound.getList("Pos", Tag.TAG_DOUBLE);
                        final int j = nbtList.size();
                        final double d = j >= 1 ? nbtList.getDouble(0) : (double)pos.getX() + (random.nextDouble() - random.nextDouble()) * (double)this.spawnRange + 0.5;
                        final double e = j >= 2 ? nbtList.getDouble(1) : (double)(pos.getY() + random.nextInt(3) - 1);
                        final double f = j >= 3 ? nbtList.getDouble(2) : (double)pos.getZ() + (random.nextDouble() - random.nextDouble()) * (double)this.spawnRange + 0.5;
                        if (world.noCollision(optional.get().getSpawnAABB(d, e, f))) {
                            final BlockPos blockPos = BlockPos.containing(d, e, f);
                            if (mobSpawnerEntry.getCustomSpawnRules().isPresent()) {
                                if (!optional.get().getCategory().isFriendly() && world.getDifficulty() == Difficulty.PEACEFUL) {
                                    continue;
                                }

                                final SpawnData.CustomSpawnRules customSpawnRules = mobSpawnerEntry.getCustomSpawnRules().get();
                                if (!customSpawnRules.isValidPosition(blockPos, world)) {
                                    continue;
                                }
                            } else if (!SpawnPlacements.checkSpawnRules((EntityType<?>)optional.get(), world, MobSpawnType.SPAWNER, blockPos, world.getRandom())) {
                                continue;
                            }

                            final Entity entity = EntityType.loadEntityRecursive(nbtCompound, world, entityX -> {
                                entityX.moveTo(d, e, f, entityX.getYRot(), entityX.getXRot());
                                return entityX;
                            });
                            if (entity == null) {
                                this.delay(world, pos);
                                return;
                            }

                            final int k = world.getEntities(
                                            EntityTypeTest.forExactClass(entity.getClass()),
                                            new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)
                                                    .inflate(this.spawnRange),
                                            EntitySelector.NO_SPECTATORS
                                    )
                                    .size();
                            if (k >= this.maxNearbyEntities) {
                                this.delay(world, pos);
                                return;
                            }

                            entity.moveTo(entity.getX(), entity.getY(), entity.getZ(), random.nextFloat() * 360.0F, 0.0F);
                            if (entity instanceof final Mob mobEntity) {
                                if (mobSpawnerEntry.getCustomSpawnRules().isEmpty() && !mobEntity.checkSpawnRules(world, MobSpawnType.SPAWNER) || !mobEntity.checkSpawnObstruction(world)) {
                                    continue;
                                }

                                final boolean bl2 = mobSpawnerEntry.getEntityToSpawn().size() == 1 && mobSpawnerEntry.getEntityToSpawn().contains("id", Tag.TAG_STRING);
                                if (bl2) {
                                    mobEntity.finalizeSpawn(world, world.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.SPAWNER, null);
                                }

                                mobSpawnerEntry.getEquipment().ifPresent(mobEntity::equip);
                            }

                            if (!world.tryAddFreshEntityWithPassengers(entity)) {
                                this.delay(world, pos);
                                return;
                            }

                            world.levelEvent(8642097, pos, 0);
                            world.gameEvent(entity, GameEvent.ENTITY_PLACE, blockPos);
                            if (entity instanceof Mob) {
                                ((Mob)entity).spawnAnim();
                            }

                            bl = true;
                        }
                    }

                    if (bl) {
                        this.delay(world, pos);
                    }
                }
            }
        }

    };

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state ->{
            state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected void loadAdditional(@NotNull final CompoundTag nbt, final HolderLookup.@NotNull Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        this.logicMonsterNest.load(this.level, this.worldPosition, nbt);
    }

    @Override
    protected void saveAdditional(@NotNull final CompoundTag nbt, final HolderLookup.@NotNull Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        this.logicMonsterNest.save(nbt);
    }

    public static void clientTick(final Level world, final BlockPos pos, final BlockState state, final MonsterNestBlockEntity blockEntity) {
        blockEntity.logicMonsterNest.clientTick(world, pos);
    }

    public static void serverTick(final Level world, final BlockPos pos, final BlockState state, final MonsterNestBlockEntity blockEntity) {
        blockEntity.logicMonsterNest.serverTick((ServerLevel)world, pos);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(final HolderLookup.@NotNull Provider registryLookup) {
        final CompoundTag nbtCompound = this.saveCustomOnly(registryLookup);
        nbtCompound.remove("SpawnPotentials");
        return nbtCompound;
    }

    @Override
    public boolean triggerEvent(final int type, final int data) {
        if (this.logicMonsterNest.onEventTriggered(this.level, type)) {
            return true;
        }
        return super.triggerEvent(type, data);
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }

    @Override
    public void setEntityId(@NotNull final EntityType<?> type, @NotNull final RandomSource random) {
        this.logicMonsterNest.setEntityId(type, this.level, random, this.worldPosition);
        this.setChanged();
    }


    public BaseSpawner getLogic() {
        return this.logicMonsterNest;
    }

}
