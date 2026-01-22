package mors.tcots.client.vanilla.renderer;

import mors.tcots.TCOTS_Main;
import mors.tcots.entity.misc.bolts.BroadheadBoltProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BroadheadBoltEntityRenderer extends BoltEntityRenderer<BroadheadBoltProjectile>{
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"textures/entity/broadhead_bolt.png");
    public BroadheadBoltEntityRenderer(final EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(final @NotNull BroadheadBoltProjectile arrowEntity) {
        return TEXTURE;
    }
}
