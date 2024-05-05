package TCOTS.particles.bombEmitters;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;

public class Samum_ExplosionEmitterParticle extends NoRenderParticle {
    private int age;
    private final int maxAge;

    Samum_ExplosionEmitterParticle(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f, 0.0, 0.0, 0.0);
        this.maxAge = 4;
    }

    @Override
    public void tick() {
        for (int i = 0; i < 8; ++i) {
            int random = this.random.nextBetween(0,1);
            double d = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 3.0;
            double e = this.y + (this.random.nextDouble()) * 4.0;
            double f = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 3.0;
            if (random==1) {
                this.world.addParticle(ParticleTypes.FIREWORK, d, e, f, 0, 0.1, 0.0);
            } else {
                this.world.addParticle(ParticleTypes.POOF, d, e, f, 0, 0.0, 0.0);
            }
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
            return new Samum_ExplosionEmitterParticle(clientWorld, d, e, f);
        }
    }
}