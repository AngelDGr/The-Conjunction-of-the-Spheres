package TCOTS.entity.misc.renderers;

import TCOTS.TCOTS_Main;
import TCOTS.entity.misc.bolts.BaseBoltProjectile;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class BaseBoltEntityRenderer extends BoltEntityRenderer<BaseBoltProjectile> {
    public static final Identifier TEXTURE = new Identifier(TCOTS_Main.MOD_ID,"textures/entity/base_bolt.png");
    public BaseBoltEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(BaseBoltProjectile arrowEntity) {
        return TEXTURE;
    }
}