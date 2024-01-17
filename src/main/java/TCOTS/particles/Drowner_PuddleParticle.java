package TCOTS.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;


public class Drowner_PuddleParticle extends SpriteBillboardParticle {
    protected Drowner_PuddleParticle(ClientWorld level, double xCoord, double yCoord, double zCoord,
                                     SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.velocityMultiplier = 0.6F;
        this.x = xd;
        this.y = yd;
        this.z = zd;
        this.scale *= 0.75F;
        this.maxAge = 20;
        this.setSpriteForAge(spriteSet);

        this.red = 1f;
        this.green = 1f;
        this.blue = 1f;

    }



    public void changeColors(){
        int waterColor = BiomeColors.getWaterColor(world, new BlockPos((int)this.x, (int)this.y, (int)this.z));

        float red = ((waterColor >> 16) & 255) / 255.0F;
        float green = ((waterColor >> 8) & 255) / 255.0F;
        float blue = (waterColor & 255) / 255.0F;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    int FirstTick=2;
    @Override
    public void tick() {
        if(FirstTick > 0){
            changeColors();
            --FirstTick;
        }

        super.tick();
        scaleOut();
    }

    private void scaleOut() {
        this.scale = (-(1/(float)maxAge) * age + 1);
    }




    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(DefaultParticleType particleType, ClientWorld level, double x, double y, double z,
                                       double dx, double dy, double dz) {


            return new Drowner_PuddleParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }

}
