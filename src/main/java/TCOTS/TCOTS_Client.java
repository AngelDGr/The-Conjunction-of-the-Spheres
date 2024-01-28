package TCOTS;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.geo.renderer.necrophages.DrownerPuddleRenderer;
import TCOTS.entity.geo.renderer.necrophages.DrownerRenderer;
//import TCOTS.entity.misc.Drowner_Puddle.client.Drowner_Puddle_Model;
//import TCOTS.entity.misc.Drowner_Puddle.client.Drowner_Puddle_Renderer;
//import TCOTS.entity.misc.Drowner_Puddle.client.ModModelLayers;
import TCOTS.entity.geo.renderer.necrophages.RotfiendRenderer;
import TCOTS.entity.geo.renderer.ogroids.NekkerRenderer;
import TCOTS.entity.ogroids.NekkerEntity;
import TCOTS.particles.Drowner_PuddleParticle;
import TCOTS.particles.Rotfiend_BloodEmitterParticle;
import TCOTS.particles.Rotfiend_BloodExplosionParticle;
import TCOTS.particles.TCOTS_Particles;
import TCOTS.sounds.TCOTS_Sounds;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class TCOTS_Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
//        TCOTS_Sounds.init();
        EntityRendererRegistry.register(TCOTS_Entities.DROWNER, DrownerRenderer::new);
        EntityRendererRegistry.register(TCOTS_Entities.DROWNER_PUDDLE, DrownerPuddleRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.ROTFIEND, RotfiendRenderer::new);

        EntityRendererRegistry.register(TCOTS_Entities.NEKKER, NekkerRenderer::new);


        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.DROWNER_PUDDLE_PARTICLE, Drowner_PuddleParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.ROTFIEND_BLOOD_EXPLOSION, Rotfiend_BloodExplosionParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TCOTS_Particles.ROTFIEND_BLOOD_EMITTER, new Rotfiend_BloodEmitterParticle.Factory());

    }
}

