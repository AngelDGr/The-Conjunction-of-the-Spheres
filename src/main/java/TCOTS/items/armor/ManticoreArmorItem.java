package TCOTS.items.armor;

import TCOTS.interfaces.MaxToxicityIncreaser;
import TCOTS.items.geo.renderer.ManticoreArmorRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ManticoreArmorItem extends ArmorItem implements GeoItem, MaxToxicityIncreaser {
    //xTODO: Add items sprites
    //xTODO: Add functionality about toxicity
    //Faster drinking
    //Extra Refilling
    //Reduce bomb cooldown
    //Increase max toxicity

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
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
    public Supplier<Object> getRenderProvider() {
        return renderProvider;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state ->{
            state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
