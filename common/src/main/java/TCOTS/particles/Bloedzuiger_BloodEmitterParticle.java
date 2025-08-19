package TCOTS.particles;

import TCOTS.registry.TCOTS_Particles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class Bloedzuiger_BloodEmitterParticle extends NoRenderParticle {
    private int age;
    private final int lifetime;

    Bloedzuiger_BloodEmitterParticle(final ClientLevel clientWorld, final double d, final double e, final double f) {
        super(clientWorld, d, e, f, 0.0, 0.0, 0.0);
        this.lifetime = 8;
    }

    @Override
    public void tick() {
        for (int i = 0; i < 6; ++i) {
            final double d = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 3.0;
            final double e = this.y + (this.random.nextDouble()) * 2.5;
            final double f = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 3.0;
            this.level.addParticle(TCOTS_Particles.CadaverineCloud(), d, e, f, 0, 0.0, 0.0);
        }

        for (int i = 0; i < 16; ++i) {
            final double d = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 3.0;
            final double e = this.y + (this.random.nextDouble()) * 3.0;
            final double f = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 3.0;
            this.level.addParticle(TCOTS_Particles.GraveHagGreenSaliva(), d, e, f, 0, 0.0, 0.0);
        }

        ++this.age;
        if (this.age == this.lifetime) {
            this.remove();
        }
    }


    public static class Factory implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(@NotNull final SimpleParticleType defaultParticleType, @NotNull final ClientLevel clientWorld, final double d, final double e, final double f, final double g, final double h, final double i) {
            return new Bloedzuiger_BloodEmitterParticle(clientWorld, d, e, f);
        }
    }
}
