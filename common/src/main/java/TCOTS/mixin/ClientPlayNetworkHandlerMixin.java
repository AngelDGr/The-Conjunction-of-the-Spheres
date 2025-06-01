package TCOTS.mixin;

import TCOTS.TCOTS_Main;
import TCOTS.entity.necrophages.GhoulEntity;
import TCOTS.sound.GhoulRegeneratingSoundInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value= EnvType.CLIENT)
@Mixin(ClientPacketListener.class)
public abstract class ClientPlayNetworkHandlerMixin extends ClientCommonPacketListenerImpl implements TickablePacketListener, ClientGamePacketListener {
    @Shadow private ClientLevel level;

    public ClientPlayNetworkHandlerMixin(Minecraft client, Connection connection, CommonListenerCookie connectionState) {
        super(client, connection, connectionState);
    }

    @Inject(method = "handleEntityEvent", at = @At("TAIL"), cancellable = true)
    private void injectGhoulSound(@NotNull ClientboundEntityEventPacket packet, CallbackInfo ci) {
        Entity entity = packet.getEntity(this.level);
        if (entity != null) {
            if (packet.getEventId() == GhoulEntity.GHOUL_REGENERATING) {
                this.minecraft.getSoundManager().play(new GhoulRegeneratingSoundInstance((GhoulEntity) entity));
                ci.cancel();
            }
        }
    }

    @Inject(method = "handleRespawn", at = @At("TAIL"))
    private void injectChangesInEyesRespawn(ClientboundRespawnPacket packet, CallbackInfo ci){
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
