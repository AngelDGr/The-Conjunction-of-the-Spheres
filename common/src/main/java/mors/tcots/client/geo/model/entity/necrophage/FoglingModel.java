package mors.tcots.client.geo.model.entity.necrophage;

import mors.tcots.TCOTS_Main;
import mors.tcots.client.geo.model.defaulted.monsters.DefaultedNecrophageModel;
import mors.tcots.entity.misc.FoglingEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;

public class FoglingModel extends DefaultedNecrophageModel<FoglingEntity> {

    public FoglingModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "foglet"), true);
    }

    @Override
    public ResourceLocation getTextureResource(final FoglingEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/monster/necrophage/foglet/fogling.png");
    }

    @Override
    public RenderType getRenderType(final FoglingEntity animatable, final ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    public void setCustomAnimations(final FoglingEntity entity, final long instanceId, final AnimationState<FoglingEntity> animationState) {
        super.setCustomAnimations(entity,instanceId,animationState);
    }
}