package TCOTS.particles.bombEmitters;

import TCOTS.particles.TCOTS_Particles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class DevilsPuffball_ExplosionEmitterParticle extends NoRenderParticle {
    private int age;
    private final int maxAge;

    DevilsPuffball_ExplosionEmitterParticle(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f, 0.0, 0.0, 0.0);
        this.maxAge = 4;
    }

    @Override
    public void tick() {
        for (int i = 0; i < 8; ++i) {
            double d = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 3.0;
            double e = this.y + (this.random.nextDouble()) * 4.0;
            double f = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 3.0;
            this.world.addParticle(TCOTS_Particles.DEVILS_PUFFBALL_EXPLOSION_PARTICLE, d, e, f, 0, 0.0, 0.0);
        }
        ++this.age;
        if (this.age == this.maxAge) {
            this.markDead();
        }
    }

    @Environment(value= EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        @Override
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new DevilsPuffball_ExplosionEmitterParticle(clientWorld, d, e, f);
        }
    }
}
