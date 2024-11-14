package TCOTS.blocks.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.AlchemyTableBlock;
import TCOTS.blocks.entity.AlchemyTableBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

public class AlchemyTableModel extends GeoModel<AlchemyTableBlockEntity> {
    @Override
    public Identifier getModelResource(AlchemyTableBlockEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "geo/block/alchemy_table.geo.json");
    }


    @Override
    public Identifier getTextureResource(AlchemyTableBlockEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "textures/block/alchemy_table.png");
    }

    @Override
    public Identifier getAnimationResource(AlchemyTableBlockEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }

    @Override
    public RenderLayer getRenderType(AlchemyTableBlockEntity animatable, Identifier texture) {
        return RenderLayer.getEntityTranslucent(getTextureResource(animatable));
    }

    @Override
    public void setCustomAnimations(AlchemyTableBlockEntity animatable, long instanceId, AnimationState<AlchemyTableBlockEntity> animationState) {
        GeoBone book = getAnimationProcessor().getBone("Book");
        if(
                animatable.getWorld() != null
                &&
                animatable.getWorld().getBlockState(animatable.getPos()) != null
                &&
                animatable.getWorld().getBlockState(animatable.getPos()).getBlock() instanceof AlchemyTableBlock
                &&
                !animatable.getWorld().getBlockState(animatable.getPos()).get(AlchemyTableBlock.HAS_ALCHEMY_BOOK)
        ){
            book.setHidden(true);
        } else book.setHidden(!(animatable.getWorld().getBlockState(animatable.getPos()).getBlock() instanceof AlchemyTableBlock));

        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
