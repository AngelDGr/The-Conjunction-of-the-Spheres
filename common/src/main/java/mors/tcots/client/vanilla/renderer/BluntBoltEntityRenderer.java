package mors.tcots.client.vanilla.renderer;

import mors.tcots.TCOTS_Main;
import mors.tcots.entity.misc.bolts.BluntBoltProjectile;
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
