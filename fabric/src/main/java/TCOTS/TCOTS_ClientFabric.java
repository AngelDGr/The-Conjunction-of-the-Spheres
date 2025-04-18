package TCOTS;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.blocks.TCOTS_Blocks_Fabric;
import TCOTS.blocks.geo.renderer.*;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.misc.renderers.*;
import TCOTS.entity.geo.renderer.*;
import TCOTS.entity.geo.renderer.necrophages.*;
import TCOTS.entity.geo.renderer.ogroids.*;
import TCOTS.entity.witcher_cosmetics.toxicity_face.ToxicityFaceModel;
import TCOTS.entity.witcher_cosmetics.witcher_eyes.WitcherEyesModel;
import TCOTS.items.TCOTS_Items_Fabric;
import TCOTS.items.concoctions.recipes.AlchemyTableRecipe;
import TCOTS.items.concoctions.recipes.ScreenHandlersAndRecipesRegister;
import TCOTS.items.weapons.GiantAnchorItem;
import TCOTS.particles.*;
import TCOTS.particles.bombEmitters.*;
import TCOTS.screen.AlchemyTableScreen;
import TCOTS.screen.HerbalTableScreen;
import TCOTS.screen.ToxicityHudOverlay;
import io.wispforest.lavender.client.LavenderBookScreen;
import io.wispforest.lavender.md.compiler.BookCompiler;
import io.wispforest.lavender.md.features.RecipeFeature;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.core.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import org.jetbrains.annotations.NotNull;


@Environment(value= EnvType.CLIENT)
public class TCOTS_ClientFabric implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        TCOTS_Client.init();

        //HUD
        HudRenderCallback.EVENT.register(((drawContext, tickCounter) -> ToxicityHudOverlay.onHudRender(drawContext,tickCounter.getGameTimeDeltaPartialTick(true))));

        //Send data when join
        {
            ClientPlayConnectionEvents.JOIN.register((handler, sender, client) ->
                    TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(new TCOTS_MainFabric.WitcherEyesFullPacket(
                            TCOTS_Main.CONFIG.witcher_eyes.activateEyes(),
                            TCOTS_Main.CONFIG.witcher_eyes.eyeShape().ordinal(),
                            TCOTS_Main.CONFIG.witcher_eyes.eyeSeparation().ordinal(),
                            TCOTS_Main.CONFIG.witcher_eyes.XEyePos(),
                            TCOTS_Main.CONFIG.witcher_eyes.YEyePos()
                    )
            ));
        }



        //Send client-packets to server
        {
            TCOTS_Main.CONFIG.witcher_eyes.subscribeToActivateEyes(activate ->
            {
                if (Minecraft.getInstance().getConnection() == null) return;
                TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(
                        new TCOTS_MainFabric.WitcherEyesFullPacket(
                                activate,
                                TCOTS_Main.CONFIG.witcher_eyes.eyeShape().ordinal(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeSeparation().ordinal(),
                                TCOTS_Main.CONFIG.witcher_eyes.XEyePos(),
                                TCOTS_Main.CONFIG.witcher_eyes.YEyePos()));
            });

            TCOTS_Main.CONFIG.witcher_eyes.subscribeToEyeSeparation(eye_separation ->
            {
                if (Minecraft.getInstance().getConnection() == null) return;
                TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(
                        new TCOTS_MainFabric.WitcherEyesFullPacket(
                                TCOTS_Main.CONFIG.witcher_eyes.activateEyes(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeShape().ordinal(),
                                eye_separation.ordinal(),
                                TCOTS_Main.CONFIG.witcher_eyes.XEyePos(),
                                TCOTS_Main.CONFIG.witcher_eyes.YEyePos()));
            });

            TCOTS_Main.CONFIG.witcher_eyes.subscribeToEyeShape(eye_shape ->
            {
                if(Minecraft.getInstance().getConnection()==null)return;
                TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(
                        new TCOTS_MainFabric.WitcherEyesFullPacket(
                                TCOTS_Main.CONFIG.witcher_eyes.activateEyes(),
                                eye_shape.ordinal(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeSeparation().ordinal(),
                                TCOTS_Main.CONFIG.witcher_eyes.XEyePos(),
                                TCOTS_Main.CONFIG.witcher_eyes.YEyePos()));
            });

            TCOTS_Main.CONFIG.witcher_eyes.subscribeToXEyePos(xEyePos ->
            {
                if(Minecraft.getInstance().getConnection()==null)return;
                TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(
                        new TCOTS_MainFabric.WitcherEyesFullPacket(
                                TCOTS_Main.CONFIG.witcher_eyes.activateEyes(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeShape().ordinal(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeSeparation().ordinal(),
                                xEyePos,
                                TCOTS_Main.CONFIG.witcher_eyes.YEyePos()));
            });

            TCOTS_Main.CONFIG.witcher_eyes.subscribeToYEyePos(yEyePos ->
            {
                if(Minecraft.getInstance().getConnection()==null)return;
                TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(
                        new TCOTS_MainFabric.WitcherEyesFullPacket(
                                TCOTS_Main.CONFIG.witcher_eyes.activateEyes(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeShape().ordinal(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeSeparation().ordinal(),
                                TCOTS_Main.CONFIG.witcher_eyes.XEyePos(),
                                yEyePos));
            });

            TCOTS_Main.CONFIG.witcher_eyes.subscribeToActivateToxicity(activateToxicity ->
            {
                if(Minecraft.getInstance().getConnection()==null)return;
                TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(
                        new TCOTS_MainFabric.ToxicityFacePacket(activateToxicity));
            });
        }

        //Grass Colors
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) {
                return GrassColor.getDefaultColor();
            }
            return BiomeColors.getAverageGrassColor(world, pos);}, TCOTS_Blocks_Fabric.HAN_FIBER_PLANT, TCOTS_Blocks_Fabric.ARENARIA_BUSH,
                TCOTS_Blocks_Fabric.CELANDINE_PLANT, TCOTS_Blocks_Fabric.CROWS_EYE_FERN, TCOTS_Blocks_Fabric.VERBENA_FLOWER, TCOTS_Blocks_Fabric.POTTED_BRYONIA_FLOWER);

        //Leaves Colors
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) {
                return FoliageColor.getDefaultColor();

            }
            return BiomeColors.getAverageGrassColor(world, pos);},
                TCOTS_Blocks_Fabric.BRYONIA_VINE);


        //Crossbow Color
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1: DyedItemColor.getOrDefault(stack, -6265536), TCOTS_Items_Fabric.KNIGHT_CROSSBOW);

        //Crossbow animation
        ItemProperties.register(TCOTS_Items_Fabric.KNIGHT_CROSSBOW, ResourceLocation.parse("pull"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);

        ItemProperties.register(TCOTS_Items_Fabric.KNIGHT_CROSSBOW, ResourceLocation.parse("pulling"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0f : 0.0f);
        ItemProperties.register(TCOTS_Items_Fabric.KNIGHT_CROSSBOW, ResourceLocation.parse("charged"), (stack, world, entity, seed) -> CrossbowItem.isCharged(stack) ? 1.0f : 0.0f);
        ItemProperties.register(TCOTS_Items_Fabric.KNIGHT_CROSSBOW, ResourceLocation.parse("firework"), (stack, world, entity, seed) -> {
            ChargedProjectiles chargedProjectilesComponent = stack.get(DataComponents.CHARGED_PROJECTILES);
            return chargedProjectilesComponent != null && chargedProjectilesComponent.contains(Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
        });

        ItemProperties.register(TCOTS_Items_Fabric.KNIGHT_CROSSBOW, ResourceLocation.parse("bolt"), (stack, world, entity, seed) ->
                CrossbowItem.isCharged(stack) && hasBoltProjectile(stack) ? 1.0f : 0.0f);
        ItemProperties.register(Items.CROSSBOW, ResourceLocation.parse("bolt"), (stack, world, entity, seed) ->
                CrossbowItem.isCharged(stack) && hasBoltProjectile(stack) ? 1.0f : 0.0f);


        //Monsters
        EntityRendererRegistry.register(TCOTS_Entities.DROWNER, DrownerRenderer::new);
        EntityRendererRegistry.register(TCOTS_Entities.DROWNER_PUDDLE, DrownerPuddleRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.ROTFIEND, RotfiendRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.GRAVE_HAG, GraveHagRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.WATER_HAG, WaterHagRenderer::new);
        EntityRendererRegistry.register(TCOTS_Entities.WATER_HAG_MUD_BALL, ThrownItemRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.FOGLET, FogletRenderer::new);
        EntityRendererRegistry.register(TCOTS_Entities.FOGLING, FoglingRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.GHOUL, GhoulRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.ALGHOUL, AlghoulRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.SCURVER, ScurverRenderer::new);
        EntityRendererRegistry.register(TCOTS_Entities.SCURVER_SPINE, ScurverSpineRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.DEVOURER, DevourerRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.GRAVEIR, GraveirRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.BULLVORE, BullvoreRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.NEKKER, NekkerRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.NEKKER_WARRIOR, NekkerWarriorRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.CYCLOPS, CyclopsRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.ROCK_TROLL, RockTrollRenderer::new);
        EntityRendererRegistry.register(TCOTS_Entities.TROLL_ROCK_PROJECTILE, context -> new ThrownItemRenderer<>(context, 2.0f, true));

        EntityRendererRegistry.register(TCOTS_Entities.ICE_TROLL, IceTrollRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.FOREST_TROLL, ForestTrollRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.ICE_GIANT, IceGiantRenderer::new);
        EntityRendererRegistry.register(TCOTS_Entities.ANCHOR_PROJECTILE, AnchorProjectileRenderer::new);

        ItemProperties.register(TCOTS_Items_Fabric.GIANT_ANCHOR, ResourceLocation.parse("invisible"), (stack, world, entity, seed) ->
                GiantAnchorItem.wasLaunched(stack)? 1.0f : 0.0f);

        //Bomb
        EntityRendererRegistry.register(TCOTS_Entities.WITCHER_BOMB, ThrownItemRenderer::new);
        //Crossbow bolts
        EntityRendererRegistry.register(TCOTS_Entities.BASE_BOLT, BaseBoltEntityRenderer::new);
        EntityRendererRegistry.register(TCOTS_Entities.BLUNT_BOLT, BluntBoltEntityRenderer::new);
        EntityRendererRegistry.register(TCOTS_Entities.PRECISION_BOLT, PrecisionBoltEntityRenderer::new);
        EntityRendererRegistry.register(TCOTS_Entities.EXPLODING_BOLT, ExplodingBoltEntityRenderer::new);
        EntityRendererRegistry.register(TCOTS_Entities.BROADHEAD_BOLT, BroadheadBoltEntityRenderer::new);


        //Blocks
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks_Fabric.ARENARIA_BUSH, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks_Fabric.CELANDINE_PLANT, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks_Fabric.CROWS_EYE_FERN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks_Fabric.BRYONIA_VINE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks_Fabric.VERBENA_FLOWER, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks_Fabric.HAN_FIBER_PLANT, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks_Fabric.PUFFBALL_MUSHROOM, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks_Fabric.SEWANT_MUSHROOMS_PLANT, RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks_Fabric.POTTED_VERBENA_FLOWER, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks_Fabric.POTTED_CELANDINE_FLOWER, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks_Fabric.POTTED_HAN_FIBER, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks_Fabric.POTTED_PUFFBALL_MUSHROOM, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks_Fabric.POTTED_SEWANT_MUSHROOMS, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks_Fabric.POTTED_BRYONIA_FLOWER, RenderType.cutout());


        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks_Fabric.FROSTED_SNOW, RenderType.translucent());


        //BlockEntity
        BlockEntityRenderers.register(TCOTS_Blocks.SKULL_NEST_ENTITY, NestSkullBlockRenderer::new);
        BlockEntityRenderers.register(TCOTS_Blocks_Fabric.MONSTER_NEST_ENTITY, MonsterNestRenderer::new);
        BlockEntityRenderers.register(TCOTS_Blocks_Fabric.ALCHEMY_TABLE_ENTITY, AlchemyTableRenderer::new);
        BlockEntityRenderers.register(TCOTS_Blocks.HERBAL_TABLE_ENTITY, HerbalTableRenderer::new);
        BlockEntityRenderers.register(TCOTS_Blocks.GIANT_ANCHOR_ENTITY, GiantAnchorRenderer::new);
        BlockEntityRenderers.register(TCOTS_Blocks.WINTERS_BLADE_SKELETON_ENTITY, WintersBladeSkeletonRenderer::new);
        BlockEntityRenderers.register(TCOTS_Blocks.SKELETON_BLOCK_ENTITY, SkeletonBlockRenderer::new);


        //Particles
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.ROTFIEND_BLOOD_EXPLOSION.getType(), Rotfiend_BloodExplosionParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.ROTFIEND_BLOOD_EMITTER, new Rotfiend_BloodEmitterParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.GRAVE_HAG_GREEN_SALIVA, GraveHag_GreenSaliva.Provider::new);
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.FOGLET_FOG, Foglet_FogParticle.FogFactory::new);
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.FOGLET_FOG_AROUND, Foglet_FogParticleAround.FogFactory::new);
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.GRAPESHOT_EXPLOSION_EMITTER, new Grapeshot_ExplosionEmitterParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.DANCING_STAR_EXPLOSION_EMITTER, new DancingStar_ExplosionEmitterParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.DEVILS_PUFFBALL_EXPLOSION_EMITTER, new DevilsPuffball_ExplosionEmitterParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.GREEN_CLOUD, CloudParticleColor.GreenCloudFactory::new);
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.SAMUM_EXPLOSION_EMITTER, new Samum_ExplosionEmitterParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.NORTHERN_WIND_EXPLOSION_EMITTER, new NorthernWind_ExplosionEmitterParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.DRAGONS_DREAM_EXPLOSION_EMITTER, new DragonsDream_ExplosionEmitterParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.YELLOW_CLOUD, CloudParticleColor.YellowCloudFactory::new);
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.DIMERITIUM_FLASH, DimeritiumFlash.FlashFactory::new);
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.MOON_DUST_EXPLOSION_EMITTER, new MoonDust_ExplosionEmitterParticle.Factory());

        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.FALLING_BLOOD_PARTICLE,
                (spriteProvider) ->
                (type, world, x, y, z, velocityX, velocityY, velocityZ) ->
                        new BloodParticle.Factory(spriteProvider,
                                BloodParticle.createFallingBlood(type, world, x, y, z, velocityX, velocityY, velocityZ))
                                .createParticle(type, world, x, y, z, velocityX, velocityY, velocityZ)
        );
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.LANDING_BLOOD_PARTICLE,
                (spriteProvider) ->
                        (type, world, x, y, z, velocityX, velocityY, velocityZ) ->
                                new BloodParticle.Factory(spriteProvider,
                                        BloodParticle.createLandingBlood(type, world, x, y, z, velocityX, velocityY, velocityZ))
                                        .createParticle(type, world, x, y, z, velocityX, velocityY, velocityZ)
        );


        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.FALLING_BLACK_BLOOD_PARTICLE,
                (spriteProvider) ->
                (type, world, x, y, z, velocityX, velocityY, velocityZ) ->
                        new BloodParticle.Factory(spriteProvider,
                                BloodParticle.createFallingBlackBlood(type, world, x, y, z, velocityX, velocityY, velocityZ))
                                .createParticle(type, world, x, y, z, velocityX, velocityY, velocityZ)
        );
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.LANDING_BLACK_BLOOD_PARTICLE,                 (spriteProvider) ->
                (type, world, x, y, z, velocityX, velocityY, velocityZ) ->
                        new BloodParticle.Factory(spriteProvider,
                                BloodParticle.createLandingBlackBlood(type, world, x, y, z, velocityX, velocityY, velocityZ))
                                .createParticle(type, world, x, y, z, velocityX, velocityY, velocityZ)
        );


        MenuScreens.register(ScreenHandlersAndRecipesRegister.ALCHEMY_TABLE_SCREEN_HANDLER, AlchemyTableScreen::new);
        MenuScreens.register(ScreenHandlersAndRecipesRegister.HERBAL_TABLE_SCREEN_HANDLER, HerbalTableScreen::new);

        //Register Recipe Previews
        LavenderBookScreen.registerRecipePreviewBuilder(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "alchemy_book"), AlchemyTableRecipe.Type.INSTANCE, (alchemyTable_RecipePreviewBuilder));

        LavenderBookScreen.registerRecipePreviewBuilder(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "witcher_bestiary"), AlchemyTableRecipe.Type.INSTANCE, (alchemyTable_RecipePreviewBuilder));

        EntityModelLayerRegistry.registerModelLayer(TCOTS_Client.WITCHER_EYES_LAYER, WitcherEyesModel_createModelData());

        EntityModelLayerRegistry.registerModelLayer(TCOTS_Client.TOXICITY_FACE_LAYER, ToxicityFaceModel_createModelData());
    }

    public static boolean hasBoltProjectile(ItemStack crossbow) {
        ChargedProjectiles chargedProjectilesComponent = crossbow.get(DataComponents.CHARGED_PROJECTILES);
        return chargedProjectilesComponent != null && (
                chargedProjectilesComponent.contains(TCOTS_Items_Fabric.BASE_BOLT)
                        || chargedProjectilesComponent.contains(TCOTS_Items_Fabric.BLUNT_BOLT)
                        || chargedProjectilesComponent.contains(TCOTS_Items_Fabric.BROADHEAD_BOLT)
                        || chargedProjectilesComponent.contains(TCOTS_Items_Fabric.PRECISION_BOLT)
                        || chargedProjectilesComponent.contains(TCOTS_Items_Fabric.EXPLODING_BOLT)
        );
    }

    public static EntityModelLayerRegistry.TexturedModelDataProvider WitcherEyesModel_createModelData(){
        return () -> LayerDefinition.create(
                WitcherEyesModel.getModelData(new CubeDeformation(0)),
                96, 64);
    }

    public static EntityModelLayerRegistry.TexturedModelDataProvider ToxicityFaceModel_createModelData(){
        return () -> LayerDefinition.create(
                ToxicityFaceModel.getModelData(new CubeDeformation(0)),
                96, 64);
    }

    @SuppressWarnings("all")
    private static final RecipeFeature.RecipePreviewBuilder<AlchemyTableRecipe> alchemyTable_RecipePreviewBuilder = new RecipeFeature.RecipePreviewBuilder<>() {
        @Override
        public @NotNull Component buildRecipePreview(BookCompiler.ComponentSource componentSource, @NotNull RecipeHolder<AlchemyTableRecipe> recipeEntry) {
            ResourceLocation TEXTURE_ID = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/gui/alchemy_book_gui.png");

            //Get the recipe
            var recipe = recipeEntry.value();

            //Makes how it's going to flow the content in root
            //Size horizontal       and      vertical
            var root = Containers.horizontalFlow(Sizing.content(), Sizing.fixed(41));
            root.verticalAlignment(VerticalAlignment.CENTER).horizontalAlignment(HorizontalAlignment.CENTER);

            //Makes how it's going to flow the content in resultContainer
            //Horizontal
            var resultContainer = Containers.horizontalFlow(Sizing.fixed(111), Sizing.fixed(41));
            resultContainer.horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.CENTER);

            root.child(resultContainer);

            if(this.hasRecipe(recipeEntry)){
                //Add child for result item
                resultContainer.child(
                        //Makes a container of Item Stack
                        Containers.stack(Sizing.fixed(22), Sizing.fixed(22))
                                //Add as child the result
                                .child(Components.item(recipe.getResultItem(null)).showOverlay(true).setTooltipFromStack(true))
                                //Add as child the texture
                                .child(Components.texture(TEXTURE_ID,
                                        435, 144,
                                        22, 22,
                                        512, 256).blend(true))
                                //Put in the result-specific place
                                .positioning(Positioning.absolute(36, 0))
                                //Align it
                                .horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.CENTER));

                //Adds the texture
                resultContainer.child(
                        Containers.stack(Sizing.content(), Sizing.fixed(18))
                                .child(Components.texture(TEXTURE_ID,
                                        399, 167,
                                        111, 18,
                                        512, 256).blend(true))
                                .positioning(Positioning.absolute(0, 23))
                                .horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.CENTER));

                //Loop to assign each ingredient to a container
                for (int i = 0; i < recipe.getIngredients().size(); i++) {
                    //Get the ingredient stack
                    ItemStack stack = recipe.getIngredients().get(i).getItems()[0];
                    //Get the quantity
                    int count = recipe.getIngredientsCounts().get(i);
                    //Creates the stack with the correct quantity
                    ItemStack ingredientStack = new ItemStack(stack.getItem(), count);
                    switch (i) {
                        case 0:
                            resultContainer.child(
                                    Containers.stack(Sizing.fixed(18), Sizing.fixed(18))
                                            .child(Components.item(ingredientStack).showOverlay(true).setTooltipFromStack(true))
                                            .positioning(Positioning.absolute(38, 23))
                                            .horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.CENTER)
                            );
                            break;

                        case 1:
                            resultContainer.child(
                                    Containers.stack(Sizing.fixed(18), Sizing.fixed(18))
                                            .child(Components.item(ingredientStack).showOverlay(true).setTooltipFromStack(true))
                                            .positioning(Positioning.absolute(19, 23))
                                            .horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.CENTER)
                            );
                            break;

                        case 2:
                            resultContainer.child(
                                    Containers.stack(Sizing.fixed(18), Sizing.fixed(18))
                                            .child(Components.item(ingredientStack).showOverlay(true).setTooltipFromStack(true))
                                            .positioning(Positioning.absolute(57, 23))
                                            .horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.CENTER)
                            );
                            break;

                        case 3:
                            resultContainer.child(
                                    Containers.stack(Sizing.fixed(18), Sizing.fixed(18))
                                            .child(Components.item(ingredientStack).showOverlay(true).setTooltipFromStack(true))
                                            .positioning(Positioning.absolute(0, 23))
                                            .horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.CENTER)
                            );
                            break;

                        case 4:
                            resultContainer.child(
                                    Containers.stack(Sizing.fixed(18), Sizing.fixed(18))
                                            .child(Components.item(ingredientStack).showOverlay(true).setTooltipFromStack(true))
                                            .positioning(Positioning.absolute(76, 23))
                                            .horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.CENTER)
                            );
                            break;
                        default:
                            break;
                    }
                }

                //Base container
                resultContainer.child(
                        Containers.stack(Sizing.content(), Sizing.fixed(16))
                                .child(Components.item(recipe.getBaseItem()).showOverlay(true).setTooltipFromStack(true))
                                .positioning(Positioning.absolute(95, 24))
                                .horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.CENTER)
                );
            } else {
                //Add child for result item
                resultContainer.child(
                        //Makes a container of Item Stack
                        Containers.stack(Sizing.fixed(22), Sizing.fixed(22))
                                //Add as child the texture
                                .child(Components.texture(TEXTURE_ID,
                                        435, 199,
                                        22, 22,
                                        512, 256).blend(true))
                                //Put in the result-specific place
                                .positioning(Positioning.absolute(36, 0))
                                //Align it
                                .horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.CENTER));

                //Adds the texture
                resultContainer.child(
                        Containers.stack(Sizing.content(), Sizing.fixed(18))
                                .child(Components.texture(TEXTURE_ID,
                                        399, 222,
                                        111, 18,
                                        512, 256).blend(true))
                                .positioning(Positioning.absolute(0, 23))
                                .horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.CENTER));
            }

            return root;
        }

        private boolean hasRecipe(@NotNull RecipeHolder<AlchemyTableRecipe> recipeEntry){
            return Minecraft.getInstance().player != null && Minecraft.getInstance().player.getRecipeBook().contains(recipeEntry);
        }
    };

}

