package TCOTS.blocks.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.AlchemyTableBlock;
import TCOTS.blocks.entity.AlchemyTableBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

public class AlchemyTableModel extends GeoModel<AlchemyTableBlockEntity> {
    @Override
    public ResourceLocation getModelResource(final AlchemyTableBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/block/alchemy_table.geo.json");
    }


    @Override
    public ResourceLocation getTextureResource(final AlchemyTableBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/block/alchemy_table.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final AlchemyTableBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }

    @Override
    public RenderType getRenderType(final AlchemyTableBlockEntity animatable, final ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

    @Override
    public void setCustomAnimations(final AlchemyTableBlockEntity animatable, final long instanceId, final AnimationState<AlchemyTableBlockEntity> animationState) {
        final GeoBone book = getAnimationProcessor().getBone("Book");
        if(
                animatable.getLevel() != null
                &&
                animatable.getLevel().getBlockState(animatable.getBlockPos()) != null
                &&
                animatable.getLevel().getBlockState(animatable.getBlockPos()).getBlock() instanceof AlchemyTableBlock
                &&
                !animatable.getLevel().getBlockState(animatable.getBlockPos()).getValue(AlchemyTableBlock.HAS_ALCHEMY_BOOK)
        ){
            book.setHidden(true);
        } else book.setHidden(!(animatable.getLevel().getBlockState(animatable.getBlockPos()).getBlock() instanceof AlchemyTableBlock));

        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
