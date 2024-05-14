package TCOTS;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.blocks.geo.renderer.AlchemyTableRenderer;
import TCOTS.blocks.geo.renderer.HerbalTableRenderer;
import TCOTS.blocks.geo.renderer.MonsterNestRenderer;
import TCOTS.blocks.geo.renderer.NestSkullBlockRenderer;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.geo.renderer.necrophages.*;
import TCOTS.entity.geo.renderer.ogroids.NekkerRenderer;
import TCOTS.items.potions.recipes.AlchemyTableRecipe;
import TCOTS.items.potions.recipes.AlchemyTableRecipesRegister;
import TCOTS.particles.*;
import TCOTS.particles.bombEmitters.*;
import TCOTS.screen.AlchemyTableScreen;
import io.wispforest.lavender.client.LavenderBookScreen;
import io.wispforest.lavender.md.compiler.BookCompiler;
import io.wispforest.lavender.md.features.RecipeFeature;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.core.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class TCOTS_Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
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
            return BiomeColors.getGrassColor(world, pos);}, TCOTS_Blocks.BRYONIA_VINE);

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


        EntityRendererRegistry.register(TCOTS_Entities.NEKKER, NekkerRenderer::new);

        //Bomb
        EntityRendererRegistry.register(TCOTS_Entities.WITCHER_BOMB, FlyingItemEntityRenderer::new);

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


        //Particles
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.ROTFIEND_BLOOD_EXPLOSION, Rotfiend_BloodExplosionParticle.Factory::new);
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


        HandledScreens.register(AlchemyTableRecipesRegister.ALCHEMY_TABLE_SCREEN_HANDLER, AlchemyTableScreen::new);

        //Register Recipe Previews
        LavenderBookScreen.registerRecipePreviewBuilder(new Identifier(TCOTS_Main.MOD_ID, "alchemy_book"), AlchemyTableRecipe.Type.INSTANCE, (alchemyTable_RecipePreviewBuilder));

        LavenderBookScreen.registerRecipePreviewBuilder(new Identifier(TCOTS_Main.MOD_ID, "witcher_bestiary"), AlchemyTableRecipe.Type.INSTANCE, (alchemyTable_RecipePreviewBuilder));

    }

    private static final RecipeFeature.RecipePreviewBuilder<AlchemyTableRecipe> alchemyTable_RecipePreviewBuilder = new RecipeFeature.RecipePreviewBuilder<>() {
        @Override
        public @NotNull Component buildRecipePreview(BookCompiler.ComponentSource componentSource, RecipeEntry<AlchemyTableRecipe> recipeEntry) {
            Identifier TEXTURE_ID = new Identifier(TCOTS_Main.MOD_ID, "textures/gui/alchemy_book_gui.png");
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

            return root;
        }
    };

}

