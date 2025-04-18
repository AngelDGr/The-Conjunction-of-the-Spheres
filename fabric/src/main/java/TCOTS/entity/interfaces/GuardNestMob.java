package TCOTS.entity.interfaces;

import TCOTS.blocks.TCOTS_Blocks_Fabric;
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

    default void writeNbtGuardNest(CompoundTag nbt){
        nbt.putInt("NestPosX", this.getNestPos().getX());
        nbt.putInt("NestPosY", this.getNestPos().getY());
        nbt.putInt("NestPosZ", this.getNestPos().getZ());

        nbt.putBoolean("CanHaveNest", this.canHaveNest());
    }

    default void readNbtGuardNest(CompoundTag nbt){
        int x = nbt.getInt("NestPosX");
        int y = nbt.getInt("NestPosY");
        int z = nbt.getInt("NestPosZ");
        this.setNestPos(new BlockPos(x, y, z));

        this.setCanHaveNest(nbt.getBoolean("CanHaveNest"));
    }

    default Predicate<BlockPos> getPredicateForNest(PathfinderMob entity){
       return pos -> entity.level().getBlockState(pos).is(TCOTS_Blocks_Fabric.MONSTER_NEST);
    }

    default boolean getExtraReasonToNotGoToNest(){
        return true;
    }

    private Optional<BlockPos> findNest(PathfinderMob entity) {
        double searchDistance = 15;

        Predicate<BlockPos> predicate = this.getPredicateForNest(entity);

        BlockPos blockPos = entity.blockPosition();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
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

    default void tickGuardNest(PathfinderMob entity){
        if(this.getNestPos()==BlockPos.ZERO && this.canHaveNest()) {
            Optional<BlockPos> optional = this.findNest(entity);
            optional.ifPresent(this::setNestPos);
            this.setCanHaveNest(false);
        }

        if(!entity.level().getBlockState(this.getNestPos()). is(TCOTS_Blocks_Fabric.MONSTER_NEST) && this.getNestPos()!=BlockPos.ZERO){
            this.setNestPos(BlockPos.ZERO);
        }
    }
}
