package TCOTS;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.blocks.geo.renderer.AlchemyTableRenderer;
import TCOTS.blocks.geo.renderer.MonsterNestRenderer;
import TCOTS.blocks.geo.renderer.NestSkullBlockRenderer;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.geo.renderer.necrophages.DrownerPuddleRenderer;
import TCOTS.entity.geo.renderer.necrophages.DrownerRenderer;
//import TCOTS.entity.misc.Drowner_Puddle.client.Drowner_Puddle_Model;
//import TCOTS.entity.misc.Drowner_Puddle.client.Drowner_Puddle_Renderer;
//import TCOTS.entity.misc.Drowner_Puddle.client.ModModelLayers;
import TCOTS.entity.geo.renderer.necrophages.RotfiendRenderer;
import TCOTS.entity.geo.renderer.ogroids.NekkerRenderer;
import TCOTS.particles.Drowner_PuddleParticle;
import TCOTS.particles.Rotfiend_BloodEmitterParticle;
import TCOTS.particles.Rotfiend_BloodExplosionParticle;
import TCOTS.particles.TCOTS_Particles;
import TCOTS.potions.screen.AlchemyTableScreen;
import TCOTS.potions.screen.AlchemyTableScreenHandler;
import TCOTS.potions.screen.WitcherPotions_Recipes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class TCOTS_Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //Monsters
        EntityRendererRegistry.register(TCOTS_Entities.DROWNER, DrownerRenderer::new);
        EntityRendererRegistry.register(TCOTS_Entities.DROWNER_PUDDLE, DrownerPuddleRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.ROTFIEND, RotfiendRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.NEKKER, NekkerRenderer::new);

        //BlockEntity
        BlockEntityRendererFactories.register(TCOTS_Blocks.SKULL_NEST_ENTITY, NestSkullBlockRenderer::new);
        BlockEntityRendererFactories.register(TCOTS_Blocks.MONSTER_NEST_ENTITY, MonsterNestRenderer::new);
        BlockEntityRendererFactories.register(TCOTS_Blocks.ALCHEMY_TABLE_ENTITY, AlchemyTableRenderer::new);

        //Particles
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.DROWNER_PUDDLE_PARTICLE, Drowner_PuddleParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.ROTFIEND_BLOOD_EXPLOSION, Rotfiend_BloodExplosionParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.ROTFIEND_BLOOD_EMITTER, new Rotfiend_BloodEmitterParticle.Factory());

        HandledScreens.register(WitcherPotions_Recipes.ALCHEMY_TABLE_SCREEN_HANDLER, AlchemyTableScreen::new);
    }
}

