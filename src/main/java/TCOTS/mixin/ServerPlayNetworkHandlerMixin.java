package TCOTS.mixin;

import TCOTS.potions.recipes.AlchemyTableRecipe;
import TCOTS.potions.screen.AlchemyTableScreenHandler;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.c2s.play.CraftRequestC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {


    @Shadow
    public ServerPlayerEntity player;

    @Final
    @Shadow
    private MinecraftServer server;

    @Inject(method = "onCraftRequest", at = @At("HEAD"), cancellable = true)
    private void manageAlchemyTableRecipes(CraftRequestC2SPacket packet, CallbackInfo ci){
        if(player.currentScreenHandler instanceof AlchemyTableScreenHandler){
            ServerPlayNetworkHandler thisObject = (ServerPlayNetworkHandler)(Object)this;
            NetworkThreadUtils.forceMainThread(packet, thisObject, this.player.getServerWorld());
            this.player.updateLastActionTime();
            if (this.player.isSpectator() || this.player.currentScreenHandler.syncId != packet.getSyncId() || !(this.player.currentScreenHandler instanceof AlchemyTableScreenHandler)) {
                return;
            }

            this.server.getRecipeManager().get(packet.getRecipe()).ifPresent(recipe -> ((AlchemyTableScreenHandler)this.player.currentScreenHandler).Craft(packet.shouldCraftAll(), (AlchemyTableRecipe)recipe, this.player));

//            ((AlchemyTableScreenHandler) player.currentScreenHandler).getInventory().setStack(0, player.getInventory().getStack(0).copyAndEmpty());player.getInventory().markDirty();

            ci.cancel();
        }
//        System.out.println("You are here2");

    }

}
