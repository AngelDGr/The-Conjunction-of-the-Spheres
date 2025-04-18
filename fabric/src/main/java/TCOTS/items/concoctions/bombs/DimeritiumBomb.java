package TCOTS.items.concoctions.bombs;

import TCOTS.TCOTS_Tags;
import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.items.concoctions.TCOTS_Effects;
import TCOTS.particles.TCOTS_Particles_Fabric;
import TCOTS.utils.BombsUtil;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class DimeritiumBomb {
    private static final byte DIMERITIUM_BOMB_EXPLODES = 40;

    public static void explosionLogic(WitcherBombEntity bomb){

        bomb.playSound(SoundEvents.GENERIC_EXPLODE.value(), 1,1);

        bomb.level().broadcastEntityEvent(bomb, DIMERITIUM_BOMB_EXPLODES);

        List<Entity> list = bomb.level().getEntitiesOfClass(Entity.class, bomb.getBoundingBox().inflate(3+(bomb.getLevel()*2),2,3+(bomb.getLevel()*2)),
                entity ->
                        ((entity instanceof LivingEntity) || entity.getType().is(TCOTS_Tags.DIMERITIUM_DAMAGE) || entity.getType().is(TCOTS_Tags.DIMERITIUM_REMOVAL))
                        && !(entity instanceof ArmorStand)
                        && entity.isAlive()
                        && entity != bomb.getOwner());


        Entity entityCause = bomb.getEffectSource();
        for(Entity entity: list){
            //To not apply effect across walls
            if(BombsUtil.getExposure(entity.position(), bomb) == 0) continue;

            //Destroy End Crystals and Foglings
            if(entity.getType().is(TCOTS_Tags.DIMERITIUM_DAMAGE))
                entity.hurt(bomb.damageSources().magic(), 1);

            //Remove magic entities
            if(entity.getType().is(TCOTS_Tags.DIMERITIUM_REMOVAL))
                entity.discard();

            //Applies dimeritium effect to entity
            if(entity instanceof LivingEntity livingEntity) {
                livingEntity.addEffect(new MobEffectInstance(TCOTS_Effects.DIMERITIUM_BOMB_EFFECT, bomb.getLevel() < 2 ? 100 : 200, bomb.getLevel()), entityCause);
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
                    for (float h = (1.25f + (bomb.getLevel() * 0.25f)) * (0.7f + bomb.level().random.nextFloat() * 0.6f); h > 0.0f; h -= 0.22500001f) {

                        BlockPos blockPos = BlockPos.containing(m, n, o);

                        BlockState blockState = bomb.level().getBlockState(blockPos);
                        FluidState fluidState = bomb.level().getFluidState(blockPos);

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

            BlockState state = bomb.level().getBlockState(blockPos);

            //To not destroy blocks behind other blocks
//            if (BombsUtil.getExposure(blockPos.toCenterPos(), bomb) == 0 && !state.isOf(Blocks.END_PORTAL_FRAME)) continue;

            //Destroy magic blocks
            if(state.is(TCOTS_Tags.DESTROYABLE_MAGIC_BLOCKS)) {
                bomb.level().destroyBlock(blockPos, false, bomb);
            } else if (CampfireBlock.isLitCampfire(state) && state.is(Blocks.SOUL_CAMPFIRE)) {
                //To unlit magic campfires
                bomb.level().levelEvent(null, LevelEvent.SOUND_EXTINGUISH_FIRE, blockPos, 0);
                CampfireBlock.dowse(bomb.getOwner(), bomb.level(), blockPos, state);
                bomb.level().setBlockAndUpdate(blockPos, state.setValue(CampfireBlock.LIT, false));
            }
            else if (state.is(Blocks.END_PORTAL_FRAME) && state.hasProperty(EndPortalFrameBlock.HAS_EYE) && state.getValue(EndPortalFrameBlock.HAS_EYE)){
                //To turn off end portals
                BlockPattern.BlockPatternMatch result = EndPortalFrameBlock.getOrCreatePortalShape().find(bomb.level(), blockPos);
                if (result != null) {
                    BlockPos blockPos2 = result.getFrontTopLeft().offset(-3, 0, -3);
                    for (int i = 0; i < 3; ++i) {
                        for (int j = 0; j < 3; ++j) {
                            bomb.level().destroyBlock(blockPos2.offset(i, 0, j), false, bomb);
                        }
                    }
                }
                bomb.level().setBlockAndUpdate(blockPos, state.setValue(EndPortalFrameBlock.HAS_EYE, false));
                Block.popResource(bomb.level(), blockPos.above(), Items.ENDER_EYE.getDefaultInstance().copyWithCount(1));
            }

        }
    }

    private static boolean canDestroy(@NotNull BlockState state){
        return state.is(TCOTS_Tags.DESTROYABLE_MAGIC_BLOCKS)
                || (CampfireBlock.isLitCampfire(state) && state.is(Blocks.SOUL_CAMPFIRE))
                || state.is(Blocks.END_PORTAL_FRAME) && state.hasProperty(EndPortalFrameBlock.HAS_EYE) && state.getValue(EndPortalFrameBlock.HAS_EYE)
                || state.is(Blocks.END_PORTAL);
    }

    public static void handleStatus(WitcherBombEntity bomb, byte status) {
        if(status== DIMERITIUM_BOMB_EXPLODES){
            bomb.level().addParticle(TCOTS_Particles_Fabric.DIMERITIUM_FLASH, bomb.getX(), bomb.getY()+2, bomb.getZ(), 0.0, 0.0, 0.0);
        }
    }

    public static void checkEffectMixin(LivingEntity entity, CallbackInfoReturnable<Boolean> cir){
        if(DimeritiumBomb.checkEffect(entity))
            cir.setReturnValue(false);
    }

    public static boolean checkEffect(LivingEntity entity){
        return entity.hasEffect(TCOTS_Effects.DIMERITIUM_BOMB_EFFECT);
    }
}
