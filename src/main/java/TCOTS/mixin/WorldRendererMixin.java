package TCOTS.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow
    private ClientWorld world;

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

}
