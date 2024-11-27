package TCOTS.items.concoctions.bombs;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.items.concoctions.TCOTS_Effects;
import TCOTS.particles.TCOTS_Particles;
import TCOTS.utils.BombsUtil;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class SamumBomb {
    private static final byte SAMUM_EXPLODES = 34;

    public static void explosionLogic(WitcherBombEntity bomb){
        Explosion explosion =
                bomb.getWorld().createExplosion(
                        bomb,
                        null,
                        null,
                        bomb.getX(),
                        bomb.getY(),
                        bomb.getZ(),
                        0.2f,
                        false,
                        World.ExplosionSourceType.BLOCK,
                        TCOTS_Particles.SAMUM_EXPLOSION_EMITTER,
                        TCOTS_Particles.SAMUM_EXPLOSION_EMITTER,
                        SoundEvents.ENTITY_GENERIC_EXPLODE
                );

        List<LivingEntity> list = bomb.getWorld().getEntitiesByClass(LivingEntity.class, bomb.getBoundingBox().expand(3+(bomb.getLevel()*2),2,3+(bomb.getLevel()*2)),
                livingEntity -> !(livingEntity instanceof ArmorStandEntity)
                        && livingEntity != bomb.getOwner());

        Entity entityCause = bomb.getEffectCause();
        for (LivingEntity entity : list) {
            //To not apply effect across walls
            if(BombsUtil.getExposure(entity.getPos(), bomb) == 0) continue;

            if (entity instanceof PlayerEntity) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 240 + (bomb.getLevel() * 60), bomb.getLevel()), entityCause);
            } else {
                entity.addStatusEffect(new StatusEffectInstance(TCOTS_Effects.SAMUM_EFFECT, 80 + (bomb.getLevel() * 40), bomb.getLevel()), entityCause);
            }
        }

        SamumBomb.destroyNests(bomb, explosion);
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
                if(state.isOf(TCOTS_Blocks.MONSTER_NEST)){
                    state.onExploded(bomb.getWorld(), blockPos, explosion, null);
                } else {
                    bomb.getWorld().breakBlock(blockPos, true, bomb);
                }
            }
        }
    }

    public static void handleStatus(WitcherBombEntity bomb, byte status) {
        if(status==SAMUM_EXPLODES){
            bomb.getWorld().addParticle(TCOTS_Particles.SAMUM_EXPLOSION_EMITTER, bomb.getX(), bomb.getY(), bomb.getZ(), 0.0, 0.0, 0.0);
        }
    }

    public static boolean checkSamumEffect(LivingEntity entity){
        return entity.hasStatusEffect(TCOTS_Effects.SAMUM_EFFECT);
    }

}
