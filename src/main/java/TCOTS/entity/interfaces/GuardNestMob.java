package TCOTS.entity.interfaces;

import TCOTS.blocks.TCOTS_Blocks;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;
import java.util.function.Predicate;

public interface GuardNestMob {
    BlockPos getNestPos();
    void setNestPos(BlockPos pos);

    boolean canHaveNest();
    void setCanHaveNest(boolean canHaveNest);

    default void writeNbtGuardNest(NbtCompound nbt){
        nbt.putInt("NestPosX", this.getNestPos().getX());
        nbt.putInt("NestPosY", this.getNestPos().getY());
        nbt.putInt("NestPosZ", this.getNestPos().getZ());

        nbt.putBoolean("CanHaveNest", this.canHaveNest());
    }

    default void readNbtGuardNest(NbtCompound nbt){
        int x = nbt.getInt("NestPosX");
        int y = nbt.getInt("NestPosY");
        int z = nbt.getInt("NestPosZ");
        this.setNestPos(new BlockPos(x, y, z));

        this.setCanHaveNest(nbt.getBoolean("CanHaveNest"));
    }

    default Predicate<BlockPos> getPredicateForNest(PathAwareEntity entity){
       return pos -> entity.getWorld().getBlockState(pos).isOf(TCOTS_Blocks.MONSTER_NEST);
    }

    default boolean getExtraReasonNotGoToNest(){
        return true;
    }

    private Optional<BlockPos> findNest(PathAwareEntity entity) {
        double searchDistance = 15;

        Predicate<BlockPos> predicate = this.getPredicateForNest(entity);

        BlockPos blockPos = entity.getBlockPos();
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int i = 0;
        while ((double)i <= searchDistance) {
            int j = 0;
            while ((double)j < searchDistance) {
                int k = 0;
                while (k <= j) {
                    int l;
                    l = k < j && k > -j ? j : 0;
                    while (l <= j) {
                        mutable.set(blockPos, k, i - 1, l);
                        if (blockPos.isWithinDistance(mutable, searchDistance) && predicate.test(mutable)) {
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

    default void tickGuardNest(PathAwareEntity entity){
        if(this.getNestPos()==BlockPos.ORIGIN && this.canHaveNest()) {
            Optional<BlockPos> optional = this.findNest(entity);
            optional.ifPresent(this::setNestPos);
            this.setCanHaveNest(false);
        }

        if(!entity.getWorld().getBlockState(this.getNestPos()).isOf(TCOTS_Blocks.MONSTER_NEST) && this.getNestPos()!=BlockPos.ORIGIN){
            this.setNestPos(BlockPos.ORIGIN);
        }
    }
}
