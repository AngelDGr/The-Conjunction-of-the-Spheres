package TCOTS.entity.interfaces;

import TCOTS.blocks.TCOTS_Blocks;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;
import java.util.function.Predicate;

public interface GuardNestMob {
    //TODO: Add to the ghoul

    BlockPos getNestPos();
    void setNestPos(BlockPos pos);

    default void writeNbtGuardNest(NbtCompound nbt){
        nbt.putInt("NestPosX", this.getNestPos().getX());
        nbt.putInt("NestPosY", this.getNestPos().getY());
        nbt.putInt("NestPosZ", this.getNestPos().getZ());
    }

    default void readNbtGuardNest(NbtCompound nbt){
        int x = nbt.getInt("NestPosX");
        int y = nbt.getInt("NestPosY");
        int z = nbt.getInt("NestPosZ");
        this.setNestPos(new BlockPos(x, y, z));
    }

    default Predicate<BlockPos> getPredicateForNest(PathAwareEntity entity){
       return pos -> {
//            MonsterNestBlockEntity nest = (MonsterNestBlockEntity) entity.getWorld().getBlockEntity(pos);
//
//            assert nest != null;
//            assert nest.getLogic().spawnEntry != null;
//
//            String id = String.valueOf(nest.getLogic().spawnEntry.getNbt().get("id"));

           return entity.getWorld().getBlockState(pos).isOf(TCOTS_Blocks.MONSTER_NEST);
       };
    }

    default boolean getExtraReasonNotGoToNest(){
        return true;
    }

    default Optional<BlockPos> findNest(PathAwareEntity entity, double searchDistance) {

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
        if(!entity.getWorld().getBlockState(this.getNestPos()).isOf(TCOTS_Blocks.MONSTER_NEST)){
            this.setNestPos(BlockPos.ORIGIN);
        }
    }
}
