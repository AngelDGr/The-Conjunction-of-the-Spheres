package TCOTS.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;


public class Rotfiend_BloodEmitterParticle extends NoRenderParticle {
    private int age;
    private final int lifetime;

    Rotfiend_BloodEmitterParticle(ClientLevel clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f, 0.0, 0.0, 0.0);
        this.lifetime = 8;
    }

    @Override
    public void tick() {
        for (int i = 0; i < 6; ++i) {
            double d = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 3.0;
            double e = this.y + (this.random.nextDouble()) * 4.0;
            double f = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 3.0;
            this.level.addParticle(TCOTS_Particles.ROTFIEND_BLOOD_EXPLOSION, d, e, f, (float)this.age / (float)this.lifetime, 0.0, 0.0);
        }
        ++this.age;
        if (this.age == this.lifetime) {
            this.remove();
        }
    }


    public static class Factory implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new Rotfiend_BloodEmitterParticle(clientWorld, d, e, f);
        }
    }
}
