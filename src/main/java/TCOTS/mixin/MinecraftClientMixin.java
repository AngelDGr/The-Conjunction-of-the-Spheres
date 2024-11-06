package TCOTS.mixin;

import TCOTS.TCOTS_Main;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.weapons.GiantAnchorItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value= EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerEntity player;

    @Inject(method = "handleInputEvents", at = @At(value ="INVOKE",
            target = "Lnet/minecraft/client/MinecraftClient;doAttack()Z", shift = At.Shift.AFTER))
    private void injectAnchorRetrieving(CallbackInfo ci){
        if(this.player!=null) {
            ItemStack stack = this.player.getStackInHand(Hand.MAIN_HAND);
            if (stack.isOf(TCOTS_Items.GIANT_ANCHOR) && GiantAnchorItem.wasLaunched(stack)){
                TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(new TCOTS_Main.RetrieveAnchorPacket());
            }
        }
    }

}
