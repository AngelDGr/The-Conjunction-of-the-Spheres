package TCOTS;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.blocks.geo.renderer.AlchemyTableRenderer;
import TCOTS.blocks.geo.renderer.MonsterNestRenderer;
import TCOTS.blocks.geo.renderer.NestSkullBlockRenderer;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.geo.renderer.necrophages.*;
import TCOTS.entity.geo.renderer.ogroids.NekkerRenderer;
import TCOTS.particles.*;
import TCOTS.potions.recipes.AlchemyTableRecipesRegister;
import TCOTS.screen.AlchemyTableScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class TCOTS_Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //Monsters
        EntityRendererRegistry.register(TCOTS_Entities.DROWNER, DrownerRenderer::new);
        EntityRendererRegistry.register(TCOTS_Entities.DROWNER_PUDDLE, DrownerPuddleRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.ROTFIEND, RotfiendRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.GRAVE_HAG, GraveHagRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.WATER_HAG, WaterHagRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.FOGLET, FogletRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.FOGLING, FoglingRenderer::new);


        EntityRendererRegistry.register(TCOTS_Entities.WATER_HAG_MUD_BALL, FlyingItemEntityRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.NEKKER, NekkerRenderer::new);


        //Blocks
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.ARENARIA_BUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.CELANDINE_PLANT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(TCOTS_Blocks.CROWS_EYE_FERN, RenderLayer.getCutout());


        //BlockEntity
        BlockEntityRendererFactories.register(TCOTS_Blocks.SKULL_NEST_ENTITY, NestSkullBlockRenderer::new);
        BlockEntityRendererFactories.register(TCOTS_Blocks.MONSTER_NEST_ENTITY, MonsterNestRenderer::new);
        BlockEntityRendererFactories.register(TCOTS_Blocks.ALCHEMY_TABLE_ENTITY, AlchemyTableRenderer::new);


        //Particles
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.ROTFIEND_BLOOD_EXPLOSION, Rotfiend_BloodExplosionParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.ROTFIEND_BLOOD_EMITTER, new Rotfiend_BloodEmitterParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.GRAVE_HAG_GREEN_SALIVA, GraveHag_GreenSaliva.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.FOGLET_FOG, Foglet_FogParticle.FogFactory::new);
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.FOGLET_FOG_AROUND, Foglet_FogParticleAround.FogFactory::new);


        HandledScreens.register(AlchemyTableRecipesRegister.ALCHEMY_TABLE_SCREEN_HANDLER, AlchemyTableScreen::new);
    }
}

