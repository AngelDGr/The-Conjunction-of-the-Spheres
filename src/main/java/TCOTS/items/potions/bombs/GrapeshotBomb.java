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
import net.minecraft.util.math.BlockPos;
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

    private static void destroyNests(WitcherBombEntity bomb, Explosion explosion){
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
}
