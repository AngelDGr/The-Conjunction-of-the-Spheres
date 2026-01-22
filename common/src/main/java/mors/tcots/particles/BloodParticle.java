package mors.tcots.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider.Sprite;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BloodParticle extends TextureSheetParticle {
    public BloodParticle(final ClientLevel clientWorld, final double d, final double e, final double f) {
        super(clientWorld, d, e, f);
        this.setSize(0.01f, 0.01f);
        this.gravity = 0.06f;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.updateAge();
        if (this.removed) {
            return;
        }
        this.yd -= this.gravity;
        this.move(this.xd, this.yd, this.zd);
        this.updateVelocity();
        if (this.removed) {
            return;
        }
        this.xd *= 0.98f;
        this.yd *= 0.98f;
        this.zd *= 0.98f;

        final BlockPos blockPos = BlockPos.containing(this.x, this.y, this.z);

        if ( this.y < blockPos.getY()) {
            this.remove();
        }
    }

    protected void updateAge() {
        if (this.lifetime-- <= 0) {
            this.remove();
        }
    }

    protected void updateVelocity() {
    }

    @SuppressWarnings("unused")
    public static TextureSheetParticle createFallingBlood(final SimpleParticleType type, final ClientLevel world, final double x, final double y, final double z, final double velocityX, final double velocityY, final double velocityZ, final ParticleOptions nextParticle) {
        final ContinuousFalling blockLeakParticle = new ContinuousFalling(world, x, y, z, nextParticle);
        blockLeakParticle.gravity = 0.01f;
        blockLeakParticle.setColor(0.4274509f, 0, 0);
        return blockLeakParticle;
    }

    @SuppressWarnings("unused")
    public static TextureSheetParticle createLandingBlood(final SimpleParticleType type, final ClientLevel world, final double x, final double y, final double z, final double velocityX, final double velocityY, final double velocityZ) {
        final Landing blockLeakParticle = new Landing(world, x, y, z);
        blockLeakParticle.lifetime = (int)(28.0 / (Math.random() * 0.8 + 0.2));
        blockLeakParticle.setColor(0.4274509f, 0, 0);
        return blockLeakParticle;
    }

    @SuppressWarnings("unused")
    public static TextureSheetParticle createFallingBlackBlood(final SimpleParticleType type, final ClientLevel world, final double x, final double y, final double z, final double velocityX, final double velocityY, final double velocityZ, final ParticleOptions nextParticle) {
        final ContinuousFalling blockLeakParticle = new ContinuousFalling(world, x, y, z, nextParticle);
        blockLeakParticle.gravity = 0.01f;
        blockLeakParticle.setColor(0.04705823f, 0.04705823f, 0.04705823f);
        return blockLeakParticle;
    }

    @SuppressWarnings("unused")
    public static TextureSheetParticle createLandingBlackBlood(final SimpleParticleType type, final ClientLevel world, final double x, final double y, final double z, final double velocityX, final double velocityY, final double velocityZ) {
        final Landing blockLeakParticle = new Landing(world, x, y, z);
        blockLeakParticle.lifetime = (int)(28.0 / (Math.random() * 0.8 + 0.2));
        blockLeakParticle.setColor(0.04705823f, 0.04705823f, 0.04705823f);
        return blockLeakParticle;
    }


    static class ContinuousFalling extends Falling {
        protected final ParticleOptions nextParticle;

        ContinuousFalling(final ClientLevel world, final double x, final double y, final double z, final ParticleOptions nextParticle) {
            super(world, x, y, z);
            this.nextParticle = nextParticle;
        }

        @Override
        protected void updateVelocity() {
            if (this.onGround) {
                this.remove();
                this.level.addParticle(this.nextParticle, this.x, this.y, this.z, 0.0, 0.0, 0.0);
            }
        }
    }


    static class Landing extends BloodParticle {
        Landing(final ClientLevel clientWorld, final double d, final double e, final double f) {
            super(clientWorld, d, e, f);
            this.lifetime = (int)(16.0 / (Math.random() * 0.8 + 0.2));
        }
    }


    static class Falling extends BloodParticle {
        Falling(final ClientLevel clientWorld, final double d, final double e, final double f) {
            this(clientWorld, d, e, f, (int)(64.0 / (Math.random() * 0.8 + 0.2)));
        }

        Falling(final ClientLevel world, final double x, final double y, final double z, final int maxAge) {
            super(world, x, y, z);
            this.lifetime = maxAge;
        }

        @Override
        protected void updateVelocity() {
            if (this.onGround) {
                this.remove();
            }
        }
    }

    public static class Factory implements Sprite<ParticleOptions> {
        private final SpriteSet spriteProvider;
        private final TextureSheetParticle particle;

        public Factory(final SpriteSet spriteProvider, final TextureSheetParticle particle) {
            this.spriteProvider = spriteProvider;
            this.particle=particle;
        }

        @Nullable
        @Override
        public TextureSheetParticle createParticle(@NotNull final ParticleOptions parameters, @NotNull final ClientLevel world, final double x, final double y, final double z, final double velocityX, final double velocityY, final double velocityZ) {
            this.particle.pickSprite(this.spriteProvider);
            return particle;
        }
    }
}

