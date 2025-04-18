package TCOTS.structurized.impl;

import com.mojang.datafixers.util.Pair;
import TCOTS.structurized.api.FabricStructurePool;
import TCOTS.mixin.structurized.StructurePoolAccessor;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

/**
 Library borrowed from <a href="https://github.com/fzzyhmstrs/structurized-reborn">Structurized Reborn</a> by <a href="https://modrinth.com/user/fzzyhmstrs">fzzyhmstrs</a>
 */
public class FabricStructurePoolImpl implements FabricStructurePool {
    private final StructureTemplatePool pool;
    private final ResourceLocation id;

    public FabricStructurePoolImpl(StructureTemplatePool pool, ResourceLocation id) {
        this.pool = pool;
        this.id = id;
    }

    @Override
    public void addStructurePoolElement(StructurePoolElement element) {
        addStructurePoolElement(element, 1);
    }

    @Override
    public void addStructurePoolElement(StructurePoolElement element, int weight) {
        //adds to elementCounts list; minecraft makes these immutable lists, so we replace them with an array list
        StructurePoolAccessor pool = (StructurePoolAccessor) getUnderlyingPool();

        if (pool.getRawTemplates() instanceof ArrayList) {
            pool.getRawTemplates().add(Pair.of(element, weight));
        } else {
            List<Pair<StructurePoolElement, Integer>> list = new ArrayList<>(pool.getRawTemplates());
            list.add(Pair.of(element, weight));
            pool.setRawTemplates(list);
        }

        //adds to elements list
        for (int i = 0; i < weight; i++) {
            pool.getTemplates().add(element);
        }
    }

    @Override
    public StructureTemplatePool getUnderlyingPool() {
        return pool;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }
}