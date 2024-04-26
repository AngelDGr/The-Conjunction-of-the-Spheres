package TCOTS.mixin;

import TCOTS.TCOTS_Main;
import TCOTS.potions.TCOTS_Effects;
import TCOTS.screen.ToxicityHudOverlay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow @Final private Random random;
    @Shadow protected abstract void renderOverlay(DrawContext context, Identifier texture, float opacity);
    @Shadow @Final private MinecraftClient client;
    @Shadow
    private PlayerEntity getCameraPlayer() {
        return null;
    }
    @Shadow
    private int ticks;
    @Shadow
    private int renderHealthValue;
    //Moving hearts for swallow
    @ModifyArg(method = "renderStatusBars", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/client/gui/hud/InGameHud;renderHealthBar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIIZ)V"), index=5)
    private int InjectMovingHearths(int x){
        int j = this.renderHealthValue;
        PlayerEntity playerEntity = this.getCameraPlayer();
        assert playerEntity != null;
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


    //MudballOverlay
    @Unique
    private static final Identifier MUD_BALL_OVERLAY_1 = new Identifier(TCOTS_Main.MOD_ID,"textures/gui/mudball_overlay1.png");
    @Unique
    private static final Identifier MUD_BALL_OVERLAY_2 = new Identifier(TCOTS_Main.MOD_ID,"textures/gui/mudball_overlay2.png");
    @Unique
    private static final Identifier MUD_BALL_OVERLAY_3 = new Identifier(TCOTS_Main.MOD_ID,"textures/gui/mudball_overlay3.png");
    @Unique
    private static final Identifier MUD_BALL_OVERLAY_4 = new Identifier(TCOTS_Main.MOD_ID,"textures/gui/mudball_overlay4.png");
    @Unique
    boolean changeOverlay=true;
    @Unique
    Identifier MUD_BALL_OVERLAY=MUD_BALL_OVERLAY_1;
    @Unique
    private void changeOverlay(){
        int random = this.random.nextBetween(0,3);
        if(changeOverlay) {
            switch (random) {
                case 0:
                    MUD_BALL_OVERLAY = MUD_BALL_OVERLAY_1;
                    changeOverlay=false;
                    break;
                case 1:
                    MUD_BALL_OVERLAY = MUD_BALL_OVERLAY_2;
                    changeOverlay=false;
                    break;
                case 2:
                    MUD_BALL_OVERLAY = MUD_BALL_OVERLAY_3;
                    changeOverlay=false;
                    break;
                case 3:
                    MUD_BALL_OVERLAY = MUD_BALL_OVERLAY_4;
                    changeOverlay=false;
                    break;
                default:
                    break;
            }
        }
    }
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getFrozenTicks()I"))
    private void renderMudBall(DrawContext context, float tickDelta, CallbackInfo ci){
        assert this.client.player != null;

        changeOverlay();

        if(this.client.player.theConjunctionOfTheSpheres$getMudInFace() > 0){
            this.renderOverlay(context, MUD_BALL_OVERLAY, this.client.player.theConjunctionOfTheSpheres$getMudTransparency());
        }
        else {
            if(!changeOverlay){
                changeOverlay=true;
            }
        }
    }

    //ToxicityHudRender
    @Inject(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;getCurrentGameMode()Lnet/minecraft/world/GameMode;", ordinal = 1))
    public void renderToxicityHud(DrawContext drawContext, float tickDelta, CallbackInfo callbackInfo) {
        ToxicityHudOverlay.onHudRender(drawContext,tickDelta);
    }

}
