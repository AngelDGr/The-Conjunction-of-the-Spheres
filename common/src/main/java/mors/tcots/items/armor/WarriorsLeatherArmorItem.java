package mors.tcots.items.armor;

import mors.tcots.items.armor.set.ArmorSet;
import mors.tcots.items.armor.set.IsArmorSet;
import mors.tcots.client.geo.renderer.item.armor.WarriorsLeatherArmorRenderer;
import mors.tcots.utils.GeckoAnimationsUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class WarriorsLeatherArmorItem extends ArmorItem implements GeoItem, IsArmorSet {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public WarriorsLeatherArmorItem(final Holder<ArmorMaterial> material, final Type type, final Properties settings) {
        super(material, type, settings);
    }

    @SuppressWarnings("all")
    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private WarriorsLeatherArmorRenderer renderer;

            @Override
            public @Nullable <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if(this.renderer==null)
                    this.renderer=new WarriorsLeatherArmorRenderer();

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(GeckoAnimationsUtil.genericIdleController(this));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public ArmorSet getSet() {
        return ArmorSet.WARRIORS_LEATHER;
    }
}
