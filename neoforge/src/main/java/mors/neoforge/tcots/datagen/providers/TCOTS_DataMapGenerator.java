package mors.neoforge.tcots.datagen.providers;

import mors.tcots.registry.TCOTS_Items;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class TCOTS_DataMapGenerator extends DataMapProvider {
        public TCOTS_DataMapGenerator(final PackOutput packOutput, final CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(packOutput, lookupProvider);
        }

        @Override
        protected void gather() {
//            registerCompostableItems()
            {
                builder(NeoForgeDataMaps.COMPOSTABLES)
                        .add(TCOTS_Items.ARENARIA, new Compostable(0.65f), false)
                        .add(TCOTS_Items.ALLSPICE, new Compostable(0.3f), false)

                        .add(TCOTS_Items.BRYONIA, new Compostable(0.65f), false)

                        .add(TCOTS_Items.CELANDINE, new Compostable(0.65f), false)
                        .add(TCOTS_Items.CROWS_EYE, new Compostable(0.65f), false)
                        .add(TCOTS_Items.CADAVERINE, new Compostable(0.85f), false)

                        .add(TCOTS_Items.HAN_FIBER, new Compostable(0.65f), false)

                        .add(TCOTS_Items.PUFFBALL, new Compostable(0.65f), false)
                        .add(TCOTS_Items.PUFFBALL_MUSHROOM_BLOCK_ITEM, new Compostable(0.85f), false)

                        .add(TCOTS_Items.SEWANT_MUSHROOMS, new Compostable(0.65f), false)
                        .add(TCOTS_Items.SEWANT_MUSHROOM_STEM_ITEM, new Compostable(0.85f), false)
                        .add(TCOTS_Items.SEWANT_MUSHROOM_BLOCK_ITEM, new Compostable(0.85f), false)

                        .add(TCOTS_Items.VERBENA, new Compostable(0.65f), false);
            }
        }
    }
