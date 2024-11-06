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
    @Inject(method = "load(Lnet/minecraft/registry/RegistryOps$RegistryInfoGetter;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V", at = @At("TAIL"))
    private static <E> void load(RegistryOps.RegistryInfoGetter registryInfoGetter, ResourceManager resourceManager, RegistryKey<? extends Registry<E>> registryRef, MutableRegistry<E> newRegistry, Decoder<E> decoder, Map<RegistryKey<?>, Exception> exceptions, CallbackInfo ci) {
        if (registryRef.equals(RegistryKeys.TEMPLATE_POOL)) {
            for(E registryEntry: newRegistry.stream().toList()) {
                if (registryEntry instanceof StructurePool pool) {
                    Identifier id = newRegistry.getId(registryEntry);
                    if (FabricStructurePoolRegistry.registryEntryLookup == null){
                        Optional<RegistryOps.RegistryInfo<StructureProcessorList>> opt = registryInfoGetter.getRegistryInfo(RegistryKeys.PROCESSOR_LIST);
                        opt.ifPresent(info -> FabricStructurePoolRegistry.registryEntryLookup = info.entryLookup());
                    }
                    //System.out.println("successfully registered a callback");
                    StructurePoolAddCallback.EVENT.invoker().onAdd(new FabricStructurePoolImpl(pool, id));
                }
            }
        }
    }
}