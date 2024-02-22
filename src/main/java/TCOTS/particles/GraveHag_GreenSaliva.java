package TCOTS.particles;

import net.minecraft.client.particle.RainSplashParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class GraveHag_GreenSaliva extends RainSplashParticle {
    protected GraveHag_GreenSaliva(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @Override
    public void tick() {
        BlockPos blockPos;
        double d;
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.maxAge-- <= 0) {
            this.markDead();
            return;
        }

        this.velocityY -= (double)this.gravityStrength;
        this.move(this.velocityX, this.velocityY, this.velocityZ);
        this.velocityX *= (double)0.98f;
        this.velocityY *= (double)0.98f;
        this.velocityZ *= (double)0.98f;
        if (this.onGround) {
            if (Math.random() < 0.5) {
                this.markDead();
            }
            this.velocityX *= (double)0.7f;
            this.velocityZ *= (double)0.7f;
        }
        if ((d = Math.max(this.world.getBlockState(blockPos = BlockPos.ofFloored(this.x, this.y, this.z)).getCollisionShape(this.world, blockPos).getEndingCoord(Direction.Axis.Y, this.x - (double)blockPos.getX(), this.z - (double)blockPos.getZ()), (double)this.world.getFluidState(blockPos).getHeight(this.world, blockPos))) > 0.0 && this.y < (double)blockPos.getY() + d) {
            this.markDead();
        }
    }
}
