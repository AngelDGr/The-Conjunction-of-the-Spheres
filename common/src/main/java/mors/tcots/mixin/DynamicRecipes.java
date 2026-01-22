package mors.tcots.mixin;

import mors.tcots.utils.TCOTS_Util;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(RecipeManager.class)
public class DynamicRecipes {

    @SuppressWarnings("all")
    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("HEAD"))
    public void tcots$interceptApply(final Map<ResourceLocation, JsonElement> map, final ResourceManager resourceManager, final ProfilerFiller profiler, final CallbackInfo ci) {
        final List<ResourceLocation> toRemove=new ArrayList<>();

        for(final ResourceLocation recipeName: map.keySet()){
            final String path = recipeName.getPath();

            if (path.endsWith(TCOTS_Util.isWitcherRPGLoaded()? "_standard": "_rpg")) {
                toRemove.add(recipeName);
            }
        }

        for(final ResourceLocation name: toRemove){
            map.remove(name);
        }
    }
}
