package fabric.TCOTS.structurized.impl;

import fabric.TCOTS.structurized.api.FabricStructurePool;
import fabric.TCOTS.structurized.api.StructurePoolAddCallback;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.pools.ListPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

/**
 Library borrowed from <a href="https://github.com/fzzyhmstrs/structurized-reborn">Structurized Reborn</a> by <a href="https://modrinth.com/user/fzzyhmstrs">fzzyhmstrs</a>
 */
@SuppressWarnings("unused")
public class FabricStructurePoolRegistry {

    private static final Multimap<String, Quintuple<String,String,ResourceKey<StructureProcessorList>,String, Integer>> structures_info = LinkedHashMultimap.create();
    private static final Map<String,String> structures_key_ref = new HashMap<>();
    private static final Multimap<String, Tuple<String, Holder<PlacedFeature>>> feature_structures = LinkedHashMultimap.create();
    private static final Multimap<String, ListPoolElement> list_structures = LinkedHashMultimap.create();
    public static HolderGetter<StructureProcessorList> registryEntryLookup;

    public static void registerSimple(ResourceLocation poolId,ResourceLocation structureId, int weight){
        register(poolId,structureId,weight,ProcessorLists.EMPTY,StructureTemplatePool.Projection.RIGID, StructurePoolElementType.LEGACY);
    }

    public static void register(ResourceLocation poolId,ResourceLocation structureId, int weight, ResourceKey<StructureProcessorList> processor){
        register(poolId,structureId,weight,processor,StructureTemplatePool.Projection.RIGID,StructurePoolElementType.LEGACY);
    }

    public static void register(ResourceLocation poolId,ResourceLocation structureId, int weight, ResourceKey<StructureProcessorList> processor, StructureTemplatePool.Projection projection){
        register(poolId,structureId,weight,processor,projection,StructurePoolElementType.LEGACY);
    }

    public static void register(ResourceLocation poolId,ResourceLocation structureId, int weight, ResourceKey<StructureProcessorList> processor, StructureTemplatePool.Projection projection ,StructurePoolElementType<?> type){
        String poolType = Objects.requireNonNull(BuiltInRegistries.STRUCTURE_POOL_ELEMENT.getKey(type)).toString();
        String projectionId = projection.getName();
        structures_info.put(poolId.toString(), new Quintuple<>(structureId.toString(), poolType, processor, projectionId, weight));
        structures_key_ref.put(structureId.toString(),poolId.toString());
    }

    public static void registerFeature(ResourceLocation poolId, ResourceLocation structureId, int weight, StructureTemplatePool.Projection projection, Holder<PlacedFeature> entry){
        register(poolId,structureId,weight,ProcessorLists.EMPTY,projection,StructurePoolElementType.FEATURE);
        feature_structures.put(poolId.toString(), new Tuple<>(structureId.toString(),entry));
    }

    public static void registerList(ResourceLocation poolId, int weight, ListPoolElement listPoolElement){
        register(poolId,ResourceLocation.parse("minecraft:air"),weight,ProcessorLists.EMPTY, StructureTemplatePool.Projection.RIGID,StructurePoolElementType.LIST);
        list_structures.put(poolId.toString(), listPoolElement);
    }

    public static @Nullable Triple<String,String,String> getPoolStructureElementInfo(String id){
        String poolId = structures_key_ref.get(id);
        for (Quintuple<String,String,ResourceKey<StructureProcessorList>,String, Integer> quint : structures_info.get(poolId)){
            if (quint.a.equals(id)){
                return Triple.of(quint.b, quint.c.location().toString(), quint.d);
            }
        }
        return null;
    }

    public static void processRegistry(FabricStructurePool structurePool){
        String poolId = structurePool.getId().toString();
        //System.out.println(poolId);
        for (String key : structures_info.keys()){
            if (Objects.equals(key, poolId)){
                //System.out.println("found a match with " + key);
                structures_info.get(key).forEach(value -> addToPool(structurePool,value, key,registryEntryLookup)
                );
                //structurePool.getUnderlyingPool().getElementIndicesInRandomOrder(new LocalRandom(5)).forEach(value -> System.out.println(value.toString()));
            }
        }
    }

    private static void addToPool(FabricStructurePool structurePool, Quintuple<String,String,ResourceKey<StructureProcessorList>,String, Integer> quint, String key, HolderGetter<StructureProcessorList> registryEntryLookup){
        List<StructurePoolElement> spe = new LinkedList<>();
        StructurePoolElementType<?> type = BuiltInRegistries.STRUCTURE_POOL_ELEMENT.get(ResourceLocation.parse(quint.b));
        if (Objects.equals(type, StructurePoolElementType.SINGLE)){
            Holder<StructureProcessorList> entry = registryEntryLookup.getOrThrow(quint.c);
            spe.add(StructurePoolElement.single(quint.a,entry).apply(StructureTemplatePool.Projection.byName(quint.d)));
        } else if (Objects.equals(type, StructurePoolElementType.LEGACY)){
            //System.out.println("adding " + quint.a);
            Holder<StructureProcessorList> entry = registryEntryLookup.getOrThrow(quint.c);
            spe.add(StructurePoolElement.legacy(quint.a,entry).apply(StructureTemplatePool.Projection.byName(quint.d)));
        }else if (Objects.equals(type, StructurePoolElementType.LIST)){
            spe.addAll(list_structures.get(key));
        }else if (Objects.equals(type, StructurePoolElementType.FEATURE)){
            List<StructurePoolElement> finalSpe = new LinkedList<>();
            feature_structures.get(key).forEach(
                    value -> {if(value.getA().equals(quint.a)){
                        finalSpe.add(StructurePoolElement.feature(value.getB()).apply(StructureTemplatePool.Projection.byName(quint.d)));
                    }}
            );
            spe.addAll(finalSpe);
        } else {
            spe.add(StructurePoolElement.empty().apply(StructureTemplatePool.Projection.RIGID));
        }
        spe.forEach(value -> structurePool.addStructurePoolElement(value,quint.e));
    }

    static{
        StructurePoolAddCallback.EVENT.register(FabricStructurePoolRegistry::processRegistry);
    }

    private record  Quintuple<A, B, C, D, E>(A a, B b, C c, D d, E e) {


    }
}
