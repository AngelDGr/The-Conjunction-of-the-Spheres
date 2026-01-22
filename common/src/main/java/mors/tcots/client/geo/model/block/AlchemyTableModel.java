package mors.tcots.client.geo.model.block;

import mors.tcots.TCOTS_Main;
import mors.tcots.block.AlchemyTableBlock;
import mors.tcots.block.entity.AlchemyTableBlockEntity;
import mors.tcots.client.geo.model.defaulted.DefaultedBlockModelNoAnimation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;

public class AlchemyTableModel extends DefaultedBlockModelNoAnimation<AlchemyTableBlockEntity> {
    public AlchemyTableModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "alchemy_table"));
    }

    @Override
    public RenderType getRenderType(final AlchemyTableBlockEntity animatable, final ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

    @Override
    public void setCustomAnimations(final AlchemyTableBlockEntity animatable, final long instanceId, final AnimationState<AlchemyTableBlockEntity> animationState) {
        final GeoBone book = getAnimationProcessor().getBone("Book");
        final BlockState blockState = animatable.getBlockState();

        if(blockState.getBlock() instanceof AlchemyTableBlock && !blockState.getValue(AlchemyTableBlock.HAS_ALCHEMY_BOOK))
            book.setHidden(true);
        else
            book.setHidden(!(blockState.getBlock() instanceof AlchemyTableBlock));

        super.setCustomAnimations(animatable, instanceId, animationState);
    }

    public static class Item extends DefaultedBlockModelNoAnimation<AlchemyTableBlock.Item> {
        public Item() {
            super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "alchemy_table"));
        }
    }
}
