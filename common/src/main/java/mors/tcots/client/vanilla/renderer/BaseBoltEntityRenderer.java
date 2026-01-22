package mors.tcots.client.vanilla.renderer;

import mors.tcots.TCOTS_Main;
import mors.tcots.entity.misc.bolts.BaseBoltProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BaseBoltEntityRenderer extends BoltEntityRenderer<BaseBoltProjectile> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"textures/entity/base_bolt.png");
    public BaseBoltEntityRenderer(final EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull final BaseBoltProjectile arrowEntity) {
        return TEXTURE;
    }
}