package TCOTS.mixin;

import TCOTS.TCOTS_Main;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class WitcherRPGChangeModels {
    @SuppressWarnings("unused")
    @Mixin(ItemRenderer.class)
    private static class ItemRenderMixin {
        @Shadow @Final private ItemModels models;

//        @Redirect(method = "<init>", at = @At(value = "INVOKE",
//                target = "Lnet/minecraft/client/render/item/ItemModels;putModel(Lnet/minecraft/item/Item;Lnet/minecraft/client/util/ModelIdentifier;)V"))
//        private void changeModels(ItemModels instance, Item item, ModelIdentifier modelId){
//            if(FabricLoader.getInstance().isModLoaded("witcher_rpg")){
//                if(item instanceof SwordItem && Registries.ITEM.getId(item).getNamespace().equals(TCOTS_Main.MOD_ID)){
//                    this.models.putModel(item, new ModelIdentifier(
//                                    Identifier.of(Registries.ITEM.getId(item).getNamespace(), Registries.ITEM.getId(item).getPath()+"_rpg"), "inventory"
//                            )
//                    );
//                } else {
//                    this.models.putModel(item, new ModelIdentifier(Registries.ITEM.getId(item), "inventory"));
//                }
//            } else {
//                this.models.putModel(item, new ModelIdentifier(Registries.ITEM.getId(item), "inventory"));
//            }
//        }
    }

    @SuppressWarnings("unused")
    @Mixin(ModelLoader.class)
    private static abstract class ModelLoaderMixin {
//        @Shadow protected abstract void loadInventoryVariantItemModel(Identifier id);
//
//        @Redirect(method = "<init>", at = @At(value = "INVOKE",
//                target = "Lnet/minecraft/client/render/model/ModelLoader;loadInventoryVariantItemModel(Lnet/minecraft/util/Identifier;)V"))
//        private void changeModels(ModelLoader instance, Identifier id) {
//            if(FabricLoader.getInstance().isModLoaded("witcher_rpg")) {
//                if (Registries.ITEM.get(id) instanceof SwordItem && id.getNamespace().equals(TCOTS_Main.MOD_ID)) {
//                    this.loadInventoryVariantItemModel(
//                            Identifier.of(id.getNamespace(), id.getPath() + "_rpg")
//                    );
//                } else {
//                    this.loadInventoryVariantItemModel(id);
//                }
//            } else {
//                this.loadInventoryVariantItemModel(id);
//            }
//        }
    }

}
