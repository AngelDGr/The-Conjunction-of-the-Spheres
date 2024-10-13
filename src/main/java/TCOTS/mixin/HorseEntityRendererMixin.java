package TCOTS.mixin;

import TCOTS.entity.WitcherHorseArmorFeatureRenderer;
import TCOTS.items.TCOTS_Items;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.client.render.entity.AbstractHorseEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.HorseEntityRenderer;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.HorseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HorseEntityRenderer.class)
public abstract class HorseEntityRendererMixin extends AbstractHorseEntityRenderer<HorseEntity, HorseEntityModel<HorseEntity>> {

    public HorseEntityRendererMixin(EntityRendererFactory.Context ctx, HorseEntityModel<HorseEntity> model, float scale) {
        super(ctx, model, scale);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectInConstructor(EntityRendererFactory.Context context, CallbackInfo ci) {
        this.addFeature(new WitcherHorseArmorFeatureRenderer(this));
    }

    @Mixin(PowderSnowBlock.class)
    public static class PowderSnow_TundraArmor {

        @Inject(method = "canWalkOnPowderSnow", at = @At("HEAD"), cancellable = true)
        private static void canWalkWithArmor(Entity entity, CallbackInfoReturnable<Boolean> cir){
            if(entity instanceof HorseEntity horse && horse.getArmorType().isOf(TCOTS_Items.TUNDRA_HORSE_ARMOR)){
                cir.setReturnValue(true);
            }
        }
    }
}
