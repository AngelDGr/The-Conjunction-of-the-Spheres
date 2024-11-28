package TCOTS.mixin;

import TCOTS.TCOTS_Main;
import TCOTS.entity.necrophages.GhoulEntity;
import TCOTS.sounds.GhoulRegeneratingSoundInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value= EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin implements TickablePacketListener, ClientPlayPacketListener {
    @Shadow private ClientWorld world;

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onEntityStatus", at = @At("TAIL"), cancellable = true)
    private void injectGhoulSound(@NotNull EntityStatusS2CPacket packet, CallbackInfo ci) {
        Entity entity = packet.getEntity(this.world);
        if (entity != null) {
            if (packet.getStatus() == GhoulEntity.GHOUL_REGENERATING) {
                this.client.getSoundManager().play(new GhoulRegeneratingSoundInstance((GhoulEntity) entity));
                ci.cancel();
            }
        }
    }

    @Inject(method = "onPlayerRespawn", at = @At("TAIL"))
    private void injectChangesInEyesRespawn(PlayerRespawnS2CPacket packet, CallbackInfo ci){
        TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(new TCOTS_Main.WitcherEyesFullPacket(
                TCOTS_Main.CONFIG.witcher_eyes.activateEyes(),
                TCOTS_Main.CONFIG.witcher_eyes.eyeShape().ordinal(),
                TCOTS_Main.CONFIG.witcher_eyes.eyeSeparation().ordinal(),
                TCOTS_Main.CONFIG.witcher_eyes.XEyePos(),
                TCOTS_Main.CONFIG.witcher_eyes.YEyePos()
                )
        );

        TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(new TCOTS_Main.ToxicityFacePacket(
                        TCOTS_Main.CONFIG.witcher_eyes.activateToxicity())
        );

    }


}
