package TCOTS.mixin;

import TCOTS.potions.TCOTS_Effects;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow
    private ClientWorld world;

    @Shadow @Final private MinecraftClient client;

    @Shadow protected abstract boolean canDrawEntityOutlines();

    @Inject(method= "processWorldEvent", at = @At("HEAD"), cancellable = true)
    public void controlSendWorldEvents(int eventId, BlockPos pos, int data, CallbackInfo ci){
        Random random = this.world.random;
        //Particles for Monster Nest
        if(eventId==8642097) {
            this.world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_GRAVEL_BREAK, SoundCategory.HOSTILE, 1.0f, 1.0f, false);
            for (int j = 0; j < 20; ++j) {
                double ac = (double)pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 2.0;
                double ad = (double)pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 2.0;
                double ae = (double)pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 2.0;
                this.world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.DIRT.getDefaultState()), ac, ad, ae, 0.0, 0.0, 0.0);
                //Witch
                //Mycelium
                this.world.addParticle(ParticleTypes.MYCELIUM, ac, ad, ae, 0.0, 0.0, 0.0);
            }
            ci.cancel();
        }

        //SpawnEggSound when Right Click
        if(eventId==5829147){
            this.world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_GRAVEL_FALL, SoundCategory.HOSTILE, 1.0f, 1.0f, false);
        }
    }

    @Redirect(method= "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;hasOutline(Lnet/minecraft/entity/Entity;)Z"))
    private boolean injectCatEffectOutline(MinecraftClient instance, Entity entity){
        assert this.client.player != null;
        if( this.canDrawEntityOutlines() && this.canHaveCatEffect() && entity != this.client.player && this.client.player.distanceTo(entity) <= 30){
         return true;
        }
        return this.client.hasOutline(entity);
    }

    @Unique
    private boolean canHaveCatEffect(){
        assert this.client.player != null;
        int lightBlock = this.client.player.getWorld().getLightLevel(LightType.BLOCK, this.client.player.getBlockPos());
        int lightSky   = this.client.player.getWorld().getLightLevel(LightType.SKY,   this.client.player.getBlockPos());
        return this.client.player.hasStatusEffect(TCOTS_Effects.CAT_EFFECT) && !(this.client.player.isSpectator()) && ((lightBlock <=4 && lightSky <= 10) || (this.isNightTicks() && lightBlock <=4));
    }

    @Unique
    private boolean isNightTicks(){
        assert this.client.player != null;
        long time = this.client.player.getWorld().getTimeOfDay() % 24000;
        return time >= 13000 && time < 23000;
    }
}
