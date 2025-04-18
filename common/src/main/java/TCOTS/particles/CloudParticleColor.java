package TCOTS.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class CloudParticleColor extends TextureSheetParticle {
    private final SpriteSet spriteProvider;

    CloudParticleColor(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteSet spriteProvider) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        float g;
        this.friction = 0.96f;
        this.spriteProvider = spriteProvider;
        this.xd *= 0.1f;
        this.yd *= 0.1f;
        this.zd *= 0.1f;
        this.xd += velocityX;
        this.yd += velocityY;
        this.zd += velocityZ;
        this.rCol = g = 1.0f - (float)(Math.random() * (double)0.3f);
        this.gCol = g;
        this.bCol = g;
        this.quadSize *= 1.875f;
        int i = (int)(8.0 / (Math.random() * 0.8 + 0.3));
        this.lifetime = (int)Math.max((float)i * 2.5f, 1.0f);
        this.hasPhysics = false;
        this.setSpriteFromAge(spriteProvider);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getQuadSize(float tickDelta) {
        return this.quadSize * Mth.clamp(((float)this.age + tickDelta) / (float)this.lifetime * 32.0f, 0.0f, 1.0f);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.removed) {
            this.setSpriteFromAge(this.spriteProvider);
        }
    }


    public static class GreenCloudFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;
        public GreenCloudFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        @Override
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            CloudParticleColor particle = new CloudParticleColor(clientWorld, d, e, f, g, h, i, this.spriteProvider);
            particle.setColor(0.0f, 0.6f, 0.0f);
            particle.setAlpha(0.6f);
            return particle;
        }
    }


    public static class YellowCloudFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;
        public YellowCloudFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        @Override
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            CloudParticleColor particle = new CloudParticleColor(clientWorld, d, e, f, g, h, i, this.spriteProvider);
            particle.setColor(0.9f, 0.9f, 0.0f);
            particle.setAlpha(0.6f);
            return particle;
        }
    }
}
