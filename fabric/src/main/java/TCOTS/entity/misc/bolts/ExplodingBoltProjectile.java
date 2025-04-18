package TCOTS.entity.misc.bolts;

import TCOTS.entity.TCOTS_Entities_Fabric;
import TCOTS.items.TCOTS_Items_Fabric;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExplodingBoltProjectile extends WitcherBolt {
    private static final ItemStack DEFAULT_STACK = new ItemStack(TCOTS_Items_Fabric.EXPLODING_BOLT);
    public ExplodingBoltProjectile(EntityType<? extends ExplodingBoltProjectile> entityType, Level world) {
        super(entityType, world);
    }

    public ExplodingBoltProjectile(Level world, LivingEntity owner, ItemStack stack, @Nullable ItemStack weapon) {
        super(TCOTS_Entities_Fabric.EXPLODING_BOLT, owner, world, stack, weapon);
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return DEFAULT_STACK;
    }

    private final float explosionPower = 1.8f;

    @Override
    protected void doPostHurtEffects(@NotNull LivingEntity target) {
        super.doPostHurtEffects(target);

        this.level().explode(this, null, null,
                this.getX(), this.getY(), this.getZ(), explosionPower, false,
                Level.ExplosionInteraction.BLOCK,
                ParticleTypes.EXPLOSION_EMITTER,
                ParticleTypes.EXPLOSION_EMITTER,
                SoundEvents.GENERIC_EXPLODE);

        this.discard();
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);

        this.level().explode(this, null, null,
                this.getX(), this.getY(), this.getZ(), explosionPower, false,
                Level.ExplosionInteraction.BLOCK,
                ParticleTypes.EXPLOSION_EMITTER,
                ParticleTypes.EXPLOSION_EMITTER,
                SoundEvents.GENERIC_EXPLODE);

        this.discard();
    }

    @Override
    public boolean shouldBlockExplode(@NotNull Explosion explosion, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull BlockState state, float explosionPower) {
        return false;
    }
}
