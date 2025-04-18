package TCOTS.items.concoctions.bombs;

import TCOTS.blocks.TCOTS_Blocks_Fabric;
import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.particles.TCOTS_Particles;
import TCOTS.utils.BombsUtil;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class DancingStarBomb {
    private static final byte DANCING_STAR_EXPLODES_L1 = 18;
    private static final byte DANCING_STAR_EXPLODES_L2 = 19;
    private static final byte DANCING_STAR_EXPLODES_L3 = 20;
    public static void explosionLogic(WitcherBombEntity bomb){
    Explosion explosion =
            bomb.level().explode(
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
                Level.ExplosionInteraction.BLOCK,
                ParticleTypes.SMOKE,
                ParticleTypes.SMOKE,
                SoundEvents.GENERIC_EXPLODE
        );

        //For emitters with different size
        switch (bomb.getLevel()){
            case 1:
                bomb.level().broadcastEntityEvent(bomb, DANCING_STAR_EXPLODES_L2);
                break;
            case 2:
                bomb.level().broadcastEntityEvent(bomb, DANCING_STAR_EXPLODES_L3);
                break;
            default:
                bomb.level().broadcastEntityEvent(bomb, DANCING_STAR_EXPLODES_L1);
                break;

        }

        //Level 0 -> 2x2x2
        //Level 1 -> 3x2x3
        //Level 2 -> 4x2x4
        List<LivingEntity> entities =
                bomb.level().getEntitiesOfClass(LivingEntity.class, bomb.getBoundingBox().inflate(2+(bomb.getLevel()),2,3+(bomb.getLevel())), livingEntity -> true);

        for (LivingEntity livingEntity : entities){
            //To not apply effect across walls
            if(BombsUtil.getExposure(livingEntity.position(), bomb) == 0) continue;
            //Level 0 -> 10s
            //Level 1 -> 15s
            //Level 2 -> 20s
            livingEntity.igniteForSeconds(10+(bomb.getLevel()*5));
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
                    for (float h = (1+(bomb.getLevel())) * (0.7f + bomb.level().random.nextFloat() * 0.6f); h > 0.0f; h -= 0.22500001f) {
                        BlockPos blockPos = BlockPos.containing(m, n, o);
                        BlockState blockState = bomb.level().getBlockState(blockPos);
                        FluidState fluidState = bomb.level().getFluidState(blockPos);
                        if (!bomb.level().isInWorldBounds(blockPos)) continue block2;
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
            BlockState state = bomb.level().getBlockState(blockPos2);

            //Destroy nest blocks
            if(bomb.destroyableBlocks(state)) {
                if(state.is(TCOTS_Blocks_Fabric.MONSTER_NEST)){
                    state.onExplosionHit(bomb.level(), blockPos2, explosion, null);
                } else {
                    bomb.level().destroyBlock(blockPos2, true, bomb);
                }
            }

            //Check if it can put fire
            if (bomb.level().random.nextInt(3) != 0 || !bomb.level().getBlockState(blockPos2).isAir() || !bomb.level().getBlockState(blockPos2.below()).isSolidRender(bomb.level(), blockPos2.below())) continue;

            //Put fire
            bomb.level().setBlockAndUpdate(blockPos2, BaseFireBlock.getState(bomb.level(), blockPos2));
        }

    }

    public static void handleStatus(WitcherBombEntity bomb, byte status){
        if(status == DANCING_STAR_EXPLODES_L1 || status == DANCING_STAR_EXPLODES_L2 || status == DANCING_STAR_EXPLODES_L3){
            switch (status){
                case DANCING_STAR_EXPLODES_L2:
                    bomb.level().addParticle(TCOTS_Particles.DANCING_STAR_EXPLOSION_EMITTER, bomb.getX(), bomb.getY(), bomb.getZ(), 0.01, 0.0, 0.0);
                    break;
                case DANCING_STAR_EXPLODES_L3:
                    bomb.level().addParticle(TCOTS_Particles.DANCING_STAR_EXPLOSION_EMITTER, bomb.getX(), bomb.getY(), bomb.getZ(), 0.02, 0.0, 0.0);
                    break;
                default:
                    bomb.level().addParticle(TCOTS_Particles.DANCING_STAR_EXPLOSION_EMITTER, bomb.getX(), bomb.getY(), bomb.getZ(), 0.0, 0.0, 0.0);
                    break;
            }
        }
    }
}
