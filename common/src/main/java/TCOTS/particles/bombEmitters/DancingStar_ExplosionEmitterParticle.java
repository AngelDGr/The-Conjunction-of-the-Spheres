package TCOTS.particles.bombEmitters;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;

public class DancingStar_ExplosionEmitterParticle extends NoRenderParticle {
    private int age;
    private final int lifetime;

    private final double code;

    DancingStar_ExplosionEmitterParticle(final ClientLevel clientWorld, final double d, final double e, final double f, final double vx, final double vy, final double vz) {
        super(clientWorld, d, e, f, vx, vy, vz);
        this.code=vx;
        this.lifetime = 4;
    }

    @Override
    public void tick() {

        //Level 0
        int multiplier=3;
        int quantity=8;
        //Level 1
        if(code==0.01){
            multiplier=4;
            quantity=12;
        }
        //Level 2
        else if (code==0.02) {
            multiplier=5;
            quantity=16;
        }

        for (int i = 0; i < quantity; ++i) {

            final int randomI=this.random.nextIntBetweenInclusive(0,4);

            final double d = this.x + (this.random.nextDouble() - this.random.nextDouble()) * multiplier;
            final double e = this.y + (this.random.nextDouble()) * 4.0;
            final double f = this.z + (this.random.nextDouble() - this.random.nextDouble()) * multiplier;
            if(randomI==4)
            {
                this.level.addParticle(ParticleTypes.FLAME, d, e, f, 0.0, 0.0, 0.0);
            }
            else {
                this.level.addParticle(ParticleTypes.LARGE_SMOKE, d, e, f, 0.0, 0.0, 0.0);
            }


        }
        ++this.age;
        if (this.age == this.lifetime) {
            this.remove();
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(final SimpleParticleType defaultParticleType, final ClientLevel clientWorld, final double d, final double e, final double f, final double g, final double h, final double i) {
            return new DancingStar_ExplosionEmitterParticle(clientWorld, d, e, f, g, h, i);
        }
    }
}
