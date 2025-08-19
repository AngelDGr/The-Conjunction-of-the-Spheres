package TCOTS.entity.misc.renderers;

import TCOTS.TCOTS_Main;
import TCOTS.entity.misc.bolts.BroadheadBoltProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BroadheadBoltEntityRenderer extends BoltEntityRenderer<BroadheadBoltProjectile>{
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"textures/entity/broadhead_bolt.png");
    public BroadheadBoltEntityRenderer(final EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(final BroadheadBoltProjectile arrowEntity) {
        return TEXTURE;
    }
}
