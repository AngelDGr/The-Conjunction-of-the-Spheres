package TCOTS.items.potions.bombs;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.items.potions.TCOTS_Effects;
import TCOTS.particles.TCOTS_Particles;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.*;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.List;

public class DimeritiumBomb {
    private static final byte DIMERITIUM_BOMB_EXPLODES = 40;

    public static void explosionLogic(WitcherBombEntity bomb){

        bomb.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1,1);

        bomb.getWorld().sendEntityStatus(bomb, DIMERITIUM_BOMB_EXPLODES);

        List<Entity> list = bomb.getWorld().getEntitiesByClass(Entity.class, bomb.getBoundingBox().expand(3+(bomb.getLevel()*2),2,3+(bomb.getLevel()*2)),
                entity ->
                        ((entity instanceof LivingEntity) || entity.getType().isIn(TCOTS_Entities.DIMERITIUM_DAMAGE) || entity.getType().isIn(TCOTS_Entities.DIMERITIUM_REMOVAL))
                        && !(entity instanceof WardenEntity) && !(entity instanceof ArmorStandEntity)
                        && entity.isAlive()
                        && entity != bomb.getOwner());


        Entity entityCause = bomb.getEffectCause();
        for(Entity entity: list){
            //To not apply effect across walls
            if(getExposure(entity.getPos(), bomb) == 0) continue;

            //Destroy End Crystals and Foglings
            if(entity.getType().isIn(TCOTS_Entities.DIMERITIUM_DAMAGE))
                entity.damage(bomb.getDamageSources().magic(), 1);

            //Remove magic entities
            if(entity.getType().isIn(TCOTS_Entities.DIMERITIUM_REMOVAL))
                entity.discard();

            //Applies dimeritium effect to entity
            if(entity instanceof LivingEntity livingEntity) {
                livingEntity.addStatusEffect(new StatusEffectInstance(TCOTS_Effects.DIMERITIUM_BOMB_EFFECT, bomb.getLevel() < 1 ? 300 : 600, bomb.getLevel()), entityCause);
            }

        }

        destroyMagicBlocks(bomb);
    }

    public static void destroyMagicBlocks(WitcherBombEntity bomb){
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

        for (BlockPos blockPos : affectedBlocks) {

            BlockState state = bomb.getWorld().getBlockState(blockPos);

            //To not destroy blocks behind other blocks
            if (getExposure(blockPos.toCenterPos(), bomb) == 0 && !state.isOf(Blocks.END_PORTAL_FRAME)) continue;

            //Destroy magic blocks
            if(state.isIn(TCOTS_Blocks.DESTROYABLE_MAGIC_BLOCKS)) {
                bomb.getWorld().breakBlock(blockPos, false, bomb);
            } else if (CampfireBlock.isLitCampfire(state) && state.isOf(Blocks.SOUL_CAMPFIRE)) {
                //To unlit magic campfires
                bomb.getWorld().syncWorldEvent(null, WorldEvents.FIRE_EXTINGUISHED, blockPos, 0);
                CampfireBlock.extinguish(bomb.getOwner(), bomb.getWorld(), blockPos, state);
                bomb.getWorld().setBlockState(blockPos, state.with(CampfireBlock.LIT, false));
            }
            else if (state.isOf(Blocks.END_PORTAL_FRAME) && state.contains(EndPortalFrameBlock.EYE) && state.get(EndPortalFrameBlock.EYE)){
                //To turn off end portals
                BlockPattern.Result result = EndPortalFrameBlock.getCompletedFramePattern().searchAround(bomb.getWorld(), blockPos);
                if (result != null) {
                    BlockPos blockPos2 = result.getFrontTopLeft().add(-3, 0, -3);
                    for (int i = 0; i < 3; ++i) {
                        for (int j = 0; j < 3; ++j) {
                            bomb.getWorld().breakBlock(blockPos2.add(i, 0, j), false, bomb);
                        }
                    }
                }
                bomb.getWorld().setBlockState(blockPos, state.with(EndPortalFrameBlock.EYE, false));
                Block.dropStack(bomb.getWorld(), blockPos.up(), Items.ENDER_EYE.getDefaultStack().copyWithCount(1));
            }

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

    public static void handleStatus(WitcherBombEntity bomb, byte status) {
        if(status== DIMERITIUM_BOMB_EXPLODES){
            bomb.getWorld().addParticle(TCOTS_Particles.DIMERITIUM_FLASH, bomb.getX(), bomb.getY()+2, bomb.getZ(), 0.0, 0.0, 0.0);
        }
    }

    public static void checkEffectMixin(LivingEntity entity, CallbackInfoReturnable<Boolean> cir){
        if(DimeritiumBomb.checkEffect(entity))
            cir.setReturnValue(false);
    }

    public static boolean checkEffect(LivingEntity entity){
        return entity.hasStatusEffect(TCOTS_Effects.DIMERITIUM_BOMB_EFFECT);
    }
}
