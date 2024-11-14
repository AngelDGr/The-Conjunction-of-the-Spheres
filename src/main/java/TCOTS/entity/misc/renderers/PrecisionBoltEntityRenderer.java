package TCOTS.entity.misc.renderers;

import TCOTS.TCOTS_Main;
import TCOTS.entity.misc.bolts.PrecisionBoltProjectile;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class PrecisionBoltEntityRenderer extends BoltEntityRenderer<PrecisionBoltProjectile> {
    public static final Identifier TEXTURE = Identifier.of(TCOTS_Main.MOD_ID,"textures/entity/precision_bolt.png");
    public PrecisionBoltEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(PrecisionBoltProjectile arrowEntity) {
        return TEXTURE;
    }
}