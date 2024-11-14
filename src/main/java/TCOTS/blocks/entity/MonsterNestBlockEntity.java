package TCOTS.blocks.entity;

import TCOTS.blocks.TCOTS_Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.Spawner;
import net.minecraft.block.spawner.MobSpawnerEntry;
import net.minecraft.block.spawner.MobSpawnerLogic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

@SuppressWarnings("unused")
public class MonsterNestBlockEntity extends BlockEntity implements GeoBlockEntity, Spawner {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public MonsterNestBlockEntity(BlockPos pos, BlockState state) {
        super(TCOTS_Blocks.MONSTER_NEST_ENTITY, pos, state);
    }

    private final MobSpawnerLogic logic = new MobSpawnerLogic(){

        @Override
        public void sendStatus(World world, BlockPos pos, int status) {
            world.addSyncedBlockEvent(pos, TCOTS_Blocks.MONSTER_NEST, status, 0);
        }

        @Override
        public void setSpawnEntry(@Nullable World world, BlockPos pos, MobSpawnerEntry spawnEntry) {
            super.setSpawnEntry(world, pos, spawnEntry);
            if (world != null) {
                BlockState blockState = world.getBlockState(pos);
                world.updateListeners(pos, blockState, blockState, Block.NO_REDRAW);
            }
        }

        @Override
        public NbtCompound writeNbt(NbtCompound nbt) {
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

            if (this.spawnEntry != null) {
                nbt.put(SPAWN_DATA_KEY,
                        MobSpawnerEntry.CODEC.encodeStart(NbtOps.INSTANCE, this.spawnEntry).result().orElseThrow(()
                                -> new IllegalStateException("Invalid SpawnData")));
            }
            nbt.put("SpawnPotentials", MobSpawnerEntry.DATA_POOL_CODEC.encodeStart(NbtOps.INSTANCE, this.spawnPotentials).result().orElseThrow());
            return nbt;
        }

        @Override
        public void serverTick(ServerWorld world, BlockPos pos) {
            if (this.isPlayerInRange(world, pos)) {
                if (this.spawnDelay == -1) {
                    this.updateSpawns(world, pos);
                }

                if (this.spawnDelay > 0) {
                    this.spawnDelay--;
                } else {
                    boolean bl = false;
                    Random random = world.getRandom();
                    MobSpawnerEntry mobSpawnerEntry = this.getSpawnEntry(world, random, pos);

                    for (int i = 0; i < this.spawnCount; i++) {
                        NbtCompound nbtCompound = mobSpawnerEntry.getNbt();
                        Optional<EntityType<?>> optional = EntityType.fromNbt(nbtCompound);
                        if (optional.isEmpty()) {
                            this.updateSpawns(world, pos);
                            return;
                        }

                        NbtList nbtList = nbtCompound.getList("Pos", NbtElement.DOUBLE_TYPE);
                        int j = nbtList.size();
                        double d = j >= 1 ? nbtList.getDouble(0) : (double)pos.getX() + (random.nextDouble() - random.nextDouble()) * (double)this.spawnRange + 0.5;
                        double e = j >= 2 ? nbtList.getDouble(1) : (double)(pos.getY() + random.nextInt(3) - 1);
                        double f = j >= 3 ? nbtList.getDouble(2) : (double)pos.getZ() + (random.nextDouble() - random.nextDouble()) * (double)this.spawnRange + 0.5;
                        if (world.isSpaceEmpty(optional.get().getSpawnBox(d, e, f))) {
                            BlockPos blockPos = BlockPos.ofFloored(d, e, f);
                            if (mobSpawnerEntry.getCustomSpawnRules().isPresent()) {
                                if (!optional.get().getSpawnGroup().isPeaceful() && world.getDifficulty() == Difficulty.PEACEFUL) {
                                    continue;
                                }

                                MobSpawnerEntry.CustomSpawnRules customSpawnRules = mobSpawnerEntry.getCustomSpawnRules().get();
                                if (!customSpawnRules.canSpawn(blockPos, world)) {
                                    continue;
                                }
                            } else if (!SpawnRestriction.canSpawn((EntityType<?>)optional.get(), world, SpawnReason.SPAWNER, blockPos, world.getRandom())) {
                                continue;
                            }

                            Entity entity = EntityType.loadEntityWithPassengers(nbtCompound, world, entityX -> {
                                entityX.refreshPositionAndAngles(d, e, f, entityX.getYaw(), entityX.getPitch());
                                return entityX;
                            });
                            if (entity == null) {
                                this.updateSpawns(world, pos);
                                return;
                            }

                            int k = world.getEntitiesByType(
                                            TypeFilter.equals(entity.getClass()),
                                            new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)
                                                    .expand(this.spawnRange),
                                            EntityPredicates.EXCEPT_SPECTATOR
                                    )
                                    .size();
                            if (k >= this.maxNearbyEntities) {
                                this.updateSpawns(world, pos);
                                return;
                            }

                            entity.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), random.nextFloat() * 360.0F, 0.0F);
                            if (entity instanceof MobEntity mobEntity) {
                                if (mobSpawnerEntry.getCustomSpawnRules().isEmpty() && !mobEntity.canSpawn(world, SpawnReason.SPAWNER) || !mobEntity.canSpawn(world)) {
                                    continue;
                                }

                                boolean bl2 = mobSpawnerEntry.getNbt().getSize() == 1 && mobSpawnerEntry.getNbt().contains("id", NbtElement.STRING_TYPE);
                                if (bl2) {
                                    ((MobEntity)entity).initialize(world, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.SPAWNER, null);
                                }

                                mobSpawnerEntry.getEquipment().ifPresent(mobEntity::setEquipmentFromTable);
                            }

                            if (!world.spawnNewEntityAndPassengers(entity)) {
                                this.updateSpawns(world, pos);
                                return;
                            }

                            world.syncWorldEvent(8642097, pos, 0);
                            world.emitGameEvent(entity, GameEvent.ENTITY_PLACE, blockPos);
                            if (entity instanceof MobEntity) {
                                ((MobEntity)entity).playSpawnEffects();
                            }

                            bl = true;
                        }
                    }

                    if (bl) {
                        this.updateSpawns(world, pos);
                    }
                }
            }
        }

    };

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
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
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.logic.readNbt(this.world, this.pos, nbt);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        this.logic.writeNbt(nbt);
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, MonsterNestBlockEntity blockEntity) {
        blockEntity.logic.clientTick(world, pos);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, MonsterNestBlockEntity blockEntity) {
        blockEntity.logic.serverTick((ServerWorld)world, pos);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound nbtCompound = this.createComponentlessNbt(registryLookup);
        nbtCompound.remove("SpawnPotentials");
        return nbtCompound;
    }

    @Override
    public boolean onSyncedBlockEvent(int type, int data) {
        if (this.logic.handleStatus(this.world, type)) {
            return true;
        }
        return super.onSyncedBlockEvent(type, data);
    }

    @Override
    public boolean copyItemDataRequiresOperator() {
        return true;
    }

    @Override
    public void setEntityType(EntityType<?> type, Random random) {
        this.logic.setEntityId(type, this.world, random, this.pos);
        this.markDirty();
    }


    public MobSpawnerLogic getLogic() {
        return this.logic;
    }

}
