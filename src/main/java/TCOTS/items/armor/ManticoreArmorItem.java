package TCOTS.items.armor;

import TCOTS.interfaces.MaxToxicityIncreaser;
import TCOTS.items.geo.renderer.ManticoreArmorRenderer;
import TCOTS.utils.GeoControllersUtil;
import TCOTS.utils.MiscUtil;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ManticoreArmorItem extends ArmorItem implements GeoItem, MaxToxicityIncreaser {
    //xTODO: Add items sprites
    //xTODO: Add functionality about toxicity
    //Faster drinking
    //Extra Refilling
    //Reduce bomb cooldown
    //Increase max toxicity

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider= GeoItem.makeRenderer(this);

    private final int extraToxicity;

    public ManticoreArmorItem(ArmorMaterial material, Type type, Settings settings) {
        this(material, type, settings, 10);
    }

    public ManticoreArmorItem(ArmorMaterial material, Type type, Settings settings, int extraToxicity) {
        super(material, type, settings);
        this.extraToxicity=extraToxicity;
    }


    @Override
    public int getExtraToxicity() {
        return extraToxicity;
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {

            private ManticoreArmorRenderer renderer;

            @Override
            public BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<LivingEntity> original) {
                if(this.renderer==null)
                    this.renderer=new ManticoreArmorRenderer();

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

                return this.renderer;
            }
        });
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        MiscUtil.setFullSetBonusTooltip(stack, tooltip,
                List.of(Text.translatable("tooltip.tcots-witcher.manticore_armor.full_set1"),
                        Text.translatable("tooltip.tcots-witcher.manticore_armor.full_set2"),
                        Text.translatable("tooltip.tcots-witcher.manticore_armor.full_set3")));
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return renderProvider;
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
