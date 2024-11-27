package TCOTS.items.concoctions.bombs;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.items.concoctions.TCOTS_Effects;
import TCOTS.particles.TCOTS_Particles;
import TCOTS.utils.BombsUtil;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.*;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class DimeritiumBomb {
    private static final byte DIMERITIUM_BOMB_EXPLODES = 40;

    public static void explosionLogic(WitcherBombEntity bomb){

        bomb.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1,1);

        bomb.getWorld().sendEntityStatus(bomb, DIMERITIUM_BOMB_EXPLODES);

        List<Entity> list = bomb.getWorld().getEntitiesByClass(Entity.class, bomb.getBoundingBox().expand(3+(bomb.getLevel()*2),2,3+(bomb.getLevel()*2)),
                entity ->
                        ((entity instanceof LivingEntity) || entity.getType().isIn(TCOTS_Entities.DIMERITIUM_DAMAGE) || entity.getType().isIn(TCOTS_Entities.DIMERITIUM_REMOVAL))
                        && !(entity instanceof ArmorStandEntity)
                        && entity.isAlive()
                        && entity != bomb.getOwner());


        Entity entityCause = bomb.getEffectCause();
        for(Entity entity: list){
            //To not apply effect across walls
            if(BombsUtil.getExposure(entity.getPos(), bomb) == 0) continue;

            //Destroy End Crystals and Foglings
            if(entity.getType().isIn(TCOTS_Entities.DIMERITIUM_DAMAGE))
                entity.damage(bomb.getDamageSources().magic(), 1);

            //Remove magic entities
            if(entity.getType().isIn(TCOTS_Entities.DIMERITIUM_REMOVAL))
                entity.discard();

            //Applies dimeritium effect to entity
            if(entity instanceof LivingEntity livingEntity) {
                livingEntity.addStatusEffect(new StatusEffectInstance(TCOTS_Effects.DIMERITIUM_BOMB_EFFECT, bomb.getLevel() < 2 ? 100 : 200, bomb.getLevel()), entityCause);
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

                        BlockState blockState = bomb.getWorld().getBlockState(blockPos);
                        FluidState fluidState = bomb.getWorld().getFluidState(blockPos);

                        Optional<Float> optional = BombsUtil.getBlastResistance(blockState, fluidState);
                        if (optional.isPresent() && !DimeritiumBomb.canDestroy(blockState)) {
                            h -= (optional.get() + 0.3f) * 0.3f;
                        }
                        if (h > 0.0f && DimeritiumBomb.canDestroy(blockState)) {
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

            //To not destroy blocks behind other blocks
//            if (BombsUtil.getExposure(blockPos.toCenterPos(), bomb) == 0 && !state.isOf(Blocks.END_PORTAL_FRAME)) continue;

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

    private static boolean canDestroy(@NotNull BlockState state){
        return state.isIn(TCOTS_Blocks.DESTROYABLE_MAGIC_BLOCKS)
                || (CampfireBlock.isLitCampfire(state) && state.isOf(Blocks.SOUL_CAMPFIRE))
                || state.isOf(Blocks.END_PORTAL_FRAME) && state.contains(EndPortalFrameBlock.EYE) && state.get(EndPortalFrameBlock.EYE)
                || state.isOf(Blocks.END_PORTAL);
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
