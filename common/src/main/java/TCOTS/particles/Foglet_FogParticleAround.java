package TCOTS.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class Foglet_FogParticleAround extends TextureSheetParticle {
    private final SpriteSet spriteProvider;

    private Foglet_FogParticleAround(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteSet spriteProvider) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        float g;
        this.friction = 0.96f;
        this.spriteProvider = spriteProvider;

        this.xd *= 0.01f;
        this.yd *= 0.01f;
        this.zd *= 0.01f;
        this.xd += velocityX;
        this.yd += velocityY;
        this.zd += velocityZ;
        this.rCol = g = 1.0f - (float)(Math.random() * (double)0.3f);
        this.gCol = g;
        this.bCol = g;
        int i = (int)(8.0 / (Math.random() * 0.8 + 0.3));
        this.lifetime = (int)Math.max((float)i * 25, 10f);
        this.hasPhysics = true;
        this.setSpriteFromAge(spriteProvider);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getQuadSize(float tickDelta) {
        return this.quadSize
                *
                (20f-(this.age* ((float) 20/this.lifetime)));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.removed) {
            double d;
            this.setSpriteFromAge(this.spriteProvider);

            Player playerEntity = this.level.getNearestPlayer(this.x, this.y, this.z, 1.0, false);

            if (playerEntity != null && this.y > (d = playerEntity.getY())) {
                this.y += (d - this.y) * 0.2;
                this.yd += (playerEntity.getDeltaMovement().y - this.yd) * 0.2;
                this.setPos(this.x, this.y, this.z);
            }
        }
    }

    @Environment(value= EnvType.CLIENT)
    public static class FogFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public FogFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType defaultParticleType, @NotNull ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new Foglet_FogParticleAround(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}