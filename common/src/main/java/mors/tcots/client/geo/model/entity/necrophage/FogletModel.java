package mors.tcots.client.geo.model.entity.necrophage;

import mors.tcots.TCOTS_Main;
import mors.tcots.client.geo.model.defaulted.monsters.DefaultedNecrophageModel;
import mors.tcots.entity.monsters.necrophages.FogletEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class FogletModel extends DefaultedNecrophageModel<FogletEntity> {

    public FogletModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "foglet"), true);
    }

    @Override
    public ResourceLocation getTextureResource(final FogletEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/monster/necrophage/foglet/foglet.png");
    }

    @Override
    public RenderType getRenderType(final FogletEntity animatable, final ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }
}
