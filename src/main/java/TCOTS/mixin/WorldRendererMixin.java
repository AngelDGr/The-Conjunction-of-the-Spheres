package TCOTS.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
//    Lnet/minecraft/client/render/WorldRenderer;processWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V

    @Shadow
    private ClientWorld world;

    @Inject(method= "processWorldEvent", at = @At("HEAD"), cancellable = true)
    public void InjectCustomSpawnerParticles(int eventId, BlockPos pos, int data, CallbackInfo ci){
        Random random = this.world.random;
        if(eventId==8642097) {
            this.world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_GRAVEL_BREAK, SoundCategory.HOSTILE, 1.0f, 1.0f, false);
//            this.playSound(SoundEvents.BLOCK_GRAVEL_HIT, 1.0F, 1.0F);
            for (int j = 0; j < 20; ++j) {
                double ac = (double)pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 2.0;
                double ad = (double)pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 2.0;
                double ae = (double)pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 2.0;
                this.world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.DIRT.getDefaultState()), ac, ad, ae, 0.0, 0.0, 0.0);
                //Witch
                //Mycelium
                this.world.addParticle(ParticleTypes.MYCELIUM, ac, ad, ae, 0.0, 0.0, 0.0);
            }
//            break;
            ci.cancel();
        }

    }



}
