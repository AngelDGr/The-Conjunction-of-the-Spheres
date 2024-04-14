package TCOTS.mixin;

import TCOTS.entity.necrophages.GhoulEntity;
import TCOTS.sounds.GhoulRegeneratingSoundInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true) // Enables exporting for the targets of this mixin
@Environment(value= EnvType.CLIENT)
@Mixin(value = ClientPlayNetworkHandler.class,  priority = 99999)
public abstract class ClientPlayNetworkHandlerMixin extends ClientCommonNetworkHandler implements TickablePacketListener, ClientPlayPacketListener {
    @Shadow private ClientWorld world;

    public ClientPlayNetworkHandlerMixin(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }

    @Inject(method = "onEntityStatus", at = @At("TAIL"), cancellable = true)
    private void injectGhoulSound(EntityStatusS2CPacket packet, CallbackInfo ci) {

        Entity entity = packet.getEntity(this.world);
        if (entity != null) {
            if (packet.getStatus() == GhoulEntity.GHOUL_REGENERATING) {
                this.client.getSoundManager().play(new GhoulRegeneratingSoundInstance((GhoulEntity) entity));
                ci.cancel();
            }
        }
    }
}
