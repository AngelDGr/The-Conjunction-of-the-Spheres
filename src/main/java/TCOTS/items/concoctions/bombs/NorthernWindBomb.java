package TCOTS.items.concoctions.bombs;

import TCOTS.blocks.FrostedSnowBlock;
import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.items.concoctions.TCOTS_Effects;
import TCOTS.particles.TCOTS_Particles;
import TCOTS.utils.BombsUtil;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

import java.util.HashSet;
import java.util.List;

public class NorthernWindBomb {
    private static final byte NORTHERN_WIND_EXPLODES = 38;

    public static void explosionLogic(WitcherBombEntity bomb){

        bomb.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1,1);

        bomb.getWorld().sendEntityStatus(bomb, NORTHERN_WIND_EXPLODES);

        List<LivingEntity> list = bomb.getWorld().getEntitiesByClass(LivingEntity.class, bomb.getBoundingBox().expand(3+(bomb.getLevel()*2),2,3+(bomb.getLevel()*2)),
                livingEntity ->
                        !(livingEntity instanceof WardenEntity) && !(livingEntity instanceof ArmorStandEntity)
                        && livingEntity.isAlive()
                        && livingEntity != bomb.getOwner()
                        && !(livingEntity.getType().isIn(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)));

        Entity entityCause = bomb.getEffectCause();
        for(LivingEntity entity: list){
            //To not apply effect across walls
            if(BombsUtil.getExposure(entity.getPos(), bomb) == 0) continue;

            //Applies slowness to players, damage to freeze_hurt_extra and effect to anything else
            if(entity instanceof PlayerEntity) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 240+(bomb.getLevel()*20), 4+bomb.getLevel(),false,false), entityCause);
            } else if (entity.getType().isIn(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
                entity.damage(bomb.getDamageSources().freeze(), 0.5f);
                entity.addStatusEffect(new StatusEffectInstance(TCOTS_Effects.NORTHERN_WIND_EFFECT, 80+(bomb.getLevel()*20), bomb.getLevel(),false,false), entityCause);
            } else {
                entity.addStatusEffect(new StatusEffectInstance(TCOTS_Effects.NORTHERN_WIND_EFFECT, 160+(bomb.getLevel()*20), bomb.getLevel(),false,false), entityCause);
            }

            if(entity.isOnFire()){
                entity.extinguish();
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

        for (BlockPos possibleBlockPos : affectedBlocks) {
            //To not destroy blocks behind other blocks
            if (BombsUtil.getExposure(possibleBlockPos.toCenterPos(), bomb) == 0 || bomb.isSubmergedInWater()) continue;

            BlockState blockStateWaterIce = Blocks.FROSTED_ICE.getDefaultState();

            //To put ice in blocks
            for (BooleanProperty booleanProperty : ConnectingBlock.FACING_PROPERTIES.values()) {
                BlockState blockStateIce = TCOTS_Blocks.FROSTED_SNOW.getDefaultState().with(booleanProperty,true);

                //To not put ice in water or in the nether
                if(bomb.getWorld().getBlockState(possibleBlockPos) == FrostedIceBlock.getMeltedState() || bomb.getWorld().getDimension().ultrawarm()) continue;

                if(blockStateIce.canPlaceAt(bomb.getWorld(),possibleBlockPos)
                        && (bomb.getWorld().getBlockState(possibleBlockPos).isAir() || bomb.getWorld().getBlockState(possibleBlockPos).isReplaceable())) {

                    //Put the ice
                    bomb.getWorld().setBlockState(possibleBlockPos, blockStateIce);

                    //Grows the ice
                    ((FrostedSnowBlock)TCOTS_Blocks.FROSTED_SNOW).getGrower().grow(blockStateIce, bomb.getWorld(), possibleBlockPos, false);
                }
            }



            //Check if it can put ice in water
            if (        bomb.getWorld().getBlockState(possibleBlockPos) != FrostedIceBlock.getMeltedState()
                    || !blockStateWaterIce.canPlaceAt(bomb.getWorld(), possibleBlockPos)
                    || !bomb.getWorld().canPlace(blockStateWaterIce, possibleBlockPos, ShapeContext.absent())) continue;


            //Put ice in water
            bomb.getWorld().setBlockState(possibleBlockPos, blockStateWaterIce);
            bomb.getWorld().scheduleBlockTick(possibleBlockPos, Blocks.FROSTED_ICE, MathHelper.nextInt(bomb.getWorld().getRandom(), 60, 120));
        }
    }

    public static void renderIce(LivingEntity livingEntity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider,  BlockRenderManager blockRenderManager){

        matrixStack.push();
        float blockSize = 1.75f;
        Box boundingBox = livingEntity.getBoundingBox();
        BlockPos blockPos = BlockPos.ofFloored(livingEntity.getX(), boundingBox.minY, livingEntity.getZ());
        matrixStack.scale(
                blockSize * (float)boundingBox.getLengthX(),
                blockSize * (float)boundingBox.getLengthY(),
                blockSize * (float)boundingBox.getLengthZ()
        );
        matrixStack.translate(-0.5, -0.3, -0.5);

        blockRenderManager
                .getModelRenderer()
                .render(
                        livingEntity.getWorld(),
                        blockRenderManager.getModel(Blocks.ICE.getDefaultState()),
                        Blocks.ICE.getDefaultState(),
                        blockPos,
                        matrixStack,
                        vertexConsumerProvider.getBuffer(RenderLayers.getMovingBlockLayer(Blocks.ICE.getDefaultState())),
                        false,
                        Random.create(),
                        Blocks.ICE.getDefaultState().getRenderingSeed(blockPos),
                        OverlayTexture.DEFAULT_UV
                );

        matrixStack.pop();
    }

    public static boolean checkEffect(LivingEntity entity){
        return entity.hasStatusEffect(TCOTS_Effects.NORTHERN_WIND_EFFECT);
    }
}
