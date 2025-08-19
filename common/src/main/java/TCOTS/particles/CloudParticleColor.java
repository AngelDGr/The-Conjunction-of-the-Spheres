package TCOTS.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class CloudParticleColor extends TextureSheetParticle {
    private final SpriteSet spriteProvider;

    CloudParticleColor(final ClientLevel world, final double x, final double y, final double z, final double velocityX, final double velocityY, final double velocityZ, final SpriteSet spriteProvider) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        final float g;
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
        final int i = (int)(8.0 / (Math.random() * 0.8 + 0.3));
        this.lifetime = (int)Math.max((float)i * 2.5f, 1.0f);
        this.hasPhysics = false;
        this.setSpriteFromAge(spriteProvider);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getQuadSize(final float tickDelta) {
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
        public GreenCloudFactory(final SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        @Override
        public Particle createParticle(final @NotNull SimpleParticleType defaultParticleType, final @NotNull ClientLevel clientWorld, final double d, final double e, final double f, final double g, final double h, final double i) {
            final CloudParticleColor particle = new CloudParticleColor(clientWorld, d, e, f, g, h, i, this.spriteProvider);
            particle.setColor(135f/255f, 163f/255, 99f/255);
            particle.setAlpha(0.6f);
            return particle;
        }
    }


    public static class YellowCloudFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;
        public YellowCloudFactory(final SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        @Override
        public Particle createParticle(final @NotNull SimpleParticleType defaultParticleType, final @NotNull ClientLevel clientWorld, final double d, final double e, final double f, final double g, final double h, final double i) {
            final CloudParticleColor particle = new CloudParticleColor(clientWorld, d, e, f, g, h, i, this.spriteProvider);
            particle.setColor(0.9f, 0.9f, 0.0f);
            particle.setAlpha(0.6f);
            return particle;
        }
    }

    public static class CadaverineCloudFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;
        public CadaverineCloudFactory(final SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        @Override
        public Particle createParticle(final @NotNull SimpleParticleType defaultParticleType, final @NotNull ClientLevel clientWorld, final double d, final double e, final double f, final double g, final double h, final double i) {
            final CloudParticleColor particle = new CloudParticleColor(clientWorld, d, e, f, g, h, i, this.spriteProvider);
            particle.setColor(0f, 189f/255f, 19f/255);
            particle.setAlpha(0.6f);
            return particle;
        }
    }
}
