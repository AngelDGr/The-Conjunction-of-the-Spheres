package neoforge.TCOTS.datagen.providers;


import TCOTS.TCOTS_Main;
import TCOTS.registry.TCOTS_Tags;
import TCOTS.registry.TCOTS_Blocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TCOTS_BlockTagsGenerator extends BlockTagsProvider {

    public TCOTS_BlockTagsGenerator(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable final ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, TCOTS_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(final HolderLookup.@NotNull Provider lookup) {
        this.tag(TCOTS_Tags.IGNITING_BLOCKS)
                .add(Blocks.FIRE)
                .add(Blocks.SOUL_FIRE)
                .add(Blocks.MAGMA_BLOCK);

        this.tag(TCOTS_Tags.DESTROYABLE_MAGIC_BLOCKS)
                .add(Blocks.NETHER_PORTAL)
                .add(Blocks.END_GATEWAY)
                .add(Blocks.SOUL_FIRE);

        this.tag(TCOTS_Tags.NEGATES_DEVOURER_JUMP)
                .add(Blocks.HAY_BLOCK)
                .add(Blocks.MOSS_BLOCK)
                .add(Blocks.COBWEB)
                .add(Blocks.SCULK)
                .add(Blocks.SLIME_BLOCK)
                .add(Blocks.HONEY_BLOCK)

                .addOptionalTag(BlockTags.BEDS.location())
                .addOptionalTag(BlockTags.WOOL.location())
                .addOptionalTag(BlockTags.LEAVES.location())
                .addOptionalTag(BlockTags.WART_BLOCKS.location());

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(TCOTS_Blocks.GiantAnchor())
                .add(TCOTS_Blocks.AlchemyTable())
                .add(TCOTS_Blocks.HerbalTable())
                .add(TCOTS_Blocks.SewantMushroomBlock())
                .add(TCOTS_Blocks.SewantMushroomStem())
                .add(TCOTS_Blocks.PuffballMushroomBlock());

        this.tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(TCOTS_Blocks.NestSlab())
                .add(TCOTS_Blocks.MonsterNest());

        this.tag(BlockTags.BEE_GROWABLES)
                .add(TCOTS_Blocks.ArenariaBush())
                .add(TCOTS_Blocks.CelandinePlant())
                .add(TCOTS_Blocks.BryoniaVine())
                .add(TCOTS_Blocks.VerbenaFlower())
                .add(TCOTS_Blocks.HanFiberPlant())
                .add(TCOTS_Blocks.CrowsEyeFern());

        this.tag(BlockTags.FLOWERS)
                .add(TCOTS_Blocks.ArenariaBush())
                .add(TCOTS_Blocks.CelandinePlant())
                .add(TCOTS_Blocks.BryoniaVine())
                .add(TCOTS_Blocks.VerbenaFlower())
                .add(TCOTS_Blocks.HanFiberPlant());
    }
}