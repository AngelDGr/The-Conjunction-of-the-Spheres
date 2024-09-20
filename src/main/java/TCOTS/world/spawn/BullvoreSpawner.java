package TCOTS.world.spawn;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.necrophages.BullvoreEntity;
import TCOTS.entity.necrophages.NecrophageMonster;
import TCOTS.entity.necrophages.RotfiendEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.spawner.SpecialSpawner;
import org.jetbrains.annotations.NotNull;

public class BullvoreSpawner implements SpecialSpawner {
    private int cooldown;
    private BullvoreEntity bullvoreEntity;
    @Override
    public int spawn(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals) {
        //If it doesn't spawn monsters and the game rule it's false
        if (!spawnMonsters || !world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
            return 0;
        }
        //If there's a player
        int i = world.getPlayers().size();
        if (i < 1) {
            return 0;
        }
        Random random = world.random;

        --this.cooldown;
        if (this.cooldown > 0) {
            return 0;
        }

        this.cooldown += 2 + random.nextInt(2);

        PlayerEntity playerEntity = world.getPlayers().get(random.nextInt(i));
        //If the player isn't in spectator mode
        if (playerEntity.isSpectator()) {
            return 0;
        }

        //From 24-48 blocks away the player
        int spawnX = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
        //From 24-88 blocks away the player
        int spawnY =  (24 + random.nextInt(64)) * (random.nextBoolean() ? -1 : 1);

        //To avoid too many invalid numbers when a player it's very deep
        if(spawnY < -60){
            //This makes any value under -60 to a range of (-60 to 4)
            spawnY= -60 + random.nextInt(64);
        }

        //From 24-48 blocks away the player
        int spawnZ = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
        BlockPos.Mutable mutable = playerEntity.getBlockPos().mutableCopy().move(spawnX, spawnY, spawnZ);

        //If it isn't an overworld biome, or it's the Deep Dark
        RegistryEntry<Biome> biome = world.getBiome(mutable);
        if (!biome.isIn(BiomeTags.IS_OVERWORLD) || biome == BiomeKeys.DEEP_DARK || biome == BiomeKeys.LUSH_CAVES) {
            return 0;
        }

        //To no spawn above sea level
        if(mutable.getY() > (world.getSeaLevel()-10)){
            return 0;
        }

        //To no spawn bullvore too close one of another
        if(!world.getEntitiesByClass(BullvoreEntity.class, new Box(mutable).expand(48, 10, 48), entity -> true).isEmpty()){
            return 0;
        }

        //Spawn Logic
        int numberOfSpawns = 0;
        int o = 2 + random.nextInt(4);
        for (int p = 0; p < o; ++p) {
            //To no spawn above sea level after finding an empty space
            if (mutable.getY() >= (world.getSeaLevel()-10)) break;

            if (p == 0) {
                if (!this.spawnBullvore(world, mutable, random)) {
                    break;
                }
//                System.out.println("Spawned in coords: "+mutable.getX()+" "+mutable.getY()+" "+mutable.getZ());
            } else {
                this.spawnRotfiend(world, mutable, random);
            }

            ++numberOfSpawns;
        }

        return numberOfSpawns;
    }

    private void spawnRotfiend(@NotNull ServerWorld world, BlockPos pos, Random random){

        if (!SpawnHelper.canSpawn(SpawnRestriction.Location.ON_GROUND, world, pos, TCOTS_Entities.ROTFIEND)) {
            return;
        }
        if (!NecrophageMonster.canSpawnInDarkW(TCOTS_Entities.ROTFIEND, world, SpawnReason.NATURAL, pos, random)) {
            return;
        }

        RotfiendEntity rotfiendEntity = TCOTS_Entities.ROTFIEND.create(world);
        if (rotfiendEntity != null) {
            //Spawn the rotfiend
            rotfiendEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());
            rotfiendEntity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.NATURAL, null, null);
            if(bullvoreEntity!=null) {
                rotfiendEntity.setOwner(bullvoreEntity);
            }
            world.spawnEntityAndPassengers(rotfiendEntity);
        }
    }

    private boolean spawnBullvore(@NotNull ServerWorld world, BlockPos pos, Random random) {
        //To avoid suffocation, it checks if none of the blocks in 4 tall block tower are solid
        if(
                        //Cross
                           !world.getBlockState(pos).isAir()
                        || !world.getBlockState(pos.east()).isAir()
                        || !world.getBlockState(pos.west()).isAir()
                        || !world.getBlockState(pos.north()).isAir()
                        || !world.getBlockState(pos.south()).isAir()
                        //Corners
                        || !world.getBlockState(pos.north().east()).isAir()
                        || !world.getBlockState(pos.north().west()).isAir()
                        || !world.getBlockState(pos.south().east()).isAir()
                        || !world.getBlockState(pos.south().west()).isAir()

                        //Cross
                        || !world.getBlockState(pos.up()).isAir()
                        || !world.getBlockState(pos.up().east()).isAir()
                        || !world.getBlockState(pos.up().west()).isAir()
                        || !world.getBlockState(pos.up().north()).isAir()
                        || !world.getBlockState(pos.up().south()).isAir()
                        //Corners
                        || !world.getBlockState(pos.up().north().east()).isAir()
                        || !world.getBlockState(pos.up().north().west()).isAir()
                        || !world.getBlockState(pos.up().south().east()).isAir()
                        || !world.getBlockState(pos.up().south().west()).isAir()

                        //Cross
                        || !world.getBlockState(pos.up(2)).isAir()
                        || !world.getBlockState(pos.up(2).east()).isAir()
                        || !world.getBlockState(pos.up(2).west()).isAir()
                        || !world.getBlockState(pos.up(2).north()).isAir()
                        || !world.getBlockState(pos.up(2).south()).isAir()
                        //Corners
                        || !world.getBlockState(pos.up(2).north().east()).isAir()
                        || !world.getBlockState(pos.up(2).north().west()).isAir()
                        || !world.getBlockState(pos.up(2).south().east()).isAir()
                        || !world.getBlockState(pos.up(2).south().west()).isAir()

                        //Cross
                        || !world.getBlockState(pos.up(3)).isAir()
                        || !world.getBlockState(pos.up(3).east()).isAir()
                        || !world.getBlockState(pos.up(3).west()).isAir()
                        || !world.getBlockState(pos.up(3).north()).isAir()
                        || !world.getBlockState(pos.up(3).south()).isAir()
                        //Corners
                        || !world.getBlockState(pos.up(3).north().east()).isAir()
                        || !world.getBlockState(pos.up(3).north().west()).isAir()
                        || !world.getBlockState(pos.up(3).south().east()).isAir()
                        || !world.getBlockState(pos.up(3).south().west()).isAir()
        ){
            return false;
        }

        if (!SpawnHelper.canSpawn(SpawnRestriction.Location.ON_GROUND, world, pos, TCOTS_Entities.BULLVORE)) {
            return false;
        }
        if (!BullvoreEntity.canSpawnInDarkW(TCOTS_Entities.BULLVORE, world, SpawnReason.NATURAL, pos, random)) {
            return false;
        }
        bullvoreEntity = TCOTS_Entities.BULLVORE.create(world);
        if (bullvoreEntity != null) {
            //Spawn the bullvore
            bullvoreEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());
            bullvoreEntity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.NATURAL, null, null);
            world.spawnEntityAndPassengers(bullvoreEntity);
            return true;
        }

        return false;
    }
}
