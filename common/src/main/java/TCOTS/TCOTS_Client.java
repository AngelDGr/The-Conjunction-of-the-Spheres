package TCOTS;

import TCOTS.items.weapons.GiantAnchorItem;
import TCOTS.registry.TCOTS_Items;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.platform.Platform;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

public class TCOTS_Client {
    public static ModelLayerLocation WITCHER_EYES_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "witcher_eyes"), "witcher_eyes");
    public static ModelLayerLocation TOXICITY_FACE_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "toxicity_face"), "toxicity_face");

    public static void initItemPropertiesRegistry(){
        ItemPropertiesRegistry.register(TCOTS_Items.GIANT_ANCHOR.get(), ResourceLocation.parse("invisible"), (stack, world, entity, seed) ->
                GiantAnchorItem.wasLaunched(stack)? 1.0f : 0.0f);

        //Crossbow animation
        ItemPropertiesRegistry.register(TCOTS_Items.KNIGHT_CROSSBOW.get(), ResourceLocation.parse("pull"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);

        //Crossbow states
        ItemPropertiesRegistry.register(TCOTS_Items.KNIGHT_CROSSBOW.get(), ResourceLocation.parse("pulling"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0f : 0.0f);
        ItemPropertiesRegistry.register(TCOTS_Items.KNIGHT_CROSSBOW.get(), ResourceLocation.parse("charged"), (stack, world, entity, seed) -> CrossbowItem.isCharged(stack) ? 1.0f : 0.0f);
        ItemPropertiesRegistry.register(TCOTS_Items.KNIGHT_CROSSBOW.get(), ResourceLocation.parse("firework"), (stack, world, entity, seed) -> {
            ChargedProjectiles chargedProjectilesComponent = stack.get(DataComponents.CHARGED_PROJECTILES);
            return chargedProjectilesComponent != null && chargedProjectilesComponent.contains(Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
        });

        ItemPropertiesRegistry.register(TCOTS_Items.KNIGHT_CROSSBOW.get(), ResourceLocation.parse("bolt"), (stack, world, entity, seed) ->
                CrossbowItem.isCharged(stack) && hasBoltProjectile(stack) ? 1.0f : 0.0f);
        ItemPropertiesRegistry.register(Items.CROSSBOW, ResourceLocation.parse("bolt"), (stack, world, entity, seed) ->
                CrossbowItem.isCharged(stack) && hasBoltProjectile(stack) ? 1.0f : 0.0f);
    }

    public static void renderNorthernWindIce(LivingEntity livingEntity, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, BlockRenderDispatcher blockRenderManager){

        matrixStack.pushPose();
        float blockSize = 1.75f;
        AABB boundingBox = livingEntity.getBoundingBox();
        BlockPos blockPos = BlockPos.containing(livingEntity.getX(), boundingBox.minY, livingEntity.getZ());
        matrixStack.scale(
                blockSize * (float)boundingBox.getXsize(),
                blockSize * (float)boundingBox.getYsize(),
                blockSize * (float)boundingBox.getZsize()
        );
        matrixStack.translate(-0.5, -0.3, -0.5);

        blockRenderManager
                .getModelRenderer()
                .tesselateBlock(
                        livingEntity.level(),
                        blockRenderManager.getBlockModel(Blocks.ICE.defaultBlockState()),
                        Blocks.ICE.defaultBlockState(),
                        blockPos,
                        matrixStack,
                        vertexConsumerProvider.getBuffer(ItemBlockRenderTypes.getMovingBlockRenderType(Blocks.ICE.defaultBlockState())),
                        false,
                        RandomSource.create(),
                        Blocks.ICE.defaultBlockState().getSeed(blockPos),
                        OverlayTexture.NO_OVERLAY
                );

        matrixStack.popPose();
    }

    @SuppressWarnings("all")
    public static boolean hasBoltProjectile(ItemStack crossbow) {
        ChargedProjectiles chargedProjectilesComponent = crossbow.get(DataComponents.CHARGED_PROJECTILES);
        return chargedProjectilesComponent != null && (
                chargedProjectilesComponent.contains(TCOTS_Items.BASE_BOLT.get())
                        || chargedProjectilesComponent.contains(TCOTS_Items.BLUNT_BOLT.get())
                        || chargedProjectilesComponent.contains(TCOTS_Items.BROADHEAD_BOLT.get())
                        || chargedProjectilesComponent.contains(TCOTS_Items.PRECISION_BOLT.get())
                        || chargedProjectilesComponent.contains(TCOTS_Items.EXPLODING_BOLT.get())
        );
    }
}
