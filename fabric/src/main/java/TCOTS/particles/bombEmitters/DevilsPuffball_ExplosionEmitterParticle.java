package TCOTS.particles.bombEmitters;

import TCOTS.particles.TCOTS_Particles_Fabric;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class DevilsPuffball_ExplosionEmitterParticle extends NoRenderParticle {
    private int age;
    private final int lifetime;

    DevilsPuffball_ExplosionEmitterParticle(ClientLevel clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f, 0.0, 0.0, 0.0);
        this.lifetime = 4;
    }

    @Override
    public void tick() {
        for (int i = 0; i < 8; ++i) {
            double d = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 3.0;
            double e = this.y + (this.random.nextDouble()) * 4.0;
            double f = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 3.0;
            this.level.addParticle(TCOTS_Particles_Fabric.GREEN_CLOUD, d, e, f, 0, 0.0, 0.0);
        }
        ++this.age;
        if (this.age == this.lifetime) {
            this.remove();
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(@NotNull SimpleParticleType defaultParticleType, @NotNull ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new DevilsPuffball_ExplosionEmitterParticle(clientWorld, d, e, f);
        }
    }
}
