package TCOTS.items.blocks;

import TCOTS.items.geo.renderer.MonsterNestItemRenderer;
import TCOTS.utils.GeoControllersUtil;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class MonsterNestItem extends BlockItem implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public MonsterNestItem(final Block block, final Properties settings) {
        super(block, settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void createGeoRenderer(final Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {

            private final MonsterNestItemRenderer renderer = new MonsterNestItemRenderer();


            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(GeoControllersUtil.genericIdleController(this));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
