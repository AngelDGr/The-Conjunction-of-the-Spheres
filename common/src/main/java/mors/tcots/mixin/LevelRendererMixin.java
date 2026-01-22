package mors.tcots.mixin;

import mors.tcots.items.armor.set.ArmorSet;
import mors.tcots.registry.TCOTS_Effects;
import mors.tcots.utils.TCOTS_EntitiesUtil;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value= EnvType.CLIENT)
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow
    private ClientLevel level;

    @Shadow @Final private Minecraft minecraft;

    @Shadow protected abstract boolean shouldShowEntityOutlines();

    @Inject(method= "levelEvent", at = @At("HEAD"), cancellable = true)
    public void tcots$controlSendWorldEvents(final int eventId, final BlockPos pos, final int data, final CallbackInfo ci){
        final RandomSource random = this.level.random;
        //Particles for Monster Nest
        if(eventId==8642097) {
            this.level.playLocalSound(pos, SoundEvents.GRAVEL_BREAK, SoundSource.HOSTILE, 1.0f, 1.0f, false);
            for (int j = 0; j < 20; ++j) {
                final double ac = (double)pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 2.0;
                final double ad = (double)pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 2.0;
                final double ae = (double)pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 2.0;
                this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.DIRT.defaultBlockState()), ac, ad, ae, 0.0, 0.0, 0.0);
                //Witch
                //Mycelium
                this.level.addParticle(ParticleTypes.MYCELIUM, ac, ad, ae, 0.0, 0.0, 0.0);
            }
            ci.cancel();
        }

        //SpawnEggSound when Right Click
        if(eventId==5829147){
            this.level.playLocalSound(pos, SoundEvents.GRAVEL_FALL, SoundSource.HOSTILE, 1.0f, 1.0f, false);
        }
    }

    @ModifyExpressionValue(method= "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;shouldEntityAppearGlowing(Lnet/minecraft/world/entity/Entity;)Z"))
    private boolean tcots$modifyAppearingOutline(final boolean original, @Local final Entity entity){
        assert this.minecraft.player != null;
        return original
                //Cat Effect
                || (this.shouldShowEntityOutlines() && this.tcots$canHaveCatEffect() && tcots$checkEntity(entity))
                //Raven's armor set
                || (entity instanceof final LivingEntity livingEntity && tcots$ravenArmorCheck(this.minecraft.player, livingEntity));
    }

    @Unique
    private boolean tcots$checkEntity(final Entity entity){
        assert this.minecraft.player != null;
        return (entity instanceof LivingEntity && !(entity instanceof ArmorStand)) && entity != this.minecraft.player && this.minecraft.player.distanceTo(entity) <= 30;
    }

    @Unique
    private boolean tcots$canHaveCatEffect(){
        assert this.minecraft.player != null;
        final int lightBlock = this.minecraft.player.level().getBrightness(LightLayer.BLOCK, this.minecraft.player.blockPosition());
        final int lightSky   = this.minecraft.player.level().getBrightness(LightLayer.SKY,   this.minecraft.player.blockPosition());
        return this.minecraft.player.hasEffect(TCOTS_Effects.CatEffect()) && !(this.minecraft.player.isSpectator()) && ((lightBlock <=4 && lightSky <= 10) || (this.tcots$isNightTicks() && lightBlock <=4));
    }

    @Unique
    private boolean tcots$isNightTicks(){
        assert this.minecraft.player != null;
        final long time = this.minecraft.player.level().getDayTime() % 24000;
        return time >= 13000 && time < 23000;
    }

    @Inject(method = "renderEntity", at = @At("HEAD"))
    private void tcots$changeEntityOutlineColorRaven(final Entity entity, final double cameraX, final double cameraY, final double cameraZ, final float tickDelta, final PoseStack matrices, final MultiBufferSource vertexConsumers, final CallbackInfo ci) {
        assert this.minecraft.player != null;
        //Armor set check
        if (entity instanceof final LivingEntity livingEntity && tcots$ravenArmorCheck(this.minecraft.player, livingEntity)
                //Cast
                && vertexConsumers instanceof final OutlineBufferSource outlineVertexConsumers) {
            outlineVertexConsumers.setColor(255, 0, 0, 255);
        }
    }

    @Unique
    private boolean tcots$ravenArmorCheck(final LivingEntity player, final LivingEntity livingEntity){
        return ArmorSet.RAVEN.hasFullBonus(player)
                //Is monster and low health
                && TCOTS_EntitiesUtil.isMonster(livingEntity) && livingEntity.getHealth()<livingEntity.getMaxHealth()*0.5
                //Is close enough
                && player.distanceTo(livingEntity) <= 40;
    }
}
