package TCOTS.particles.bombEmitters;

import TCOTS.registry.TCOTS_Particles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class DevilsPuffball_ExplosionEmitterParticle extends NoRenderParticle {
    private int age;
    private final int lifetime;

    DevilsPuffball_ExplosionEmitterParticle(final ClientLevel clientWorld, final double d, final double e, final double f) {
        super(clientWorld, d, e, f, 0.0, 0.0, 0.0);
        this.lifetime = 4;
    }

    @Override
    public void tick() {
        for (int i = 0; i < 8; ++i) {
            final double d = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 3.0;
            final double e = this.y + (this.random.nextDouble()) * 4.0;
            final double f = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 3.0;
            this.level.addParticle(TCOTS_Particles.GreenCloud(), d, e, f, 0, 0.0, 0.0);
        }
        ++this.age;
        if (this.age == this.lifetime) {
            this.remove();
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(@NotNull final SimpleParticleType defaultParticleType, @NotNull final ClientLevel clientWorld, final double d, final double e, final double f, final double g, final double h, final double i) {
            return new DevilsPuffball_ExplosionEmitterParticle(clientWorld, d, e, f);
        }
    }
}
