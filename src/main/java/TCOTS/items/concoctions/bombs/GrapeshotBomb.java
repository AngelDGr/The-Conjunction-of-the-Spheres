package TCOTS.items.concoctions.bombs;

import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.particles.TCOTS_Particles;
import TCOTS.utils.BombsUtil;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;

public class GrapeshotBomb {

    public static void explosionLogic(WitcherBombEntity bomb, @Nullable Entity entity){
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

        GrapeshotBomb.destroyNests(bomb);

        if(entity!=null){
            //Level 0 -> 5s
            //Level 1 -> 8s
            //Level 2 -> 11s
            entity.setOnFireFor(5+(bomb.getLevel()*3));
        }
    }

    public static void destroyNests(WitcherBombEntity bomb){
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

                        BlockState blockState = bomb.getWorld().getBlockState(blockPos);
                        FluidState fluidState = bomb.getWorld().getFluidState(blockPos);

                        Optional<Float> optional = BombsUtil.getBlastResistance(blockState, fluidState);
                        if (optional.isPresent() && !bomb.destroyableBlocks(blockState)) {
                            h -= (optional.get() + 0.3f) * 0.3f;
                        }
                        if (h > 0.0f && bomb.destroyableBlocks(blockState)) {
                            set.add(blockPos);
                        }
                        m += d * (double) 0.3f;
                        n += e * (double) 0.3f;
                        o += f * (double) 0.3f;
                    }
                }
            }
        }

        affectedBlocks.addAll(set);

        for (BlockPos blockPos : affectedBlocks) {
            BlockState state = bomb.getWorld().getBlockState(blockPos);

            //Destroy nest blocks
            if(bomb.destroyableBlocks(state)) {

                bomb.getWorld().breakBlock(blockPos, true, bomb);
            }
        }
    }

}
