package TCOTS.mixin;

import TCOTS.potions.recipes.AlchemyTableRecipe;
import TCOTS.screen.AlchemyTableScreenHandler;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.server.network.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onCraftRequest", at = @At("HEAD"), cancellable = true)
    private void manageAlchemyTableRecipes(CraftRequestC2SPacket packet, CallbackInfo ci){
        if(player.currentScreenHandler instanceof AlchemyTableScreenHandler){
            ServerPlayNetworkHandler thisObject = (ServerPlayNetworkHandler)(Object)this;
            NetworkThreadUtils.forceMainThread(packet, thisObject, this.player.getServerWorld());
            this.player.updateLastActionTime();
            if (this.player.isSpectator() || this.player.currentScreenHandler.syncId != packet.getSyncId() || !(this.player.currentScreenHandler instanceof AlchemyTableScreenHandler)) {
                return;
            }

            player.server.getRecipeManager().get(packet.getRecipe()).ifPresent(
                    recipe -> ((AlchemyTableScreenHandler)this.player.currentScreenHandler).CraftWithBook((AlchemyTableRecipe) recipe.value(), this.player));

            ci.cancel();
        }
    }
}
