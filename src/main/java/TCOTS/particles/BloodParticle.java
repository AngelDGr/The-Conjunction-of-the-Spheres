package TCOTS.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleFactory.BlockLeakParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class BloodParticle extends SpriteBillboardParticle {
    public BloodParticle(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
        this.setBoundingBoxSpacing(0.01f, 0.01f);
        this.gravityStrength = 0.06f;
    }



    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        this.updateAge();
        if (this.dead) {
            return;
        }
        this.velocityY -= this.gravityStrength;
        this.move(this.velocityX, this.velocityY, this.velocityZ);
        this.updateVelocity();
        if (this.dead) {
            return;
        }
        this.velocityX *= 0.98f;
        this.velocityY *= 0.98f;
        this.velocityZ *= 0.98f;

        BlockPos blockPos = BlockPos.ofFloored(this.x, this.y, this.z);

        if ( this.y < blockPos.getY()) {
            this.markDead();
        }
    }

    protected void updateAge() {
        if (this.maxAge-- <= 0) {
            this.markDead();
        }
    }

    protected void updateVelocity() {
    }

    @SuppressWarnings("unused")
    public static SpriteBillboardParticle createFallingBlood(SimpleParticleType type, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        ContinuousFalling blockLeakParticle = new ContinuousFalling(world, x, y, z, TCOTS_Particles.LANDING_BLOOD_PARTICLE);
        blockLeakParticle.gravityStrength = 0.01f;
        blockLeakParticle.setColor(0.4274509f, 0, 0);
        return blockLeakParticle;
    }

    @SuppressWarnings("unused")
    public static SpriteBillboardParticle createLandingBlood(SimpleParticleType type, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        Landing blockLeakParticle = new Landing(world, x, y, z);
        blockLeakParticle.maxAge = (int)(28.0 / (Math.random() * 0.8 + 0.2));
        blockLeakParticle.setColor(0.4274509f, 0, 0);
        return blockLeakParticle;
    }

    @SuppressWarnings("unused")
    public static SpriteBillboardParticle createFallingBlackBlood(SimpleParticleType type, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        ContinuousFalling blockLeakParticle = new ContinuousFalling(world, x, y, z, TCOTS_Particles.LANDING_BLACK_BLOOD_PARTICLE);
        blockLeakParticle.gravityStrength = 0.01f;
        blockLeakParticle.setColor(0.04705823f, 0.04705823f, 0.04705823f);
        return blockLeakParticle;
    }

    @SuppressWarnings("unused")
    public static SpriteBillboardParticle createLandingBlackBlood(SimpleParticleType type, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        Landing blockLeakParticle = new Landing(world, x, y, z);
        blockLeakParticle.maxAge = (int)(28.0 / (Math.random() * 0.8 + 0.2));
        blockLeakParticle.setColor(0.04705823f, 0.04705823f, 0.04705823f);
        return blockLeakParticle;
    }

    @Environment(value=EnvType.CLIENT)
    static class ContinuousFalling extends Falling {
        protected final ParticleEffect nextParticle;

        ContinuousFalling(ClientWorld world, double x, double y, double z, ParticleEffect nextParticle) {
            super(world, x, y, z);
            this.nextParticle = nextParticle;
        }

        @Override
        protected void updateVelocity() {
            if (this.onGround) {
                this.markDead();
                this.world.addParticle(this.nextParticle, this.x, this.y, this.z, 0.0, 0.0, 0.0);
            }
        }
    }

    @Environment(value=EnvType.CLIENT)
    static class Landing extends BloodParticle {
        Landing(ClientWorld clientWorld, double d, double e, double f) {
            super(clientWorld, d, e, f);
            this.maxAge = (int)(16.0 / (Math.random() * 0.8 + 0.2));
        }
    }


    @Environment(value= EnvType.CLIENT)
    static class Falling extends BloodParticle {
        Falling(ClientWorld clientWorld, double d, double e, double f) {
            this(clientWorld, d, e, f, (int)(64.0 / (Math.random() * 0.8 + 0.2)));
        }

        Falling(ClientWorld world, double x, double y, double z, int maxAge) {
            super(world, x, y, z);
            this.maxAge = maxAge;
        }

        @Override
        protected void updateVelocity() {
            if (this.onGround) {
                this.markDead();
            }
        }
    }

    @Environment(value= EnvType.CLIENT)
    public static class Factory implements BlockLeakParticleFactory<ParticleEffect> {
        private final SpriteProvider spriteProvider;
        private final SpriteBillboardParticle particle;

        public Factory(SpriteProvider spriteProvider, SpriteBillboardParticle particle) {
            this.spriteProvider = spriteProvider;
            this.particle=particle;
        }

        @Nullable
        @Override
        public SpriteBillboardParticle createParticle(ParticleEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            this.particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}

