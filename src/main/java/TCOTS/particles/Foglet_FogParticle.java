package TCOTS.particles;


import TCOTS.items.concoctions.TCOTS_Effects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.SimpleParticleType;

public class Foglet_FogParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;


    private Foglet_FogParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        float g;
        this.velocityMultiplier = 0.96f;
        this.spriteProvider = spriteProvider;
        this.velocityX *= 0.01f;
        this.velocityY *= 0.01f;
        this.velocityZ *= 0.01f;
        this.velocityX += velocityX;
        this.velocityY += velocityY;
        this.velocityZ += velocityZ;
        this.red = g = 1.0f - (float)(Math.random() * (double)0.3f);
        this.green = g;
        this.blue = g;
        int i = (int)(8.0 / (Math.random() * 0.8 + 0.3));
        this.maxAge = (int)Math.max((float)i * 25, 10f);
        this.collidesWithWorld = true;
        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getSize(float tickDelta) {
        return this.scale
                *
                (4f-(this.age* ((float) 4/this.maxAge)));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.dead) {
            double d;
            this.setSpriteForAge(this.spriteProvider);

            PlayerEntity playerEntity = this.world.getClosestPlayer(this.x, this.y, this.z, 1.0, false);

            if (playerEntity != null && this.y > (d = playerEntity.getY()) && !(playerEntity.hasStatusEffect(TCOTS_Effects.FOGLET_DECOCTION_EFFECT))) {
                this.y += (d - this.y) * 0.2;
                this.velocityY += (playerEntity.getVelocity().y - this.velocityY) * 0.2;
                this.setPos(this.x, this.y, this.z);
            }
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static class FogFactory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public FogFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new Foglet_FogParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}
