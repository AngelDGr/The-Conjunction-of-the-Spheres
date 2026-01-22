package mors.tcots.client.vanilla.renderer;

import mors.tcots.TCOTS_Main;
import mors.tcots.entity.misc.bolts.PrecisionBoltProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class PrecisionBoltEntityRenderer extends BoltEntityRenderer<PrecisionBoltProjectile> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"textures/entity/precision_bolt.png");
    public PrecisionBoltEntityRenderer(final EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(final PrecisionBoltProjectile arrowEntity) {
        return TEXTURE;
    }
}