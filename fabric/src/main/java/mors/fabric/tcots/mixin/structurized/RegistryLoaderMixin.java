package mors.fabric.tcots.mixin.structurized;

import com.mojang.serialization.Decoder;
import mors.fabric.tcots.structurized.api.StructurePoolAddCallback;
import mors.fabric.tcots.structurized.impl.FabricStructurePoolImpl;
import mors.fabric.tcots.structurized.impl.FabricStructurePoolRegistry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Optional;

/**
 Library borrowed from <a href="https://github.com/fzzyhmstrs/structurized-reborn">Structurized Reborn</a> by <a href="https://modrinth.com/user/fzzyhmstrs">fzzyhmstrs</a>
 */
@Mixin(RegistryDataLoader.class)
public class RegistryLoaderMixin {

    @Inject(method = "loadContentsFromManager(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/RegistryOps$RegistryInfoLookup;Lnet/minecraft/core/WritableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V", at = @At("TAIL"))
    private static <E> void load(final ResourceManager resourceManager, final RegistryOps.RegistryInfoLookup infoGetter, final WritableRegistry<E> registry, final Decoder<E> elementDecoder, final Map<ResourceKey<?>, Exception> errors, final CallbackInfo ci) {
        if (registry.key().equals(Registries.TEMPLATE_POOL)) {
            for (final E registryEntry : registry.stream().toList()) {
                if (!(registryEntry instanceof final StructureTemplatePool pool)) {
                    continue;
                }
                final ResourceLocation id = registry.getKey(registryEntry);
                if (FabricStructurePoolRegistry.registryEntryLookup == null) {
                    final Optional<RegistryOps.RegistryInfo<StructureProcessorList>> optionalRegistryInfo = infoGetter.lookup(Registries.PROCESSOR_LIST);
                    optionalRegistryInfo.ifPresent(info ->
                            FabricStructurePoolRegistry.registryEntryLookup = info.getter());
                }
                //System.out.println("successfully registered a callback");
                StructurePoolAddCallback.EVENT.invoker().onAdd(new FabricStructurePoolImpl(pool, id));
            }
        }
    }
}