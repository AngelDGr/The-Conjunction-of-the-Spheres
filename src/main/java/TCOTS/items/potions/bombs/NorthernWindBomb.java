package TCOTS.items.potions.bombs;

import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.items.potions.TCOTS_Effects;
import TCOTS.particles.TCOTS_Particles;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.HashSet;
import java.util.List;

public class NorthernWindBomb {
    private static final byte NORTHERN_WIND_EXPLODES = 38;

    public static void northern_windBehavior(WitcherBombEntity bomb){

        bomb.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1,1);

        bomb.getWorld().sendEntityStatus(bomb, NORTHERN_WIND_EXPLODES);

        List<LivingEntity> list = bomb.getWorld().getEntitiesByClass(LivingEntity.class, bomb.getBoundingBox().expand(3+(bomb.getLevel()*2),2,3+(bomb.getLevel()*2)),
                livingEntity ->
                        !(livingEntity instanceof WardenEntity) && !(livingEntity instanceof ArmorStandEntity)
                        && livingEntity != bomb.getOwner()
                        && !(livingEntity.getType().isIn(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)));

        Entity entityCause = bomb.getEffectCause();
        for(LivingEntity entity: list){
            //To not apply effect across walls
            if(getExposure(entity.getPos(), bomb) == 0) continue;

            //Applies slowness to players, damage to freeze hurt extra and effect to anything else
            if(entity instanceof PlayerEntity) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 240+(bomb.getLevel()*20), 4+bomb.getLevel()), entityCause);
            } else if (entity.getType().isIn(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
                entity.damage(bomb.getDamageSources().freeze(), 0.5f);
                entity.addStatusEffect(new StatusEffectInstance(TCOTS_Effects.NORTHERN_WIND_EFFECT, 80+(bomb.getLevel()*20), bomb.getLevel()), entityCause);
            } else {
                entity.addStatusEffect(new StatusEffectInstance(TCOTS_Effects.NORTHERN_WIND_EFFECT, 160+(bomb.getLevel()*20), bomb.getLevel()), entityCause);
            }
        }

        createIce(bomb);
    }

    public static void handleStatus(WitcherBombEntity bomb, byte status) {
        if(status== NORTHERN_WIND_EXPLODES){
            bomb.getWorld().addParticle(TCOTS_Particles.NORTHERN_WIND_EXPLOSION_EMITTER, bomb.getX(), bomb.getY(), bomb.getZ(), 0.0, 0.0, 0.0);
        }
    }

    private static void createIce(WitcherBombEntity bomb){
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
                    for (float h = (1f+(bomb.getLevel())) * (0.7f + bomb.getWorld().random.nextFloat() * 0.6f); h > 0.0f; h -= 0.22500001f) {
                        BlockPos blockPos = BlockPos.ofFloored(m, n, o);
                        if (!bomb.getWorld().isInBuildLimit(blockPos)) continue block2;
                        set.add(blockPos);
                        m += d * (double)0.3f;
                        n += e * (double)0.3f;
                        o += f * (double)0.3f;
                    }
                }
            }
        }
        affectedBlocks.addAll(set);

        for (BlockPos blockPos2 : affectedBlocks) {
            //To not destroy blocks behind other blocks
            if (getExposure(blockPos2.toCenterPos(), bomb) == 0) continue;

            BlockState blockState = Blocks.FROSTED_ICE.getDefaultState();

            //Check if it can put ice
            if (bomb.getWorld().getBlockState(blockPos2) != FrostedIceBlock.getMeltedState()
                    || !blockState.canPlaceAt(bomb.getWorld(), blockPos2)
                    || !bomb.getWorld().canPlace(blockState, blockPos2, ShapeContext.absent())
                    || bomb.isSubmergedInWater()) continue;


            //Put ice
            bomb.getWorld().setBlockState(blockPos2, blockState);
            bomb.getWorld().scheduleBlockTick(blockPos2, Blocks.FROSTED_ICE, MathHelper.nextInt(bomb.getWorld().getRandom(), 60, 120));
        }
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
