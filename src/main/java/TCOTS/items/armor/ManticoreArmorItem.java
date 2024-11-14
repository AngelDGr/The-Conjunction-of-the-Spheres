package TCOTS.items.armor;

import TCOTS.interfaces.MaxToxicityIncreaser;
import TCOTS.items.geo.renderer.ManticoreArmorRenderer;
import TCOTS.utils.GeoControllersUtil;
import TCOTS.utils.MiscUtil;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class ManticoreArmorItem extends ArmorItem implements GeoItem, MaxToxicityIncreaser {
    //xTODO: Add items sprites
    //xTODO: Add functionality about toxicity
    //Faster drinking
    //Extra Refilling
    //Reduce bomb cooldown
    //Increase max toxicity

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private final int extraToxicity;

    public ManticoreArmorItem(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        this(material, type, settings, 10);
    }

    public ManticoreArmorItem(RegistryEntry<ArmorMaterial> material, Type type, Settings settings, int extraToxicity) {
        super(material, type, settings);
        this.extraToxicity=extraToxicity;
    }


    @Override
    public int getExtraToxicity() {
        return extraToxicity;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private ManticoreArmorRenderer renderer;

            @Override
            public @Nullable <T extends LivingEntity> BipedEntityModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable BipedEntityModel<T> original) {
                if(this.renderer==null)
                    this.renderer=new ManticoreArmorRenderer();

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

                return this.renderer;
            }
        });
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        MiscUtil.setFullSetBonusTooltip(stack, tooltip,
                List.of(Text.translatable("tooltip.tcots-witcher.manticore_armor.full_set1"),
                        Text.translatable("tooltip.tcots-witcher.manticore_armor.full_set2"),
                        Text.translatable("tooltip.tcots-witcher.manticore_armor.full_set3")));
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
