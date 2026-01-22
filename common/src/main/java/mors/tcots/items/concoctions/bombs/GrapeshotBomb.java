package mors.tcots.items.concoctions.bombs;

import mors.tcots.registry.TCOTS_Blocks;
import mors.tcots.entity.misc.WitcherBombEntity;
import mors.tcots.registry.TCOTS_Particles;
import mors.tcots.utils.BombsUtil;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class GrapeshotBomb {

    public static void explosionLogic(final WitcherBombEntity bomb, @Nullable final Entity entity){
        final Explosion explosion =
                bomb.level().explode(
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
                Level.ExplosionInteraction.BLOCK,
                TCOTS_Particles.GrapeshotExplosionEmitter(),
                TCOTS_Particles.GrapeshotExplosionEmitter(),
                SoundEvents.GENERIC_EXPLODE
        );

        GrapeshotBomb.destroyNests(bomb, explosion);

        if(entity!=null){
            //Level 0 -> 5s
            //Level 1 -> 8s
            //Level 2 -> 11s
            entity.igniteForSeconds(5+(bomb.getLevel()*3));
        }
    }

    public static void destroyNests(final WitcherBombEntity bomb, final Explosion explosion){
        final ObjectArrayList<BlockPos> affectedBlocks = new ObjectArrayList<>();
        int l;
        int k;
        final HashSet<BlockPos> set = Sets.newHashSet();
        for (int j = 0; j < 16; ++j) {
            for (k = 0; k < 16; ++k) {
                for (l = 0; l < 16; ++l) {
                    if (j != 0 && j != 15 && k != 0 && k != 15 && l != 0 && l != 15) continue;
                    double d = (float) j / 15.0f * 2.0f - 1.0f;
                    double e = (float) k / 15.0f * 2.0f - 1.0f;
                    double f = (float) l / 15.0f * 2.0f - 1.0f;
                    final double g = Math.sqrt(d * d + e * e + f * f);
                    d /= g;
                    e /= g;
                    f /= g;
                    double m = bomb.getX();
                    double n = bomb.getY();
                    double o = bomb.getZ();
                    for (float h = (1.25f + (bomb.getLevel() * 0.25f)) * (0.7f + bomb.level().random.nextFloat() * 0.6f); h > 0.0f; h -= 0.22500001f) {
                        final BlockPos blockPos = BlockPos.containing(m, n, o);

                        final BlockState blockState = bomb.level().getBlockState(blockPos);
                        final FluidState fluidState = bomb.level().getFluidState(blockPos);

                        final Optional<Float> optional = BombsUtil.getBlastResistance(blockState, fluidState);
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

        for (final BlockPos blockPos : affectedBlocks) {
            final BlockState state = bomb.level().getBlockState(blockPos);

            //Destroy nest blocks
            if(bomb.destroyableBlocks(state)) {
                if(state.is(TCOTS_Blocks.MonsterNest())){
                    state.onExplosionHit(bomb.level(), blockPos, explosion, null);
                } else {
                    bomb.level().destroyBlock(blockPos, true, bomb);
                }
            }
        }
    }

}
