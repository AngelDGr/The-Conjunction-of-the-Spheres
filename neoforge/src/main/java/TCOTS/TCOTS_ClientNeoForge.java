package TCOTS;

import TCOTS.entity.witcher_cosmetics.toxicity_face.ToxicityFaceModel;
import TCOTS.entity.witcher_cosmetics.witcher_eyes.WitcherEyesModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import java.util.function.Supplier;

@EventBusSubscriber(modid = TCOTS_Main.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
@Mod(value = TCOTS_Main.MOD_ID, dist = Dist.CLIENT)
public class TCOTS_ClientNeoForge {

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TCOTS_Client.WITCHER_EYES_LAYER, WitcherEyesModel_createModelData());
        event.registerLayerDefinition(TCOTS_Client.TOXICITY_FACE_LAYER, ToxicityFaceModel_createModelData());
    }

    public static Supplier<LayerDefinition> WitcherEyesModel_createModelData(){
        return () -> LayerDefinition.create(
                WitcherEyesModel.getModelData(new CubeDeformation(0)),
                96, 64);
    }

    public static Supplier<LayerDefinition> ToxicityFaceModel_createModelData(){
        return () -> LayerDefinition.create(
                ToxicityFaceModel.getModelData(new CubeDeformation(0)),
                96, 64);
    }

}
