package TCOTS.entity.misc.renderers;

import TCOTS.TCOTS_Main;
import TCOTS.entity.misc.bolts.BluntBoltProjectile;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class BluntBoltEntityRenderer extends BoltEntityRenderer<BluntBoltProjectile> {
    public static final Identifier TEXTURE = new Identifier(TCOTS_Main.MOD_ID,"textures/entity/misc/blunt_bolt.png");
    public BluntBoltEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(BluntBoltProjectile arrowEntity) {
        return TEXTURE;
    }
}
