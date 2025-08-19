package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.DevourerModel;
import TCOTS.entity.monsters.necrophages.DevourerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DevourerRenderer extends GeoEntityRenderer<DevourerEntity> {
    public DevourerRenderer(final EntityRendererProvider.Context renderManager) {
        super(renderManager, new DevourerModel());
        this.shadowRadius = 0.55f;
    }

    @Override
    public float getMotionAnimThreshold(final DevourerEntity animatable) {
        return 0.001f;
    }
    @Override
    public @NotNull ResourceLocation getTextureLocation(final @NotNull DevourerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/devourer.png");
    }

}
