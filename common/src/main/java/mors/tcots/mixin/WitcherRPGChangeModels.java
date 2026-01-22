package mors.tcots.mixin;

import mors.tcots.TCOTS_Main;
import mors.tcots.utils.TCOTS_Util;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

public class WitcherRPGChangeModels {
    @SuppressWarnings("unused")
    @Mixin(ItemRenderer.class)
    private static class ItemRenderMixin {
        @Shadow @Final private ItemModelShaper itemModelShaper;

        @ModifyArg(method = "<init>", at = @At(value = "INVOKE",
                target = "Lnet/minecraft/client/renderer/ItemModelShaper;register(Lnet/minecraft/world/item/Item;Lnet/minecraft/client/resources/model/ModelResourceLocation;)V"), index = 1)
        private ModelResourceLocation tcots$changeModels(final ModelResourceLocation modelId, @Local final Item item){
            if((TCOTS_Util.isWitcherRPGLoaded() || TCOTS_Main.CONFIG.hasRPGTextures())
                    && item instanceof SwordItem && BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(TCOTS_Main.MOD_ID)){
                return ModelResourceLocation.inventory(
                        ResourceLocation.fromNamespaceAndPath(BuiltInRegistries.ITEM.getKey(item).getNamespace(), BuiltInRegistries.ITEM.getKey(item).getPath()+"_rpg")
                );
            } else {
                return modelId;
            }
        }
    }

    @SuppressWarnings("unused")
    @Mixin(ModelBakery.class)
    private static abstract class ModelLoaderMixin {
        @Shadow protected abstract void loadItemModelAndDependencies(ResourceLocation id);

        @ModifyArg(method = "<init>", at = @At(value = "INVOKE",
                target = "Lnet/minecraft/client/resources/model/ModelBakery;loadItemModelAndDependencies(Lnet/minecraft/resources/ResourceLocation;)V"))
        private ResourceLocation tcots$changeModels(final ResourceLocation id) {
            if ((TCOTS_Util.isWitcherRPGLoaded() || TCOTS_Main.CONFIG.hasRPGTextures())
                    && BuiltInRegistries.ITEM.get(id) instanceof SwordItem && id.getNamespace().equals(TCOTS_Main.MOD_ID)) {
                return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath() + "_rpg");
            } else {
                return id;
            }
        }
    }

}
