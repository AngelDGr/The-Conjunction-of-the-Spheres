package TCOTS.items.armor;

import TCOTS.items.geo.renderer.ManticoreArmorRenderer;
import TCOTS.registry.TCOTS_ItemsMaterials;
import TCOTS.utils.GeoControllersUtil;
import TCOTS.utils.MiscUtil;
import TCOTS.utils.SwordsAndArmorAttributes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class ManticoreArmorItem extends ArmorItem implements GeoItem {
    //xTODO: Add items sprites
    //xTODO: Add functionality about toxicity
    //Faster drinking
    //Extra Refilling
    //Reduce bomb cooldown
    //Increase max toxicity

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ManticoreArmorItem(Holder<ArmorMaterial> material, Type type, Properties settings) {
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
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag type) {
        MiscUtil.setFullSetBonusTooltip(stack, tooltip,
                List.of(Component.translatable("tooltip.tcots_witcher.manticore_armor.full_set1"),
                        Component.translatable("tooltip.tcots_witcher.manticore_armor.full_set2"),
                        Component.translatable("tooltip.tcots_witcher.manticore_armor.full_set3")));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(GeoControllersUtil.genericIdleController(this));

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
