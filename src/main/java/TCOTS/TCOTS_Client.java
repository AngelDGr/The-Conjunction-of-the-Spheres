package TCOTS;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.blocks.geo.renderer.*;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.geo.renderer.AnchorProjectileRenderer;
import TCOTS.entity.geo.renderer.necrophages.*;
import TCOTS.entity.geo.renderer.ogroids.*;
import TCOTS.entity.misc.renderers.*;
import TCOTS.entity.witcher_cosmetics.toxicity_face.ToxicityFaceModel;
import TCOTS.entity.witcher_cosmetics.witcher_eyes.WitcherEyesModel;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.concoctions.recipes.AlchemyTableRecipe;
import TCOTS.items.concoctions.recipes.ScreenHandlersAndRecipesRegister;
import TCOTS.items.weapons.GiantAnchorItem;
import TCOTS.items.weapons.WitcherBaseCrossbow;
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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.FoliageColors;
import net.minecraft.world.biome.GrassColors;
import org.jetbrains.annotations.NotNull;

@Environment(value= EnvType.CLIENT)
public class TCOTS_Client implements ClientModInitializer {
    public static EntityModelLayer WITCHER_EYES_LAYER = new EntityModelLayer(Identifier.of(TCOTS_Main.MOD_ID, "witcher_eyes"), "witcher_eyes");
    public static EntityModelLayer TOXICITY_FACE_LAYER = new EntityModelLayer(Identifier.of(TCOTS_Main.MOD_ID, "toxicity_face"), "toxicity_face");


    @Override
    public void onInitializeClient() {

        //HUD
        HudRenderCallback.EVENT.register(((drawContext, tickCounter) -> ToxicityHudOverlay.onHudRender(drawContext,tickCounter.getTickDelta(true))));


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



        //Send client-packets to server
        {
            TCOTS_Main.CONFIG.witcher_eyes.subscribeToActivateEyes(activate ->
            {
                if (MinecraftClient.getInstance().getNetworkHandler() == null) return;
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
                if (MinecraftClient.getInstance().getNetworkHandler() == null) return;
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
                if(MinecraftClient.getInstance().getNetworkHandler()==null)return;
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
                if(MinecraftClient.getInstance().getNetworkHandler()==null)return;
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
                if(MinecraftClient.getInstance().getNetworkHandler()==null)return;
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
                if(MinecraftClient.getInstance().getNetworkHandler()==null)return;
                TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(
                        new TCOTS_Main.ToxicityFacePacket(activateToxicity));
            });
        }

        //Grass Colors
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) {
                return GrassColors.getDefaultColor();
            }
            return BiomeColors.getGrassColor(world, pos);}, TCOTS_Blocks.HAN_FIBER_PLANT, TCOTS_Blocks.ARENARIA_BUSH,
                TCOTS_Blocks.CELANDINE_PLANT, TCOTS_Blocks.CROWS_EYE_FERN, TCOTS_Blocks.VERBENA_FLOWER, TCOTS_Blocks.POTTED_BRYONIA_FLOWER);

        //Leaves Colors
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) {
                return FoliageColors.getDefaultColor();

            }
            return BiomeColors.getGrassColor(world, pos);},
                TCOTS_Blocks.BRYONIA_VINE);


        //Crossbow Color
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1: DyedColorComponent.getColor(stack, -6265536), TCOTS_Items.KNIGHT_CROSSBOW);

        //Crossbow animation
        ModelPredicateProviderRegistry.register(TCOTS_Items.KNIGHT_CROSSBOW, Identifier.of("pull"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);

        ModelPredicateProviderRegistry.register(TCOTS_Items.KNIGHT_CROSSBOW, Identifier.of("pulling"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0f : 0.0f);
        ModelPredicateProviderRegistry.register(TCOTS_Items.KNIGHT_CROSSBOW, Identifier.of("charged"), (stack, world, entity, seed) -> CrossbowItem.isCharged(stack) ? 1.0f : 0.0f);
        ModelPredicateProviderRegistry.register(TCOTS_Items.KNIGHT_CROSSBOW, Identifier.of("firework"), (stack, world, entity, seed) -> {
            ChargedProjectilesComponent chargedProjectilesComponent = stack.get(DataComponentTypes.CHARGED_PROJECTILES);
            return chargedProjectilesComponent != null && chargedProjectilesComponent.contains(Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
        });

        ModelPredicateProviderRegistry.register(TCOTS_Items.KNIGHT_CROSSBOW, Identifier.of("bolt"), (stack, world, entity, seed) ->
                CrossbowItem.isCharged(stack) && WitcherBaseCrossbow.hasBoltProjectile(stack) ? 1.0f : 0.0f);
        ModelPredicateProviderRegistry.register(Items.CROSSBOW, Identifier.of("bolt"), (stack, world, entity, seed) ->
                CrossbowItem.isCharged(stack) && WitcherBaseCrossbow.hasBoltProjectile(stack) ? 1.0f : 0.0f);


        //Monsters
        EntityRendererRegistry.register(TCOTS_Entities.DROWNER, DrownerRenderer::new);
        EntityRendererRegistry.register(TCOTS_Entities.DROWNER_PUDDLE, DrownerPuddleRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.ROTFIEND, RotfiendRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.GRAVE_HAG, GraveHagRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.WATER_HAG, WaterHagRenderer::new);
        EntityRendererRegistry.register(TCOTS_Entities.WATER_HAG_MUD_BALL, FlyingItemEntityRenderer::new);

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
        EntityRendererRegistry.register(TCOTS_Entities.TROLL_ROCK_PROJECTILE, context -> new FlyingItemEntityRenderer<>(context, 2.0f, true));

        EntityRendererRegistry.register(TCOTS_Entities.ICE_TROLL, IceTrollRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.FOREST_TROLL, ForestTrollRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.ICE_GIANT, IceGiantRenderer::new);
        EntityRendererRegistry.register(TCOTS_Entities.ANCHOR_PROJECTILE, AnchorProjectileRenderer::new);

        ModelPredicateProviderRegistry.register(TCOTS_Items.GIANT_ANCHOR, Identifier.of("invisible"), (stack, world, entity, seed) ->
                GiantAnchorItem.wasLaunched(stack)? 1.0f : 0.0f);

        //Bomb
        EntityRendererRegistry.register(TCOTS_Entities.WITCHER_BOMB, FlyingItemEntityRenderer::new);
        //Crossbow bolts
        EntityRendererRegistry.register(TCOTS_Entities.BASE_BOLT, BaseBoltEntityRenderer::new);
        EntityRendererRegistry.register(TCOTS_Entities.BLUNT_BOLT, BluntBoltEntityRenderer::new);
        EntityRendererRegistry.register(TCOTS_Entities.PRECISION_BOLT, PrecisionBoltEntityRenderer::new);
        EntityRendererRegistry.register(TCOTS_Entities.EXPLODING_BOLT, ExplodingBoltEntityRenderer::new);
        EntityRendererRegistry.register(TCOTS_Entities.BROADHEAD_BOLT, BroadheadBoltEntityRenderer::new);


        //Blocks
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.ARENARIA_BUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.CELANDINE_PLANT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.CROWS_EYE_FERN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.BRYONIA_VINE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.VERBENA_FLOWER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.HAN_FIBER_PLANT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.PUFFBALL_MUSHROOM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.SEWANT_MUSHROOMS_PLANT, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.POTTED_VERBENA_FLOWER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.POTTED_CELANDINE_FLOWER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.POTTED_HAN_FIBER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.POTTED_PUFFBALL_MUSHROOM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.POTTED_SEWANT_MUSHROOMS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.POTTED_BRYONIA_FLOWER, RenderLayer.getCutout());


        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.FROSTED_SNOW, RenderLayer.getTranslucent());


        //BlockEntity
        BlockEntityRendererFactories.register(TCOTS_Blocks.SKULL_NEST_ENTITY, NestSkullBlockRenderer::new);
        BlockEntityRendererFactories.register(TCOTS_Blocks.MONSTER_NEST_ENTITY, MonsterNestRenderer::new);
        BlockEntityRendererFactories.register(TCOTS_Blocks.ALCHEMY_TABLE_ENTITY, AlchemyTableRenderer::new);
        BlockEntityRendererFactories.register(TCOTS_Blocks.HERBAL_TABLE_ENTITY, HerbalTableRenderer::new);
        BlockEntityRendererFactories.register(TCOTS_Blocks.GIANT_ANCHOR_ENTITY, GiantAnchorRenderer::new);
        BlockEntityRendererFactories.register(TCOTS_Blocks.WINTERS_BLADE_SKELETON_ENTITY, WintersBladeSkeletonRenderer::new);
        BlockEntityRendererFactories.register(TCOTS_Blocks.SKELETON_BLOCK_ENTITY, SkeletonBlockRenderer::new);


        //Particles
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.ROTFIEND_BLOOD_EXPLOSION.getType(), Rotfiend_BloodExplosionParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.ROTFIEND_BLOOD_EMITTER, new Rotfiend_BloodEmitterParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.GRAVE_HAG_GREEN_SALIVA, GraveHag_GreenSaliva.Factory::new);
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


        HandledScreens.register(ScreenHandlersAndRecipesRegister.ALCHEMY_TABLE_SCREEN_HANDLER, AlchemyTableScreen::new);
        HandledScreens.register(ScreenHandlersAndRecipesRegister.HERBAL_TABLE_SCREEN_HANDLER, HerbalTableScreen::new);

        //Register Recipe Previews
        LavenderBookScreen.registerRecipePreviewBuilder(Identifier.of(TCOTS_Main.MOD_ID, "alchemy_book"), AlchemyTableRecipe.Type.INSTANCE, (alchemyTable_RecipePreviewBuilder));

        LavenderBookScreen.registerRecipePreviewBuilder(Identifier.of(TCOTS_Main.MOD_ID, "witcher_bestiary"), AlchemyTableRecipe.Type.INSTANCE, (alchemyTable_RecipePreviewBuilder));

        EntityModelLayerRegistry.registerModelLayer(WITCHER_EYES_LAYER, WitcherEyesModel.createModelData());

        EntityModelLayerRegistry.registerModelLayer(TOXICITY_FACE_LAYER, ToxicityFaceModel.createModelData());
    }

    private static final RecipeFeature.RecipePreviewBuilder<AlchemyTableRecipe> alchemyTable_RecipePreviewBuilder = new RecipeFeature.RecipePreviewBuilder<>() {
        @Override
        public @NotNull Component buildRecipePreview(BookCompiler.ComponentSource componentSource, @NotNull RecipeEntry<AlchemyTableRecipe> recipeEntry) {
            Identifier TEXTURE_ID = Identifier.of(TCOTS_Main.MOD_ID, "textures/gui/alchemy_book_gui.png");

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
                                .child(Components.item(recipe.getResult(null)).showOverlay(true).setTooltipFromStack(true))
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
                    ItemStack stack = recipe.getIngredients().get(i).getMatchingStacks()[0];
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

        private boolean hasRecipe(@NotNull RecipeEntry<AlchemyTableRecipe> recipeEntry){
            return MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.getRecipeBook().contains(recipeEntry);
        }
    };

}

