package TCOTS.world.spawn;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.necrophages.BullvoreEntity;
import TCOTS.entity.necrophages.NecrophageMonster;
import TCOTS.entity.necrophages.RotfiendEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class BullvoreSpawner implements CustomSpawner {
    private int cooldown;
    private BullvoreEntity bullvoreEntity;
    @Override
    public int tick(ServerLevel world, boolean spawnMonsters, boolean spawnAnimals) {
        //If it doesn't spawn monsters and the game rule it's false
        if (!spawnMonsters || !world.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            return 0;
        }
        //If there's a player
        int i = world.players().size();
        if (i < 1) {
            return 0;
        }
        RandomSource random = world.random;

        --this.cooldown;
        if (this.cooldown > 0) {
            return 0;
        }

        this.cooldown += 2 + random.nextInt(2);

        Player playerEntity = world.players().get(random.nextInt(i));
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
        BlockPos.MutableBlockPos mutable = playerEntity.blockPosition().mutable().move(spawnX, spawnY, spawnZ);

        //If it isn't an overworld biome, or it's the Deep Dark
        Holder<Biome> biome = world.getBiome(mutable);
        if (!biome.is(BiomeTags.IS_OVERWORLD) || biome == Biomes.DEEP_DARK || biome == Biomes.LUSH_CAVES) {
            return 0;
        }

        //To no spawn above sea level
        if(mutable.getY() > (world.getSeaLevel()-10)){
            return 0;
        }

        //To no spawn bullvore too close one of another
        if(!world.getEntitiesOfClass(BullvoreEntity.class, new AABB(mutable).inflate(48, 10, 48), entity -> true).isEmpty()){
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

    private void spawnRotfiend(@NotNull ServerLevel world, BlockPos pos, RandomSource random){
        BlockState blockState = world.getBlockState(pos);
        if (!NaturalSpawner.isValidEmptySpawnBlock(world, pos, blockState, blockState.getFluidState(), TCOTS_Entities.ROTFIEND)) {
            return;
        }
        if (!NecrophageMonster.canSpawnInDarkW(TCOTS_Entities.ROTFIEND, world, MobSpawnType.NATURAL, pos, random)) {
            return;
        }

        RotfiendEntity rotfiendEntity = TCOTS_Entities.ROTFIEND.create(world);
        if (rotfiendEntity != null) {
            //Spawn the rotfiend
            rotfiendEntity.setPos(pos.getX(), pos.getY(), pos.getZ());
            rotfiendEntity.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, null);
            if(bullvoreEntity!=null) {
                rotfiendEntity.setOwner(bullvoreEntity);
            }
            world.addFreshEntityWithPassengers(rotfiendEntity);
        }
    }

    private boolean spawnBullvore(@NotNull ServerLevel world, BlockPos pos, RandomSource random) {
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
                        || !world.getBlockState(pos.above()).isAir()
                        || !world.getBlockState(pos.above().east()).isAir()
                        || !world.getBlockState(pos.above().west()).isAir()
                        || !world.getBlockState(pos.above().north()).isAir()
                        || !world.getBlockState(pos.above().south()).isAir()
                        //Corners
                        || !world.getBlockState(pos.above().north().east()).isAir()
                        || !world.getBlockState(pos.above().north().west()).isAir()
                        || !world.getBlockState(pos.above().south().east()).isAir()
                        || !world.getBlockState(pos.above().south().west()).isAir()

                        //Cross
                        || !world.getBlockState(pos.above(2)).isAir()
                        || !world.getBlockState(pos.above(2).east()).isAir()
                        || !world.getBlockState(pos.above(2).west()).isAir()
                        || !world.getBlockState(pos.above(2).north()).isAir()
                        || !world.getBlockState(pos.above(2).south()).isAir()
                        //Corners
                        || !world.getBlockState(pos.above(2).north().east()).isAir()
                        || !world.getBlockState(pos.above(2).north().west()).isAir()
                        || !world.getBlockState(pos.above(2).south().east()).isAir()
                        || !world.getBlockState(pos.above(2).south().west()).isAir()

                        //Cross
                        || !world.getBlockState(pos.above(3)).isAir()
                        || !world.getBlockState(pos.above(3).east()).isAir()
                        || !world.getBlockState(pos.above(3).west()).isAir()
                        || !world.getBlockState(pos.above(3).north()).isAir()
                        || !world.getBlockState(pos.above(3).south()).isAir()
                        //Corners
                        || !world.getBlockState(pos.above(3).north().east()).isAir()
                        || !world.getBlockState(pos.above(3).north().west()).isAir()
                        || !world.getBlockState(pos.above(3).south().east()).isAir()
                        || !world.getBlockState(pos.above(3).south().west()).isAir()
        ){
            return false;
        }
        BlockState blockState = world.getBlockState(pos);
        if (!NaturalSpawner.isValidEmptySpawnBlock(world, pos, blockState, blockState.getFluidState(), TCOTS_Entities.BULLVORE)) {
            return false;
        }
        if (!BullvoreEntity.canSpawnInDarkW(TCOTS_Entities.BULLVORE, world, MobSpawnType.NATURAL, pos, random)) {
            return false;
        }
        bullvoreEntity = TCOTS_Entities.BULLVORE.create(world);
        if (bullvoreEntity != null) {
            //Spawn the bullvore
            bullvoreEntity.setPos(pos.getX(), pos.getY(), pos.getZ());
            bullvoreEntity.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, null);
            world.addFreshEntityWithPassengers(bullvoreEntity);
            return true;
        }

        return false;
    }
}
