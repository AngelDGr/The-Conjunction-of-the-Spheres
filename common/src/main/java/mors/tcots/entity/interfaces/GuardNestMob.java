package mors.tcots.entity.interfaces;

import mors.tcots.registry.TCOTS_Blocks;

import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.PathfinderMob;

public interface GuardNestMob {
    BlockPos getNestPos();
    void setNestPos(BlockPos pos);

    boolean canHaveNest();
    void setCanHaveNest(boolean canHaveNest);

    default void writeNbtGuardNest(final CompoundTag nbt){
        nbt.putInt("NestPosX", this.getNestPos().getX());
        nbt.putInt("NestPosY", this.getNestPos().getY());
        nbt.putInt("NestPosZ", this.getNestPos().getZ());

        nbt.putBoolean("CanHaveNest", this.canHaveNest());
    }

    default void readNbtGuardNest(final CompoundTag nbt){
        final int x = nbt.getInt("NestPosX");
        final int y = nbt.getInt("NestPosY");
        final int z = nbt.getInt("NestPosZ");
        this.setNestPos(new BlockPos(x, y, z));

        this.setCanHaveNest(nbt.getBoolean("CanHaveNest"));
    }

    default Predicate<BlockPos> getPredicateForNest(final PathfinderMob entity){
       return pos -> entity.level().getBlockState(pos).is(TCOTS_Blocks.MonsterNest());
    }

    default boolean getExtraReasonToNotGoToNest(){
        return true;
    }

    private Optional<BlockPos> findNest(final PathfinderMob entity) {
        final double searchDistance = 15;

        final Predicate<BlockPos> predicate = this.getPredicateForNest(entity);

        final BlockPos blockPos = entity.blockPosition();
        final BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        int i = 0;
        while ((double)i <= searchDistance) {
            int j = 0;
            while ((double)j < searchDistance) {
                int k = 0;
                while (k <= j) {
                    int l;
                    l = k < j && k > -j ? j : 0;
                    while (l <= j) {
                        mutable.setWithOffset(blockPos, k, i - 1, l);
                        if (blockPos.closerThan(mutable, searchDistance) && predicate.test(mutable)) {
                            return Optional.of(mutable);
                        }
                        l = l > 0 ? -l : 1 - l;
                    }
                    k = k > 0 ? -k : 1 - k;
                }
                ++j;
            }
            i = i > 0 ? -i : 1 - i;
        }
        return Optional.empty();
    }

    default void tickGuardNest(final PathfinderMob entity){
        if(this.getNestPos()==BlockPos.ZERO && this.canHaveNest()) {
            final Optional<BlockPos> optional = this.findNest(entity);
            optional.ifPresent(this::setNestPos);
            this.setCanHaveNest(false);
        }

        if(!entity.level().getBlockState(this.getNestPos()). is(TCOTS_Blocks.MonsterNest()) && this.getNestPos()!=BlockPos.ZERO){
            this.setNestPos(BlockPos.ZERO);
        }
    }
}
