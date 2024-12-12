package TCOTS.entity.misc.bolts;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.items.TCOTS_Items;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class ExplodingBoltProjectile extends WitcherBolt {
    private static final ItemStack DEFAULT_STACK = new ItemStack(TCOTS_Items.EXPLODING_BOLT);
    public ExplodingBoltProjectile(EntityType<? extends ExplodingBoltProjectile> entityType, World world) {
        super(entityType, world);
    }

    public ExplodingBoltProjectile(World world, LivingEntity owner) {
        super(TCOTS_Entities.EXPLODING_BOLT, owner, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return DEFAULT_STACK.copy();
    }

    private final float explosionPower = 1.8f;

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);

        this.getWorld().createExplosion(
                this,
                null,
                null,
                this.getX(), this.getY(), this.getZ(),
                explosionPower,
                false,
                World.ExplosionSourceType.BLOCK);

        this.discard();
    }

    private static final byte EXPLOSION_PARTICLES=54;

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);

        this.getWorld().createExplosion(this, null, null,
                this.getX(), this.getY(), this.getZ(), explosionPower, false,
                World.ExplosionSourceType.BLOCK);

        this.getWorld().sendEntityStatus(this, EXPLOSION_PARTICLES);
        this.discard();
    }

    @Override
    public void handleStatus(byte status) {
        super.handleStatus(status);

        if(status == EXPLOSION_PARTICLES){
            this.getWorld().addParticle(ParticleTypes.EXPLOSION_EMITTER,
                    this.getX(), this.getY(), this.getZ(),
                    0, 0, 0);
        }
    }

    @Override
    public boolean canExplosionDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float explosionPower) {
        return false;
    }
}
