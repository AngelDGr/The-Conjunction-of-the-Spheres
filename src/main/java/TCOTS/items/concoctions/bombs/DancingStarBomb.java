package TCOTS.items.concoctions.bombs;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.particles.TCOTS_Particles;
import TCOTS.utils.BombsUtil;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class DancingStarBomb {
    private static final byte DANCING_STAR_EXPLODES_L1 = 18;
    private static final byte DANCING_STAR_EXPLODES_L2 = 19;
    private static final byte DANCING_STAR_EXPLODES_L3 = 20;
    public static void explosionLogic(WitcherBombEntity bomb){
    Explosion explosion =
            bomb.getWorld().createExplosion(
                bomb,
                null,
                null,
                bomb.getX(),
                bomb.getY(),
                bomb.getZ(),
                //Level 0 -> 1.25
                //Level 1 -> 1.25
                //Level 2 -> 1.25
                1.25f,
                true,
                World.ExplosionSourceType.BLOCK
        );

        //For emitters with different size
        switch (bomb.getLevel()){
            case 1:
                bomb.getWorld().sendEntityStatus(bomb, DANCING_STAR_EXPLODES_L2);
                break;
            case 2:
                bomb.getWorld().sendEntityStatus(bomb, DANCING_STAR_EXPLODES_L3);
                break;
            default:
                bomb.getWorld().sendEntityStatus(bomb, DANCING_STAR_EXPLODES_L1);
                break;

        }

        //Level 0 -> 2x2x2
        //Level 1 -> 3x2x3
        //Level 2 -> 4x2x4
        List<LivingEntity> entities =
                bomb.getWorld().getEntitiesByClass(LivingEntity.class, bomb.getBoundingBox().expand(2+(bomb.getLevel()),2,3+(bomb.getLevel())), livingEntity -> true);

        for (LivingEntity livingEntity : entities){
            //To not apply effect across walls
            if(BombsUtil.getExposure(livingEntity.getPos(), bomb) == 0) continue;
            //Level 0 -> 10s
            //Level 1 -> 15s
            //Level 2 -> 20s
            livingEntity.setOnFireFor(10+(bomb.getLevel()*5));
        }

        createFire(bomb, explosion);
    }

    private static void createFire(WitcherBombEntity bomb, Explosion explosion){
        ObjectArrayList<BlockPos> affectedBlocks = new ObjectArrayList<>();
        int l;
        int k;
        HashSet<BlockPos> set = Sets.newHashSet();
        for (int j = 0; j < 16; ++j) {
            for (k = 0; k < 16; ++k) {
                block2: for (l = 0; l < 16; ++l) {
                    if (j != 0 && j != 15 && k != 0 && k != 15 && l != 0 && l != 15) continue;
                    double d = (float)j / 15.0f * 2.0f - 1.0f;
                    double e = (float)k / 15.0f * 2.0f - 1.0f;
                    double f = (float)l / 15.0f * 2.0f - 1.0f;
                    double g = Math.sqrt(d * d + e * e + f * f);
                    d /= g;
                    e /= g;
                    f /= g;
                    double m = bomb.getX();
                    double n = bomb.getY();
                    double o = bomb.getZ();
                    for (float h = (1+(bomb.getLevel())) * (0.7f + bomb.getWorld().random.nextFloat() * 0.6f); h > 0.0f; h -= 0.22500001f) {
                        BlockPos blockPos = BlockPos.ofFloored(m, n, o);
                        BlockState blockState = bomb.getWorld().getBlockState(blockPos);
                        FluidState fluidState = bomb.getWorld().getFluidState(blockPos);
                        if (!bomb.getWorld().isInBuildLimit(blockPos)) continue block2;
                        Optional<Float> optional = BombsUtil.getBlastResistance(blockState, fluidState);
                        if (optional.isPresent() && !bomb.destroyableBlocks(blockState)) {
                            h -= (optional.get() + 0.3f) * 0.3f;
                        }
                        if (h > 0.0f) {
                            set.add(blockPos);
                        }

                        m += d * (double)0.3f;
                        n += e * (double)0.3f;
                        o += f * (double)0.3f;
                    }
                }
            }
        }
        affectedBlocks.addAll(set);

        for (BlockPos blockPos2 : affectedBlocks) {
            BlockState state = bomb.getWorld().getBlockState(blockPos2);

            //Destroy nest blocks
            if(bomb.destroyableBlocks(state)) {
                if(state.isOf(TCOTS_Blocks.MONSTER_NEST)){
                    state.getBlock().onDestroyedByExplosion(bomb.getWorld(), blockPos2, explosion);
                } else {
                    bomb.getWorld().breakBlock(blockPos2, true, bomb);
                }
            }

            //Check if it can put fire
            if (bomb.getWorld().random.nextInt(3) != 0 || !bomb.getWorld().getBlockState(blockPos2).isAir() || !bomb.getWorld().getBlockState(blockPos2.down()).isOpaqueFullCube(bomb.getWorld(), blockPos2.down())) continue;

            //Put fire
            bomb.getWorld().setBlockState(blockPos2, AbstractFireBlock.getState(bomb.getWorld(), blockPos2));
        }

    }

    public static void handleStatus(WitcherBombEntity bomb, byte status){
        if(status == DANCING_STAR_EXPLODES_L1 || status == DANCING_STAR_EXPLODES_L2 || status == DANCING_STAR_EXPLODES_L3){
            switch (status){
                case DANCING_STAR_EXPLODES_L2:
                    bomb.getWorld().addParticle(TCOTS_Particles.DANCING_STAR_EXPLOSION_EMITTER, bomb.getX(), bomb.getY(), bomb.getZ(), 0.01, 0.0, 0.0);
                    break;
                case DANCING_STAR_EXPLODES_L3:
                    bomb.getWorld().addParticle(TCOTS_Particles.DANCING_STAR_EXPLOSION_EMITTER, bomb.getX(), bomb.getY(), bomb.getZ(), 0.02, 0.0, 0.0);
                    break;
                default:
                    bomb.getWorld().addParticle(TCOTS_Particles.DANCING_STAR_EXPLOSION_EMITTER, bomb.getX(), bomb.getY(), bomb.getZ(), 0.0, 0.0, 0.0);
                    break;
            }
        }
    }
}
