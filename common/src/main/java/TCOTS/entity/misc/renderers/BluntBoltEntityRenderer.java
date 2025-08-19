package TCOTS.entity.misc.renderers;

import TCOTS.TCOTS_Main;
import TCOTS.entity.misc.bolts.BluntBoltProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BluntBoltEntityRenderer extends BoltEntityRenderer<BluntBoltProjectile> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"textures/entity/blunt_bolt.png");
    public BluntBoltEntityRenderer(final EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(final BluntBoltProjectile arrowEntity) {
        return TEXTURE;
    }
}
