package TCOTS.particles;

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

        public FlashFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType defaultParticleType, @NotNull ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            DimeritiumFlash.Flash flash = new DimeritiumFlash.Flash(clientWorld, d, e, f);
            flash.pickSprite(this.spriteProvider);
            return flash;
        }
    }


    public static class Flash extends TextureSheetParticle {
        Flash(ClientLevel clientWorld, double d, double e, double f) {
            super(clientWorld, d, e, f);
            this.lifetime = 4;
        }

        @Override
        public @NotNull ParticleRenderType getRenderType() {
            return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        }

        @Override
        public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float tickDelta) {
            this.setAlpha(0.6f - ((float)this.age + tickDelta - 1.0f) * 0.25f * 0.5f);
            super.render(vertexConsumer, camera, tickDelta);
        }

        @Override
        public float getQuadSize(float tickDelta) {
            return 7.1f * Mth.sin(((float)this.age + tickDelta - 1.0f) * 0.25f * (float)Math.PI);
        }
    }
}
