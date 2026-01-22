package mors.tcots.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class DimeritiumFlash {
    public static class FlashFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public FlashFactory(final SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(@NotNull final SimpleParticleType defaultParticleType, @NotNull final ClientLevel clientWorld, final double d, final double e, final double f, final double g, final double h, final double i) {
            final DimeritiumFlash.Flash flash = new DimeritiumFlash.Flash(clientWorld, d, e, f);
            flash.pickSprite(this.spriteProvider);
            return flash;
        }
    }


    public static class Flash extends TextureSheetParticle {
        Flash(final ClientLevel clientWorld, final double d, final double e, final double f) {
            super(clientWorld, d, e, f);
            this.lifetime = 4;
        }

        @Override
        public @NotNull ParticleRenderType getRenderType() {
            return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        }

        @Override
        public void render(@NotNull final VertexConsumer vertexConsumer, @NotNull final Camera camera, final float tickDelta) {
            this.setAlpha(0.6f - ((float)this.age + tickDelta - 1.0f) * 0.25f * 0.5f);
            super.render(vertexConsumer, camera, tickDelta);
        }

        @Override
        public float getQuadSize(final float tickDelta) {
            return 7.1f * Mth.sin(((float)this.age + tickDelta - 1.0f) * 0.25f * (float)Math.PI);
        }
    }
}
