package TCOTS.world.gen;

import TCOTS.registry.TCOTS_Blocks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class BryoniaPatchFeatureConfig implements FeatureConfiguration {

    public static final Codec<BryoniaPatchFeatureConfig> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(

                            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block")
                                    .flatXmap(BryoniaPatchFeatureConfig::validateBlock, DataResult::success)
                                    .orElse((MultifaceBlock) TCOTS_Blocks.BryoniaVine())
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

    public final MultifaceBlock lichen;
    public final int searchRange;
    public final boolean placeOnFloor;
    public final boolean placeOnCeiling;
    public final boolean placeOnWalls;
    private final ObjectArrayList<Direction> directions;

    private static DataResult<MultifaceBlock> validateBlock(final Block block) {
        final DataResult<MultifaceBlock> dataResult;
        if (block instanceof final MultifaceBlock multifaceGrowthBlock) {
            dataResult = DataResult.success(multifaceGrowthBlock);
        } else {
            dataResult = DataResult.error(() -> "Growth block should be a multiface block");
        }
        return dataResult;
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    public BryoniaPatchFeatureConfig(final MultifaceBlock lichen, final int searchRange, final boolean placeOnFloor, final boolean placeOnCeiling, final boolean placeOnWalls, final float YMin, final float YMax) {

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
            Direction.Plane.HORIZONTAL.forEach(this.directions::add);
        }
    }

    @SuppressWarnings("unused")
    public List<Direction> shuffleDirections(final RandomSource random, final Direction excluded) {
        return Util.toShuffledList(this.directions.stream().filter(direction -> direction != excluded), random);
    }

    public List<Direction> shuffleDirections(final RandomSource random) {
        return Util.shuffledCopy(this.directions, random);
    }

}
