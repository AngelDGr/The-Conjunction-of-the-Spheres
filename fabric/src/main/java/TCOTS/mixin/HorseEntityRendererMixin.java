package TCOTS.mixin;

import TCOTS.entity.WitcherHorseArmorFeatureRenderer;
import TCOTS.items.TCOTS_Items_Fabric;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HorseRenderer.class)
public abstract class HorseEntityRendererMixin extends AbstractHorseRenderer<Horse, HorseModel<Horse>> {

    public HorseEntityRendererMixin(EntityRendererProvider.Context ctx, HorseModel<Horse> model, float scale) {
        super(ctx, model, scale);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectInConstructor(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.addLayer(new WitcherHorseArmorFeatureRenderer(this));
    }

    @Mixin(PowderSnowBlock.class)
    public static class PowderSnow_TundraArmor {
        @Inject(method = "canEntityWalkOnPowderSnow", at = @At("HEAD"), cancellable = true)
        private static void canWalkWithArmor(Entity entity, CallbackInfoReturnable<Boolean> cir){
            if(entity instanceof Horse horse && horse.getBodyArmorItem().is(TCOTS_Items_Fabric.TUNDRA_HORSE_ARMOR)){
                cir.setReturnValue(true);
            }
        }
    }
}
