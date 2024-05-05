package TCOTS.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;

public class CloudParticleColor extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    CloudParticleColor(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        float g;
        this.velocityMultiplier = 0.96f;
        this.spriteProvider = spriteProvider;
        this.velocityX *= 0.1f;
        this.velocityY *= 0.1f;
        this.velocityZ *= 0.1f;
        this.velocityX += velocityX;
        this.velocityY += velocityY;
        this.velocityZ += velocityZ;
        this.red = g = 1.0f - (float)(Math.random() * (double)0.3f);
        this.green = g;
        this.blue = g;
        this.scale *= 1.875f;
        int i = (int)(8.0 / (Math.random() * 0.8 + 0.3));
        this.maxAge = (int)Math.max((float)i * 2.5f, 1.0f);
        this.collidesWithWorld = false;
        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getSize(float tickDelta) {
        return this.scale * MathHelper.clamp(((float)this.age + tickDelta) / (float)this.maxAge * 32.0f, 0.0f, 1.0f);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.dead) {
            this.setSpriteForAge(this.spriteProvider);
        }
    }

    @Environment(value= EnvType.CLIENT)
    public static class GreenCloudFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public GreenCloudFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            CloudParticleColor particle = new CloudParticleColor(clientWorld, d, e, f, g, h, i, this.spriteProvider);
            particle.setColor(0.0f, 0.6f, 0.0f);
            particle.setAlpha(0.6f);
            return particle;
        }
    }
}
