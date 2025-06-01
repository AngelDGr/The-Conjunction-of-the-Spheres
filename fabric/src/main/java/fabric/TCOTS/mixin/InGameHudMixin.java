package fabric.TCOTS.mixin;

import TCOTS.registry.TCOTS_Effects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(value= EnvType.CLIENT)
@Mixin(Gui.class)
public abstract class InGameHudMixin {
    @Shadow
    private Player getCameraPlayer() {
        return null;
    }
    @Shadow
    private int tickCount;
    @Shadow
    private int displayHealth;

    //Moving hearts for swallow
    @ModifyArg(method = "renderPlayerHealth", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/client/gui/Gui;renderHearts(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V"), index=5)
    private int InjectMovingHearths(int x){
        int j = this.displayHealth;
        Player playerEntity = this.getCameraPlayer();
        assert playerEntity != null;
        int i = Mth.ceil(playerEntity.getHealth());
        float f = Math.max((float)playerEntity.getAttributeValue(Attributes.MAX_HEALTH), (float)Math.max(j, i));

            if(playerEntity.hasEffect(TCOTS_Effects.GraveHagDecoctionEffect())){
                return this.tickCount % Mth.ceil(
                        ( f + 5.0f ) -
                        ((playerEntity.theConjunctionOfTheSpheres$getKillCount())*0.7));
            }

            if (playerEntity.hasEffect(TCOTS_Effects.SwallowEffect())) {
                return this.tickCount % Mth.ceil(f - 5.0F);
            }
        return x;
    }

}
