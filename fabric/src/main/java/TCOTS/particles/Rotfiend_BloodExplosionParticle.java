package TCOTS.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;


public class Rotfiend_BloodExplosionParticle extends TextureSheetParticle {
    private final SpriteSet spriteProvider;

    protected Rotfiend_BloodExplosionParticle(ClientLevel world, double x, double y, double z, double d, SpriteSet spriteProvider) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        float f;
        this.lifetime = 6 + this.random.nextInt(4);
        this.rCol = f = this.random.nextFloat() * 0.6f + 0.4f;
        this.gCol = f;
        this.bCol = f;
        this.quadSize = 2.0f * (1.0f - (float)d * 0.5f);
        this.spriteProvider = spriteProvider;
        this.setSpriteFromAge(spriteProvider);
    }

    @Override
    public int getLightColor(float tint) {
        return 0xF000F0;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        this.setSpriteFromAge(this.spriteProvider);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Environment(value=EnvType.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new Rotfiend_BloodExplosionParticle(clientWorld, d, e, f, g, this.spriteProvider);
        }
    }
}
