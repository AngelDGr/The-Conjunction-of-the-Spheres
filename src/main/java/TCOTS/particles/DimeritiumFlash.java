package TCOTS.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;

public class DimeritiumFlash {
    @Environment(value= EnvType.CLIENT)
    public static class FlashFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public FlashFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            DimeritiumFlash.Flash flash = new DimeritiumFlash.Flash(clientWorld, d, e, f);
            flash.setSprite(this.spriteProvider);
            return flash;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static class Flash extends SpriteBillboardParticle {
        Flash(ClientWorld clientWorld, double d, double e, double f) {
            super(clientWorld, d, e, f);
            this.maxAge = 4;
        }

        @Override
        public ParticleTextureSheet getType() {
            return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
        }

        @Override
        public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
            this.setAlpha(0.6f - ((float)this.age + tickDelta - 1.0f) * 0.25f * 0.5f);
            super.buildGeometry(vertexConsumer, camera, tickDelta);
        }

        @Override
        public float getSize(float tickDelta) {
            return 7.1f * MathHelper.sin(((float)this.age + tickDelta - 1.0f) * 0.25f * (float)Math.PI);
        }
    }
}
