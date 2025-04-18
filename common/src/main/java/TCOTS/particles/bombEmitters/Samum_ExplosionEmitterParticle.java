package TCOTS.particles.bombEmitters;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;

public class Samum_ExplosionEmitterParticle extends NoRenderParticle {
    private int age;
    private final int lifetime;

    Samum_ExplosionEmitterParticle(ClientLevel clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f, 0.0, 0.0, 0.0);
        this.lifetime = 4;
    }

    @Override
    public void tick() {
        for (int i = 0; i < 8; ++i) {
            int random = this.random.nextIntBetweenInclusive(0,1);
            double d = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 3.0;
            double e = this.y + (this.random.nextDouble()) * 4.0;
            double f = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 3.0;
            if (random==1) {
                this.level.addParticle(ParticleTypes.FIREWORK, d, e, f, 0, 0.1, 0.0);
            } else {
                this.level.addParticle(ParticleTypes.POOF, d, e, f, 0, 0.0, 0.0);
            }
        }
        ++this.age;
        if (this.age == this.lifetime) {
            this.remove();
        }
    }


    public static class Factory implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new Samum_ExplosionEmitterParticle(clientWorld, d, e, f);
        }
    }
}