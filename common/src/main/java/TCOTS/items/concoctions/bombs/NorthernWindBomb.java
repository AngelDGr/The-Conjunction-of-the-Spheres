package TCOTS.items.concoctions.bombs;

import TCOTS.blocks.FrostedSnowBlock;
import TCOTS.registry.TCOTS_Blocks;
import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.registry.TCOTS_Particles;
import TCOTS.registry.TCOTS_Effects;
import TCOTS.utils.BombsUtil;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FrostedIceBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import java.util.HashSet;
import java.util.List;

public class NorthernWindBomb {
    private static final byte NORTHERN_WIND_EXPLODES = 38;

    public static void explosionLogic(final WitcherBombEntity bomb){

        bomb.playSound(SoundEvents.GENERIC_EXPLODE.value(), 1,1);

        bomb.level().broadcastEntityEvent(bomb, NORTHERN_WIND_EXPLODES);

        final List<LivingEntity> list = bomb.level().getEntitiesOfClass(LivingEntity.class, bomb.getBoundingBox().inflate(3+(bomb.getLevel()*2),2,3+(bomb.getLevel()*2)),
                livingEntity ->
                        !(livingEntity instanceof Warden) && !(livingEntity instanceof ArmorStand)
                        && livingEntity.isAlive()
                        && livingEntity != bomb.getOwner()
                        && !(livingEntity.getType().is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)));

        final Entity entityCause = bomb.getEffectSource();
        for(final LivingEntity entity: list){
            //To not apply effect across walls
            if(BombsUtil.getExposure(entity.position(), bomb) == 0) continue;

            //Applies slowness to players, damage to freeze_hurt_extra and effect to anything else
            if(entity instanceof Player) {
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 240+(bomb.getLevel()*20), 4+bomb.getLevel(),false,false), entityCause);
            } else if (entity.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
                entity.hurt(bomb.damageSources().freeze(), 0.5f);
                entity.addEffect(new MobEffectInstance(TCOTS_Effects.NorthernWindEffect(), 80+(bomb.getLevel()*20), bomb.getLevel(),false,false), entityCause);
            } else {
                entity.addEffect(new MobEffectInstance(TCOTS_Effects.NorthernWindEffect(), 160+(bomb.getLevel()*20), bomb.getLevel(),false,false), entityCause);
            }

            if(entity.isOnFire()){
                entity.clearFire();
            }
        }

        createIce(bomb);
    }

    public static void handleStatus(final WitcherBombEntity bomb, final byte status) {
        if(status== NORTHERN_WIND_EXPLODES){
            bomb.level().addParticle(TCOTS_Particles.NorthernWindExplosionEmitter(), bomb.getX(), bomb.getY(), bomb.getZ(), 0.0, 0.0, 0.0);
        }
    }

    private static void createIce(final WitcherBombEntity bomb){
        final ObjectArrayList<BlockPos> affectedBlocks = new ObjectArrayList<>();
        int l;
        int k;
        final HashSet<BlockPos> set = Sets.newHashSet();
        for (int j = 0; j < 16; ++j) {
            for (k = 0; k < 16; ++k) {
                block2: for (l = 0; l < 16; ++l) {
                    if (j != 0 && j != 15 && k != 0 && k != 15 && l != 0 && l != 15) continue;
                    double d = (float)j / 15.0f * 2.0f - 1.0f;
                    double e = (float)k / 15.0f * 2.0f - 1.0f;
                    double f = (float)l / 15.0f * 2.0f - 1.0f;
                    final double g = Math.sqrt(d * d + e * e + f * f);
                    d /= g;
                    e /= g;
                    f /= g;
                    double m = bomb.getX();
                    double n = bomb.getY();
                    double o = bomb.getZ();
                    for (float h = (1f+(bomb.getLevel())) * (0.7f + bomb.level().random.nextFloat() * 0.6f); h > 0.0f; h -= 0.22500001f) {
                        final BlockPos blockPos = BlockPos.containing(m, n, o);
                        if (!bomb.level().isInWorldBounds(blockPos)) continue block2;
                        set.add(blockPos);
                        m += d * (double)0.3f;
                        n += e * (double)0.3f;
                        o += f * (double)0.3f;
                    }
                }
            }
        }
        affectedBlocks.addAll(set);

        for (final BlockPos possibleBlockPos : affectedBlocks) {
            //To not destroy blocks behind other blocks
            if (BombsUtil.getExposure(possibleBlockPos.getCenter(), bomb) == 0 || bomb.isUnderWater()) continue;

            final BlockState blockStateWaterIce = Blocks.FROSTED_ICE.defaultBlockState();

            //To put ice in blocks
            for (final BooleanProperty booleanProperty : PipeBlock.PROPERTY_BY_DIRECTION.values()) {
                final BlockState blockStateIce = TCOTS_Blocks.FrostedSnow().defaultBlockState().setValue(booleanProperty,true);

                //To not put ice in water or in the nether
                if(bomb.level().getBlockState(possibleBlockPos) == FrostedIceBlock.meltsInto() || bomb.level().dimensionType().ultraWarm()) continue;

                if(blockStateIce.canSurvive(bomb.level(),possibleBlockPos)
                        && (bomb.level().getBlockState(possibleBlockPos).isAir() || bomb.level().getBlockState(possibleBlockPos).canBeReplaced())) {

                    //Put the ice
                    bomb.level().setBlockAndUpdate(possibleBlockPos, blockStateIce);

                    //Grows the ice
                    ((FrostedSnowBlock) TCOTS_Blocks.FrostedSnow()).getSpreader().spreadAll(blockStateIce, bomb.level(), possibleBlockPos, false);
                }
            }



            //Check if it can put ice in water
            if (        bomb.level().getBlockState(possibleBlockPos) != FrostedIceBlock.meltsInto()
                    || !blockStateWaterIce.canSurvive(bomb.level(), possibleBlockPos)
                    || !bomb.level().isUnobstructed(blockStateWaterIce, possibleBlockPos, CollisionContext.empty())) continue;


            //Put ice in water
            bomb.level().setBlockAndUpdate(possibleBlockPos, blockStateWaterIce);
            bomb.level().scheduleTick(possibleBlockPos, Blocks.FROSTED_ICE, Mth.nextInt(bomb.level().getRandom(), 60, 120));
        }
    }

    public static boolean checkEffect(final LivingEntity entity){
        return entity.hasEffect(TCOTS_Effects.NorthernWindEffect());
    }
}
