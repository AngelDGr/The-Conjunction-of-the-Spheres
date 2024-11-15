package TCOTS.mixin.structurized;

import com.mojang.serialization.Decoder;
import TCOTS.structurized.api.StructurePoolAddCallback;
import TCOTS.structurized.impl.FabricStructurePoolImpl;
import TCOTS.structurized.impl.FabricStructurePoolRegistry;
import net.minecraft.registry.*;
import net.minecraft.resource.ResourceManager;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Optional;

/**
 Library borrowed from <a href="https://github.com/fzzyhmstrs/structurized-reborn">Structurized Reborn</a> by <a href="https://modrinth.com/user/fzzyhmstrs">fzzyhmstrs</a>
 */
@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {
    @Inject(method = "loadFromResource(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryOps$RegistryInfoGetter;Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V", at = @At("TAIL"))
    private static <E> void load(ResourceManager resourceManager, RegistryOps.RegistryInfoGetter infoGetter, MutableRegistry<E> registry, Decoder<E> elementDecoder, Map<RegistryKey<?>, Exception> errors, CallbackInfo ci) {
        if (registry.getKey().equals(RegistryKeys.TEMPLATE_POOL)) {
            for (E registryEntry : registry.stream().toList()) {
                if (!(registryEntry instanceof StructurePool pool)) {
                    continue;
                }
                Identifier id = registry.getId(registryEntry);
                if (FabricStructurePoolRegistry.registryEntryLookup == null) {
                    Optional<RegistryOps.RegistryInfo<StructureProcessorList>> optionalRegistryInfo = infoGetter.getRegistryInfo(RegistryKeys.PROCESSOR_LIST);
                    optionalRegistryInfo.ifPresent(info ->
                            FabricStructurePoolRegistry.registryEntryLookup = info.entryLookup());
                }
                //System.out.println("successfully registered a callback");
                StructurePoolAddCallback.EVENT.invoker().onAdd(new FabricStructurePoolImpl(pool, id));
            }
        }
    }
}