package TCOTS.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MonsterNestLogic extends BaseSpawner {

    private final Block block;

    public MonsterNestLogic(Block block){
        this.block=block;
    }

    @Override
    public void broadcastEvent(Level world, @NotNull BlockPos pos, int status) {
        world.blockEvent(pos, block, status, 0);
    }

    @Override
    public void setNextSpawnData(@Nullable Level world, @NotNull BlockPos pos, @NotNull SpawnData spawnEntry) {
        super.setNextSpawnData(world, pos, spawnEntry);
        if (world != null) {
            BlockState blockState = world.getBlockState(pos);
            world.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_INVISIBLE);
        }
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag nbt) {
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
    public void serverTick(@NotNull ServerLevel world, @NotNull BlockPos pos) {
        if (this.isNearPlayer(world, pos)) {
            if (this.spawnDelay == -1) {
                this.delay(world, pos);
            }

            if (this.spawnDelay > 0) {
                this.spawnDelay--;
            } else {
                boolean bl = false;
                RandomSource random = world.getRandom();
                SpawnData mobSpawnerEntry = this.getOrCreateNextSpawnData(world, random, pos);

                for (int i = 0; i < this.spawnCount; i++) {
                    CompoundTag nbtCompound = mobSpawnerEntry.getEntityToSpawn();
                    Optional<EntityType<?>> optional = EntityType.by(nbtCompound);
                    if (optional.isEmpty()) {
                        this.delay(world, pos);
                        return;
                    }

                    ListTag nbtList = nbtCompound.getList("Pos", Tag.TAG_DOUBLE);
                    int j = nbtList.size();
                    double d = j >= 1 ? nbtList.getDouble(0) : (double)pos.getX() + (random.nextDouble() - random.nextDouble()) * (double)this.spawnRange + 0.5;
                    double e = j >= 2 ? nbtList.getDouble(1) : (double)(pos.getY() + random.nextInt(3) - 1);
                    double f = j >= 3 ? nbtList.getDouble(2) : (double)pos.getZ() + (random.nextDouble() - random.nextDouble()) * (double)this.spawnRange + 0.5;
                    if (world.noCollision(optional.get().getSpawnAABB(d, e, f))) {
                        BlockPos blockPos = BlockPos.containing(d, e, f);
                        if (mobSpawnerEntry.getCustomSpawnRules().isPresent()) {
                            if (!optional.get().getCategory().isFriendly() && world.getDifficulty() == Difficulty.PEACEFUL) {
                                continue;
                            }

                            SpawnData.CustomSpawnRules customSpawnRules = mobSpawnerEntry.getCustomSpawnRules().get();
                            if (!customSpawnRules.isValidPosition(blockPos, world)) {
                                continue;
                            }
                        } else if (!SpawnPlacements.checkSpawnRules((EntityType<?>)optional.get(), world, MobSpawnType.SPAWNER, blockPos, world.getRandom())) {
                            continue;
                        }

                        Entity entity = EntityType.loadEntityRecursive(nbtCompound, world, entityX -> {
                            entityX.moveTo(d, e, f, entityX.getYRot(), entityX.getXRot());
                            return entityX;
                        });
                        if (entity == null) {
                            this.delay(world, pos);
                            return;
                        }

                        int k = world.getEntities(
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
                        if (entity instanceof Mob mobEntity) {
                            if (mobSpawnerEntry.getCustomSpawnRules().isEmpty() && !mobEntity.checkSpawnRules(world, MobSpawnType.SPAWNER) || !mobEntity.checkSpawnObstruction(world)) {
                                continue;
                            }

                            boolean bl2 = mobSpawnerEntry.getEntityToSpawn().size() == 1 && mobSpawnerEntry.getEntityToSpawn().contains("id", Tag.TAG_STRING);
                            if (bl2) {
                                ((Mob)entity).finalizeSpawn(world, world.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.SPAWNER, null);
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

}
