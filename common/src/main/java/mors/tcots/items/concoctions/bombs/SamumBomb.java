package mors.tcots.items.concoctions.bombs;

import mors.tcots.registry.TCOTS_Blocks;
import mors.tcots.entity.misc.WitcherBombEntity;
import mors.tcots.registry.TCOTS_Particles;
import mors.tcots.registry.TCOTS_Effects;
import mors.tcots.utils.BombsUtil;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class SamumBomb {
    private static final byte SAMUM_EXPLODES = 34;

    public static void explosionLogic(final WitcherBombEntity bomb){
        final Explosion explosion =
                bomb.level().explode(
                        bomb,
                        null,
                        null,
                        bomb.getX(),
                        bomb.getY(),
                        bomb.getZ(),
                        0.2f,
                        false,
                        Level.ExplosionInteraction.BLOCK,
                        TCOTS_Particles.SamumExplosionEmitter(),
                        TCOTS_Particles.SamumExplosionEmitter(),
                        SoundEvents.GENERIC_EXPLODE
                );

        final List<LivingEntity> list = bomb.level().getEntitiesOfClass(LivingEntity.class, bomb.getBoundingBox().inflate(3+(bomb.getLevel()*2),2,3+(bomb.getLevel()*2)),
                livingEntity -> !(livingEntity instanceof ArmorStand)
                        && livingEntity != bomb.getOwner());

        final Entity entityCause = bomb.getEffectSource();
        for (final LivingEntity entity : list) {
            //To not apply effect across walls
            if(BombsUtil.getExposure(entity.position(), bomb) == 0) continue;

            if (entity instanceof Player) {
                entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 240 + (bomb.getLevel() * 60), bomb.getLevel()), entityCause);
            } else {
                entity.addEffect(new MobEffectInstance(TCOTS_Effects.SamumEffect(), 80 + (bomb.getLevel() * 40), bomb.getLevel()), entityCause);
            }
        }

        SamumBomb.destroyNests(bomb, explosion);
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

    public static void handleStatus(final WitcherBombEntity bomb, final byte status) {
        if(status==SAMUM_EXPLODES){
            bomb.level().addParticle(TCOTS_Particles.SamumExplosionEmitter(), bomb.getX(), bomb.getY(), bomb.getZ(), 0.0, 0.0, 0.0);
        }
    }

    public static boolean checkSamumEffect(final LivingEntity entity){
        return entity.hasEffect(TCOTS_Effects.SamumEffect());
    }

}
