package mors.tcots.utils;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BombsUtil {

    public static Optional<Float> getBlastResistance(final BlockState blockState, final FluidState fluidState) {
        if (blockState.isAir() && fluidState.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Math.max(blockState.getBlock().getExplosionResistance(), fluidState.getExplosionResistance()));
    }

    public static float getExposure(final Vec3 source, final Entity entity) {
        final AABB box = entity.getBoundingBox();
        final double d = 1.0 / ((box.maxX - box.minX) * 2.0 + 1.0);
        final double e = 1.0 / ((box.maxY - box.minY) * 2.0 + 1.0);
        final double f = 1.0 / ((box.maxZ - box.minZ) * 2.0 + 1.0);
        final double g = (1.0 - Math.floor(1.0 / d) * d) / 2.0;
        final double h = (1.0 - Math.floor(1.0 / f) * f) / 2.0;
        if (d < 0.0 || e < 0.0 || f < 0.0) {
            return 0.0f;
        }
        int i = 0;
        int j = 0;
        for (double k = 0.0; k <= 1.0; k += d) {
            for (double l = 0.0; l <= 1.0; l += e) {
                for (double m = 0.0; m <= 1.0; m += f) {
                    final double n = Mth.lerp(k, box.minX, box.maxX);
                    final double o = Mth.lerp(l, box.minY, box.maxY);
                    final double p = Mth.lerp(m, box.minZ, box.maxZ);
                    final Vec3 vec3d = new Vec3(n + g, o, p + h);
                    if (entity.level().clip(
                                    new ClipContext(vec3d, source, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity))
                            .getType() == HitResult.Type.MISS) {
                        ++i;
                    }
                    ++j;
                }
            }
        }
        return (float)i / (float)j;
    }

    @SuppressWarnings("unused")
    public static void tryMergeStack(final List<Pair<ItemStack, BlockPos>> stacks, final ItemStack stack, final BlockPos pos) {
        for (int i = 0; i < stacks.size(); ++i) {
            final Pair<ItemStack, BlockPos> pair = stacks.get(i);
            final ItemStack itemStack = pair.getFirst();
            if (!ItemEntity.areMergable(itemStack, stack)) continue;
            stacks.set(i, Pair.of(ItemEntity.merge(itemStack, stack, 16), pair.getSecond()));
            if (!stack.isEmpty()) continue;
            return;
        }
        stacks.add(Pair.of(stack, pos));
    }
}
