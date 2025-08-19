package neoforge.TCOTS.datagen.providers;

import TCOTS.TCOTS_Main;
import TCOTS.registry.TCOTS_Tags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TCOTS_BiomeTagGenerator extends BiomeTagsProvider {

    public TCOTS_BiomeTagGenerator(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable final ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, TCOTS_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(final HolderLookup.@NotNull Provider arg) {

        //Has_Flora
        {
            this.tag(TCOTS_Tags.CELANDINE_SPAWN)
                    .add(Biomes.PLAINS).add(Biomes.MEADOW)
                    .add(Biomes.BIRCH_FOREST).add(Biomes.OLD_GROWTH_BIRCH_FOREST).add(Biomes.FOREST).add(Biomes.DARK_FOREST)
            ;

            this.tag(TCOTS_Tags.VERBENA_SPAWN)
                    .add(Biomes.PLAINS).add(Biomes.MEADOW)
                    .add(Biomes.BIRCH_FOREST).add(Biomes.OLD_GROWTH_BIRCH_FOREST).add(Biomes.FOREST).add(Biomes.DARK_FOREST)
                    .add(Biomes.FLOWER_FOREST)
            ;

            this.tag(TCOTS_Tags.HAN_FIBER_SPAWN)
                    .addOptionalTag(BiomeTags.IS_JUNGLE.location())
                    .addOptionalTag(BiomeTags.HAS_RUINED_PORTAL_SWAMP.location())
                    .addOptionalTag(BiomeTags.HAS_VILLAGE_SAVANNA.location())
            ;

            this.tag(TCOTS_Tags.CROWS_EYE_SPAWN)
                    .addOptionalTag(BiomeTags.IS_TAIGA.location())
                    .add(Biomes.GROVE)
            ;

            this.tag(TCOTS_Tags.ARENARIA_SPAWN)
                    .addOptionalTag(BiomeTags.IS_TAIGA.location())
                    .add(Biomes.FLOWER_FOREST)
            ;

            this.tag(TCOTS_Tags.PUFFBALL_SPAWN_NORMAL)
                    .add(Biomes.PLAINS)
                    .add(Biomes.SUNFLOWER_PLAINS)
                    .add(Biomes.SNOWY_PLAINS)
                    .add(Biomes.ICE_SPIKES)
                    .add(Biomes.DESERT)
                    .add(Biomes.SWAMP)
                    .add(Biomes.MANGROVE_SWAMP)
                    .add(Biomes.FOREST)
                    .add(Biomes.FLOWER_FOREST)
                    .add(Biomes.BIRCH_FOREST)
                    .add(Biomes.DARK_FOREST)
                    .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
                    .add(Biomes.SAVANNA)
                    .add(Biomes.SAVANNA_PLATEAU)
                    .add(Biomes.WINDSWEPT_HILLS)
                    .add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
                    .add(Biomes.WINDSWEPT_FOREST)
                    .add(Biomes.WINDSWEPT_SAVANNA)
                    .add(Biomes.JUNGLE)
                    .add(Biomes.SPARSE_JUNGLE)
                    .add(Biomes.BAMBOO_JUNGLE)
                    .add(Biomes.BADLANDS)
                    .add(Biomes.ERODED_BADLANDS)
                    .add(Biomes.WOODED_BADLANDS)
                    .add(Biomes.MEADOW)
                    .add(Biomes.CHERRY_GROVE)
                    .add(Biomes.RIVER)
                    .add(Biomes.FROZEN_RIVER)
                    .add(Biomes.BEACH)
                    .add(Biomes.SNOWY_BEACH)
                    .add(Biomes.STONY_SHORE)
                    .add(Biomes.WARM_OCEAN)
                    .add(Biomes.LUKEWARM_OCEAN)
                    .add(Biomes.DEEP_LUKEWARM_OCEAN)
                    .add(Biomes.OCEAN)
                    .add(Biomes.DEEP_OCEAN)
                    .add(Biomes.COLD_OCEAN)
                    .add(Biomes.DEEP_COLD_OCEAN)
                    .add(Biomes.FROZEN_OCEAN)
                    .add(Biomes.DEEP_FROZEN_OCEAN)
                    .add(Biomes.DRIPSTONE_CAVES)
            ;

            this.tag(TCOTS_Tags.PUFFBALL_SPAWN_TAIGA)
                    .add(Biomes.TAIGA).add(Biomes.SNOWY_TAIGA)
            ;

            this.tag(TCOTS_Tags.PUFFBALL_SPAWN_SWAMP)
                    .addOptionalTag(BiomeTags.HAS_RUINED_PORTAL_SWAMP.location())
            ;

            this.tag(TCOTS_Tags.MUSHROOM_SPAWN_OLD_GROWTH)
                    .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA).add(Biomes.OLD_GROWTH_PINE_TAIGA)
            ;

            this.tag(TCOTS_Tags.SEWANT_SPAWN_NORMAL)
                    .add(Biomes.PLAINS)
                    .add(Biomes.SUNFLOWER_PLAINS)
                    .add(Biomes.SNOWY_PLAINS)
                    .add(Biomes.ICE_SPIKES)
                    .add(Biomes.DESERT)
                    .add(Biomes.SWAMP)
                    .add(Biomes.MANGROVE_SWAMP)
                    .add(Biomes.FOREST)
                    .add(Biomes.FLOWER_FOREST)
                    .add(Biomes.BIRCH_FOREST)
                    .add(Biomes.DARK_FOREST)
                    .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
                    .add(Biomes.SNOWY_TAIGA)
                    .add(Biomes.SAVANNA)
                    .add(Biomes.SAVANNA_PLATEAU)
                    .add(Biomes.WINDSWEPT_HILLS)
                    .add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
                    .add(Biomes.WINDSWEPT_FOREST)
                    .add(Biomes.WINDSWEPT_SAVANNA)
                    .add(Biomes.JUNGLE)
                    .add(Biomes.SPARSE_JUNGLE)
                    .add(Biomes.BAMBOO_JUNGLE)
                    .add(Biomes.BADLANDS)
                    .add(Biomes.ERODED_BADLANDS)
                    .add(Biomes.WOODED_BADLANDS)
                    .add(Biomes.CHERRY_GROVE)
                    .add(Biomes.RIVER)
                    .add(Biomes.FROZEN_RIVER)
                    .add(Biomes.BEACH)
                    .add(Biomes.SNOWY_BEACH)
                    .add(Biomes.STONY_SHORE)
                    .add(Biomes.WARM_OCEAN)
                    .add(Biomes.LUKEWARM_OCEAN)
                    .add(Biomes.DEEP_LUKEWARM_OCEAN)
                    .add(Biomes.OCEAN)
                    .add(Biomes.DEEP_OCEAN)
                    .add(Biomes.COLD_OCEAN)
                    .add(Biomes.DEEP_COLD_OCEAN)
                    .add(Biomes.FROZEN_OCEAN)
                    .add(Biomes.DEEP_FROZEN_OCEAN)
                    .add(Biomes.MUSHROOM_FIELDS)
                    .add(Biomes.DRIPSTONE_CAVES)
            ;

            this.tag(TCOTS_Tags.SEWANT_SPAWN_TAIGA)
                    .add(Biomes.TAIGA)
            ;

            this.tag(TCOTS_Tags.SEWANT_SPAWN_DARK)
                    .add(Biomes.DARK_FOREST)
            ;
        }

        //Has_Monster
        {
            //Necrophages
            {
                //Drowners
                {
                    this.tag(TCOTS_Tags.DROWNER_SWAMP)
                            .addOptionalTag(BiomeTags.HAS_RUINED_PORTAL_SWAMP.location());

                    this.tag(TCOTS_Tags.DROWNER_BEACH)
                            .addOptionalTag(BiomeTags.IS_BEACH.location());

                    this.tag(TCOTS_Tags.DROWNER_WATER)
                            .addOptionalTag(BiomeTags.IS_OCEAN.location())
                            .addOptionalTag(BiomeTags.IS_DEEP_OCEAN.location())
                            .add(Biomes.RIVER);
                }

                //Rotfiends
                {
                    this.tag(TCOTS_Tags.ROTFIEND)
                            .add(Biomes.BIRCH_FOREST).add(Biomes.FOREST).add(Biomes.DARK_FOREST)
                            .add(Biomes.DRIPSTONE_CAVES)
                            .add(Biomes.OLD_GROWTH_PINE_TAIGA).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                            .add(Biomes.PLAINS).add(Biomes.TAIGA);
                }

                //Foglets
                {
                    this.tag(TCOTS_Tags.FOGLET_SWAMP)
                            .addOptionalTag(BiomeTags.HAS_RUINED_PORTAL_SWAMP.location())
                            .add(Biomes.RIVER);

                    this.tag(TCOTS_Tags.FOGLET_DARK)
                            .addOptionalTag(BiomeTags.HAS_WOODLAND_MANSION.location());

                    this.tag(TCOTS_Tags.FOGLET_HILLS_FORESTS)
                            .add(Biomes.OLD_GROWTH_PINE_TAIGA).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                            .add(Biomes.JAGGED_PEAKS).add(Biomes.STONY_PEAKS)
                            .addOptionalTag(BiomeTags.IS_HILL.location());
                }

                //Water Hags
                {
                    this.tag(TCOTS_Tags.WATER_HAG_SWAMP)
                            .addOptionalTag(BiomeTags.HAS_RUINED_PORTAL_SWAMP.location());

                    this.tag(TCOTS_Tags.WATER_HAG_RIVER)
                            .add(Biomes.RIVER);
                }

                //Grave Hags
                {

                    this.tag(TCOTS_Tags.GRAVE_HAG)
                            .add(Biomes.BIRCH_FOREST).add(Biomes.FOREST).add(Biomes.DARK_FOREST)
                            .add(Biomes.DRIPSTONE_CAVES)
                            .add(Biomes.OLD_GROWTH_PINE_TAIGA).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                            .add(Biomes.PLAINS).add(Biomes.SAVANNA).add(Biomes.TAIGA);
                }

                //Ghouls & Alghouls
                {
                    this.tag(TCOTS_Tags.GHOUL)
                            .add(Biomes.SAVANNA).add(Biomes.PLAINS)
                            .add(Biomes.TAIGA).add(Biomes.OLD_GROWTH_PINE_TAIGA).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                            .add(Biomes.FOREST).add(Biomes.FLOWER_FOREST).add(Biomes.BIRCH_FOREST)
                            .add(Biomes.DARK_FOREST);
                }

                //Scurvers
                {
                    this.tag(TCOTS_Tags.SCURVER)
                            .add(Biomes.BIRCH_FOREST).add(Biomes.FOREST).add(Biomes.DARK_FOREST)
                            .addOptionalTag(BiomeTags.HAS_VILLAGE_SAVANNA.location())
                            .addOptionalTag(BiomeTags.IS_JUNGLE.location());
                }

                //Devourer
                {
                    this.tag(TCOTS_Tags.DEVOURER)
                            .add(Biomes.BIRCH_FOREST).add(Biomes.FOREST)
                            .add(Biomes.SWAMP).add(Biomes.RIVER)
                            .add(Biomes.PLAINS);
                }

                //Bloedzuiger
                {
                    this.tag(TCOTS_Tags.BLOEDZUIGER)
                            .addOptionalTag(BiomeTags.HAS_RUINED_PORTAL_SWAMP.location());
                }

                //Graveir
                {
                    this.tag(TCOTS_Tags.GRAVEIR)
                            .add(Biomes.PLAINS)
                            .add(Biomes.SUNFLOWER_PLAINS)
                            .add(Biomes.SNOWY_PLAINS)
                            .add(Biomes.ICE_SPIKES)
                            .add(Biomes.DESERT)
                            .add(Biomes.SWAMP)
                            .add(Biomes.MANGROVE_SWAMP)
                            .add(Biomes.FOREST)
                            .add(Biomes.FLOWER_FOREST)
                            .add(Biomes.BIRCH_FOREST)
                            .add(Biomes.DARK_FOREST)
                            .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
                            .add(Biomes.OLD_GROWTH_PINE_TAIGA)
                            .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                            .add(Biomes.TAIGA)
                            .add(Biomes.SNOWY_TAIGA)
                            .add(Biomes.SAVANNA)
                            .add(Biomes.SAVANNA_PLATEAU)
                            .add(Biomes.WINDSWEPT_HILLS)
                            .add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
                            .add(Biomes.WINDSWEPT_FOREST)
                            .add(Biomes.WINDSWEPT_SAVANNA)
                            .add(Biomes.JUNGLE)
                            .add(Biomes.SPARSE_JUNGLE)
                            .add(Biomes.BAMBOO_JUNGLE)
                            .add(Biomes.BADLANDS)
                            .add(Biomes.ERODED_BADLANDS)
                            .add(Biomes.WOODED_BADLANDS)
                            .add(Biomes.MEADOW)
                            .add(Biomes.CHERRY_GROVE)
                            .add(Biomes.GROVE)
                            .add(Biomes.SNOWY_SLOPES)
                            .add(Biomes.FROZEN_PEAKS)
                            .add(Biomes.JAGGED_PEAKS)
                            .add(Biomes.STONY_PEAKS)
                            .add(Biomes.RIVER)
                            .add(Biomes.FROZEN_RIVER)
                            .add(Biomes.BEACH)
                            .add(Biomes.SNOWY_BEACH)
                            .add(Biomes.STONY_SHORE)
                            .add(Biomes.WARM_OCEAN)
                            .add(Biomes.LUKEWARM_OCEAN)
                            .add(Biomes.DEEP_LUKEWARM_OCEAN)
                            .add(Biomes.OCEAN)
                            .add(Biomes.DEEP_OCEAN)
                            .add(Biomes.COLD_OCEAN)
                            .add(Biomes.DEEP_COLD_OCEAN)
                            .add(Biomes.FROZEN_OCEAN)
                            .add(Biomes.DEEP_FROZEN_OCEAN)
                            .add(Biomes.DRIPSTONE_CAVES);
                }

            }

            //Ogroids
            {
                //Nekkers
                {
                    this.tag(TCOTS_Tags.NEKKER)
                            .add(Biomes.SAVANNA).add(Biomes.PLAINS)
                            .addOptionalTag(BiomeTags.IS_JUNGLE.location())
                            .addOptionalTag(BiomeTags.IS_FOREST.location());
                }

                //Cyclops
                {
                    this.tag(TCOTS_Tags.CYCLOPS)
                            .add(Biomes.SNOWY_PLAINS)
                            .add(Biomes.STONY_SHORE)
                            .addOptionalTag(BiomeTags.IS_HILL.location())
                            .add(Biomes.MEADOW).add(Biomes.FROZEN_PEAKS).add(Biomes.JAGGED_PEAKS).add(Biomes.STONY_PEAKS).add(Biomes.SNOWY_SLOPES)
                            .add(Biomes.OLD_GROWTH_PINE_TAIGA).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA);
                }

                //Rock Troll
                {
                    this.tag(TCOTS_Tags.ROCK_TROLL)
                            .add(Biomes.STONY_PEAKS).add(Biomes.MEADOW)
                            .addOptionalTag(BiomeTags.IS_HILL.location())
                            .add(Biomes.TAIGA).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA).add(Biomes.OLD_GROWTH_PINE_TAIGA)
                            .add(Biomes.DRIPSTONE_CAVES).add(Biomes.STONY_SHORE);
                }

                //Ice Troll
                {
                    this.tag(TCOTS_Tags.ICE_TROLL)
                            .add(Biomes.SNOWY_PLAINS)
                            .add(Biomes.ICE_SPIKES)
                            .add(Biomes.SNOWY_TAIGA)
                            .add(Biomes.FROZEN_PEAKS)
                            .add(Biomes.JAGGED_PEAKS)
                            .add(Biomes.SNOWY_SLOPES)
                            .add(Biomes.GROVE);
                }


                //Forest Troll
                {
                    this.tag(TCOTS_Tags.FOREST_TROLL)
                            .addOptionalTag(BiomeTags.IS_FOREST.location());
                }
            }
        }

//            All overworld biomes
//                    .add(Biomes.PLAINS)
//                    .add(Biomes.SUNFLOWER_PLAINS)
//                    .add(Biomes.SNOWY_PLAINS)
//                    .add(Biomes.ICE_SPIKES)
//                    .add(Biomes.DESERT)
//                    .add(Biomes.SWAMP)
//                    .add(Biomes.MANGROVE_SWAMP)
//                    .add(Biomes.FOREST)
//                    .add(Biomes.FLOWER_FOREST)
//                    .add(Biomes.BIRCH_FOREST)
//                    .add(Biomes.DARK_FOREST)
//                    .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
//                    .add(Biomes.OLD_GROWTH_PINE_TAIGA)
//                    .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
//                    .add(Biomes.TAIGA)
//                    .add(Biomes.SNOWY_TAIGA)
//                    .add(Biomes.SAVANNA)
//                    .add(Biomes.SAVANNA_PLATEAU)
//                    .add(Biomes.WINDSWEPT_HILLS)
//                    .add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
//                    .add(Biomes.WINDSWEPT_FOREST)
//                    .add(Biomes.WINDSWEPT_SAVANNA)
//                    .add(Biomes.JUNGLE)
//                    .add(Biomes.SPARSE_JUNGLE)
//                    .add(Biomes.BAMBOO_JUNGLE)
//                    .add(Biomes.BADLANDS)
//                    .add(Biomes.ERODED_BADLANDS)
//                    .add(Biomes.WOODED_BADLANDS)
//                    .add(Biomes.MEADOW)
//                    .add(Biomes.CHERRY_GROVE)
//                    .add(Biomes.GROVE)
//                    .add(Biomes.SNOWY_SLOPES)
//                    .add(Biomes.FROZEN_PEAKS)
//                    .add(Biomes.JAGGED_PEAKS)
//                    .add(Biomes.STONY_PEAKS)
//                    .add(Biomes.RIVER)
//                    .add(Biomes.FROZEN_RIVER)
//                    .add(Biomes.BEACH)
//                    .add(Biomes.SNOWY_BEACH)
//                    .add(Biomes.STONY_SHORE)
//                    .add(Biomes.WARM_OCEAN)
//                    .add(Biomes.LUKEWARM_OCEAN)
//                    .add(Biomes.DEEP_LUKEWARM_OCEAN)
//                    .add(Biomes.OCEAN)
//                    .add(Biomes.DEEP_OCEAN)
//                    .add(Biomes.COLD_OCEAN)
//                    .add(Biomes.DEEP_COLD_OCEAN)
//                    .add(Biomes.FROZEN_OCEAN)
//                    .add(Biomes.DEEP_FROZEN_OCEAN)
//                    .add(Biomes.MUSHROOM_FIELDS)
//                    .add(Biomes.DRIPSTONE_CAVES)
//                    .add(Biomes.LUSH_CAVES)
//                    .add(Biomes.DEEP_DARK);
//            ;

    }
}