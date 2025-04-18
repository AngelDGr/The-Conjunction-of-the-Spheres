package TCOTS.mixin;

import TCOTS.TCOTS_Main;
import net.fabricmc.loader.api.FabricLoader;
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
import org.spongepowered.asm.mixin.injection.Redirect;

public class WitcherRPGChangeModels {
    @SuppressWarnings("unused")
    @Mixin(ItemRenderer.class)
    private static class ItemRenderMixin {
        @Shadow @Final private ItemModelShaper itemModelShaper;

        @Redirect(method = "<init>", at = @At(value = "INVOKE",
                target = "Lnet/minecraft/client/renderer/ItemModelShaper;register(Lnet/minecraft/world/item/Item;Lnet/minecraft/client/resources/model/ModelResourceLocation;)V"))
        private void changeModels(ItemModelShaper instance, Item item, ModelResourceLocation modelId){
            if(FabricLoader.getInstance().isModLoaded("witcher_rpg")){
                if(item instanceof SwordItem && BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(TCOTS_Main.MOD_ID)){
                    this.itemModelShaper.register(item,
                            ModelResourceLocation.inventory(
                                    ResourceLocation.fromNamespaceAndPath(BuiltInRegistries.ITEM.getKey(item).getNamespace(), BuiltInRegistries.ITEM.getKey(item).getPath()+"_rpg")
                            )
                    );
                } else {
                    this.itemModelShaper.register(item, ModelResourceLocation.inventory(BuiltInRegistries.ITEM.getKey(item)));
                }
            } else {
                this.itemModelShaper.register(item, ModelResourceLocation.inventory(BuiltInRegistries.ITEM.getKey(item)));
            }
        }
    }

    @SuppressWarnings("unused")
    @Mixin(ModelBakery.class)
    private static abstract class ModelLoaderMixin {
        @Shadow protected abstract void loadItemModelAndDependencies(ResourceLocation id);

        @Redirect(method = "<init>", at = @At(value = "INVOKE",
                target = "Lnet/minecraft/client/resources/model/ModelBakery;loadItemModelAndDependencies(Lnet/minecraft/resources/ResourceLocation;)V"))
        private void changeModels(ModelBakery instance, ResourceLocation id) {
            if(FabricLoader.getInstance().isModLoaded("witcher_rpg")) {
                if (BuiltInRegistries.ITEM.get(id) instanceof SwordItem && id.getNamespace().equals(TCOTS_Main.MOD_ID)) {
                    this.loadItemModelAndDependencies(
                            ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath() + "_rpg")
                    );
                } else {
                    this.loadItemModelAndDependencies(id);
                }
            } else {
                this.loadItemModelAndDependencies(id);
            }
        }
    }

}
