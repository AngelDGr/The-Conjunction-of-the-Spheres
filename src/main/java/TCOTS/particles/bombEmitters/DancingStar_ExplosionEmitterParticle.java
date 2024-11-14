package TCOTS.particles.bombEmitters;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;

public class DancingStar_ExplosionEmitterParticle extends NoRenderParticle {
    private int age;
    private final int maxAge;

    private final double code;

    DancingStar_ExplosionEmitterParticle(ClientWorld clientWorld, double d, double e, double f, double vx, double vy, double vz) {
        super(clientWorld, d, e, f, vx, vy, vz);
        this.code=vx;
        this.maxAge = 4;
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

            int randomI=this.random.nextBetween(0,4);

            double d = this.x + (this.random.nextDouble() - this.random.nextDouble()) * multiplier;
            double e = this.y + (this.random.nextDouble()) * 4.0;
            double f = this.z + (this.random.nextDouble() - this.random.nextDouble()) * multiplier;
            if(randomI==4)
            {
                this.world.addParticle(ParticleTypes.FLAME, d, e, f, 0.0, 0.0, 0.0);
            }
            else {
                this.world.addParticle(ParticleTypes.LARGE_SMOKE, d, e, f, 0.0, 0.0, 0.0);
            }


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
            return new DancingStar_ExplosionEmitterParticle(clientWorld, d, e, f, g, h, i);
        }
    }
}
