package TCOTS.entity.misc.bolts;

import TCOTS.registry.TCOTS_Entities;
import TCOTS.registry.TCOTS_Items;
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
    private static final ItemStack DEFAULT_STACK = new ItemStack(TCOTS_Items.EXPLODING_BOLT.get());
    public ExplodingBoltProjectile(final EntityType<? extends ExplodingBoltProjectile> entityType, final Level world) {
        super(entityType, world);
    }

    public ExplodingBoltProjectile(final Level world, final LivingEntity owner, final ItemStack stack, @Nullable final ItemStack weapon) {
        super(TCOTS_Entities.ExplodingBolt(), owner, world, stack, weapon);
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return DEFAULT_STACK;
    }

    private final float explosionPower = 1.8f;

    @Override
    protected void doPostHurtEffects(@NotNull final LivingEntity target) {
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
    protected void onHitBlock(@NotNull final BlockHitResult blockHitResult) {
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
    public boolean shouldBlockExplode(@NotNull final Explosion explosion, @NotNull final BlockGetter world, @NotNull final BlockPos pos, @NotNull final BlockState state, final float explosionPower) {
        return false;
    }
}
