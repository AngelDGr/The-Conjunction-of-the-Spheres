package TCOTS.items.potions.bombs;

import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.particles.TCOTS_Particles;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GrapeshotBomb {

    public static void grapeShotBehavior(WitcherBombEntity bomb, @Nullable Entity entity){
        Explosion explosion =
        bomb.getWorld().createExplosion(
                bomb,
                null,
                null,
                bomb.getX(),
                bomb.getY(),
                bomb.getZ(),
                //Level 0 -> 1.25
                //Level 1 -> 1.50
                //Level 2 -> 1.75
                1.25f+(bomb.getLevel()*0.25f),
                false,
                World.ExplosionSourceType.BLOCK,
                TCOTS_Particles.GRAPESHOT_EXPLOSION_EMITTER,
                TCOTS_Particles.GRAPESHOT_EXPLOSION_EMITTER,
                SoundEvents.ENTITY_GENERIC_EXPLODE
        );

        destroyNests(bomb, explosion);

        if(entity!=null){
            //Level 0 -> 5s
            //Level 1 -> 8s
            //Level 2 -> 11s
            entity.setOnFireFor(5+(bomb.getLevel()*3));
        }
    }

    public static void destroyNests(WitcherBombEntity bomb, Explosion explosion){
        ObjectArrayList<BlockPos> affectedBlocks = new ObjectArrayList<>();
        int l;
        int k;
        HashSet<BlockPos> set = Sets.newHashSet();
        for (int j = 0; j < 16; ++j) {
            for (k = 0; k < 16; ++k) {
                for (l = 0; l < 16; ++l) {
                    if (j != 0 && j != 15 && k != 0 && k != 15 && l != 0 && l != 15) continue;
                    double d = (float) j / 15.0f * 2.0f - 1.0f;
                    double e = (float) k / 15.0f * 2.0f - 1.0f;
                    double f = (float) l / 15.0f * 2.0f - 1.0f;
                    double g = Math.sqrt(d * d + e * e + f * f);
                    d /= g;
                    e /= g;
                    f /= g;
                    double m = bomb.getX();
                    double n = bomb.getY();
                    double o = bomb.getZ();
                    for (float h = (1.25f + (bomb.getLevel() * 0.25f)) * (0.7f + bomb.getWorld().random.nextFloat() * 0.6f); h > 0.0f; h -= 0.22500001f) {
                        BlockPos blockPos = BlockPos.ofFloored(m, n, o);
                        set.add(blockPos);
                        m += d * (double) 0.3f;
                        n += e * (double) 0.3f;
                        o += f * (double) 0.3f;
                    }
                }
            }
        }
        affectedBlocks.addAll(set);

        List<Pair<ItemStack, BlockPos>> list = new ArrayList<>();
        for (BlockPos blockPos2 : affectedBlocks) {
            //To not destroy blocks behind other blocks
            if (getExposure(blockPos2.toCenterPos(), bomb) == 0) continue;

            //Destroy nest blocks
            if(bomb.destroyableBlocks(bomb.getWorld().getBlockState(blockPos2))) {
                bomb.getWorld().getBlockState(blockPos2).onExploded(bomb.getWorld(), blockPos2, explosion, (stack, pos) -> tryMergeStack(list, stack, pos));

                for (Pair<ItemStack, BlockPos> pair : list) {
                    Block.dropStack(bomb.getWorld(), pair.getSecond(), pair.getFirst());
                }
            }
        }
    }

    private static void tryMergeStack(List<Pair<ItemStack, BlockPos>> stacks, ItemStack stack, BlockPos pos) {
        for (int i = 0; i < stacks.size(); ++i) {
            Pair<ItemStack, BlockPos> pair = stacks.get(i);
            ItemStack itemStack = pair.getFirst();
            if (!ItemEntity.canMerge(itemStack, stack)) continue;
            stacks.set(i, Pair.of(ItemEntity.merge(itemStack, stack, 16), pair.getSecond()));
            if (!stack.isEmpty()) continue;
            return;
        }
        stacks.add(Pair.of(stack, pos));
    }

    private static float getExposure(Vec3d source, Entity entity) {
        Box box = entity.getBoundingBox();
        double d = 1.0 / ((box.maxX - box.minX) * 2.0 + 1.0);
        double e = 1.0 / ((box.maxY - box.minY) * 2.0 + 1.0);
        double f = 1.0 / ((box.maxZ - box.minZ) * 2.0 + 1.0);
        double g = (1.0 - Math.floor(1.0 / d) * d) / 2.0;
        double h = (1.0 - Math.floor(1.0 / f) * f) / 2.0;
        if (d < 0.0 || e < 0.0 || f < 0.0) {
            return 0.0f;
        }
        int i = 0;
        int j = 0;
        for (double k = 0.0; k <= 1.0; k += d) {
            for (double l = 0.0; l <= 1.0; l += e) {
                for (double m = 0.0; m <= 1.0; m += f) {
                    double n = MathHelper.lerp(k, box.minX, box.maxX);
                    double o = MathHelper.lerp(l, box.minY, box.maxY);
                    double p = MathHelper.lerp(m, box.minZ, box.maxZ);
                    Vec3d vec3d = new Vec3d(n + g, o, p + h);
                    if (entity.getWorld().raycast(new RaycastContext(vec3d, source, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity)).getType() == HitResult.Type.MISS) {
                        ++i;
                    }
                    ++j;
                }
            }
        }
        return (float)i / (float)j;
    }

}
