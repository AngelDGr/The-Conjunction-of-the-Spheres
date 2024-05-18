package TCOTS.entity.misc.bolts.renderers;

import TCOTS.TCOTS_Main;
import TCOTS.entity.misc.bolts.BroadheadBoltProjectile;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class BroadheadBoltEntityRenderer extends BoltEntityRenderer<BroadheadBoltProjectile>{
    public static final Identifier TEXTURE = new Identifier(TCOTS_Main.MOD_ID,"textures/entity/misc/broadhead_bolt.png");
    public BroadheadBoltEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(BroadheadBoltProjectile arrowEntity) {
        return TEXTURE;
    }
}
