package mors.tcots.client.geo.model.entity.misc;

import mors.tcots.TCOTS_Main;
import mors.tcots.entity.misc.DrownerPuddleEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class DrownerPuddleModel extends DefaultedEntityGeoModel<DrownerPuddleEntity> {
    public DrownerPuddleModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "drowner_puddle"), false);
    }

    @Override
    public RenderType getRenderType(final DrownerPuddleEntity animatable, final ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }
}
