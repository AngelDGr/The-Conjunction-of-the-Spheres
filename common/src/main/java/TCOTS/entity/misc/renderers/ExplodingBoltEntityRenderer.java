package TCOTS.entity.misc.renderers;

import TCOTS.TCOTS_Main;
import TCOTS.entity.misc.bolts.ExplodingBoltProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ExplodingBoltEntityRenderer extends BoltEntityRenderer<ExplodingBoltProjectile>{
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"textures/entity/exploding_bolt.png");
    public ExplodingBoltEntityRenderer(final EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(final ExplodingBoltProjectile arrowEntity) {
        return TEXTURE;
    }
}