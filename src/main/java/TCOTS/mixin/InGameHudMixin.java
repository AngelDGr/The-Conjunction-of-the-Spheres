package TCOTS.mixin;

import TCOTS.potions.TCOTS_Effects;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(InGameHud.class)
public class InGameHudMixin {

//this.renderHealthBar(context, playerEntity, m, o, r, v, f, i, j, p, bl);
//      "La/b/c/renderHealthBar(LDrawContext,LPlayerEntity,IIIIFIIIZ)V"), index=5
//
    //      "Lnet/minecraft/client/gui/hud/InGameHud;renderHealthBar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIIZ)V"), index=5


    @ModifyArg(method = "renderStatusBars", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/client/gui/hud/InGameHud;renderHealthBar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIIZ)V"), index=5)
    private int InjectMovingHearths(int x){
        int j = this.renderHealthValue;
        PlayerEntity playerEntity = this.getCameraPlayer();
        int i = MathHelper.ceil(playerEntity.getHealth());
        float f = Math.max((float)playerEntity.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH), (float)Math.max(j, i));

            if(playerEntity.hasStatusEffect(TCOTS_Effects.GRAVE_HAG_DECOCTION_EFFECT)){
                return this.ticks % MathHelper.ceil(
                        ( f + 5.0f ) -
                        ((playerEntity.theConjunctionOfTheSpheres$getKillCount())*0.7));
            }

            if (playerEntity.hasStatusEffect(TCOTS_Effects.SWALLOW_EFFECT)) {
                return this.ticks % MathHelper.ceil(f - 5.0F);
            }
        return x;
    }

    @Shadow
    private PlayerEntity getCameraPlayer() {
        return null;
    }

    @Shadow
    private int ticks;

    @Shadow
    private int renderHealthValue;

}
