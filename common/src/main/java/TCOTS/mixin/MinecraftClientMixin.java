package TCOTS.mixin;

import TCOTS.TCOTS_Main;
import TCOTS.registry.TCOTS_Items;
import TCOTS.items.weapons.GiantAnchorItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value= EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftClientMixin {

    @Shadow @Nullable public LocalPlayer player;

    @Inject(method = "handleKeybinds", at = @At(value ="INVOKE",
            target = "Lnet/minecraft/client/Minecraft;startAttack()Z", shift = At.Shift.AFTER))
    private void injectAnchorRetrieving(CallbackInfo ci){
        if(this.player!=null) {
            ItemStack stack = this.player.getItemInHand(InteractionHand.MAIN_HAND);
            if (stack.is(TCOTS_Items.GIANT_ANCHOR.get()) && GiantAnchorItem.wasLaunched(stack)){
                TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(new TCOTS_Main.RetrieveAnchorPacket());
            }
        }
    }

}
