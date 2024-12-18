package TCOTS.particles.bombEmitters;

import TCOTS.particles.TCOTS_Particles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

public class DragonsDream_ExplosionEmitterParticle extends NoRenderParticle {
    private int age;
    private final int maxAge;

    DragonsDream_ExplosionEmitterParticle(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f, 0.0, 0.0, 0.0);
        this.maxAge = 4;
    }

    @Override
    public void tick() {
        for (int i = 0; i < 8; ++i) {
            double d = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 3.0;
            double e = this.y + (this.random.nextDouble()) * 4.0;
            double f = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 3.0;
            this.world.addParticle(TCOTS_Particles.YELLOW_CLOUD, d, e, f, 0, 0.0, 0.0);
        }
        ++this.age;
        if (this.age == this.maxAge) {
            this.markDead();
        }
    }

    @Environment(value= EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new DragonsDream_ExplosionEmitterParticle(clientWorld, d, e, f);
        }
    }
}