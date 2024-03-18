package TCOTS.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Final
    @Shadow
    private MinecraftClient client;

    @Inject(method = "onExplosion", at= @At("HEAD"), cancellable = true)
    public void onExplosion(ExplosionS2CPacket packet, CallbackInfo ci) {
        PlayerEntity entity = this.client.player;
        BlockPos check = new BlockPos(0,0,0);


        if(packet.getAffectedBlocks().size()==5) {
            if (
                Objects.equals(packet.getAffectedBlocks().get(0), check) &&
                Objects.equals(packet.getAffectedBlocks().get(1), check) &&
                Objects.equals(packet.getAffectedBlocks().get(2), check) &&
                Objects.equals(packet.getAffectedBlocks().get(3), check) &&
                Objects.equals(packet.getAffectedBlocks().get(4), check)
            ) {

                ClientPlayNetworkHandler thisObject = (ClientPlayNetworkHandler) (Object) this;
                NetworkThreadUtils.forceMainThread(packet, thisObject, this.client);
                Explosion explosion = new Explosion(this.client.world, entity, packet.getX(), packet.getY(), packet.getZ(), packet.getRadius(), packet.getAffectedBlocks());
                explosion.affectWorld(true);
                this.client.player.setVelocity(this.client.player.getVelocity().add(packet.getPlayerVelocityX(), packet.getPlayerVelocityY(), packet.getPlayerVelocityZ()));
                ci.cancel();
            }
        }
    }
}
