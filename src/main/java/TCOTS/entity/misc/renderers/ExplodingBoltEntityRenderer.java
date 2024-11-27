package TCOTS.entity.misc.renderers;

import TCOTS.TCOTS_Main;
import TCOTS.entity.misc.bolts.ExplodingBoltProjectile;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class ExplodingBoltEntityRenderer extends BoltEntityRenderer<ExplodingBoltProjectile>{
    public static final Identifier TEXTURE = Identifier.of(TCOTS_Main.MOD_ID,"textures/entity/exploding_bolt.png");
    public ExplodingBoltEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(ExplodingBoltProjectile arrowEntity) {
        return TEXTURE;
    }
}