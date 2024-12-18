package TCOTS.mixin;

import TCOTS.TCOTS_Main;
import TCOTS.items.concoctions.TCOTS_Effects;
import TCOTS.screen.TCOTS_HeartTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value= EnvType.CLIENT)
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
    @Shadow
    public abstract void drawHeart(DrawContext context, InGameHud.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half);

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
    private static final Identifier MUD_BALL_OVERLAY_1 = Identifier.of(TCOTS_Main.MOD_ID,"textures/gui/mudball_overlay1.png");
    @Unique
    private static final Identifier MUD_BALL_OVERLAY_2 = Identifier.of(TCOTS_Main.MOD_ID,"textures/gui/mudball_overlay2.png");
    @Unique
    private static final Identifier MUD_BALL_OVERLAY_3 = Identifier.of(TCOTS_Main.MOD_ID,"textures/gui/mudball_overlay3.png");
    @Unique
    private static final Identifier MUD_BALL_OVERLAY_4 = Identifier.of(TCOTS_Main.MOD_ID,"textures/gui/mudball_overlay4.png");
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
    @Inject(method = "renderMiscOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getFrozenTicks()I"))
    private void renderMudBall(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
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


    @Redirect(method = "renderHealthBar", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/InGameHud;drawHeart(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/gui/hud/InGameHud$HeartType;IIZZZ)V",
            ordinal = 3))
    private void injectEffectsHearts(InGameHud instance, DrawContext context, InGameHud.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half){
        PlayerEntity player = this.getCameraPlayer();
        if(player!=null) {

            if (player.theConjunctionOfTheSpheres$toxicityOverThreshold() && !player.hasStatusEffect(StatusEffects.WITHER)) {
                context.drawGuiTexture(TCOTS_HeartTypes.TOXIC.getTexture(hardcore,half,blinking), x, y, 9, 9);
            } else if (player.hasStatusEffect(TCOTS_Effects.CADAVERINE) && !player.hasStatusEffect(StatusEffects.WITHER)){
                context.drawGuiTexture(TCOTS_HeartTypes.CADAVERINE.getTexture(hardcore,half,blinking), x, y, 9, 9);
            }
            else {
                drawHeart(context, type, x, y, hardcore, blinking, half);
            }
        }
    }
}
