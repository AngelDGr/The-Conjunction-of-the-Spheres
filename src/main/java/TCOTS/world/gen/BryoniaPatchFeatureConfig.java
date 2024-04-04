package TCOTS.world.gen;

import TCOTS.blocks.TCOTS_Blocks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.MultifaceGrowthBlock;
import net.minecraft.registry.Registries;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.FeatureConfig;

import java.util.List;

public class BryoniaPatchFeatureConfig implements FeatureConfig {

    public static final Codec<BryoniaPatchFeatureConfig> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(

                            Registries.BLOCK.getCodec().fieldOf("block")
                                    .flatXmap(BryoniaPatchFeatureConfig::validateBlock, DataResult::success)
                                    .orElse((MultifaceGrowthBlock) TCOTS_Blocks.BRYONIA_VINE)
                                    .forGetter(config -> config.lichen),


                            Codec.intRange(1, 64).fieldOf("search_range").orElse(10)
                                    .forGetter(config -> config.searchRange),

                            Codec.BOOL.fieldOf("can_place_on_floor").orElse(false)
                                    .forGetter(config -> config.placeOnFloor),

                            Codec.BOOL.fieldOf("can_place_on_ceiling").orElse(false)
                                    .forGetter(config -> config.placeOnCeiling),

                            Codec.BOOL.fieldOf("can_place_on_wall").orElse(false)
                                    .forGetter(config -> config.placeOnWalls),

                            Codec.floatRange(-500f, 500f).fieldOf("y_min").orElse(0f)
                                    .forGetter( config -> config.YMin),

                            Codec.floatRange(-500f, 500f).fieldOf("y_max").orElse(100f)
                                    .forGetter( config -> config.YMax)

                            ).apply(instance, BryoniaPatchFeatureConfig::new));

    public final float YMin;
    public final float YMax;

    public final MultifaceGrowthBlock lichen;
    public final int searchRange;
    public final boolean placeOnFloor;
    public final boolean placeOnCeiling;
    public final boolean placeOnWalls;
    private final ObjectArrayList<Direction> directions;

    private static DataResult<MultifaceGrowthBlock> validateBlock(Block block) {
        DataResult<MultifaceGrowthBlock> dataResult;
        if (block instanceof MultifaceGrowthBlock multifaceGrowthBlock) {
            dataResult = DataResult.success(multifaceGrowthBlock);
        } else {
            dataResult = DataResult.error(() -> "Growth block should be a multiface block");
        }
        return dataResult;
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    public BryoniaPatchFeatureConfig(MultifaceGrowthBlock lichen, int searchRange, boolean placeOnFloor, boolean placeOnCeiling, boolean placeOnWalls, float YMin, float YMax) {

        this.YMin=YMin;
        this.YMax=YMax;

        this.lichen = lichen;
        this.searchRange = searchRange;
        this.placeOnFloor = placeOnFloor;
        this.placeOnCeiling = placeOnCeiling;
        this.placeOnWalls = placeOnWalls;

        this.directions = new ObjectArrayList(6);
        if (placeOnCeiling) {
            this.directions.add(Direction.UP);
        }
        if (placeOnFloor) {
            this.directions.add(Direction.DOWN);
        }
        if (placeOnWalls) {
            Direction.Type.HORIZONTAL.forEach(this.directions::add);
        }
    }

    @SuppressWarnings("unused")
    public List<Direction> shuffleDirections(Random random, Direction excluded) {
        return Util.copyShuffled(this.directions.stream().filter(direction -> direction != excluded), random);
    }

    public List<Direction> shuffleDirections(Random random) {
        return Util.copyShuffled(this.directions, random);
    }

}
