package TCOTS.effects.decoctions;

import TCOTS.effects.WitcherPotionEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class DecoctionEffectBase extends WitcherPotionEffect {

    private LivingEntity entity;
    private final int decoctionToxicity;
    public DecoctionEffectBase(final MobEffectCategory category, final int color, final int decoctionToxicity) {
        super(category, color);
        this.decoctionToxicity=decoctionToxicity;
    }

    @Override
    public void onEffectStarted(@NotNull final LivingEntity entity, final int amplifier) {
        this.entity=entity;
    }

    @Override
    public boolean applyEffectTick(@NotNull final LivingEntity entity, final int amplifier) {
        if (this.entity == null) {
            this.entity = entity;
        }

        return super.applyEffectTick(entity, amplifier);
    }

    @Override
    public void removeAttributeModifiers(@NotNull final AttributeMap attributeContainer) {
        if(!entity.level().isClientSide){
            if(entity!=null && entity instanceof final Player player){
                player.theConjunctionOfTheSpheres$decreaseToxicity(this.decoctionToxicity,true);
            }
        }
        super.removeAttributeModifiers(attributeContainer);
    }

}
