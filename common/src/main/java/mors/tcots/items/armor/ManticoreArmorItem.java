package mors.tcots.items.armor;

import mors.tcots.items.armor.set.ArmorSet;
import mors.tcots.items.armor.set.IsArmorSet;
import mors.tcots.client.geo.renderer.item.armor.ManticoreArmorRenderer;
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

public class ManticoreArmorItem extends ArmorItem implements GeoItem, IsArmorSet {
    //xTODO: Add items sprites
    //xTODO: Add functionality about toxicity
    //Faster drinking
    //Extra Refilling
    //Reduce bomb cooldown
    //Increase max toxicity

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ManticoreArmorItem(final Holder<ArmorMaterial> material, final Type type, final Properties settings) {
        super(material, type, settings);
    }

    @SuppressWarnings("all")
    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private ManticoreArmorRenderer renderer;

            @Override
            public @Nullable <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if(this.renderer==null)
                    this.renderer=new ManticoreArmorRenderer();

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
        return ArmorSet.MANTICORE;
    }
}
