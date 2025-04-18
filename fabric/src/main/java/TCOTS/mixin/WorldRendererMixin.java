package TCOTS.mixin;

import TCOTS.items.concoctions.TCOTS_Effects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value= EnvType.CLIENT)
@Mixin(LevelRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow
    private ClientLevel level;

    @Shadow @Final private Minecraft minecraft;

    @Shadow protected abstract boolean shouldShowEntityOutlines();

    @Inject(method= "levelEvent", at = @At("HEAD"), cancellable = true)
    public void controlSendWorldEvents(int eventId, BlockPos pos, int data, CallbackInfo ci){
        RandomSource random = this.level.random;
        //Particles for Monster Nest
        if(eventId==8642097) {
            this.level.playLocalSound(pos, SoundEvents.GRAVEL_BREAK, SoundSource.HOSTILE, 1.0f, 1.0f, false);
            for (int j = 0; j < 20; ++j) {
                double ac = (double)pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 2.0;
                double ad = (double)pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 2.0;
                double ae = (double)pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 2.0;
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

    @Redirect(method= "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;shouldEntityAppearGlowing(Lnet/minecraft/world/entity/Entity;)Z"))
    private boolean injectCatEffectOutline(Minecraft instance, Entity entity){
        if(this.shouldShowEntityOutlines() && this.canHaveCatEffect() && checkEntity(entity) ){
         return true;
        }

        return this.minecraft.shouldEntityAppearGlowing(entity);
    }

    @Unique
    private boolean checkEntity(Entity entity){
        assert this.minecraft.player != null;
        return (entity instanceof LivingEntity && !(entity instanceof ArmorStand)) && entity != this.minecraft.player && this.minecraft.player.distanceTo(entity) <= 30;
    }

    @Unique
    private boolean canHaveCatEffect(){
        assert this.minecraft.player != null;
        int lightBlock = this.minecraft.player.level().getBrightness(LightLayer.BLOCK, this.minecraft.player.blockPosition());
        int lightSky   = this.minecraft.player.level().getBrightness(LightLayer.SKY,   this.minecraft.player.blockPosition());
        return this.minecraft.player.hasEffect(TCOTS_Effects.CAT_EFFECT) && !(this.minecraft.player.isSpectator()) && ((lightBlock <=4 && lightSky <= 10) || (this.isNightTicks() && lightBlock <=4));
    }

    @Unique
    private boolean isNightTicks(){
        assert this.minecraft.player != null;
        long time = this.minecraft.player.level().getDayTime() % 24000;
        return time >= 13000 && time < 23000;
    }
}
