package fabric.TCOTS;

import TCOTS.TCOTS_Client;
import TCOTS.TCOTS_Main;
import TCOTS.registry.TCOTS_Blocks;
import TCOTS.registry.TCOTS_Entities;
import TCOTS.registry.TCOTS_Items;
import TCOTS.blocks.geo.renderer.*;
import TCOTS.registry.TCOTS_Particles;
import TCOTS.registry.TCOTS_ScreenHandlersAndRecipes;
import TCOTS.blocks.geo.renderer.AlchemyTableRenderer;
import TCOTS.blocks.geo.renderer.MonsterNestRenderer;
import TCOTS.blocks.geo.renderer.NestSkullBlockRenderer;
import TCOTS.entity.witcher_cosmetics.toxicity_face.ToxicityFaceModel;
import TCOTS.entity.witcher_cosmetics.witcher_eyes.WitcherEyesModel;
import TCOTS.entity.geo.renderer.AnchorProjectileRenderer;
import TCOTS.entity.geo.renderer.necrophages.*;
import TCOTS.entity.geo.renderer.ogroids.*;
import TCOTS.entity.misc.renderers.*;
import TCOTS.recipes.AlchemyTableRecipe;
import TCOTS.particles.*;
import TCOTS.particles.bombEmitters.*;
import TCOTS.particles.Foglet_FogParticle;
import TCOTS.particles.Rotfiend_BloodEmitterParticle;
import TCOTS.particles.bombEmitters.DevilsPuffball_ExplosionEmitterParticle;
import TCOTS.particles.bombEmitters.DragonsDream_ExplosionEmitterParticle;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import org.jetbrains.annotations.NotNull;


@Environment(value= EnvType.CLIENT)
public class TCOTS_ClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        //Send data when join
        {
            ClientPlayConnectionEvents.JOIN.register((handler, sender, client) ->
                    TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(new TCOTS_Main.WitcherEyesFullPacket(
                                    TCOTS_Main.CONFIG.witcher_eyes.activateEyes(),
                                    TCOTS_Main.CONFIG.witcher_eyes.eyeShape().ordinal(),
                                    TCOTS_Main.CONFIG.witcher_eyes.eyeSeparation().ordinal(),
                                    TCOTS_Main.CONFIG.witcher_eyes.XEyePos(),
                                    TCOTS_Main.CONFIG.witcher_eyes.YEyePos()
                            )
                    ));
        }

//        registerHUDLayers()
        {
            HudRenderCallback.EVENT.register(((drawContext, tickCounter) -> ToxicityHudOverlay.onHudRender(drawContext, tickCounter.getGameTimeDeltaPartialTick(true))));
        }

//        registerClientEvent()
        {
            TCOTS_Client.initItemPropertiesRegistry();

            //Send client-packets to server
            {
                TCOTS_Main.CONFIG.witcher_eyes.subscribeToActivateEyes(activate ->
                {
                    if (Minecraft.getInstance().getConnection() == null) return;
                    TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(
                            new TCOTS_Main.WitcherEyesFullPacket(
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
                            new TCOTS_Main.WitcherEyesFullPacket(
                                    TCOTS_Main.CONFIG.witcher_eyes.activateEyes(),
                                    TCOTS_Main.CONFIG.witcher_eyes.eyeShape().ordinal(),
                                    eye_separation.ordinal(),
                                    TCOTS_Main.CONFIG.witcher_eyes.XEyePos(),
                                    TCOTS_Main.CONFIG.witcher_eyes.YEyePos()));
                });

                TCOTS_Main.CONFIG.witcher_eyes.subscribeToEyeShape(eye_shape ->
                {
                    if (Minecraft.getInstance().getConnection() == null) return;
                    TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(
                            new TCOTS_Main.WitcherEyesFullPacket(
                                    TCOTS_Main.CONFIG.witcher_eyes.activateEyes(),
                                    eye_shape.ordinal(),
                                    TCOTS_Main.CONFIG.witcher_eyes.eyeSeparation().ordinal(),
                                    TCOTS_Main.CONFIG.witcher_eyes.XEyePos(),
                                    TCOTS_Main.CONFIG.witcher_eyes.YEyePos()));
                });

                TCOTS_Main.CONFIG.witcher_eyes.subscribeToXEyePos(xEyePos ->
                {
                    if (Minecraft.getInstance().getConnection() == null) return;
                    TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(
                            new TCOTS_Main.WitcherEyesFullPacket(
                                    TCOTS_Main.CONFIG.witcher_eyes.activateEyes(),
                                    TCOTS_Main.CONFIG.witcher_eyes.eyeShape().ordinal(),
                                    TCOTS_Main.CONFIG.witcher_eyes.eyeSeparation().ordinal(),
                                    xEyePos,
                                    TCOTS_Main.CONFIG.witcher_eyes.YEyePos()));
                });

                TCOTS_Main.CONFIG.witcher_eyes.subscribeToYEyePos(yEyePos ->
                {
                    if (Minecraft.getInstance().getConnection() == null) return;
                    TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(
                            new TCOTS_Main.WitcherEyesFullPacket(
                                    TCOTS_Main.CONFIG.witcher_eyes.activateEyes(),
                                    TCOTS_Main.CONFIG.witcher_eyes.eyeShape().ordinal(),
                                    TCOTS_Main.CONFIG.witcher_eyes.eyeSeparation().ordinal(),
                                    TCOTS_Main.CONFIG.witcher_eyes.XEyePos(),
                                    yEyePos));
                });

                TCOTS_Main.CONFIG.witcher_eyes.subscribeToActivateToxicity(activateToxicity ->
                {
                    if (Minecraft.getInstance().getConnection() == null) return;
                    TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(
                            new TCOTS_Main.ToxicityFacePacket(activateToxicity));
                });
            }

//            registerBlockRenderers();
        }

//        registerColorBlocks()
        {
            //Grass Colors
            ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
                        if (world == null || pos == null) {
                            return GrassColor.getDefaultColor();
                        }
                        return BiomeColors.getAverageGrassColor(world, pos);
                    }, TCOTS_Blocks.HanFiberPlant(), TCOTS_Blocks.ArenariaBush(),
                    TCOTS_Blocks.CelandinePlant(), TCOTS_Blocks.CrowsEyeFern(), TCOTS_Blocks.VerbenaFlower(), TCOTS_Blocks.PottedBryoniaFlower());

            //Leaves Colors
            ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
                        if (world == null || pos == null) {
                            return FoliageColor.getDefaultColor();

                        }
                        return BiomeColors.getAverageGrassColor(world, pos);
                    },
                    TCOTS_Blocks.BryoniaVine());
        }

//        registerColorItems()
        {
            //Crossbow Color
            ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : DyedItemColor.getOrDefault(stack, -6265536), TCOTS_Items.KNIGHT_CROSSBOW.get());
        }

//        registerEntityRenderers()
        {
            //Monsters
            EntityRendererRegistry.register(TCOTS_Entities.Drowner(), DrownerRenderer::new);
            EntityRendererRegistry.register(TCOTS_Entities.DrownerPuddle(), DrownerPuddleRenderer::new);

            EntityRendererRegistry.register(TCOTS_Entities.Rotfiend(), RotfiendRenderer::new);

            EntityRendererRegistry.register(TCOTS_Entities.GraveHag(), GraveHagRenderer::new);

            EntityRendererRegistry.register(TCOTS_Entities.WaterHag(), WaterHagRenderer::new);
            EntityRendererRegistry.register(TCOTS_Entities.WaterHagMudBall(), ThrownItemRenderer::new);

            EntityRendererRegistry.register(TCOTS_Entities.Foglet(), FogletRenderer::new);
            EntityRendererRegistry.register(TCOTS_Entities.Fogling(), FoglingRenderer::new);

            EntityRendererRegistry.register(TCOTS_Entities.Ghoul(), GhoulRenderer::new);

            EntityRendererRegistry.register(TCOTS_Entities.Alghoul(), AlghoulRenderer::new);

            EntityRendererRegistry.register(TCOTS_Entities.Scurver(), ScurverRenderer::new);
            EntityRendererRegistry.register(TCOTS_Entities.ScurverSpine(), ScurverSpineRenderer::new);

            EntityRendererRegistry.register(TCOTS_Entities.Devourer(), DevourerRenderer::new);

            EntityRendererRegistry.register(TCOTS_Entities.Graveir(), GraveirRenderer::new);

            EntityRendererRegistry.register(TCOTS_Entities.Bullvore(), BullvoreRenderer::new);

            EntityRendererRegistry.register(TCOTS_Entities.Nekker(), NekkerRenderer::new);

            EntityRendererRegistry.register(TCOTS_Entities.NekkerWarrior(), NekkerWarriorRenderer::new);

            EntityRendererRegistry.register(TCOTS_Entities.Cyclops(), CyclopsRenderer::new);

            EntityRendererRegistry.register(TCOTS_Entities.RockTroll(), RockTrollRenderer::new);
            EntityRendererRegistry.register(TCOTS_Entities.TrollRockProjectile(), context -> new ThrownItemRenderer<>(context, 2.0f, true));

            EntityRendererRegistry.register(TCOTS_Entities.IceTroll(), IceTrollRenderer::new);

            EntityRendererRegistry.register(TCOTS_Entities.ForestTroll(), ForestTrollRenderer::new);

            EntityRendererRegistry.register(TCOTS_Entities.IceGiant(), IceGiantRenderer::new);
            EntityRendererRegistry.register(TCOTS_Entities.AnchorProjectile(), AnchorProjectileRenderer::new);

//        ItemPropertiesRegistry.register(TCOTS_Items.GIANT_ANCHOR.get(), ResourceLocation.parse("invisible"), (stack, world, entity, seed) ->
//                GiantAnchorItem.wasLaunched(stack)? 1.0f : 0.0f);

            //Bomb
            EntityRendererRegistry.register(TCOTS_Entities.WitcherBomb(), ThrownItemRenderer::new);
            //Crossbow bolts
            EntityRendererRegistry.register(TCOTS_Entities.BaseBolt(), BaseBoltEntityRenderer::new);
            EntityRendererRegistry.register(TCOTS_Entities.BluntBolt(), BluntBoltEntityRenderer::new);
            EntityRendererRegistry.register(TCOTS_Entities.PrecisionBolt(), PrecisionBoltEntityRenderer::new);
            EntityRendererRegistry.register(TCOTS_Entities.ExplodingBolt(), ExplodingBoltEntityRenderer::new);
            EntityRendererRegistry.register(TCOTS_Entities.BroadheadBolt(), BroadheadBoltEntityRenderer::new);
        }

//        registerBlockRenderers()
        {
            //Blocks
            BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.ArenariaBush(), RenderType.cutout());
            BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.CelandinePlant(), RenderType.cutout());
            BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.CrowsEyeFern(), RenderType.cutout());
            BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.BryoniaVine(), RenderType.cutout());
            BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.VerbenaFlower(), RenderType.cutout());
            BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.HanFiberPlant(), RenderType.cutout());
            BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.PuffballMushroom(), RenderType.cutout());
            BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.SewantMushroomsPlant(), RenderType.cutout());

            BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.PottedVerbenaFlower(), RenderType.cutout());
            BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.PottedCelandineFlower(), RenderType.cutout());
            BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.PottedHanFiber(), RenderType.cutout());
            BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.PottedPuffballMushroom(), RenderType.cutout());
            BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.PottedSewantMushrooms(), RenderType.cutout());
            BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.PottedBryoniaFlower(), RenderType.cutout());

            BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.FrostedSnow(), RenderType.translucent());

            //BlockEntity
            BlockEntityRenderers.register(TCOTS_Blocks.SkullNestEntity(), NestSkullBlockRenderer::new);
            BlockEntityRenderers.register(TCOTS_Blocks.MonsterNestBlockEntity(), MonsterNestRenderer::new);
            BlockEntityRenderers.register(TCOTS_Blocks.AlchemyTableBlockEntity(), AlchemyTableRenderer::new);
            BlockEntityRenderers.register(TCOTS_Blocks.HerbalTableEntity(), HerbalTableRenderer::new);
            BlockEntityRenderers.register(TCOTS_Blocks.GiantAnchorEntity(), GiantAnchorRenderer::new);
            BlockEntityRenderers.register(TCOTS_Blocks.WintersBladeSkeletonBlockEntity(), WintersBladeSkeletonRenderer::new);
            BlockEntityRenderers.register(TCOTS_Blocks.SkeletonBlockEntity(), SkeletonBlockRenderer::new);
        }

//        registerParticleProviders()
        {
            //Particles
            ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.RotfiendBloodExplosion().getType(), Rotfiend_BloodExplosionParticle.Factory::new);

            ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.RotfiendBloodEmitter(), new Rotfiend_BloodEmitterParticle.Factory());
            ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.GraveHagGreenSaliva(), GraveHag_GreenSaliva.Provider::new);
            ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.FogletFog(), Foglet_FogParticle.FogFactory::new);
            ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.FogletFogAround(), Foglet_FogParticleAround.FogFactory::new);
            ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.GrapeshotExplosionEmitter(), new Grapeshot_ExplosionEmitterParticle.Factory());
            ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.DancingStarExplosionEmitter(), new DancingStar_ExplosionEmitterParticle.Factory());
            ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.DevilsPuffballExplosionEmitter(), new DevilsPuffball_ExplosionEmitterParticle.Factory());
            ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.GreenCloud(), CloudParticleColor.GreenCloudFactory::new);
            ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.SamumExplosionEmitter(), new Samum_ExplosionEmitterParticle.Factory());
            ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.NorthernWindExplosionEmitter(), new NorthernWind_ExplosionEmitterParticle.Factory());
            ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.DragonsDreamExplosionEmitter(), new DragonsDream_ExplosionEmitterParticle.Factory());
            ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.YellowCloud(), CloudParticleColor.YellowCloudFactory::new);
            ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.DimeritiumFlash(), DimeritiumFlash.FlashFactory::new);
            ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.MoonDustExplosionEmitter(), new MoonDust_ExplosionEmitterParticle.Factory());

            ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.FallingBloodParticle(),
                    (spriteProvider) ->
                            (type, world, x, y, z, velocityX, velocityY, velocityZ) ->
                                    new BloodParticle.Factory(spriteProvider,
                                            BloodParticle.createFallingBlood(type, world, x, y, z, velocityX, velocityY, velocityZ, TCOTS_Particles.LandingBloodParticle()))
                                            .createParticle(type, world, x, y, z, velocityX, velocityY, velocityZ)
            );
            ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.LandingBloodParticle(),
                    (spriteProvider) ->
                            (type, world, x, y, z, velocityX, velocityY, velocityZ) ->
                                    new BloodParticle.Factory(spriteProvider,
                                            BloodParticle.createLandingBlood(type, world, x, y, z, velocityX, velocityY, velocityZ))
                                            .createParticle(type, world, x, y, z, velocityX, velocityY, velocityZ)
            );

            ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.FallingBlackBloodParticle(),
                    (spriteProvider) ->
                            (type, world, x, y, z, velocityX, velocityY, velocityZ) ->
                                    new BloodParticle.Factory(spriteProvider,
                                            BloodParticle.createFallingBlackBlood(type, world, x, y, z, velocityX, velocityY, velocityZ, TCOTS_Particles.LandingBlackBloodParticle()))
                                            .createParticle(type, world, x, y, z, velocityX, velocityY, velocityZ)
            );
            ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.LandingBlackBloodParticle(), (spriteProvider) ->
                    (type, world, x, y, z, velocityX, velocityY, velocityZ) ->
                            new BloodParticle.Factory(spriteProvider,
                                    BloodParticle.createLandingBlackBlood(type, world, x, y, z, velocityX, velocityY, velocityZ))
                                    .createParticle(type, world, x, y, z, velocityX, velocityY, velocityZ)
            );
        }

//        registerMenuScreen()
        {
            MenuScreens.register(TCOTS_ScreenHandlersAndRecipes.AlchemyTableScreenHandler(), AlchemyTableScreen::new);
            MenuScreens.register(TCOTS_ScreenHandlersAndRecipes.HerbalTableScreenHandler(), HerbalTableScreen::new);
        }

//        registerLayerDefinitions()
        {
            EntityModelLayerRegistry.registerModelLayer(TCOTS_Client.WITCHER_EYES_LAYER, WitcherEyesModel_createModelData());
            EntityModelLayerRegistry.registerModelLayer(TCOTS_Client.TOXICITY_FACE_LAYER, ToxicityFaceModel_createModelData());
        }

        //Register Recipe Previews
        LavenderBookScreen.registerRecipePreviewBuilder(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "alchemy_book"), AlchemyTableRecipe.Type.INSTANCE, (alchemyTable_RecipePreviewBuilder));
        LavenderBookScreen.registerRecipePreviewBuilder(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "witcher_bestiary"), AlchemyTableRecipe.Type.INSTANCE, (alchemyTable_RecipePreviewBuilder));
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

