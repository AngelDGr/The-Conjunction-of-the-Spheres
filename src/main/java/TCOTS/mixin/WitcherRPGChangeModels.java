package TCOTS.mixin;

import TCOTS.TCOTS_Main;
import TCOTS.utils.MiscUtil;
import com.llamalad7.mixinextras.sugar.Local;
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
import org.spongepowered.asm.mixin.injection.ModifyArg;

public class WitcherRPGChangeModels {
    @SuppressWarnings("all")
    @Mixin(ItemRenderer.class)
    private static class ItemRenderMixin {
        @Shadow @Final private ItemModels models;

        @ModifyArg(method = "<init>", at = @At(value = "INVOKE",
                target = "Lnet/minecraft/client/render/item/ItemModels;putModel(Lnet/minecraft/item/Item;Lnet/minecraft/client/util/ModelIdentifier;)V"), index = 1)
        private ModelIdentifier changeModels(ModelIdentifier modelId, @Local Item item){
            if((MiscUtil.isWitcherRPGLoaded() || TCOTS_Main.CONFIG.hasRPGTextures())
                    && item instanceof SwordItem && Registries.ITEM.getId(item).getNamespace().equals(TCOTS_Main.MOD_ID)){
                return new ModelIdentifier(
                        Identifier.of(Registries.ITEM.getId(item).getNamespace(), Registries.ITEM.getId(item).getPath()+"_rpg"),
                        "inventory");
            } else {
                return modelId;
            }
        }
    }

    @SuppressWarnings("all")
    @Mixin(ModelLoader.class)
    private static abstract class ModelLoaderMixin {

        @Shadow protected abstract void addModel(ModelIdentifier modelId);

        @ModifyArg(method = "<init>", at = @At(value = "INVOKE",
                target = "Lnet/minecraft/client/render/model/ModelLoader;addModel(Lnet/minecraft/client/util/ModelIdentifier;)V"))
        private ModelIdentifier changeModels(ModelIdentifier modelId) {

            if ((MiscUtil.isWitcherRPGLoaded() || TCOTS_Main.CONFIG.hasRPGTextures())
                    && Registries.ITEM.get(Identifier.of(modelId.getNamespace(), modelId.getPath())) instanceof SwordItem
                    && modelId.getNamespace().equals(TCOTS_Main.MOD_ID)) {
                return new ModelIdentifier(Identifier.of(modelId.getNamespace(), modelId.getPath() + "_rpg"),"inventory");
            } else {
                return modelId;
            }

        }
    }

}
